package com.seu.magicfilter.filter.advanced;

import android.graphics.Bitmap;

import com.seu.magicfilter.filter.base.MagicBaseGroupFilter;
import com.seu.magicfilter.filter.base.gpuimage.GPUImageFilter;

import java.util.ArrayList;
import java.util.List;

public class MagicAAFilter extends MagicBaseGroupFilter {

	public MagicAAFilter() {
		super(initFilters());
	}
	
	private static List<GPUImageFilter> initFilters(){
		List<GPUImageFilter> filters = new ArrayList<GPUImageFilter>();
		filters.add(new MagicMattingFilter());
		filters.add(new MagicBlurFilter());
		filters.add(new MagicBlendFilter());
		return filters;		
	}

	public void setParams(final float params){
		((MagicMattingFilter) filters.get(0)).setParams(params);
	}


	public void setBitmap(final Bitmap bitmap){
		((MagicBlendFilter) filters.get(2)).setExtraTexture(bitmap);
	}

	public void setAsset(String name){
		((MagicBlendFilter) filters.get(2)).setExtraTexture(name);
	}

	public void setPositionTransformMatrix(float[] mvp) {
		((MagicMattingFilter) filters.get(0)).setPositionTransformMatrix(mvp);
		((MagicBlurFilter) filters.get(1)).setPositionTransformMatrix(mvp);
		((MagicBlendFilter) filters.get(2)).setPositionTransformMatrix(mvp);
	}

}
