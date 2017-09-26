package com.phicomm.hu.Utils;

import android.util.Log;

import com.phicomm.hu.FtSDK;

/**
 * Created by le on 2017/9/18.
 */

public class MLog {

    public static void e(String TAG,String msg){
        if (FtSDK.DEBUG){
            Log.e(TAG,msg);
        }
    }

    public static void e(String msg){
        if (FtSDK.DEBUG){
            Log.e("Test",msg);
        }
    }
}
