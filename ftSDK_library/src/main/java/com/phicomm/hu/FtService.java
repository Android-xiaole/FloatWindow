package com.phicomm.hu;

import android.app.Activity;
import android.app.AppOpsManager;
import android.app.Service;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.view.WindowManager;
import android.widget.Toast;
import android.provider.Settings;

import com.phicomm.hu.Enum.FtGravity;
import com.phicomm.hu.Utils.HtmlIO;
import com.phicomm.hu.Utils.MLog;
import com.phicomm.hu.Utils.ParserUrl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by le on 2017/9/14.
 */

public class FtService extends Service{

    public static final String TAG = FtService.class.getSimpleName();
    WindowManager.LayoutParams wmParams;
    WindowManager mWindowManager;
    FloatView myFloatView;

    public static FtService ftService;
    boolean viewAdded;
    FtGravity mGravity = FtGravity.BOTTOM;

    public synchronized static FtService getInstance(){
        if(ftService == null){
            ftService = new FtService();
        }
        return ftService;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        MLog.e(TAG, "onCreate: ");
        super.onCreate();
        createFloatView();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        MLog.e(TAG, "onStartCommand: " );
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        MLog.e(TAG, "onStart: " );
        super.onStart(intent, startId);
        refreshView();
    }

    private void refreshView(){
        MLog.e(TAG, "refreshView: " );
        myFloatView.startTask();
        if (viewAdded){
            mWindowManager.updateViewLayout(myFloatView,wmParams);
        }else{
            mWindowManager.addView(myFloatView,wmParams);
            viewAdded = true;
        }
    }

    public void removeView(){
        if (viewAdded){
            myFloatView.stopTask();
            mWindowManager.removeView(myFloatView);
            viewAdded = false;
        }
    }

    private void createFloatView() {
        MLog.e(TAG, "createFloatView: ");
        wmParams = new WindowManager.LayoutParams();
        //获取的是WindowManagerImpl.CompatModeWrapper
        mWindowManager = (WindowManager)getApplication().getSystemService(WINDOW_SERVICE);
        //设置window type
//        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR ;
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = FtSDK.GRAVITY.value();
        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = 0;
        wmParams.y = 0;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.MATCH_PARENT;

        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        ParserUrl.parser(getApplicationContext());
        myFloatView = new FloatView(getApplicationContext(),this);
        mWindowManager.addView(myFloatView, wmParams);
        viewAdded = true;
    }

    @Override
    public void onDestroy() {
        MLog.e(TAG, "onDestroy: " );
        super.onDestroy();
        myFloatView.stopTask();
        removeView();
        myFloatView = null;
        System.gc();
    }

    /**
     * 配置位置参数
     * @return
     */
    public void init(FtGravity gravity){
        mGravity = gravity;
    }

    Intent intent;
    /**
     * 开启服务
     * @param context
     */
    public void startService(Activity context){
        askForPermission(context);
    }

    /**
     * 关闭服务
     * @param context
     */
    public void stopService(Activity context){
        if (intent!=null){
            context.stopService(intent);
        }
    }

    /***———————————————悬浮窗权限检查 用于适配高版本的手机————————————————*/
    public static final int OVERLAY_PERMISSION_REQ_CODE = 1;
    /**
     * 请求用户给予悬浮窗的权限
     */
    public void askForPermission(Activity context) {
        if (!checkAlertWindowsPermission(context)) {
            Toast.makeText(context, "当前无权限，请授权！", Toast.LENGTH_SHORT).show();
            try {//ACTION_MANAGE_OVERLAY_PERMISSION
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,Uri.parse("package:" + context.getPackageName()));
                context.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
            } catch (ActivityNotFoundException e) {////有的手机没有这个设置界面，那就跳转到应用设置里面
                e.printStackTrace();
                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);//ACTION_MANAGE_WRITE_SETTINGS
//                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                intent.setData(uri);
                    context.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                } catch (ActivityNotFoundException e1) {
                    e1.printStackTrace();
                    try {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        context.startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
                    } catch (ActivityNotFoundException e2) {
                        Toast.makeText(context, "您的手机有点特别，未找到悬浮窗设置界面", Toast.LENGTH_LONG).show();
                        e2.printStackTrace();
                    }
                }
            }
        } else {
            intent =new Intent(context,FtService.class);
            context.startService(intent);
        }
    }

    /**
     * 判断 悬浮窗口权限是否打开
     * @param context
     * @return true 允许  false禁止
     */
    public boolean checkAlertWindowsPermission(Context context) {
        try {
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            if (object == null) {
                return false;
            }
            Class localClass = object.getClass();
            Class[] arrayOfClass = new Class[3];
            arrayOfClass[0] = Integer.TYPE;
            arrayOfClass[1] = Integer.TYPE;
            arrayOfClass[2] = String.class;
            Method method = localClass.getMethod("checkOp", arrayOfClass);
            if (method == null) {
                return false;
            }
            Object[] arrayOfObject1 = new Object[3];
            arrayOfObject1[0] = 24;
            arrayOfObject1[1] = Binder.getCallingUid();
            arrayOfObject1[2] = context.getPackageName();
            int m = ((Integer) method.invoke(object, arrayOfObject1));
            return m == AppOpsManager.MODE_ALLOWED;
        } catch (Exception ex) {

        }
        return false;
    }
}
