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
import com.zgreenmatting.blservice.MattingImageService;
import com.zgreenmatting.entity.DownloadStatus;

import java.io.File;
import java.util.List;


public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterHolder> {

    private List<MattingImage> data;
    private Context context;
    private int selected = 0;


    public void setSelected(int position){
        if(this.selected!=position){
            int preSelect = this.selected;
            this.selected = position;
            notifyItemChanged(preSelect);
        }
    }

    public FilterAdapter(Context context, List<MattingImage> data) {
        this.data = data;
        this.context = context;
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
        if(position!=0){
            if(!TextUtils.isEmpty(item.getSdPath())){
                //检查本地是否有文件
                File file = new File(item.getSdPath());
                if(file.exists()){
                    Glide.with(context).load(item.getSdPath()).into(holder.iv_thumb);
                }else{
                    item.setSdPath("");
                    item.setDownloadState(DownloadStatus.NONE.getValue());
                    MattingImageService.getInstance().update(item);
                }
            }else{
                //未下载
                item.setDownloadState(DownloadStatus.NONE.getValue());
                MattingImageService.getInstance().update(item);
                //未下载显示原图
                Glide.with(context).load(R.mipmap.filter_thumb_original).into(holder.iv_thumb);
            }
        }else {
            Glide.with(context).load(R.mipmap.filter_thumb_original).into(holder.iv_thumb);
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
                    if(itemOperation!=null)itemOperation.onItemClick(position);
                }
            }
        });
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

    public interface ItemOperation {
        void onItemClick(int position);
    }

    private ItemOperation itemOperation;

    public ItemOperation getItemOperation() {
        return itemOperation;
    }

    public void setItemOperation(ItemOperation itemOperation) {
        this.itemOperation = itemOperation;
    }
}