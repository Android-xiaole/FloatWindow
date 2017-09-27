package com.phicomm.hu;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.adwindow.R;
import com.phicomm.hu.Utils.DensityUtil;
import com.phicomm.hu.Utils.MLog;

/**
 * Created by le on 2017/9/14.
 */

public class FloatView extends RelativeLayout implements View.OnClickListener{

    final static String TAG = FloatView.class.getSimpleName();
    Context context;
    Object objContext;
    LayoutParams layoutParams;
    ImageView iv_close;
    WebView webView;

    boolean isRun;

    public FloatView(final Context context, Object objContext) {
        super(context);
//        Log.e(TAG, "MyFloatView: "+url);
        this.context = context;
        this.objContext = objContext;
//        this.setOrientation(VERTICAL);
//        this.setBackgroundColor(Color.BLACK);

        webView = new WebView(context);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setAllowFileAccess(true);// 设置允许访问文件数据
        startTask();
        String url = "file://"+context.getFilesDir().getPath()+"/ichile.html";
        webView.loadUrl(url);
//        webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);
        webView.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                MLog.e("shouldOverrideUrlLoading:"+url);
                if (!url.contains("biedese")){//这里跳转到系统浏览器
                    Intent intent= new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    context.startActivity(intent);
                    return true;
                }
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
                MLog.e("onLoadResource:"+url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){


        });
        int hei = DensityUtil.dp2px(context,100);
        layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,hei);
        this.addView(webView,layoutParams);

        iv_close = new ImageView(context);
        iv_close.setOnClickListener(this);
//        iv_close.setImageResource(MResource.getIdByName(context,"drawable","img_close_whilte"));
        iv_close.setImageResource(R.drawable.img_close);
        int size = DensityUtil.dp2px(context,25);
        layoutParams = new LayoutParams(size,size);
//        layoutParams.gravity = Gravity.RIGHT;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int screenWidth = wm.getDefaultDisplay().getWidth();
        layoutParams.leftMargin = screenWidth - size;
        this.addView(iv_close,layoutParams);
    }

    @Override
    public void onClick(View view) {
        if (view == iv_close){
            FtSDK.COUNT_TIME++;
            if (objContext instanceof FtService){
                ((FtService)objContext).removeView();
            }
            if (objContext instanceof FloatPopwindow) {
                ((FloatPopwindow)objContext).dismissPop();
            }
        }
    }

    public void startTask(){
        if (runnable!=null&& isRun == false) {
            handler.post(runnable);
            isRun = true;
            MLog.e(TAG, "startTask: task start!" );
        }
    }

    public void stopTask(){
        if (runnable!=null&&isRun == true){
            MLog.e(TAG, "stopTask: task stop!");
            handler.removeCallbacks(runnable);
            isRun = false;
//            runnable = null;
//            handler = null;
            System.gc();
        }
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {

        @Override
        public void run() {
            try {
                handler.postDelayed(this, 10000);
                webView.reload();
                Log.e(TAG, "run: 任务进行中..." );
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                System.out.println("exception...");
            }
        }
    };
}
