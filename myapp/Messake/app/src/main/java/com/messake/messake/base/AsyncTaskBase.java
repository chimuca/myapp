package com.messake.messake.base;

import android.os.AsyncTask;
import android.view.View;

import com.messake.messake.R;
import com.messake.messake.view.LoadingView;

/**
 * Created by messake on 2015/12/26.
 */
public class AsyncTaskBase extends AsyncTask<Integer, Integer, Integer> {
    private LoadingView mLoadingView;
    public AsyncTaskBase(LoadingView loadingView){
        this.mLoadingView=loadingView;
    }
    @Override
    protected Integer doInBackground(Integer... params) {

        return null;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        if(result==1){
            mLoadingView.setVisibility(View.GONE);
        }else{
            mLoadingView.setText(R.string.no_data);
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoadingView.setVisibility(View.VISIBLE);
    }

}
