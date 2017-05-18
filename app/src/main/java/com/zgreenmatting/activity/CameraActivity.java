package com.zgreenmatting.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.igoda.dao.entity.MattingImage;
import com.seu.magicfilter.MagicEngine;
import com.seu.magicfilter.filter.advanced.MagicAAFilter;
import com.seu.magicfilter.filter.base.gpuimage.GPUImageFilter;
import com.seu.magicfilter.filter.helper.MagicFilterType;
import com.seu.magicfilter.widget.MagicCameraView;
import com.seu.magicfilter.widget.base.MagicBaseView;
import com.zgreenmatting.BaseActivity;
import com.zgreenmatting.R;
import com.zgreenmatting.adapter.FilterAdapter;
import com.zgreenmatting.download.DownloadManager;
import com.zgreenmatting.download.IDownloadStateListener;
import com.zgreenmatting.download.status.DownloadStatus;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;

public class CameraActivity extends BaseActivity implements View.OnClickListener, SeekBar.OnSeekBarChangeListener, FilterAdapter.onFilterChangeListener{

    //相机
    @BindView(R.id.cameraView)
    MagicCameraView cameraView;
    @BindView(R.id.btn_camera_mode)
    ImageView btn_camera_mode;//相机mode
    @BindView(R.id.btn_camera_switch)
    ImageView btn_camera_switch;//相机切换

    @BindView(R.id.btn_camera_beauty)
    SeekBar btn_camera_beauty;//透明度

    @BindView(R.id.btn_album)
    TextView btn_album;//相册
    @BindView(R.id.btn_camera_shutter)
    ImageView btn_camera_shutter;//拍照

    @BindView(R.id.btn_camera_filter)
    TextView btn_camera_filter;//背景

    @BindView(R.id.btn_camera_closefilter)
    ImageView btn_camera_closefilter;//关闭背景
    @BindView(R.id.new_layout_filter)
    LinearLayout mFilterLayout;//背景布局
    @BindView(R.id.filter_listView)
    RecyclerView mFilterListView;//背景列表
    private FilterAdapter mAdapter;
    private List<MattingImage> data;

    private MagicEngine magicEngine;
    private final int MODE_PIC = 1;
    private final int MODE_VIDEO = 2;
    private int mode = MODE_PIC;
    private ObjectAnimator animator;
    private SoundPool soundPool;
    private Map<Integer, Integer> soundMap;

