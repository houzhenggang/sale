package com.hshc.sale.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 文件上传工具类
 */
public class UploadUtil {
    private static Logger logger = LoggerFactory.getLogger(UploadUtil.class);

    /**
     * 创建文件（若目录不存在，创建目录）
     * @param path 文件路径
     * @return java.io.File
     */
    public static File createFile(String path) throws IOException {
        File file = new File(path);
        if(!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        return file;
    }

    /**
     * 获取文件后缀
     * @param filename 文件名
     * @return 文件后缀字符串
     */
    public static String getFileSuffix(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length() - 1))) {
                return filename.substring(dot + 1);
            }
        }
        return filename;
    }

    /**
     * 是否为系统支持文件格式
     * @param contentType
     * @return true支持，false不支持
     */
    public static boolean isSupportMime(String contentType){
        Set<String> mimeKeySet = PropertiesUtil.getMimeKeySet();
        return mimeKeySet.contains(contentType);
    }

    /**
     * 上传文件
     * @param file MultipartFile
     * @param buis_type 业务类型
     * @return Map<String, String> 包含 filePath、fileName
     * @throws IOException
     */
    public static Map<String, String> upload(MultipartFile file, String buis_type) throws IOException {
        String yearStr = DateUtil.date2String(new Date(), "yyyy");
        String monthStr = DateUtil.date2String(new Date(), "MM");
        Map<String, String> map = new HashMap<>();
        if (file.isEmpty()) {
            return null;
        }
        String fileName = file.getOriginalFilename();
        String filePath = "";
        if (!"".equals(fileName)) {
            String newFileName = String.valueOf(System.nanoTime()) + "." + getFileSuffix(fileName);
            filePath = File.separator + buis_type + File.separator + yearStr + File.separator + monthStr + File.separator + newFileName;
            String local_file_path = PropertiesUtil.getUPLOAD("store_root_path") + filePath;
            file.transferTo(createFile(local_file_path));
            map.put("filePath", filePath.replace("\\","/"));
//            map.put("filePath", filePath);
            map.put("fileName", fileName);
            return map;
        } else {
            return null;
        }
    }
}
