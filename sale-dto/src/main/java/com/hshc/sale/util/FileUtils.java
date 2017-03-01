package com.hshc.sale.util;

import org.jsoup.helper.StringUtil;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 文件处理辅助类
 * 
 * @className FileUtils
 * @author 弋攀 E-mail：panyi@jiezhongchina.com
 * @version V1.0 2016年3月25日 上午10:57:41
 */
public class FileUtils {

    /**
     * 当前目录路径
     */
    public static String currentWorkDir = System.getProperty("file.properties") + "\\";

    /**
     * 得到文件的输入流
     * @param url 文件地址
     * @return 输入流对象
     * @author 弋攀 E-mail：panyi@huashenghaoche.com
     * @version 2016年8月18日 16:44:19
     */
    public static InputStream getInputStream(String url) {
        InputStream is = null;
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            // 获取文件转化为byte流
            is = http.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return is;

    }

    /**
     * 拼接字符串
     * @param rootPath 服务器地址
     * @param imgPath 图片地址
     * @return 拼接后的地址
     * @author 弋攀 E-mail：panyi@huashenghaoche.com
     * @version 2016年8月18日 14:17:10
     */
    public static String joinImgPath(String rootPath, String imgPath) {
        if (null != imgPath) {
            return rootPath + imgPath;
        }
        return null;
    }

    /**
     * 根据图片路径将图片写入具体位置；如果文件不存在则不进行操作
     * @param filePath 写入图片文件路径
     * @param rootPath 服务器地址
     * @param imgPath 文件路径
     * @param imgName 文件写入时名称
     * @throws IOException
     * @author 弋攀 E-mail：panyi@huashenghaoche.com
     * @version 2016年8月18日 14:17:10
     */
    public static String writeFileByImgPath(String filePath, String rootPath, String imgPath, String imgName) throws IOException {
        String downloadImgPath = joinImgPath(rootPath, imgPath);
        if (!StringUtil.isBlank(downloadImgPath)) {
            if (StringUtil.isBlank(imgName)) {
                imgName = System.currentTimeMillis() + imgPath.substring(imgPath.indexOf("."), imgPath.length());
            } else {
                imgName += imgPath.substring(imgPath.indexOf("."), imgPath.length());
            }
            StringBuilder stringBuilder = new StringBuilder(filePath);
            if (!File.separator.equals(imgName.substring(1))){
                stringBuilder.append(File.separator);
            }
            stringBuilder.append(imgName);
            write(stringBuilder.toString(), readFileResultBytes(downloadImgPath));
            return stringBuilder.toString();
        }
        return null;
    }

