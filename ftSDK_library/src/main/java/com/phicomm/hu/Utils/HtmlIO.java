package com.phicomm.hu.Utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

/**
 * Created by le on 2017/9/19.
 */

public class HtmlIO {

    public static String readString(Context context) throws IOException {
        AssetManager assets = context.getAssets();
        StringBuffer sb = new StringBuffer();
        BufferedReader br = new BufferedReader (new InputStreamReader(assets.open("ichile.html")));
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
            sb.append("\n");
        }
        br.close();
        return sb.toString();
    }

    public static void writeString(Context context,String newHtml) throws IOException {
        FileOutputStream fos = context.openFileOutput("ichile.html", Context.MODE_PRIVATE);
//        byte[] b = new byte[1024];
//        byte[] bytes = newHtml.getBytes();
//        fos.write(b,0,bytes.length);
//        fos.close();
        PrintStream ps = new PrintStream(fos);
        ps.print(newHtml);
        ps.close();
        String[] strings = context.fileList();
        for (String filePath:strings) {
            MLog.e("writeString 本地包含的文件："+filePath);
        }
        MLog.e("writeString：写入完成！");
    }
}
