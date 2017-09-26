package com.phicomm.hu;

/**
 * Created by le on 2017/9/20.
 */

public class FtSDKException extends RuntimeException{

    public FtSDKException(){
        super();
    }

    public FtSDKException(String message){
        super(message);
    }

    public FtSDKException(String message,Throwable cause){
        super(message,cause);
    }
}
