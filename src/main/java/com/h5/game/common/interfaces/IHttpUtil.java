package com.h5.game.common.interfaces;

import com.h5.game.common.tools.HttpFileModel;

import java.io.IOException;
import java.util.Map;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public interface IHttpUtil {
    /**
     * 发送Http post请求
     * @param sUrl post的连接
     * @param params 需要发送的参数
     * @return 返回接收的内容
     * @throws IOException
     */
    String sendPost(String sUrl, String params) throws IOException;

    /**
     * 发送Http get请求
     * @param sUrl http连接
     * @return 返回获取到的内容
     * @throws IOException
     */
    String sendGet(String sUrl) throws IOException;

    String sendPost(String sUrl, String contextBody, String headName, String headValue) throws IOException;

    String sendFileByPost(String actionUrl, Map<String, String> formData, HttpFileModel... fileData) throws IOException;
}
