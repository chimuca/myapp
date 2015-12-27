package com.messake.messake.view;

/**
 * Created by messake on 2015/12/26.
 */
public interface DialogControl {

    public abstract void hideWaitDialog();

    public abstract WaitDialog showWaitDialog();

    public abstract WaitDialog showWaitDialog(int resid);

    public abstract WaitDialog showWaitDialog(String text);
}
