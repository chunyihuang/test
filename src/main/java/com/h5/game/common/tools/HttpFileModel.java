package com.h5.game.common.tools;

/**
 * Created by 黄春怡 on 2017/4/1.
 */
public class HttpFileModel {

    private String fileName;

    private String paramName;

    private String filePath;

    private String contentType = "application/octet-stream";

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public HttpFileModel() {
    }

    public HttpFileModel(String fileName, String paramName, String filePath) {
        this.fileName = fileName;
        this.paramName = paramName;
        this.filePath = filePath;
    }

    public HttpFileModel(String fileName, String paramName, String filePath, String contentType) {
        this.fileName = fileName;
        this.paramName = paramName;
        this.filePath = filePath;
        this.contentType = contentType;
    }
}
