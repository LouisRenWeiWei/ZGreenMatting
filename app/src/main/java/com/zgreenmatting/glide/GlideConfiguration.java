package com.zgreenmatting.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.module.GlideModule;

public class GlideConfiguration  implements GlideModule{

	@Override
	public void applyOptions(Context context, GlideBuilder builder) {
		// TODO Auto-generated method stub
		builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
//		外部磁盘SD卡
//		builder.setDiskCache(new ExternalCacheDiskCacheFactory(context, "MY_CACHE_LOCATION",200 * 1024 * 1024));
		//自定义缓存目录，磁盘缓存给150M
		builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "image_catch", 150 * 1024 * 1024));

	}

	@Override
	public void registerComponents(Context context, Glide glide) {
		// TODO Auto-generated method stub
//		glide.clearDiskCache();可用于清除缓存图片
	}
}
