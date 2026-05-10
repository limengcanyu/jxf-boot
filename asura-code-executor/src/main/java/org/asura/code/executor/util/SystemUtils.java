package org.asura.code.executor.util;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.List;

/**
 * 系统工具类
 */
public class SystemUtils {

    /**
     * 获取JVM进程ID
     */
    public static String getProcessId() {
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        String name = runtimeBean.getName();
        return name.split("@")[0];
    }

    /**
     * 获取JVM启动参数
     */
    public static List<String> getJvmArgs() {
        RuntimeMXBean runtimeBean = ManagementFactory.getRuntimeMXBean();
        return runtimeBean.getInputArguments();
    }

    /**
     * 检查是否为JDK环境
     */
    public static boolean isJdkEnvironment() {
        try {
            Class.forName("javax.tools.JavaCompiler");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    /**
     * 获取系统信息
     */
    public static String getSystemInfo() {
        StringBuilder info = new StringBuilder();
        info.append("Java Version: ").append(System.getProperty("java.version")).append("\n");
        info.append("Java Home: ").append(System.getProperty("java.home")).append("\n");
        info.append("OS Name: ").append(System.getProperty("os.name")).append("\n");
        info.append("OS Arch: ").append(System.getProperty("os.arch")).append("\n");
        info.append("OS Version: ").append(System.getProperty("os.version")).append("\n");
        info.append("Process ID: ").append(getProcessId()).append("\n");
        info.append("Max Memory: ").append(Runtime.getRuntime().maxMemory() / 1024 / 1024).append("MB").append("\n");
        info.append("JDK Environment: ").append(isJdkEnvironment()).append("\n");
        return info.toString();
    }
}
