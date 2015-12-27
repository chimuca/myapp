package com.messake.messake.api;

import android.os.Looper;

import org.apache.http.Header;

import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.ByteArrayInputStream;

/**
 *
 * Created by messake on 2015/12/26.
 */
public class OperationResponseHandler  extends AsyncHttpResponseHandler {

    private Object[] args;

    public OperationResponseHandler(Looper looper, Object... args) {
        super(looper);
        this.args = args;
    }

    public OperationResponseHandler(Object... args) {
        this.args = args;
    }

    @Override
    public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
        onFailure(arg0, arg3.getMessage(), args);
    }

    public void onFailure(int code, String errorMessage, Object[] args) {
    }

    @Override
    public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
        try {
            onSuccess(arg0, new ByteArrayInputStream(arg2), args);
        } catch (Exception e) {
            e.printStackTrace();
            onFailure(arg0, e.getMessage(), args);
        }
    }

    public void onSuccess(int code, ByteArrayInputStream is, Object[] args)
            throws Exception {

    }
}
