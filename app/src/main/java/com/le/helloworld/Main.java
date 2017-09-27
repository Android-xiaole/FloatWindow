package com.le.helloworld;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.phicomm.hu.Enum.FtType;
import com.phicomm.hu.Enum.FtGravity;
import com.phicomm.hu.FtSDK;
import com.phicomm.hu.FtService;

public class Main extends Activity implements View.OnClickListener{

    public static final String TAG = Main.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: " );
//        FtSDK.init().setDebug(true).setGravity(FtGravity.BOTTOM).setType(FtType.SERVICE).start(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        FtSDK.init().setDebug(true).setType(FtType.POPWINDOW).setGravity(FtGravity.TOP).setTime(2).start(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){//如果是FtType.POPWINDOW 必须要在这里调这个方法！
            FtSDK.post();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        FtSDK.stop(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FtService.OVERLAY_PERMISSION_REQ_CODE){
            if (FtService.getInstance().checkAlertWindowsPermission(this)){
                FtService.getInstance().startService(this);
            }else{//这里权限未开启的时候 你可以用绑定Activity的popwindow来实现广告窗口
                Toast.makeText(this,"悬浮窗权限未开启，请到权限管理中心重新设置！",Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        startActivity(new Intent(this,Main2.class));
    }
}
