package com.phicomm.hu.Enum;

import android.view.Gravity;

/**
 * Created by le on 2017/9/15.
 * 适配弹窗位置的枚举类 可配置在屏幕的：上  中  下
 */

public enum FtGravity {

    TOP(Gravity.TOP),CENTER(Gravity.CENTER),BOTTOM(Gravity.BOTTOM);

    private int value;
    FtGravity(int value) {
        this.value = value;
    }

    public static FtGravity valueOf(int value) {
        switch (value) {
            case Gravity.TOP:
                return TOP;
            case Gravity.CENTER:
                return CENTER;
            case Gravity.BOTTOM:
                return BOTTOM;
            default:
                return BOTTOM;
        }
    }

    public int value() {
        return this.value;
    }
}
