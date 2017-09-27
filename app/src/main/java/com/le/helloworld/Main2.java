package com.le.helloworld;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.phicomm.hu.Enum.FtType;
import com.phicomm.hu.FtSDK;

/**
 * Created by le on 2017/9/20.
 */

public class Main2 extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FtSDK.init().setType(FtType.POPWINDOW).setTime(3).start(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            FtSDK.post();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FtSDK.stop(this);
    }
}
