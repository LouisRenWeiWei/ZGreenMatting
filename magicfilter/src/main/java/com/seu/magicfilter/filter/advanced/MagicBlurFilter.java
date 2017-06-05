package com.seu.magicfilter.filter.advanced;

import android.opengl.GLES20;

import com.momo.zgreenmattingshader.ZGreenMattingUtil;
import com.seu.magicfilter.filter.base.gpuimage.GPUImageFilter;

/**
 * Created by Administrator on 2016/5/22.
 */
public class MagicBlurFilter extends GPUImageFilter {

    private int u_delta_x;
    private int u_delta_y;
    private float[] mPositionTransformMatrix;
    private int mPositionTransformMatrixLocation;



    public MagicBlurFilter(){
//        super(NO_FILTER_VERTEX_SHADER_1 ,
//                OpenGlUtils.readShaderFromRawResource(R.raw.fragment_shader_blur));

//        super(mBlurVertexShader, OpenGlUtils.readShaderFromRawResource(R.raw.fragment_shader_blur));
        super(ZGreenMattingUtil.getBlurVertexShader(), ZGreenMattingUtil.getBlurFragmentShader());
    }

    protected void onInit() {
        super.onInit();
        u_delta_x = GLES20.glGetUniformLocation(getProgram(), "u_delta_x");
        u_delta_y = GLES20.glGetUniformLocation(getProgram(), "u_delta_y");
        mGLUniformTexture = GLES20.glGetUniformLocation(mGLProgId, "uTexture");
        mPositionTransformMatrixLocation = GLES20.glGetUniformLocation(mGLProgId, "uMVPMatrix");

    }
    private void setSize(final float w, final float h) {
        setDeltaX(1.0f / w);
        setDeltaY(1.0f / h);
    }
    public void setDeltaX(float x){
        setFloat(u_delta_x, x);

    }
    public void setDeltaY(float y){
        setFloat(u_delta_x, y);

    }

    @Override
    public void onInputSizeChanged(final int width, final int height) {
        setSize(width,height);
        super.onInputSizeChanged(width, height);
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
