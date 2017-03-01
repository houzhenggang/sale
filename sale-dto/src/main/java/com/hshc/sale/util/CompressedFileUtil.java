package com.hshc.sale.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 文件压缩工具类
 * 将指定文件/文件夹压缩成zip、rar压缩文件
 * @author 弋攀 E-mail：panyi@huashenghaoche.com
 * @date 2016年8月17日 10:47:15
 */
public class CompressedFileUtil {
    /**
     * 默认构造函数
     */
    public CompressedFileUtil(){

    }

    /**
     * @desc 将源文件/文件夹生成指定格式的压缩文件,格式zip
     * @param resourcesPath 源文件/文件夹
     * @param targetPath  目的压缩文件保存路径
     * @return void
     * @throws Exception
     */
    public void compressedFile(String resourcesPath,String targetPath) throws Exception{
        File resourcesFile = new File(resourcesPath);     // 源文件
        File targetFile = new File(targetPath);           // 目的
        // 如果目的路径不存在，则新建
        if(!targetFile.exists()){
            targetFile.mkdirs();
        }

        String targetName = resourcesFile.getName()+".zip";   // 目的压缩文件名
        FileOutputStream outputStream = new FileOutputStream(targetPath + File.separator + targetName);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(outputStream));

        createCompressedFile(out, resourcesFile, resourcesFile.getName());

        out.close();
    }

    /**
     * 生成压缩文件。
     * 如果是文件夹，则使用递归，进行文件遍历、压缩
     * 如果是文件，直接压缩
     * @param out  输出流
     * @param file  目标文件
     * @return void
     * @throws Exception
     */
    public void createCompressedFile(ZipOutputStream out,File file,String dir) throws Exception{
        // 如果当前的是文件夹，则进行进一步处理
        if(file.isDirectory()){
            // 得到文件列表信息
            File[] files = file.listFiles();
            // 将文件夹添加到下一级打包目录
            out.putNextEntry(new ZipEntry(dir+"/"));

            dir = dir.length() == 0 ? "" : dir +"/";

            // 循环将文件夹中的文件打包
            for(int i = 0 ; i < files.length ; i++){
                createCompressedFile(out, files[i], dir + files[i].getName());         // 递归处理
            }
        } else{
            // 当前的是文件，打包处理
            // 文件输入流
            FileInputStream fis = new FileInputStream(file);

            out.putNextEntry(new ZipEntry(dir));
            // 进行写操作
            int j =  0;
            byte[] buffer = new byte[1024];
            while((j = fis.read(buffer)) > 0){
                out.write(buffer,0,j);
            }
            // 关闭输入流
            fis.close();
        }
    }

    public static void main(String[] args){
        CompressedFileUtil compressedFileUtil = new CompressedFileUtil();

        try {

            // 删除文件夹
            FileUtils.delete("E:/zip/zip.zip");
            FileOperateUtil.deleteGeneralFile("D:\\apache-tomcat-8.0.23\\temp\\zip");

            // System.out.println(System.currentTimeMillis()+".jpg");

            // System.getProperty("java.io.tmpdir"); // Java临时目录

            FileUtils.delete("E:/zip");

            String[] strings = new String[]{"一", "二", "3", "4"};
            
            // 1:每个小级中的图片保存在小级zip中
            for (String str : strings) {

                //如果目的路径不存在，则新建
                File targetFile = new File("E:/zip/"+str);
                if(!targetFile.exists()){
                    targetFile.mkdirs();
                }

                FileUtils.write(targetFile.getPath()+"/temp.jpg", FileUtils.readFileResultBytes("E:\\temp.jpg"));

            }

            // 2:最上级的打包zip
            compressedFileUtil.compressedFile("E:/zip", "E:/zip");

            System.out.println("压缩文件已经生成...");
        } catch (Exception e) {
            System.out.println("压缩文件生成失败...");
            e.printStackTrace();
        }
    }

    /*public static void main(String[] args) {
        // FileUtils.writeFileByImgPath("http://int.car.jiezhongchina.com/uplimages/carAsset/2016/06/2675321862147739.jpg", "E:/zip/aaa.jpg");

        System.out.println(System.getProperty("java.io.tmpdir"));

    }*/

}