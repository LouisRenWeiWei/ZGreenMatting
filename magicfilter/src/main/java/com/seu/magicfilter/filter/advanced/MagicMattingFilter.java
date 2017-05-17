package com.seu.magicfilter.filter.advanced;

import android.opengl.GLES20;

import com.seu.magicfilter.R;
import com.seu.magicfilter.filter.base.gpuimage.GPUImageFilter;
import com.seu.magicfilter.utils.OpenGlUtils;

/**
 * Created by Administrator on 2016/5/22.
 */
public class MagicMattingFilter extends GPUImageFilter {
    private int mParamsLocation;

    public MagicMattingFilter(){
        super(NO_FILTER_VERTEX_SHADER_1 ,
                OpenGlUtils.readShaderFromRawResource(R.raw.fragment_shader_matting));
    }

    protected void onInit() {
        super.onInit();

        mGLUniformTexture = GLES20.glGetUniformLocation(mGLProgId, "uTexture");
        mParamsLocation = GLES20.glGetUniformLocation(getProgram(), "params");
    }


    @Override
    public void onInputSizeChanged(final int width, final int height) {
        super.onInputSizeChanged(width, height);
    }

    public void setParams(float params){
        setFloat(mParamsLocation, params);

    }

    protected void onInitialized(){
        super.onInitialized();
        setParams(0.5f);

    }
}
