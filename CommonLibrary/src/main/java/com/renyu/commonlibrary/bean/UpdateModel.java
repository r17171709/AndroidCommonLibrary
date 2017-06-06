package com.renyu.commonlibrary.bean;

import java.io.Serializable;

/**
 * Created by renyu on 16/1/23.
 */
public class UpdateModel implements Serializable {

    /**
     * content : 1.修改BUG ，2.UI调整
     * title : 升级提醒
     * url : http://www.baidu.com/aba.apk
     * version : 10.10.4
     */

    private String content;
    private String title;
    private String url;
    private String version;
    private int forced;

    //下载相关
    private int process;
    public enum State {
        SUCCESS, FAIL, DOWNLOADING
    }
    private State state;
    private String localPath;
    private String notificationTitle;

    private long bytesRead;
    private long contentLength;

    public void setContent(String content) {
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getContent() {
        return content;
    }

    public String getTitle() {
        return title;
    }

    public String getUrl() {
        return url;
    }

    public String getVersion() {
        return version;
    }

    public int getProcess() {
        return process;
    }

    public void setProcess(int process) {
        this.process = process;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public int getForced() {
        return forced;
    }

    public void setForced(int forced) {
        this.forced = forced;
    }

    public String getNotificationTitle() {
        return notificationTitle;
    }

    public void setNotificationTitle(String notificationTitle) {
        this.notificationTitle = notificationTitle;
    }
}
