package com.phicomm.hu;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.phicomm.hu.Utils.MLog;
import com.phicomm.hu.Utils.ParserUrl;

/**
 * Created by le on 2017/9/15.
 */

public class FloatPopwindow extends PopupWindow{

    FloatView floatView;

    public FloatPopwindow(Context context){
        ParserUrl.parser(context);
        floatView = new FloatView(context,this);
        setContentView(floatView);
        setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void showPop(View v){
        if (!this.isShowing()){
            if (FtSDK.COUNT_TIME >= FtSDK.TIME){
                return;
            }
            floatView.startTask();
            this.showAtLocation(v, FtSDK.GRAVITY.value(),0,0);
        }
    }

    public void dismissPop(){
        this.dismiss();
        floatView.stopTask();
    }
}
