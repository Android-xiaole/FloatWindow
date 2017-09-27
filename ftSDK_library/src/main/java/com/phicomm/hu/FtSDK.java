package com.phicomm.hu;

import android.app.Activity;
import android.view.View;

import com.phicomm.hu.Enum.FtType;
import com.phicomm.hu.Enum.FtGravity;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;

/**
 * Created by le on 2017/9/18.
 * FtSDK的基本配置都在里面初始化 采用简单的建造者模式
 */

public class FtSDK implements FtBuilder{

    public static FtSDK ftSDK;
    private static FtType type = FtType.SERVICE;//默认是service类型
    public static FtGravity GRAVITY = FtGravity.BOTTOM;//默认显示在屏幕最下方
    public static boolean DEBUG = false;//默认关闭调试
    public static int TIME = 3;//默认最多弹出三次
    public static int COUNT_TIME;//标记关闭弹窗的次数

    public static FloatPopwindow popwindow;
    private Activity context;


    /**
     * 获取单例
     */
    public synchronized static FtSDK init( ){
        if (ftSDK == null){
            ftSDK = new FtSDK();
        }
        return ftSDK;
    }

    @Override
    public FtSDK setType(FtType type) {
        this.type = type;
        return this;
    }

    /**
     * 默认值:GravityEnum.BOTTOM 显示在屏幕的最下方
     * @param gravity
     * @return
     */
    @Override
    public FtSDK setGravity(FtGravity gravity) {
        this.GRAVITY = gravity;
        return this;
    }

    /**
     * 默认值：false 关闭调试
     * @param debug
     * @return
     */
    @Override
    public FtSDK setDebug(boolean debug) {
        this.DEBUG = debug;
        return this;
    }

    /**
     * 设置广告弹出次数
     * @param time
     * @return
     */
    @Override
    public FtSDK setTime(int time) {
        this.TIME = time;
        return this;
    }

    /**
     * 如果是FtType.POPWINDOW 必须在activity onPause的时候调这个方法 以便释放资源
     * @param context
     * @return
     */
    @Override
    public void start(Activity context) {
        this.context = context;
        if (type.value() == FtType.POPWINDOW.value()){//如果是窗口类型，那就是依赖于Activity的
//            if ( !(context instanceof Activity)){
//                throw new FtSDKException("please sure your ftSDK init in a Activity ");
//            }
            EventBus.getDefault().register(ftSDK);//先注册触发事件 方便activity界面渲染完成的时候 显示popwindow
            if (popwindow == null){
                popwindow = new FloatPopwindow(context);
            }
        }else {
            FtService.getInstance().startService(context);
        }
    }

    /**
     * 如果是FtType.POPWINDOW 必须在activity onPause的时候调这个方法 以便释放资源
     * @param context
     */
    public static void stop(Activity context){
        if (type.value() == FtType.POPWINDOW.value()){
            EventBus.getDefault().unregister(ftSDK);
            if (popwindow!=null){
                popwindow.dismissPop();
                popwindow = null;
                System.gc();
            }
        }else{
            FtService.getInstance().stopService(context);
        }
    }

    /**
     * 如果是FtType.POPWINDOW 这里是释放资源
     */
    public static void release(){
        if (popwindow!=null){
            popwindow = null;
            System.gc();
        }
    }

    /**
     * 如果是FtType.POPWINDOW 必须在activity onWindowFocusChanged获取焦点的时候调这个方法 才能将窗口显示出来
     */
    public static void post(){
        if (type.value() == FtType.POPWINDOW.value()){
            EventBus.getDefault().post(new Message());
        }
    }

    // 接收方处理消息（处理数据）-- 主线程中执行
    @Subscribe(threadMode = ThreadMode.MainThread)
    public void onMainEventBus(Message msg) {
        if (popwindow == null){
            throw new FtSDKException("popwindow is null,are you have start FtSDK?");
        }
        popwindow.showPop(new View(context));
    }


}
