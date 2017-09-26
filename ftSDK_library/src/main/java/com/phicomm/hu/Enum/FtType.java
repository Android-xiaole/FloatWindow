package com.phicomm.hu.Enum;


/**
 * Created by le on 2017/9/18.
 * 配置两种类型的广告弹窗的枚举
 */

public enum FtType {

    SERVICE(0),POPWINDOW(1);

    private int value;

    FtType(int value) {
        this.value = value;
    }

    public static FtType valueOf(int value) {
        switch (value) {
            case 0:
                return SERVICE;
            case 1:
                return POPWINDOW;
            default:
                return SERVICE;
        }
    }

    public int value() {
        return this.value;
    }
}