    /**
     * 根据图片路径将图片写入具体位置（使用网路链接）
     * @param downloadFileUrl 文件下载路径
     * @param writeFileUrl 文件写入路径
     * @author 弋攀 E-mail：panyi@huashenghaoche.com
     * @version 2016年8月18日 14:17:10
     */
    public static void writeFileByImgPath(String downloadFileUrl, String writeFileUrl) {
        try {
            InputStream inputStream = getInputStream(downloadFileUrl);
            byte[] data = new byte[1024];
            int len = 0;
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(writeFileUrl);
                while ((len = inputStream.read(data)) != -1) {
                    fileOutputStream.write(data, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 左填充
     * 
     * @param str
     * @param length
     * @param ch
     * @return
     */
    public static String leftPad(String str, int length, char ch) {
        if (str.length() >= length) {
            return str;
        }
        char[] chs = new char[length];
        Arrays.fill(chs, ch);
        char[] src = str.toCharArray();
        System.arraycopy(src, 0, chs, length - src.length, src.length);
        return new String(chs);

    }

    /**
     * 删除文件
     * 
     * @param fileName
     *            待删除的完整文件名
     * @return
     */
    public static boolean delete(String fileName) {
        boolean result = false;
        File f = new File(fileName);
        if (f.exists()) {
            result = f.delete();

        } else {
            result = true;
        }
        return result;
    }

    /***
     * 递归获取指定目录下的所有的文件（不包括文件夹）
     * 
     * @param dirPath
     * @return
     */
    public static ArrayList<File> getAllFiles(String dirPath) {
        File dir = new File(dirPath);

        ArrayList<File> files = new ArrayList<File>();

        if (dir.isDirectory()) {
            File[] fileArr = dir.listFiles();
            for (int i = 0; i < fileArr.length; i++) {
                File f = fileArr[i];
                if (f.isFile()) {
                    files.add(f);
                } else {
                    files.addAll(getAllFiles(f.getPath()));
                }
            }
        }
        return files;
    }

    /**
     * 获取指定目录下的所有文件(不包括子文件夹)
     * 
     * @param dirPath
     * @return
     */
    public static ArrayList<File> getDirFiles(String dirPath) {
        File path = new File(dirPath);
        File[] fileArr = path.listFiles();
        ArrayList<File> files = new ArrayList<File>();

        for (File f : fileArr) {
            if (f.isFile()) {
                files.add(f);
            }
        }
        return files;
    }

    /**
     * 获取指定目录下特定文件后缀名的文件列表(不包括子文件夹)
     * 
     * @param dirPath
     *            目录路径
     * @param suffix
     *            文件后缀
     * @return
     */
    public static ArrayList<File> getDirFiles(String dirPath, final String suffix) {
        File path = new File(dirPath);
        File[] fileArr = path.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                String lowerName = name.toLowerCase();
                String lowerSuffix = suffix.toLowerCase();
                if (lowerName.endsWith(lowerSuffix)) {
                    return true;
                }
                return false;
            }

        });
        ArrayList<File> files = new ArrayList<File>();

        for (File f : fileArr) {
            if (f.isFile()) {
                files.add(f);
            }
        }
        return files;
    }

    /**
     * 读取文件内容(String)
     * 
     * @param fileName 待读取的完整文件名
     * @return 文件内容
     * @throws IOException
     */
    public static String read(String fileName) throws IOException {
        File f = new File(fileName);
        FileInputStream fs = new FileInputStream(f);
        String result = null;
        byte[] b = new byte[fs.available()];
        fs.read(b);
        fs.close();
        result = new String(b);
        return result;
    }

    /**
     * 读取文件内容(byte[])
     *
     * @param fileName 待读取的完整文件名
     * @return 文件内容
     * @throws IOException
     */
    public static byte[] readFileResultBytes(String fileName) throws IOException {
        File f = new File(fileName);
        FileInputStream fs = new FileInputStream(f);
        byte[] b = new byte[fs.available()];
        fs.read(b);
        fs.close();
        return b;
    }

    /**
     * 写文件(String)
     * 
     * @param fileName 目标文件名
     * @param fileContent 写入的内容
     * @return
     * @throws IOException
     */
    public static boolean write(String fileName, String fileContent) throws IOException {
        return write(fileName, fileContent, true, true);
    }

    /**
     * 写文件(byte[])
     *
     * @param fileName 目标文件名
     * @param contentBytes 写入的内容
     * @return
     * @throws IOException
     */
    public static boolean write(String fileName, byte[] contentBytes) throws IOException {
        return write(fileName, contentBytes, true, true);
    }

    /**
     * 写文件
     * 
     * @param fileName
     *            完整文件名(类似：/usr/a/b/c/d.txt)
     * @param fileContent
     *            文件内容
     * @param autoCreateDir
     *            目录不存在时，是否自动创建(多级)目录
     * @param autoOverWrite
     *            目标文件存在时，是否自动覆盖
     * @return
     * @throws IOException
     */
    public static boolean write(String fileName, String fileContent, boolean autoCreateDir, boolean autoOverWrite) throws IOException {
        return write(fileName, fileContent.getBytes(), autoCreateDir, autoOverWrite);
    }

    /**
     * 写文件
     * 
     * @param fileName
     *            完整文件名(类似：/usr/a/b/c/d.txt)
     * @param contentBytes
     *            文件内容的字节数组
     * @param autoCreateDir
     *            目录不存在时，是否自动创建(多级)目录
     * @param autoOverWrite
     *            目标文件存在时，是否自动覆盖
     * @return
     * @throws IOException
     */
    public static boolean write(String fileName, byte[] contentBytes, boolean autoCreateDir, boolean autoOverWrite) throws IOException {
        boolean result = false;
        if (autoCreateDir) {
            createDirs(fileName);
        }
        if (autoOverWrite) {
            delete(fileName);
        }
        File f = new File(fileName);
        FileOutputStream fs = new FileOutputStream(f);
        fs.write(contentBytes);
        fs.flush();
        fs.close();
        result = true;
        return result;
    }

    /**
     * 追加内容到指定文件
     * 
     * @param fileName
     * @param fileContent
     * @return
     * @throws IOException
     */
    public static boolean append(String fileName, String fileContent) throws IOException {
        boolean result = false;
        File f = new File(fileName);
        if (f.exists()) {
            RandomAccessFile rFile = new RandomAccessFile(f, "rw");
            byte[] b = fileContent.getBytes();
            long originLen = f.length();
            rFile.setLength(originLen + b.length);
            rFile.seek(originLen);
            rFile.write(b);
            rFile.close();
        }
        result = true;
        return result;
    }

    /**
     * 拆分文件
     * 
     * @param fileName
     *            待拆分的完整文件名
     * @param byteSize
     *            按多少字节大小拆分
     * @return 拆分后的文件名列表
     * @throws IOException
     */
    public List<String> splitBySize(String fileName, int byteSize) throws IOException {
        List<String> parts = new ArrayList<String>();
        File file = new File(fileName);
        int count = (int) Math.ceil(file.length() / (double) byteSize);
        int countLen = (count + "").length();
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(count, count * 3, 1, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(count * 2));

        for (int i = 0; i < count; i++) {
            String partFileName = file.getPath() + "." + leftPad((i + 1) + "", countLen, '0') + ".part";
            threadPool.execute(new SplitRunnable(byteSize, i * byteSize, partFileName, file));
            parts.add(partFileName);
        }
        return parts;
    }

    /**
     * 合并文件
     * 
     * @param dirPath
     *            拆分文件所在目录名
     * @param partFileSuffix
     *            拆分文件后缀名
     * @param partFileSize
     *            拆分文件的字节数大小
     * @param mergeFileName
     *            合并后的文件名
     * @throws IOException
     */
    public void mergePartFiles(String dirPath, String partFileSuffix, int partFileSize, String mergeFileName) throws IOException {
        ArrayList<File> partFiles = FileUtils.getDirFiles(dirPath, partFileSuffix);
        Collections.sort(partFiles, new FileComparator());

        RandomAccessFile randomAccessFile = new RandomAccessFile(mergeFileName, "rw");
        randomAccessFile.setLength(partFileSize * (partFiles.size() - 1) + partFiles.get(partFiles.size() - 1).length());
        randomAccessFile.close();

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(partFiles.size(), partFiles.size() * 3, 1, TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(partFiles.size() * 2));

        for (int i = 0; i < partFiles.size(); i++) {
            threadPool.execute(new MergeRunnable(i * partFileSize, mergeFileName, partFiles.get(i)));
        }

    }

    /**
     * 根据文件名，比较文件
     * 
     * @author 弋攀 E-mail：panyi@jiezhongchina.com
     *
     */
    private class FileComparator implements Comparator<File> {
        public int compare(File o1, File o2) {
            return o1.getName().compareToIgnoreCase(o2.getName());
        }
    }

    /**
     * 创建(多级)目录
     * 
     * @param filePath
     *            完整的文件名(类似：/usr/a/b/c/d.xml)
     */
    public static void createDirs(String filePath) {
        File file = new File(filePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

    }

    /**
     * 分割处理Runnable
     * 
     * @author 弋攀 E-mail：panyi@jiezhongchina.com
     *
     */
    private class SplitRunnable implements Runnable {
        int byteSize;
        String partFileName;
        File originFile;
        int startPos;

        public SplitRunnable(int byteSize, int startPos, String partFileName, File originFile) {
            this.startPos = startPos;
            this.byteSize = byteSize;
            this.partFileName = partFileName;
            this.originFile = originFile;
        }

        public void run() {
            RandomAccessFile rFile;
            OutputStream os;
            try {
                rFile = new RandomAccessFile(originFile, "r");
                byte[] b = new byte[byteSize];
                rFile.seek(startPos);// 移动指针到每“段”开头
                int s = rFile.read(b);
                os = new FileOutputStream(partFileName);
                os.write(b, 0, s);
                os.flush();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 合并处理Runnable
     * 
     * @author 弋攀 E-mail：panyi@jiezhongchina.com
     *
     */
    private class MergeRunnable implements Runnable {
        long startPos;
        String mergeFileName;
        File partFile;

        public MergeRunnable(long startPos, String mergeFileName, File partFile) {
            this.startPos = startPos;
            this.mergeFileName = mergeFileName;
            this.partFile = partFile;
        }

        public void run() {
            RandomAccessFile rFile;
            try {
                rFile = new RandomAccessFile(mergeFileName, "rw");
                rFile.seek(startPos);
                FileInputStream fs = new FileInputStream(partFile);
                byte[] b = new byte[fs.available()];
                fs.read(b);
                fs.close();
                rFile.write(b);
                rFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 下载图片
     * @Title: downloadPicture
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @author 谷寅飞
     * @date 2016年8月4日 上午10:34:54
     * @param @param urlList 设定文件
     * @return void 返回类型
     * @throws
     */
    public static void downloadPicture(ArrayList<String> urlList) {
        URL url = null;
        int imageNumber = 0;

        for (String urlString : urlList) {
            try {
                url = new URL(urlString);
                DataInputStream dataInputStream = new DataInputStream(url.openStream());
                String imageName = urlString.substring(urlString.lastIndexOf("/"));
                FileOutputStream fileOutputStream = new FileOutputStream(new File(imageName));

                byte[] buffer = new byte[1024];
                int length;

                while ((length = dataInputStream.read(buffer)) > 0) {
                    fileOutputStream.write(buffer, 0, length);
                }

                dataInputStream.close();
                fileOutputStream.close();
                imageNumber++;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
