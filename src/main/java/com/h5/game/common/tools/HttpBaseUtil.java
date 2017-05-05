package com.h5.game.common.tools;

import com.h5.game.common.interfaces.IHttpUtil;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
@Component("httpBaseUtil")
public class HttpBaseUtil implements IHttpUtil {

    public String sendPost(String sUrl, String params) throws IOException {
        URL url = new URL(sUrl);
        URLConnection conn = url.openConnection();
        // 设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        if(params != null){
            conn.getOutputStream().write(params.getBytes());
            conn.getOutputStream().flush();
        }
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = in.readLine()) != null) {
            result += line;
        }
        return result;
    }

    public String sendGet(String sUrl) throws IOException {
        URL url = new URL(sUrl);
        URLConnection conn = url.openConnection();
        // 设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = in.readLine()) != null) {
            result += line;
        }
        return result;
    }

    public String sendPost(String sUrl, String contextBody, String headName,String headValue) throws IOException {
        URL url = new URL(sUrl);
        URLConnection conn = url.openConnection();
        // 设置通用的请求属性
        conn.setRequestProperty("accept", "*/*");
        conn.setRequestProperty("connection", "Keep-Alive");
        conn.setRequestProperty("user-agent",
                "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        conn.setRequestProperty(headName,headValue);
        // 发送POST请求必须设置如下两行
        conn.setDoOutput(true);
        conn.setDoInput(true);
        if(contextBody != null){
            conn.getOutputStream().write(contextBody.getBytes());
            conn.getOutputStream().flush();
        }
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = in.readLine()) != null) {
            result += line;
        }
        return result;
    }

    public String sendFileByPost(String actionUrl, Map<String,String> formData,HttpFileModel... fileData) throws IOException {
        // 产生随机分隔内容
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        // 定义URL实例
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        // 设置从主机读取数据超时
        //conn.setReadTimeout(10 * 1000);
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");
        // 维持长连接
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charset", "UTF-8");
        // 设置文件类型
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
                + ";boundary=" + BOUNDARY);
        // 创建一个新的数据输出流，将数据写入指定基础输出流
        DataOutputStream outStream = new DataOutputStream(
                conn.getOutputStream());
        //写入参数
        if (formData != null) {
            StringBuffer strBuf = new StringBuffer();
            for (Map.Entry<String,String> text : formData.entrySet()) {
                String inputName = text.getKey();
                String inputValue = text.getValue();
                if (inputValue == null) {
                    continue;
                }
                strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                strBuf.append("Content-Disposition: form-data; name=\"" + inputName + "\"\r\n\r\n");
                strBuf.append(inputValue);
            }
            outStream.write(strBuf.toString().getBytes());
        }

        //写入文件
        if(fileData != null){
            for (HttpFileModel fileModel: fileData){
                File file = new File(fileModel.getFilePath());
                String filename = file.getName();
                String contentType = fileModel.getContentType();

                StringBuffer strBuf = new StringBuffer();
                strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
                strBuf.append("Content-Disposition: form-data; name=\"" + fileModel.getParamName() + "\"; filename=\"" + filename+ "\"\r\n");
                strBuf.append("Content-Type:" + contentType + "\r\n\r\n");

                outStream.write(strBuf.toString().getBytes());

                DataInputStream in = new DataInputStream(new FileInputStream(file));
                int bytes = 0;
                byte[] bufferOut = new byte[1024];
                while ((bytes = in.read(bufferOut)) != -1) {
                    outStream.write(bufferOut, 0, bytes);
                }
                in.close();
            }
        }
        // 请求结束标志
        byte[] end_data = (LINEND + PREFFIX + BOUNDARY + PREFFIX + LINEND).getBytes();
        outStream.write(end_data);
        // 刷新发送数据
        outStream.flush();
        // 得到响应码
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        String line;
        String result = "";
        while ((line = in.readLine()) != null) {
            result += line;
        }
        // 如果数据不为空，则以字符串方式返回数据，否则返回null
        return result;
    }
}
