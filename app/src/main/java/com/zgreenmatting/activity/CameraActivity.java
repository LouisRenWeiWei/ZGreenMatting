package com.zgreenmatting.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.listener.Listener;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.igoda.dao.entity.MattingImage;
import com.igoda.dao.entity.TempImage;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.rey.material.widget.Slider;
import com.seu.magicfilter.MagicEngine;
import com.seu.magicfilter.filter.advanced.MagicAAFilter;
import com.seu.magicfilter.filter.base.gpuimage.GPUImageFilter;
import com.seu.magicfilter.helper.SavePictureTask;
import com.seu.magicfilter.widget.MagicCameraView;
import com.seu.magicfilter.widget.base.MagicBaseView;
import com.zgreenmatting.BaseActivity;
import com.zgreenmatting.R;
import com.zgreenmatting.adapter.LinerVerticalHorizonSpace;
import com.zgreenmatting.adapter.FilterAdapter;
import com.zgreenmatting.blservice.MattingImageService;
import com.zgreenmatting.entity.DownloadStatus;
import com.zgreenmatting.entity.ProgressInfo;
import com.zgreenmatting.utils.AppData;
import com.zgreenmatting.utils.GetBigFileMD5;
import com.zgreenmatting.utils.JSONUtil;
import com.zgreenmatting.utils.NetworkUtils;
import com.zgreenmatting.utils.NumberUtils;
import com.zgreenmatting.utils.PhoneUtil;
import com.zgreenmatting.utils.RequestUtil;
import com.zgreenmatting.utils.SDUtils;
import com.zgreenmatting.utils.ToastUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;

