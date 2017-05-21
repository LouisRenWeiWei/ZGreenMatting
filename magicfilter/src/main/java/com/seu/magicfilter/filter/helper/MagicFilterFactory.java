package com.seu.magicfilter.filter.helper;

import com.seu.magicfilter.filter.advanced.MagicAAFilter;
import com.seu.magicfilter.filter.base.gpuimage.GPUImageFilter;

public class MagicFilterFactory{
	public static GPUImageFilter initFilters(boolean needFilter){
		if(needFilter){
			return new MagicAAFilter();
		}else {
			return null;
		}

	}
}
