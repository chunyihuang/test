package com.h5.game.common.tools;

import java.io.*;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public class IOUtil {

    public static byte[] getFileBytes(String filePath) throws IOException {
        return getFileBytes(new File(filePath));
    }

    public static byte[] getFileBytes(File file) throws IOException {
        return getFileBytes(new FileInputStream(file));
    }

    public static byte[] getFileBytes(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        byte[] buffer = new byte[bufferedInputStream.available()];
        bufferedInputStream.read(buffer);
        bufferedInputStream.close();
        return buffer;
    }

    public static void toDist(byte[] bytes,String filePath) throws IOException {
        toDist(bytes,new File(filePath));
    }

    public static void toDist(byte[] bytes,File file) throws IOException {
        if(!file.getParentFile().exists()){
            file.mkdirs();
        }
        toDist(bytes,new FileOutputStream(file));
    }

    public static void toDist(byte[] bytes,OutputStream outputStream) throws IOException {
        outputStream.write(bytes,0,bytes.length);
        outputStream.flush();
        outputStream.close();
    }

}
