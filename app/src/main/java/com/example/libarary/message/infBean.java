package com.example.libarary.xinxibao;

/**
 * Created by 陈金桁 on 2018/2/7.
 */

public class infBean {
    private int imgId;
    private String sbj;

    public infBean(int imgId, String sbj) {
        this.imgId = imgId;
        this.sbj = sbj;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getSbj() {
        return sbj;
    }

    public void setSbj(String sbj) {
        this.sbj = sbj;
    }
}