    //下载
    IDownloadStateListener iDownloadStateListener;
    DownloadManager downloadManager;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_camera;
    }

    /**
     * onCreate
     */
    @Override
    protected void preInitView() {
        //初始化相机
        MagicEngine.Builder builder = new MagicEngine.Builder();
        magicEngine = builder.build(cameraView);

        //初始化背景列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFilterListView.setLayoutManager(linearLayoutManager);
        data = new ArrayList<>();
        mAdapter = new FilterAdapter(this, data);
        mAdapter.setOnFilterChangeListener(this);
        mFilterListView.setAdapter(mAdapter);

        animator = ObjectAnimator.ofFloat(btn_camera_shutter,"rotation",0,360);
        animator.setDuration(500);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        Point screenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(screenSize);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) cameraView.getLayoutParams();
        params.width = screenSize.x;
        params.height = screenSize.x * 4 / 3;
        cameraView.setLayoutParams(params);

        btn_camera_filter.setOnClickListener(this);
        btn_camera_closefilter.setOnClickListener(this);
        btn_camera_shutter.setOnClickListener(this);
        btn_camera_switch.setOnClickListener(this);
        btn_camera_mode.setOnClickListener(this);
        btn_album.setOnClickListener(this);
        btn_camera_beauty.setOnSeekBarChangeListener(this);

        try {
            /**
             * 下载状态回调
             */
            iDownloadStateListener = new IDownloadStateListener() {
                @Override
                public void onPrepare(final Object entity, final long size) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int progress = (int)(((double)size / (double)((MattingImage)entity).getSize())  * 100);
                            ((MattingImage)entity).setDownloadSize(size);
                            updateView(entity, DownloadStatus.WAIT, progress);
                        }
                    });
                }

                @Override
                public void onProcess(final Object entity, final long size) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int progress = (int)(((double)size / (double)((MattingImage)entity).getSize())  * 100);
                            ((MattingImage)entity).setDownloadSize(size);
                            updateView(entity, DownloadStatus.DLING, progress);
                        }
                    });
                }

                @Override
                public void onFinish(final Object entity, final String savePath) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateView(entity, DownloadStatus.DONE, savePath);
                        }
                    });
                }

                @Override
                public void onFailed(final Object entity, final String msg) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateView(entity, DownloadStatus.ERROR, msg);
                        }
                    });
                }

                @Override
                public void onPause(final Object entity, final long size) {
                    Log.i("tag", "onProcess " + entity.toString() + ",size=" + size);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int progress = (int)(((double)size / (double)((MattingImage)entity).getSize())  * 100);
                            ((MattingImage)entity).setDownloadSize(size);
                            updateView(entity, DownloadStatus.PAUSE, progress);
                        }
                    });
                }

                @Override
                public void onCancel(final Object entity) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateView(entity, DownloadStatus.NONE);
                        }
                    });
                }
            };
            downloadManager = DownloadManager.INSTANCE.init(CameraActivity.this);
            downloadManager.registerStateListener(iDownloadStateListener);
            downloadManager.onStart();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 更新item
     * @param entity
     * @param status
     * @param objects
     */
    protected void updateView(Object entity, DownloadStatus status, Object... objects) {
        ((MattingImage)entity).setDownloadState(status.getValue());
        int itemIdx = data.indexOf(entity);
        mAdapter.notifyItemChanged(itemIdx);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downloadManager != null) {
            downloadManager.unRegisterStateListener(iDownloadStateListener);
            downloadManager.onDestroy();
        }
    }

    /**
     * onResume
     */
    @Override
    protected void preInitData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_camera_mode:
                switchMode();
                break;
            case R.id.btn_camera_shutter:

                if (PermissionChecker.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(CameraActivity.this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, 1);
                } else {
                    takePhoto();
                }
                break;
            case R.id.btn_camera_filter:
                showFilters();
                break;
            case R.id.btn_camera_switch:
                magicEngine.switchCamera();
                break;
            case R.id.btn_camera_closefilter:
                hideFilters();
                break;
            case R.id.btn_album:
                startActivity(new Intent(CameraActivity.this,GalleryActivity.class));
                break;
        }
    }

    private void initSP() throws Exception{
        //创建一个SoundPool对象，该对象可以容纳5个音频流
        AudioManager systemService = (AudioManager) CameraActivity.this.getSystemService(Context.AUDIO_SERVICE);
        //初始化soundPool 对象,第一个参数是允许有多少个声音流同时播放,第2个参数是声音类型,第三个参数是声音的品质
        soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
        soundMap=new HashMap<>();
        soundMap.put(1, soundPool.load(CameraActivity.this, R.raw.tackphoto, 1));
        int streamVolume = systemService.getStreamVolume(AudioManager.STREAM_MUSIC);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        soundPool.play(soundMap.get(1), streamVolume, streamVolume, 1, 0, 1f);
    }

    private void switchMode(){
        if(mode == MODE_PIC){
            mode = MODE_VIDEO;
            btn_camera_mode.setImageResource(R.mipmap.icon_camera);
        }else{
            mode = MODE_PIC;
            btn_camera_mode.setImageResource(R.mipmap.icon_video);
        }
    }

    private void takePhoto(){
        try {
            initSP();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ZGreenMatting");
        Log.e("test","file : "+getOutputMediaFile());
        Log.e("test","file : "+mediaStorageDir.getPath() + File.separator);
        magicEngine.savePicture(getOutputMediaFile(),null);
    }
    public File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "ZGreenMatting");
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }
    private void showFilters(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterLayout, "translationY", mFilterLayout.getHeight(), 0);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                btn_camera_shutter.setClickable(false);
                mFilterLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        animator.start();
    }

    private void hideFilters(){
        ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterLayout, "translationY", 0 ,  mFilterLayout.getHeight());
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mFilterLayout.setVisibility(View.INVISIBLE);
                btn_camera_shutter.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mFilterLayout.setVisibility(View.INVISIBLE);
                btn_camera_shutter.setClickable(true);
            }
        });
        animator.start();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (seekBar == btn_camera_beauty){
            int params = btn_camera_beauty.getProgress();
            Log.e("params",params+"");
            MagicAAFilter filter = (MagicAAFilter)cameraView.getFilter();
            if(filter != null){
                float value = params/10f;
                Log.e("value",value+"");
                filter.setParams(value);
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onFilterChanged() {
        magicEngine.setFilter();
    }

    @Override
    public void onChangePostion(final int position) {
        MagicAAFilter filter = (MagicAAFilter)cameraView.getFilter();
        if(filter != null) {
            if(position!=0){
                magicEngine.setFilterListener(new MagicBaseView.OnFilterChangedListener() {
                    @Override
                    public void filterChange(GPUImageFilter filter) {
                        if(filter instanceof MagicAAFilter){
                            MagicAAFilter aafilter =(MagicAAFilter) filter;
                            aafilter.setAsset(position+".jpg");
                        }
                    }
                });
                Log.e("photo:",position+".jpg");
            }
        }
        hideFilters();
    }
}