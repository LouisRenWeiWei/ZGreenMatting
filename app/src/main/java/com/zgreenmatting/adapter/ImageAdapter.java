package com.zgreenmatting.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zgreenmatting.R;

import java.io.File;
import java.util.Collections;
import java.util.List;

/**
 * 25  *     自定义适配器ImageAdapter
 * 26  * 在ImageAdapter中主要做了图片的倒影效果 以及 创建了对原始图片和倒影的显示区域
 * 27  *
 * 28  * @author hebao
 * 29  *
 * 30
 */


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageAdapterHolder> {
    private Context mContext;// 上下文，提供给外界(Activity)使用
    private List<String> imageList;

    public ImageAdapter(Context mContext, List<String> imageList) {
        this.mContext = mContext;
        this.imageList = imageList;
        Collections.reverse(imageList);
    }

    private Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    @Override
    public ImageAdapterHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ImageAdapterHolder holder = new ImageAdapterHolder(LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(ImageAdapterHolder holder, int position) {
        Glide.with(mContext)
                .load(imageList.get(position))
                .placeholder(R.mipmap.ic_camera_beauty)
                .crossFade()
                .into(holder.image);
    }

    @Override
    public int getItemCount() {
        return imageList.size();
    }

    class ImageAdapterHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public ImageAdapterHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.image);
        }
    }
}