package com.seu.magicfilter.filter.advanced;

import android.opengl.GLES20;

import com.momo.zgreenmattingshader.ZGreenMattingUtil;
import com.seu.magicfilter.filter.base.gpuimage.GPUImageFilter;

/**
 * Created by Administrator on 2016/5/22.
 */
public class MagicMattingFilter extends GPUImageFilter {
    private int mParamsLocation;

    private float[] mPositionTransformMatrix;
    private int mPositionTransformMatrixLocation;



    public MagicMattingFilter(){
//        super(NO_FILTER_VERTEX_SHADER_1 ,
//                OpenGlUtils.readShaderFromRawResource(R.raw.fragment_shader_matting));
//        super(mMattingVertexShader, OpenGlUtils.readShaderFromRawResource(R.raw.fragment_shader_matting));
        super(ZGreenMattingUtil.getMattingVertexShader(), ZGreenMattingUtil.getMattingFragmentShader());
    }

    protected void onInit() {
        super.onInit();

        mGLUniformTexture = GLES20.glGetUniformLocation(mGLProgId, "uTexture");
        mParamsLocation = GLES20.glGetUniformLocation(getProgram(), "params");
        mPositionTransformMatrixLocation = GLES20.glGetUniformLocation(mGLProgId, "uMVPMatrix");
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

    @Override
    protected void onDrawArraysPre() {
        super.onDrawArraysPre();
        GLES20.glUniformMatrix4fv(mPositionTransformMatrixLocation, 1, false, mPositionTransformMatrix, 0);
    }

    public void setPositionTransformMatrix(float[] mtx) {
        mPositionTransformMatrix = mtx;
    }
}
