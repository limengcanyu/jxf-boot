package org.asura.fastexcel.utils;

import cn.idev.excel.EasyExcel;
import cn.idev.excel.ExcelWriter;
import cn.idev.excel.write.metadata.WriteSheet;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 多Sheet多线程大数据导出工具类
 */
public class MultiSheetBigDataExportUtil {

    // 阻塞队列大小（控制内存，建议为2-5倍批次大小）
    private static final int QUEUE_CAPACITY = 10;
    // Excel单Sheet最大行数
    private static final int MAX_ROWS_PER_SHEET = 1048576;

    /**
     * 初始化Excel响应头
     */
    private static void initResponse(HttpServletResponse response, String fileName) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + encodedFileName + ".xlsx");
    }

    /**
     * 数据生产者线程（分批查询数据，存入阻塞队列）
     * @param <T> 数据类型
     */
    @FunctionalInterface
    public interface DataProducer<T> {
        void produce(BlockingQueue<List<T>> queue, int pageSize) throws Exception;
    }

    /**
     * 多Sheet大数据导出（核心方法）
     * @param response HTTP响应
     * @param fileName 导出文件名
     * @param sheet1Name Sheet1名称
     * @param sheet1Clazz Sheet1数据类型
     * @param sheet1Producer Sheet1数据生产者
     * @param sheet2Name Sheet2名称
     * @param sheet2Clazz Sheet2数据类型
     * @param sheet2Producer Sheet2数据生产者
     * @param pageSize 每批查询行数
     */
    public static <T1, T2> void exportMultiSheet(
            HttpServletResponse response,
            String fileName,
            String sheet1Name,
            Class<T1> sheet1Clazz,
            DataProducer<T1> sheet1Producer,
            String sheet2Name,
            Class<T2> sheet2Clazz,
            DataProducer<T2> sheet2Producer,
            int pageSize
    ) {
        // 1. 初始化响应头
        try {
            initResponse(response, fileName);
        } catch (IOException e) {
            throw new RuntimeException("响应头初始化失败", e);
        }

        // 2. 创建线程安全的阻塞队列（存储分批数据）
        BlockingQueue<List<T1>> sheet1Queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);
        BlockingQueue<List<T2>> sheet2Queue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

        // 3. 标记生产者是否完成
        AtomicBoolean sheet1ProduceDone = new AtomicBoolean(false);
        AtomicBoolean sheet2ProduceDone = new AtomicBoolean(false);

        // 4. 创建线程池（核心数=2，对应两个Sheet的查询线程）
        ThreadFactory threadFactory = new BasicThreadFactory.Builder()
                .namingPattern("export-thread-%d")
                .daemon(true)
                .build();
        ExecutorService executor = new ThreadPoolExecutor(
                2, 2,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy()
        );

        // 5. 提交Sheet1数据查询任务
        executor.submit(() -> {
            try {
                sheet1Producer.produce(sheet1Queue, pageSize);
            } catch (Exception e) {
                throw new RuntimeException("Sheet1数据查询失败", e);
            } finally {
                sheet1ProduceDone.set(true);
                // 放入空数据，唤醒消费线程
                try {
                    sheet1Queue.put(null);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // 6. 提交Sheet2数据查询任务
        executor.submit(() -> {
            try {
                sheet2Producer.produce(sheet2Queue, pageSize);
            } catch (Exception e) {
                throw new RuntimeException("Sheet2数据查询失败", e);
            } finally {
                sheet2ProduceDone.set(true);
                // 放入空数据，唤醒消费线程
                try {
                    sheet2Queue.put(null);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });

        // 7. 初始化ExcelWriter（单线程写入，核心：保证线程安全）
        try (ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream())
                .autoCloseStream(true)
                .inMemory(false) // 禁用内存模式，流式写入
                .useDefaultStyle(false) // 禁用默认样式，减少内存
                .build()) {

            // 8. 构建Sheet1和Sheet2
            WriteSheet writeSheet1 = EasyExcel.writerSheet(0, sheet1Name).head(sheet1Clazz).build();
            WriteSheet writeSheet2 = EasyExcel.writerSheet(1, sheet2Name).head(sheet2Clazz).build();

            // 9. 消费Sheet1数据并写入
            consumeAndWrite(sheet1Queue, sheet1ProduceDone, excelWriter, writeSheet1, MAX_ROWS_PER_SHEET);

            // 10. 消费Sheet2数据并写入
            consumeAndWrite(sheet2Queue, sheet2ProduceDone, excelWriter, writeSheet2, MAX_ROWS_PER_SHEET);

        } catch (IOException e) {
            throw new RuntimeException("ExcelWriter初始化/关闭失败", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // 11. 关闭线程池
            executor.shutdown();
            try {
                if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * 消费队列数据并写入Excel（支持单Sheet分行）
     * @param queue 数据队列
     * @param produceDone 生产者是否完成
     * @param excelWriter Excel写入器
     * @param writeSheet 目标Sheet
     * @param maxRowsPerSheet 单Sheet最大行数
     * @param <T> 数据类型
     */
    private static <T> void consumeAndWrite(
            BlockingQueue<List<T>> queue,
            AtomicBoolean produceDone,
            ExcelWriter excelWriter,
            WriteSheet writeSheet,
            int maxRowsPerSheet
    ) throws InterruptedException {
        int rowCount = 0;
        int sheetIndex = writeSheet.getSheetNo();
        String sheetName = writeSheet.getSheetName();

        while (true) {
            // 从队列取数据（阻塞，直到有数据或生产者完成）
            List<T> data = queue.take();

            // 生产者完成且无数据，退出循环
            if (data == null && produceDone.get()) {
                break;
            }

            // 空数据跳过（防止异常）
            if (data == null || data.isEmpty()) {
                continue;
            }

            // 检查是否需要新建Sheet（单Sheet行数超限）
            if (rowCount >= maxRowsPerSheet) {
                sheetIndex++;
                rowCount = 0;
                writeSheet = EasyExcel.writerSheet(sheetIndex, sheetName + "-" + sheetIndex).head(writeSheet.getHead()).build();
            }

            // 写入数据（单线程，线程安全）
            excelWriter.write(data, writeSheet);

            // 更新计数 + 释放当前批次内存
            rowCount += data.size();
            data.clear();
            System.gc(); // 轻量GC，减少内存占用
        }
    }
}

