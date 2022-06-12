package com.aystudio.core.common.util;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author Blank038
 * @since 1.0.3-BETA
 */
public class FileUtils {

    /**
     * 复制文件至指定文件, 前提是二者都存在
     *
     * @param in  源文件
     * @param out 目标文件
     * @return 复制结果
     */
    public static boolean copyFile(File in, File out) {
        if (in.exists()) {
            if (in.isDirectory()) {
                out.mkdir();
                for (File i : in.listFiles()) {
                    FileUtils.copyFile(new File(in, i.getName()), new File(out, i.getName()));
                }
            } else if (in.isFile()) {
                InputStreamReader isr = null;
                OutputStreamWriter osw = null;
                try {
                    isr = new InputStreamReader(new FileInputStream(in), StandardCharsets.UTF_8);
                    osw = new OutputStreamWriter(new FileOutputStream(out), StandardCharsets.UTF_8);
                    char[] bytes = new char[1024];
                    int len;
                    while ((len = isr.read(bytes)) > 0){
                        osw.write(bytes, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                } finally {
                    if (isr != null) {
                        try {
                            isr.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (osw != null) {
                        try {
                            osw.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }
}
