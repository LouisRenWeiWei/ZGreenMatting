package com.momo.zgreenmattingshader;

/**
 * Created on 30/05/2017.
 *
 */

public class ZGreenMattingUtil {

    public static native String getBlendVertexShader();

    public static native String getBlendFragmentShader();

    public static native String getBlurVertexShader();

    public static native String getBlurFragmentShader();

    public static native String getMattingVertexShader();

    public static native String getMattingFragmentShader();

    static {
        System.loadLibrary("zgreenmattingutil");
    }

}
