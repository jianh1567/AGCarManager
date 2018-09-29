package com.wind.carmanager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by houjian on 2018/5/15.
 */

public class FileUtil {
    public static void writeSDFile(String str, String fileName, String filePath){
        try {
            File file = new File(filePath);
            if(!file.exists()){
                file.mkdirs();
            }

            File file1 = new File(filePath + "/" + fileName);
            if(!file1.exists()){
                file1.createNewFile();
            }

            FileOutputStream output = new FileOutputStream(filePath + "//" + fileName,false);
            byte[] buffer = str.getBytes("UTF-8");
            output.write(buffer);
            output.flush();
            output.close();
        } catch (Exception e) {
             e.printStackTrace();
        }
    }

    public static String readSDFile(String fileName, String filePath){
        StringBuffer sb = new StringBuffer();
        File file = new File(filePath + "//" + fileName);
        try {
            FileInputStream fis = new FileInputStream(file);
            int c;
            while ((c = fis.read()) != -1) {
                sb.append((char) c);
            }
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
