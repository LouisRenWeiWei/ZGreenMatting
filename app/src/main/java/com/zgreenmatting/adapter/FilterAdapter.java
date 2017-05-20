package com.zgreenmatting.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.igoda.dao.entity.MattingImage;
import com.zgreenmatting.R;
import com.zgreenmatting.download.DownloadManager;
import com.zgreenmatting.download.status.DownloadStatus;

import java.util.List;

/**
 * Created by why8222 on 2016/3/17.
 */
public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {

    private List<MattingImage> data;
    private Context context;
    private int selected = 0;

    //
    DownloadManager downloadManager;


    public FilterAdapter(Context context, List<MattingImage> data) {
        this.data = data;
        this.context = context;
        downloadManager = DownloadManager.INSTANCE;
    }

    @Override
    public FilterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.filter_item_layout,
                parent, false);
        FilterHolder viewHolder = new FilterHolder(view);
        viewHolder.iv_thumb = (ImageView) view
                .findViewById(R.id.iv_thumb);
        viewHolder.iv_selected = (ImageView) view
                .findViewById(R.id.iv_selected);
        viewHolder.fl_item = (FrameLayout) view
                .findViewById(R.id.fl_item);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(FilterHolder holder, final int position) {
        MattingImage item = data.get(position);
        if(!TextUtils.isEmpty(item.getSdPath())){
            Glide.with(context).load(item.getSdPath()).into(holder.iv_thumb);
        }else{
            //未下载
        }

        if (position == selected) {
            holder.iv_selected.setVisibility(View.VISIBLE);
        } else {
            holder.iv_selected.setVisibility(View.GONE);
        }

        holder.fl_item.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (selected != position){
                    notifyItemChanged(selected);
                    notifyItemChanged(position);
                    selected = position;
                    onFilterChangeListener.onChangePostion(position);
                }
            }
        });

        //添加到下载任务
        if (item.getDownloadState() == DownloadStatus.NONE.getValue()) {
            downloadManager.onOffer(item);
        } else if (item.getDownloadState() == DownloadStatus.DLING.getValue()) {
            //下载中
            downloadManager.onPause(item);
        } else if (item.getDownloadState() == DownloadStatus.WAIT.getValue()) {
            //等待

        }else if (item.getDownloadState() == DownloadStatus.DONE.getValue()) {
            //完成

        }else if (item.getDownloadState() == DownloadStatus.PAUSE.getValue()) {
            //暂停
            downloadManager.onContinue(item);
        } else if (item.getDownloadState() == DownloadStatus.ERROR.getValue()) {
            //失败
            downloadManager.onContinue(item);
        }
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class FilterHolder extends RecyclerView.ViewHolder {
        ImageView iv_thumb;//缩略图
        ImageView iv_selected;//选择
        FrameLayout fl_item;

        public FilterHolder(View itemView) {
            super(itemView);
        }
    }

    public interface onFilterChangeListener {
        void onChangePostion(int position);
    }

    private onFilterChangeListener onFilterChangeListener;

    public void setOnFilterChangeListener(onFilterChangeListener onFilterChangeListener) {
        this.onFilterChangeListener = onFilterChangeListener;
    }
}