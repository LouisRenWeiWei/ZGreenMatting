package com.zgreenmatting.helper;

import com.seu.magicfilter.filter.helper.MagicFilterType;
import com.zgreenmatting.R;


public class FilterTypeHelper {
	
	public static int FilterType2Color(MagicFilterType filterType){
		switch (filterType) {
			case NONE:
				return R.color.filter_color_grey_light;
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
				return R.color.filter_color_brown_dark;

//			case WHITECAT:
//			case BLACKCAT:
//			case SUNRISE:
//			case SUNSET:
//				return R.color.filter_color_brown_light;
//			case COOL:
//				return R.color.filter_color_blue_dark;
//			case EMERALD:
//			case EVERGREEN:
//				return R.color.filter_color_blue_dark_dark;
//			case FAIRYTALE:
//				return R.color.filter_color_blue;
//			case ROMANCE:
//			case SAKURA:
//			case WARM:
//				return R.color.filter_color_pink;
//			case AMARO:
//			case BRANNAN:
//			case BROOKLYN:
//			case EARLYBIRD:
//			case FREUD:
//			case HEFE:
//			case HUDSON:
//			case INKWELL:
//			case KEVIN:
//			case LOMO:
//			case N1977:
//			case NASHVILLE:
//			case PIXAR:
//			case RISE:
//			case SIERRA:
//			case SUTRO:
//			case TOASTER2:
//			case VALENCIA:
//			case WALDEN:
//			case XPROII:
//				return R.color.filter_color_brown_dark;
//			case ANTIQUE:
//			case NOSTALGIA:
//				return R.color.filter_color_green_dark;
//			case SKINWHITEN:
//			case HEALTHY:
//				return R.color.filter_color_red;
//			case SWEETS:
//				return R.color.filter_color_red_dark;
//			case CALM:
//			case LATTE:
//			case TENDER:
//				return R.color.filter_color_brown;
			default:
				return R.color.filter_color_grey_light;
		}
	}
	
	public static int FilterType2Thumb(MagicFilterType filterType){
		switch (filterType) {
		case NONE:
			return R.mipmap.filter_thumb_original;
			case AA1:
				return R.mipmap.a1;
			case AA2:
				return R.mipmap.a2;
			case AA3:
				return R.mipmap.a3;
			case AA4:
				return R.mipmap.a4;
			case AA5:
				return R.mipmap.a5;
			case AA6:
				return R.mipmap.a6;
			case AA7:
				return R.mipmap.a7;
			case AA8:
				return R.mipmap.a8;
			case AA9:
				return R.mipmap.a9;
			case AA10:
				return R.mipmap.a10;
			case AA11:
				return R.mipmap.a11;
			case AA12:
				return R.mipmap.a12;
			case AA13:
				return R.mipmap.a13;
			case AA14:
				return R.mipmap.a14;
			case AA15:
				return R.mipmap.a15;
			case AA16:
				return R.mipmap.a16;
			case AA17:
				return R.mipmap.a17;
			case AA18:
				return R.mipmap.a18;
			case AA19:
				return R.mipmap.a19;
			case AA20:
				return R.mipmap.a20;
			case AA21:
				return R.mipmap.a21;
			case AA22:
				return R.mipmap.a22;
			case AA23:
				return R.mipmap.a23;
			case AA24:
				return R.mipmap.a24;
			case AA25:
				return R.mipmap.a25;
			case AA26:
				return R.mipmap.a26;
//		case WHITECAT:
//			return R.mipmap.filter_thumb_whitecat;
//		case BLACKCAT:
//			return R.mipmap.filter_thumb_blackcat;
//		case ROMANCE:
//			return R.mipmap.filter_thumb_romance;
//		case SAKURA:
//			return R.mipmap.filter_thumb_sakura;
//		case AMARO:
//			return R.mipmap.filter_thumb_amoro;
//		case BRANNAN:
//			return R.mipmap.filter_thumb_brannan;
//		case BROOKLYN:
//			return R.mipmap.filter_thumb_brooklyn;
//		case EARLYBIRD:
//			return R.mipmap.filter_thumb_earlybird;
//		case FREUD:
//			return R.mipmap.filter_thumb_freud;
//		case HEFE:
//			return R.mipmap.filter_thumb_hefe;
//		case HUDSON:
//			return R.mipmap.filter_thumb_hudson;
//		case INKWELL:
//			return R.mipmap.filter_thumb_inkwell;
//		case KEVIN:
//			return R.mipmap.filter_thumb_kevin;
//		case LOMO:
//			return R.mipmap.filter_thumb_lomo;
//		case N1977:
//			return R.mipmap.filter_thumb_1977;
//		case NASHVILLE:
//			return R.mipmap.filter_thumb_nashville;
//		case PIXAR:
//			return R.mipmap.filter_thumb_piaxr;
//		case RISE:
//			return R.mipmap.filter_thumb_rise;
//		case SIERRA:
//			return R.mipmap.filter_thumb_sierra;
//		case SUTRO:
//			return R.mipmap.filter_thumb_sutro;
//		case TOASTER2:
//			return R.mipmap.filter_thumb_toastero;
//		case VALENCIA:
//			return R.mipmap.filter_thumb_valencia;
//		case WALDEN:
//			return R.mipmap.filter_thumb_walden;
//		case XPROII:
//			return R.mipmap.filter_thumb_xpro;
//		case ANTIQUE:
//			return R.mipmap.filter_thumb_antique;
//		case SKINWHITEN:
//			return R.mipmap.filter_thumb_beauty;
//		case CALM:
//			return R.mipmap.filter_thumb_calm;
//		case COOL:
//			return R.mipmap.filter_thumb_cool;
//		case EMERALD:
//			return R.mipmap.filter_thumb_emerald;
//		case EVERGREEN:
//			return R.mipmap.filter_thumb_evergreen;
//		case FAIRYTALE:
//			return R.mipmap.filter_thumb_fairytale;
//		case HEALTHY:
//			return R.mipmap.filter_thumb_healthy;
//		case NOSTALGIA:
//			return R.mipmap.filter_thumb_nostalgia;
//		case TENDER:
//			return R.mipmap.filter_thumb_tender;
//		case SWEETS:
//			return R.mipmap.filter_thumb_sweets;
//		case LATTE:
//			return R.mipmap.filter_thumb_latte;
//		case WARM:
//			return R.mipmap.filter_thumb_warm;
//		case SUNRISE:
//			return R.mipmap.filter_thumb_sunrise;
//		case SUNSET:
//			return R.mipmap.filter_thumb_sunset;
//		case CRAYON:
//			return R.mipmap.filter_thumb_crayon;
//		case SKETCH:
//			return R.mipmap.filter_thumb_sketch;
		default:
			return R.mipmap.filter_thumb_original;
		}
	}
	
