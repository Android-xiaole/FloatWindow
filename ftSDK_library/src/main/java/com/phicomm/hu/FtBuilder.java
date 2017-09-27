package com.phicomm.hu;

import android.app.Activity;

import com.phicomm.hu.Enum.FtType;
import com.phicomm.hu.Enum.FtGravity;

/**
 * Created by le on 2017/9/18.
 * 初始化ftSDK的接口
 */

public interface FtBuilder {

    FtSDK setType(FtType type);
    FtSDK setGravity(FtGravity gravity);
    FtSDK setDebug(boolean debug);
    FtSDK setTime(int time);
    void start(Activity context);
}
