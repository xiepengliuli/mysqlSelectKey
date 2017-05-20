package cn.com.infcn.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.log4j.Logger;

public class FileUtil {

    /**
     * 日志对象
     */
    private static final Logger LOG = Logger.getLogger(FileUtil.class);

    /**
     * 缓存用的安装路径
     */
    private static String installPath = null;

    /**
     * 获得项目的路径
     * 
     * @return 项目的路径
     */
    public static String getInstallPath() {

        if (installPath != null) {
            return installPath;
        }

        Class<FileUtil> cls = FileUtil.class;

        ClassLoader loader = cls.getClassLoader();
        // 获得类的全名，包括包名
        String clsName = cls.getName() + ".class";
        // 获得传入参数所在的包
        Package pack = cls.getPackage();
        String path = "";
        // 如果不是匿名包，将包名转化为路径
        if (pack != null) {
            String packName = pack.getName();
            // 此处简单判定是否是Java基础类库，防止用户传入JDK内置的类库
            if (!packName.startsWith("cn.com.infcn")) {
                throw new java.lang.IllegalArgumentException("类型不支持");
            }
            // 在类的名称中，去掉包名的部分，获得类的文件名
            clsName = clsName.substring(packName.length() + 1);
            // 判定包名是否是简单包名，如果是，则直接将包名转换为路径，
            if (packName.indexOf(".") < 0)
                path = packName + "/";
            else {// 否则按照包名的组成部分，将包名转换为路径
                int start = 0, end = 0;
                end = packName.indexOf(".");
                while (end != -1) {
                    path = path + packName.substring(start, end) + "/";
                    start = end + 1;
                    end = packName.indexOf(".", start);
                }
                path = path + packName.substring(start) + "/";
            }
        }

        // 调用ClassLoader的getResource方法，传入包含路径信息的类文件名
        java.net.URL url = loader.getResource(path + clsName);
        // 从URL对象中获取路径信息

        String realPath = url.getPath();
        // 去掉路径信息中的协议名"file:"
        int pos = realPath.indexOf("file:");
        if (pos > -1)
            realPath = realPath.substring(pos + 5);

        // 去掉路径信息最后包含类文件信息的部分，得到类所在的路径
        pos = realPath.indexOf(path + clsName);
        realPath = realPath.substring(0, pos - 1);

        // 如果类文件被打包到JAR等文件中时，去掉对应的JAR等打包文件名
        boolean isRar = realPath.endsWith("!");
        if (isRar) {
            realPath = realPath.substring(0, realPath.lastIndexOf("/"));
        }

        // 结果字符串可能因平台默认编码不同而不同。因此，改用 decode(String,String) 方法指定编码。
        try {
            realPath = java.net.URLDecoder.decode(realPath, "utf-8");
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }

        File file = new File(realPath);
        if (file.getName().equals("classes")) {
            file = file.getParentFile();
        }

        if (file.getName().equals("target")) {
            file = file.getParentFile();
        }

        LOG.info("安装路径为：" + file.getAbsolutePath());

        return file.getAbsolutePath();
    }

    /**
     * 从一个文件中读取字符串
     * 
     * @param file
     *            要读取的文件
     * @param encoding
     *            字符集
     * @return 文件中的字符串
     */
    public static String read2String(File file, String encoding) {

        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);

            byte[] bytes = new byte[1024 * 8];

            int off = -1;

            while ((off = bis.read(bytes)) != -1) {
                baos.write(bytes, 0, off);
            }

            return new String(baos.toByteArray(), encoding);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return null;
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }

    }

    public static boolean write2File(File file, String value, String encoding) {

        FileOutputStream fos = null;
        BufferedOutputStream bos = null;

        try {

            File forder = file.getParentFile();

            if (!forder.exists()) {
                forder.mkdirs();
            }

            fos = new FileOutputStream(file, false);
            bos = new BufferedOutputStream(fos);

            byte[] bytes = value.getBytes(encoding);
            bos.write(bytes);

            return true;
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
            return false;
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
        }

    }
}
