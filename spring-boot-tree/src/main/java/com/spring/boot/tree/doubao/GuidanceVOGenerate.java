package com.spring.boot.tree.doubao;

import java.util.Arrays;
import java.util.List;

public class GuidanceVOGenerate {

    public static List<GuidanceVO> generate() {
        return Arrays.asList(
                new GuidanceVO("root01", "根节点01", "folder", null),
                    new GuidanceVO("folder01", "文件夹01", "folder", "root01"),
                        new GuidanceVO("folder0101", "文件夹0101", "folder", "folder01"),
                        new GuidanceVO("template01", "模版01", "template", "folder01"),
                        new GuidanceVO("file01", "文件01", "file", "folder01"),

                new GuidanceVO("root02", "根节点02", "folder", null),
                    new GuidanceVO("folder02", "文件夹02", "folder", "root02"),
                        new GuidanceVO("folder0201", "文件夹0201", "folder", "folder02"),
                        new GuidanceVO("checklist02", "检查单02", "checklist", "folder02"),
                        new GuidanceVO("file01", "风险01", "risk", "folder02"),

                new GuidanceVO("root03", "根节点03", "file", null)
        );
    }

}