public class CameraActivity extends BaseActivity implements View.OnClickListener, FilterAdapter.ItemOperation,
        MagicBaseView.OnFilterChangedListener,MagicCameraView.OnCameraInitedListener {
    //相机
    @BindView(R.id.cameraView)
    MagicCameraView cameraView;
    @BindView(R.id.btn_camera_mode)
    ImageView btn_camera_mode;//相机mode
    @BindView(R.id.btn_camera_switch)
    ImageView btn_camera_switch;//相机切换

    @BindView(R.id.btn_camera_beauty)
    Slider btn_camera_beauty;//透明度

    @BindView(R.id.btn_album)
    TextView btn_album;//相册
    @BindView(R.id.btn_camera_shutter)
    ImageView btn_camera_shutter;//拍照

    @BindView(R.id.btn_camera_filter)
    TextView btn_camera_filter;//背景

    @BindView(R.id.btn_camera_closefilter)
    ImageView btn_camera_closefilter;//关闭背景
    @BindView(R.id.ll_filterLayout)//
    LinearLayout ll_filterLayout;//背景布局
    @BindView(R.id.filter_listView)
    RecyclerView mFilterListView;//背景列表
    private FilterAdapter mAdapter;
    private List<MattingImage> data;

    //下载view
    @BindView(R.id.ll_download_info)
    LinearLayout ll_download_info;
    @BindView(R.id.tv_download_speed)
    TextView tv_download_speed;//下载速度
    @BindView(R.id.progress)
    ProgressBar progress;//下载进度条
    @BindView(R.id.tv_progress_txt)
    TextView tv_progress_txt;//下载进度

    private MagicEngine magicEngine;
    private final int MODE_PIC = 1;
    private final int MODE_VIDEO = 2;
    private int mode = MODE_PIC;
    private ObjectAnimator animator;
    private SoundPool soundPool;
    private Map<Integer, Integer> soundMap;

    //下载
//    IDownloadStateListener iDownloadStateListener;
//    DownloadManager downloadManager;
    FileDownloadListener fileDownloadListener;


    MattingImage orginalImage;

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
        magicEngine.setFilterListener(this);
        cameraView.setOnCameraInitedListener(this);


        //初始化背景列表
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mFilterListView.setLayoutManager(linearLayoutManager);
        data = new ArrayList<>();
        orginalImage = new MattingImage();

        initDownload();
        mAdapter = new FilterAdapter(this, data);
        mAdapter.setItemOperation(this);
        mFilterListView.addItemDecoration(new LinerVerticalHorizonSpace(0,PhoneUtil.dip2px(mContext,5)));
        mFilterListView.setAdapter(mAdapter);

        animator = ObjectAnimator.ofFloat(btn_camera_shutter, "rotation", 0, 360);
        animator.setDuration(500);
        animator.setRepeatCount(ValueAnimator.INFINITE);

        btn_camera_filter.setOnClickListener(this);
        btn_camera_closefilter.setOnClickListener(this);
        btn_camera_shutter.setOnClickListener(this);
        btn_camera_switch.setOnClickListener(this);
        btn_camera_mode.setOnClickListener(this);
        btn_album.setOnClickListener(this);
        btn_camera_beauty.setValue(5,false);//设置默认为5
        btn_camera_beauty.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                MagicAAFilter filter = (MagicAAFilter) cameraView.getFilter();
                if (filter != null) {
                    filter.setParams(newValue / 10f);
                }
            }
        });

    }

    //初始化下载
    private void initDownload() {

        fileDownloadListener = new FileDownloadListener() {
            private int preBytes = 0;
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                preBytes = 0;
                updateView((MattingImage)task.getTag(),DownloadStatus.PAUSEING);
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                //soFarBytes ： 当前文件已经下载的大小， totalBytes：当前文件的大小
                updateView((MattingImage)task.getTag(), DownloadStatus.DLING);
                tv_download_speed.setText(NumberUtils.format((soFarBytes-preBytes)/(1024f))+"KB");
                preBytes = soFarBytes;
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                MattingImage tmp = (MattingImage)task.getTag();
                tmp.setSdPath(task.getPath());
                updateView(tmp,DownloadStatus.DONE);
                int index = 0;
                for(MattingImage item: data){
                    if(tmp.getUrl().equalsIgnoreCase(item.getUrl())){
                        break;
                    }
                    index ++;
                }
                mAdapter.notifyItemChanged(index);
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                updateView((MattingImage)task.getTag(),DownloadStatus.PAUSE);
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                updateView((MattingImage)task.getTag(),DownloadStatus.ERROR);
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                //updateView((MattingImage)task.getTag(),DownloadStatus.ERROR);
            }
        };
    }

    /**
     * onResume 初始化数据
     */
    @Override
    protected void preInitData() {
        getBackdrops();
        updateProgressView();
    }

    /**
     * 下载完成更新item数据
     * 更新下载进度： 下载进度是下载照片的数量作为计算单位，而不是照片大大小
     * @param entity
     * @param status
     */
    protected void updateView(MattingImage entity, DownloadStatus status) {
        entity.setDownloadState(status.getValue());
        MattingImageService.getInstance().update(entity);
        updateProgressView();
    }

    private void updateProgressView(){
        //更新下载进度
        ProgressInfo progressInfo = MattingImageService.getInstance().getProgressInfo();
        if(progressInfo!=null){
            if(progressInfo.getTotal()!=0){
                progress.setMax((int)progressInfo.getTotal());
                progress.setProgress((int)progressInfo.getFinished());
            }else {
                progress.setProgress(0);
                progress.setMax(0);
            }
            tv_progress_txt.setText(progressInfo.getFinished()+"/"+progressInfo.getTotal());
            if(progressInfo.getTotal()==progressInfo.getFinished()){
                tv_download_speed.setText("0KB");
                //ll_download_info.setVisibility(View.GONE);
            }else {
                //ll_download_info.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //click
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_camera_mode:
                switchMode();
                break;
            case R.id.btn_camera_shutter:
                //这个地方检查一下，本地如果超过15个未上传图片，提示用户打开网络进行上传操作
                int unuploadCount = MattingImageService.getInstance().getLocalUnuploadCount();
                if (unuploadCount >= 15) {
                    //如果本地有离线数据就立马上传一次
                    if (NetworkUtils.isNetworkAvailable(mContext)) {
                        uploadPicInfo();
                    }else {
                        ToastUtils.showCustomerToast(mContext, "您无法拍照，请让手机连上网络");
                    }
                    return;
                }

                if (PermissionChecker.checkSelfPermission(CameraActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(CameraActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
                startActivity(new Intent(CameraActivity.this, GalleryActivity.class));
                break;
        }
    }
    //初始化声音
    private void initSoundPool(){
        try {
            //创建一个SoundPool对象，该对象可以容纳5个音频流
            AudioManager systemService = (AudioManager) CameraActivity.this.getSystemService(Context.AUDIO_SERVICE);
            //初始化soundPool 对象,第一个参数是允许有多少个声音流同时播放,第2个参数是声音类型,第三个参数是声音的品质
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
            soundMap = new HashMap<>();
            soundMap.put(1, soundPool.load(CameraActivity.this, R.raw.tackphoto, 1));
            int streamVolume = systemService.getStreamVolume(AudioManager.STREAM_MUSIC);
            Thread.sleep(1000);
            soundPool.play(soundMap.get(1), streamVolume, streamVolume, 1, 0, 1f);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //切换镜头模式
    private void switchMode() {
        if (mode == MODE_PIC) {
            mode = MODE_VIDEO;
            btn_camera_mode.setImageResource(R.mipmap.icon_camera);
        } else {
            mode = MODE_PIC;
            btn_camera_mode.setImageResource(R.mipmap.icon_video);
        }
    }
    //拍照
    private void takePhoto() {
        initSoundPool();
        magicEngine.savePicture(getOutputMediaFile(), new SavePictureTask.OnPictureSaveListener() {
            @Override
            public void onSaved(String result) {
                //拍照完成，保存离线数据，上传数据
                if (!TextUtils.isEmpty(result)) {
                    saveTmpPicInfo(result);
                }
            }
        });
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

    private void showFilters() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(ll_filterLayout, "translationY", ll_filterLayout.getHeight(), 0);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                btn_camera_shutter.setClickable(false);
                ll_filterLayout.setVisibility(View.VISIBLE);
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

    private void hideFilters() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(ll_filterLayout, "translationY", 0, ll_filterLayout.getHeight());
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
                ll_filterLayout.setVisibility(View.INVISIBLE);
                btn_camera_shutter.setClickable(true);
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                ll_filterLayout.setVisibility(View.INVISIBLE);
                btn_camera_shutter.setClickable(true);
            }
        });
        animator.start();
    }

    private int currentItem = 0;
    //receycleview 的item点击
    @Override
    public void onItemClick(final int position) {
        currentItem = position;
        if(position!=0){
            MattingImage item = data.get(position);
            if(TextUtils.isEmpty(item.getSdPath())||item.getDownloadState()!=DownloadStatus.DONE.getValue()){
                ToastUtils.showCustomerToast(mContext,"请等待下载");
                return;
            }
            //检查本地文件是否存在
            File tmp = new File(item.getSdPath());
            if(tmp.exists()){
                magicEngine.setFilter(true);
            }else {
                String sdPaht = item.getSdPath();
                item.setSdPath("");
                item.setDownloadState(DownloadStatus.NONE.getValue());
                MattingImageService.getInstance().update(item);
                mAdapter.notifyItemChanged(position);
                updateProgressView();
                FileDownloader.getImpl().create(item.getUrl()).setPath(sdPaht)
                        .setTag(item)
                        .setListener(fileDownloadListener)
                        .ready();
                FileDownloader.getImpl().start(fileDownloadListener, true);
            }
        }else{
            magicEngine.setFilter(false);
        }
        //原图不处理
        hideFilters();
    }

    @Override
    public void filterChange(GPUImageFilter filter) {
        if(filter!=null){
            if (filter instanceof MagicAAFilter) {
                try {
                    MagicAAFilter aafilter = (MagicAAFilter) filter;
                    if(currentItem!=0){//0 是把filter清空了
                        MattingImage item = data.get(currentItem);
                        aafilter.setBitmap(BitmapFactory.decodeStream(new FileInputStream(item.getSdPath())));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onCameraInited() {
        //magicEngine.setFilter();
    }


    //////////////////
    //获取背景数据
    private void getBackdrops() {
        StringRequest request = new StringRequest(Request.Method.POST, RequestUtil.backdrops, new Listener<String>() {
            @Override
            public void onSuccess(final String response) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //[{"value":"cc9a1852b09bd828ae2fefe3889dc44a","ext":"jpg","url":"http://tv.xxpost.com/camera/backdrop/cc9a1852b09bd828ae2fefe3889dc44a.jpg","createTime":"2017-05-19 16:27"}]
                            JSONArray data = new JSONArray(response);
                            List<MattingImage> mattingImages = JSONUtil.toBeans(data, MattingImage.class);
                            //
                            List<String> exist = new ArrayList<String>();
                            for (MattingImage item : mattingImages) {
                                item.setName(item.getValue()+"."+(item.getExt()!=null?item.getExt():"jpg"));
                                //这里修改数据
                                MattingImageService.getInstance().save(item);
                                exist.add(item.getUrl());
                            }
                            //需要删除已经被服务器删除的图片
                            MattingImageService.getInstance().deleteWithout(exist);
                            getBackdropsFromLocal();//更新ui数据
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

            @Override
            public void onError(VolleyError error) {
                getBackdropsFromLocal();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("account", AppData.getString(mContext, AppData.ACCOUNT));
                map.put("device_id", PhoneUtil.getDevicesID(mContext));
                return map;
            }
        };
        Volley.getRequestQueue().add(request);
    }

    private void getBackdropsFromLocal() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                data.clear();
                data.add(orginalImage);
                data.addAll(MattingImageService.getInstance().getList());
                mAdapter.notifyDataSetChanged();
                updateProgressView();
                monitorUndownload();
            }
        });
    }

    private void monitorUndownload(){
        for(int i=1;i<data.size();i++){
            MattingImage item = data.get(i);
            if (item.getDownloadState() != DownloadStatus.DONE.getValue()) {
                String sdPath = SDUtils.getImagePath()+item.getName();
                FileDownloader.getImpl().create(item.getUrl()).setPath(sdPath)
                        .setTag(item)
                        .setListener(fileDownloadListener)
                        .ready();
            }
        }
        FileDownloader.getImpl().start(fileDownloadListener, true);// 串行执行该队列
    }

    //上传本地离线数据
    private void uploadPicInfo() {
        if (NetworkUtils.isNetworkAvailable(mContext)){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final TempImage tempImage = MattingImageService.getInstance().getNextTmpImage();
                    if (NetworkUtils.isNetworkAvailable(mContext) && tempImage != null) {
                        StringRequest request = new StringRequest(Request.Method.POST, RequestUtil.sendImageInfo, new Listener<String>() {
                            @Override
                            public void onSuccess(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if (obj.getInt("errCode") == 1) {
                                        //上传完毕删除本地离线数据
                                        MattingImageService.getInstance().deleteTmpImage(tempImage);
                                    } else {
                                        ToastUtils.showSystemToast(mContext, obj.getString("desc"));
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(VolleyError error) {
                                if(NetworkUtils.isNetworkAvailable(mContext)){
                                    ToastUtils.showCustomerToast(mContext, "网络有问题，请检查网络");
                                }else {
                                    ToastUtils.showCustomerToast(mContext, "请让手机连上网络");
                                }
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("account", AppData.getString(mContext, AppData.ACCOUNT));
                                map.put("value", tempImage.getValue());
                                map.put("device_id", PhoneUtil.getDevicesID(mContext));
                                return map;
                            }
                        };
                        Volley.getRequestQueue().add(request);
                    }
                }
            }).start();
        }
    }

    //先把数据保存到本地,然后再上传
    private void saveTmpPicInfo(String picPath) {
        File file = new File(picPath);
        if (file.exists()) {
            String hash = GetBigFileMD5.getMD5(file);
            MattingImageService.getInstance().saveTmpImage(picPath, hash);
            uploadPicInfo();
        }
    }



}