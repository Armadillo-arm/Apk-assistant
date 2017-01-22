package com.perfume.VideoPlay;

/*
视频信息
*/
public class ItemEntity {
    private String name;//影片名字
    private String msg;//影片信息
    private String tb;//影片图标
    private String yanyuan;//影片演员
    private String url;//影片连接
    private String pingfen;//影片评分

    public String getPingfen() {
        return pingfen;
    }

    public void setPingfen(String pingfen) {
        this.pingfen = pingfen;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    public void setYanyuan(String yanyuan) {
        this.yanyuan = yanyuan;
    }
    public String getYanyuan() {
        return yanyuan;
    }
    public void setTb(String tb) {
        this.tb = tb;
    }
    public String getTb() {
        return tb;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMsg() {
        return msg;
    }
    public void setMsg(String msg) {
        this.msg = msg;
    }
}

