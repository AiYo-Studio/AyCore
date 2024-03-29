package com.aystudio.core.bukkit.util.common;

import com.aystudio.core.bukkit.interfaces.CustomExecute;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Blank038
 * @since 2021-04-26
 */
public class CommonUtil {

    /**
     * 将 String 转换为槽位数组, 允许拼接
     * 支持格式:
     * (1): 纯数字
     * (2): 数字,数字
     * (3): 最小数-最大数
     * 例子: 0,1,2,3,4-10
     *
     * @param text 目标文本
     * @return 槽位数组
     */
    public static Integer[] formatSlots(String text) {
        if (text == null) {
            return new Integer[0];
        }
        if (text.contains(",")) {
            List<Integer> list = new ArrayList<>();
            for (String s : text.split(",")) {
                list.addAll(Arrays.asList(formatSlots(s)));
            }
            return list.toArray(new Integer[0]);
        } else if (text.contains("-")) {
            String[] split = text.split("-");
            int n1 = Integer.parseInt(split[0]), n2 = Integer.parseInt(split[1]);
            int min = Math.min(n1, n2), max = Math.max(n1, n2);
            Integer[] result = new Integer[max - min + 1];
            for (int index = 0, temp = min; temp <= max; temp++, index++) {
                result[index] = temp;
            }
            return result;
        } else {
            return new Integer[]{Integer.parseInt(text)};
        }
    }

    /**
     * 将输入流写入指定文件
     *
     * @param in   源输入流
     * @param file 目标文件
     */
    public static void outputFileTool(InputStream in, File file) {
        try {
            if (!file.getParentFile().exists()) {
                CommonUtil.createFolder(file.getParentFile());
            }
            if (!file.exists()) {
                Files.createFile(file.toPath());
            }
            OutputStream out = new FileOutputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = in.read(b)) != -1) {
                out.write(b, 0, length);
            }
            out.close();
            in.close();
        } catch (IOException ignored) {
        }
    }

    public static void createFolder(File file) {
        if (file.exists()) {
            return;
        }
        File folder = file.getParentFile();
        if (!folder.exists()) {
            CommonUtil.createFolder(folder);
        }
        file.mkdir();
    }

    public static byte[] transferFileToBytes(File f) {
        try {
            FileInputStream fis = new FileInputStream(f);
            ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
            int b;
            while ((b = fis.read()) != -1) {
                byteArray.write(b);
            }
            byteArray.flush();
            byteArray.close();
            fis.close();
            return byteArray.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

    /**
     * 将插件资源以输入流写入指定文件
     *
     * @param plugin   源插件
     * @param source   源地址(Resource Path)
     * @param target   目标地址(Target Path)
     * @param execute 文件执行器
     */
    public static void saveResource(JavaPlugin plugin, String source, String target, boolean replace, CustomExecute<File> execute) {
        File file = new File(plugin.getDataFolder(), target);
        boolean exists = file.exists();
        if (exists && !replace) {
            if (execute != null) {
                execute.run(file);
            }
            return;
        }
        CommonUtil.outputFileTool(plugin.getResource(source), file);
        if (execute != null) {
            execute.run(file);
        }
    }

    /**
     * 匹配列表文本开头是否匹配, 不区分大小写
     *
     * @param list  目标列表
     * @param start 开头文本
     * @return 返回列表
     */
    public static List<String> matchStart(List<String> list, String start) {
        return list.stream().filter((s) -> s.toLowerCase().startsWith(start.toLowerCase())).collect(Collectors.toList());
    }
}
