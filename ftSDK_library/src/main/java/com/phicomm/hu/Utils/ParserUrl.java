package com.phicomm.hu.Utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.phicomm.hu.FtSDKException;

import java.io.IOException;

/**
 * Created by le on 2017/9/19.
 */

public class ParserUrl {

    public static final String TAG = ParserUrl.class.getSimpleName();

    public static void parser(Context context){
        String ftSDKKey = null;
        ApplicationInfo appInfo;
        try {
            appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            ftSDKKey = appInfo.metaData.getString("FtSDKKey");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            throw new FtSDKException("you have not register FtSDKKey in your AndroidManifest.xml ");
        }
        if (ftSDKKey == null){
            throw new FtSDKException("you have not register FtSDKKey in your AndroidManifest.xml ");
        }
        String url;
        if (ftSDKKey.startsWith("v")){
            url = "http://wap-cpv.biedese.cn/?id="+ftSDKKey.substring(1);
        }else if (ftSDKKey.startsWith("m")){
            url = "http://wap-cpm.biedese.cn/?id="+ftSDKKey.substring(1);
        }else{//get key fail
            throw new FtSDKException("please check your AndroidManifest.xml  <meta-data android:name=\"FtSDKKey\" android:value=\"your key\"/> your key is right?");
        }
        try {
            String strHtml = HtmlIO.readString(context);
            MLog.e("createFloatView oldHtml:"+strHtml);
            StringBuffer sb = new StringBuffer();
            String[] srcs = strHtml.split("src");
            sb.append(srcs[0]+"src=\""+url+"\"");
            sb.append(srcs[1].split("\"")[2]);
            MLog.e(TAG,"createFloatView newHtml："+sb.toString());
            HtmlIO.writeString(context,sb.toString());
        } catch (IOException e) {
            MLog.e(e+"");
            e.printStackTrace();
        }
    }
}
