package com.seu.magicfilter.filter.helper;

import com.seu.magicfilter.filter.advanced.MagicAAFilter;
import com.seu.magicfilter.filter.base.gpuimage.GPUImageFilter;

public class MagicFilterFactory{
	
	private static MagicFilterType filterType = MagicFilterType.NONE;
	
	public static GPUImageFilter initFilters(MagicFilterType type){
		filterType = type;
		switch (type) {
			case AA:
			case AA1:
			case AA2:
			case AA3:
			case AA4:
			case AA5:
			case AA6:
			case AA7:
			case AA8:
			case AA9:
			case AA10:
			case AA11:
			case AA12:
			case AA13:
			case AA14:
			case AA15:
			case AA16:
			case AA17:
			case AA18:
			case AA19:
			case AA20:
			case AA21:
			case AA22:
			case AA23:
			case AA24:
			case AA25:
			case AA26:
				return new MagicAAFilter();
		default:
			return null;
		}
	}
	
	public MagicFilterType getCurrentFilterType(){
		return filterType;
	}
}