	public static int FilterType2Name(MagicFilterType filterType){
		switch (filterType) {
		case NONE:
			return R.string.filter_none;
//		case WHITECAT:
//			return R.string.filter_whitecat;
//		case BLACKCAT:
//			return R.string.filter_blackcat;
//		case ROMANCE:
//			return R.string.filter_romance;
//		case SAKURA:
//			return R.string.filter_sakura;
//		case AMARO:
//			return R.string.filter_amaro;
//		case BRANNAN:
//			return R.string.filter_brannan;
//		case BROOKLYN:
//			return R.string.filter_brooklyn;
//		case EARLYBIRD:
//			return R.string.filter_Earlybird;
//		case FREUD:
//			return R.string.filter_freud;
//		case HEFE:
//			return R.string.filter_hefe;
//		case HUDSON:
//			return R.string.filter_hudson;
//		case INKWELL:
//			return R.string.filter_inkwell;
//		case KEVIN:
//			return R.string.filter_kevin;
//		case LOMO:
//			return R.string.filter_lomo;
//		case N1977:
//			return R.string.filter_n1977;
//		case NASHVILLE:
//			return R.string.filter_nashville;
//		case PIXAR:
//			return R.string.filter_pixar;
//		case RISE:
//			return R.string.filter_rise;
//		case SIERRA:
//			return R.string.filter_sierra;
//		case SUTRO:
//			return R.string.filter_sutro;
//		case TOASTER2:
//			return R.string.filter_toastero;
//		case VALENCIA:
//			return R.string.filter_valencia;
//		case WALDEN:
//			return R.string.filter_walden;
//		case XPROII:
//			return R.string.filter_xproii;
//		case ANTIQUE:
//			return R.string.filter_antique;
//		case CALM:
//			return R.string.filter_calm;
//		case COOL:
//			return R.string.filter_cool;
//		case EMERALD:
//			return R.string.filter_emerald;
//		case EVERGREEN:
//			return R.string.filter_evergreen;
//		case FAIRYTALE:
//			return R.string.filter_fairytale;
//		case HEALTHY:
//			return R.string.filter_healthy;
//		case NOSTALGIA:
//			return R.string.filter_nostalgia;
//		case TENDER:
//			return R.string.filter_tender;
//		case SWEETS:
//			return R.string.filter_sweets;
//		case LATTE:
//			return R.string.filter_latte;
//		case WARM:
//			return R.string.filter_warm;
//		case SUNRISE:
//			return R.string.filter_sunrise;
//		case SUNSET:
//			return R.string.filter_sunset;
//		case SKINWHITEN:
//			return R.string.filter_skinwhiten;
//		case CRAYON:
//			return R.string.filter_crayon;
//		case SKETCH:
//			return R.string.filter_sketch;
		case AA:
			return R.string.filter_aa;
		case AA1:
			return R.string.filter_aa1;
		case AA2:
			return R.string.filter_aa2;
		case AA3:
			return R.string.filter_aa3;
		case AA4:
			return R.string.filter_aa4;
		case AA5:
			return R.string.filter_aa5;
		case AA6:
			return R.string.filter_aa6;
		case AA7:
			return R.string.filter_aa7;
		case AA8:
			return R.string.filter_aa8;
		case AA9:
			return R.string.filter_aa9;
		case AA10:
			return R.string.filter_aa10;
		case AA11:
			return R.string.filter_aa11;
		case AA12:
			return R.string.filter_aa12;
		case AA13:
			return R.string.filter_aa13;
		case AA14:
			return R.string.filter_aa14;
		case AA15:
			return R.string.filter_aa15;
		case AA16:
			return R.string.filter_aa16;
		case AA17:
			return R.string.filter_aa17;
		case AA18:
			return R.string.filter_aa18;
		case AA19:
			return R.string.filter_aa19;
		case AA20:
			return R.string.filter_aa20;
		case AA21:
			return R.string.filter_aa21;
		case AA22:
			return R.string.filter_aa22;
		case AA23:
			return R.string.filter_aa23;
		case AA24:
			return R.string.filter_aa24;
		case AA25:
			return R.string.filter_aa25;
		case AA26:
			return R.string.filter_aa26;
		default:
			return R.string.filter_none;
		}
	}
}
