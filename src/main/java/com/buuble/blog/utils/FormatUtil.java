package com.buuble.blog.utils;

import org.springframework.stereotype.Component;

@Component
public class FormatUtil {
    /**
     * 获取文件格式
     *
     * @param fileName 完整文件名
     * @return
     */
    public String getFileFormat(String fileName) {
        if (null == fileName) {
            return null;
        }
        String[] formatNames = fileName.split("\\.");
        if (formatNames.length <= 1) {
            return null;
        }
        String format = "." + formatNames[formatNames.length - 1];
        return format;
    }




}
