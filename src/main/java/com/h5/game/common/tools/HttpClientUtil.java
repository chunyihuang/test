package com.h5.game.common.tools;

import org.apache.log4j.Logger;

/**
 * Created by 黄春怡 on 2017/5/1.
 */
public class HttpClientUtil {
    private static Logger logger = Logger.getLogger(HttpClientUtil.class);
    private HttpClientUtil(){}

    public static String sendGetRequest(String reqUrl,String decodeCharset){
       /* long responseLength = 0;
        String responseContent = null;
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(reqUrl);
        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            if(null != httpEntity){
                responseLength = httpEntity.getContentLength();
                responseContent = EntityUtils.toString(httpEntity,decodeCharset==null?"utf-8":decodeCharset);


            }
        }catch (IOException e){
            e.printStackTrace();
        }*/
        return "";
    }



}
