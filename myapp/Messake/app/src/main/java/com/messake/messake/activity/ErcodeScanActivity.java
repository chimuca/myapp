package com.messake.messake.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.messake.messake.R;
import com.messake.messake.bean.Constants;
import com.messake.messake.bean.User;
import com.messake.messake.cache.CacheManager;
import com.messake.messake.ercode.CameraManager;
import com.messake.messake.ercode.CaptureActivityHandler;
import com.messake.messake.view.ErcodeScanView;
import com.messake.messake.ercode.InactivityTimer;
import com.messake.messake.utils.UIHelper;

import java.io.IOException;
import java.util.Vector;

/**
 *
 * Created by messake on 2015/12/26.
 */
public class ErcodeScanActivity extends Activity implements SurfaceHolder.Callback {

    private Context mContext;
    private CaptureActivityHandler handler;
    private ErcodeScanView viewfinderView;
    private boolean hasSurface;
    private Vector<BarcodeFormat> decodeFormats;
    private String characterSet;
    private InactivityTimer inactivityTimer;
    private MediaPlayer mediaPlayer;
    private boolean playBeep;
    private static final float BEEP_VOLUME = 0.10f;
    private boolean vibrate;
    private SurfaceView surfaceView;
    private ImageView mBack;
    private View mDialogView;
    private Button mCancle;
    private Button mSure;
    private TextView mUrl;
    private Dialog mDialog;

    private String resultString = "";
    private int screenWidth;

    private String token;
    private User user;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.fragment_news_code_scan);
        mContext = this;
        CameraManager.init(getApplication());
        initControl();

        hasSurface = false;
        inactivityTimer = new InactivityTimer(this);

        user = (User) CacheManager.readObject(this,
                Constants.CACHE_CURRENT_USER);
        token = (String) CacheManager.readObject(this,
                Constants.CACHE_CURRENT_USER_TOKEN);
    }

    private void initControl() {
        viewfinderView = (ErcodeScanView) findViewById(R.id.viewfinder_view);
        surfaceView = (SurfaceView) findViewById(R.id.preview_view);
        mBack = (ImageView) findViewById(R.id.back);

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        screenWidth = dm.widthPixels;

    }

    private void initCustomerDialog() {
        mDialogView = LayoutInflater.from(mContext).inflate(
                R.layout.dialog_scan, null);
        mCancle = (Button) mDialogView.findViewById(R.id.btn_cancle);
        mSure = (Button) mDialogView.findViewById(R.id.btn_sure);
        mUrl = (TextView) mDialogView.findViewById(R.id.tv_url);

        mCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                stop();
                start();

            }
        });

        mSure.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                intent.setData(Uri.parse(resultString));
                intent.setClassName("com.android.browser",
                        "com.android.browser.BrowserActivity");
                startActivity(intent);
                finish();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    @Override
    public void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    /**
     * @param result
     * @param barcode
     */
    public void handleDecode(Result result, Bitmap barcode) {
        inactivityTimer.onActivity();
        playBeepSoundAndVibrate();

        resultString = result.getText();
        Intent resultIntent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putString("result", resultString);
        resultIntent.putExtras(bundle);
        this.setResult(RESULT_OK, resultIntent);

        try {
            // 判断是否是User
            User u = JSON.parseObject(resultString, User.class);
            if (u != null) {

                String type = Constants.INTENT_EXTRA_USER_INFOR_TYPE_ADD_FRIENDS;
                if (user.getId() == u.getId()) {
                    type = Constants.INTENT_EXTRA_USER_INFOR_TYPE_USERINFOR;
                }
                UIHelper.startUserInforActivity(this, user.getId(), u.getId(),
                        token, type);
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Toast.makeText(this, resultString, Toast.LENGTH_SHORT).show();
        // 跳转
        initCustomerDialog();// 必须没错初始化，否则dialog报错
        mUrl.setText(resultString);
        mDialog = new Dialog(mContext, R.style.scan_dialog);
        mDialog.addContentView(mDialogView, new ViewGroup.LayoutParams(screenWidth - 60,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mDialog.show();

    }

    /**
     * 开始扫描
     *
     */
    private void start() {
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            initCamera(surfaceHolder);
        } else {
            surfaceHolder.addCallback(this);
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        }
        decodeFormats = null;
        characterSet = null;

        playBeep = true;
        AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
        if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
            playBeep = false;
        }
        initBeepSound();
        vibrate = true;
    }

    /**
     * 停止扫描
     */
    private void stop() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        CameraManager.get().closeDriver();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        try {
            CameraManager.get().openDriver(surfaceHolder);
        } catch (IOException ioe) {
            return;
        } catch (RuntimeException e) {
            return;
        }
        if (handler == null) {
            handler = new CaptureActivityHandler(this, decodeFormats,
                    characterSet);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    public ErcodeScanView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();

    }

    /**
     * 扫描正确后的震动声音,如果感觉apk大了,可以删除
     */
    private void initBeepSound() {
        if (playBeep && mediaPlayer == null) {
            setVolumeControlStream(AudioManager.STREAM_MUSIC);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnCompletionListener(beepListener);

            AssetFileDescriptor file = getResources().openRawResourceFd(
                    R.raw.beep);
            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(),
                        file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException e) {
                mediaPlayer = null;
            }
        }
    }

    private static final long VIBRATE_DURATION = 200L;

    private void playBeepSoundAndVibrate() {
        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        }
        if (vibrate) {
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(VIBRATE_DURATION);
        }
    }

    /**
     * When the beep has finished playing, rewind to queue up another one.
     */
    private final MediaPlayer.OnCompletionListener beepListener = new MediaPlayer.OnCompletionListener() {
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.seekTo(0);
        }
    };

}
