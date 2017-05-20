package cn.com.infcn.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileNameList {

    public static void main(String[] args) {
        String path = "C:\\xp\\mysqlSelectKey\\bat";
        String mainClass = "cn.com.infcn.OneTest";
        generatorBat(path, mainClass);

    }

    public static String getFileList(String path) {

        String tempList = path + "\\jdk\\bin\\java  -cp ";

        File file = new File(path + "\\lib");
        File[] listFiles = file.listFiles();
        for (File file2 : listFiles) {
            if (file2.getName().contains(".jar")) {
                tempList += ("../lib/" + file2.getName() + ";");
            }
        }
        return tempList;
    }

    public static String getMainString(String path) {
        return " " + path;
    }

    private static void generatorBat(String path, String mainClass) {
        String begin = "@echo off     ";
        String end = "pause";
        String fileList = FileNameList.getFileList(path);
        String mainString = FileNameList.getMainString(mainClass);

        File file = new File(path + "\\src\\Launch.bat");
        FileWriter fw = null;
        try {
            fw = new FileWriter(file);
            fw.write(begin);
            fw.write(System.getProperty("line.separator"));
            fw.write(fileList + mainString);
            fw.write(System.getProperty("line.separator"));
            fw.write(end);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
