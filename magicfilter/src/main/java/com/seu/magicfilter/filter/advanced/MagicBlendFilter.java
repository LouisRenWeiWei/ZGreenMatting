package com.seu.magicfilter.filter.advanced;

import android.graphics.Bitmap;
import android.opengl.GLES20;

import com.momo.zgreenmattingshader.ZGreenMattingUtil;
import com.seu.magicfilter.filter.base.gpuimage.GPUImageFilter;
import com.seu.magicfilter.utils.MagicParams;
import com.seu.magicfilter.utils.OpenGlUtils;
import com.seu.magicfilter.utils.Rotation;
import com.seu.magicfilter.utils.TextureRotationUtil;

import java.nio.FloatBuffer;

/**
 * Created by Administrator on 2016/5/22.
 */
public class MagicBlendFilter extends GPUImageFilter {


    protected int mGLUniformExtraTexture;
    protected int extraTextureHandle;
    protected int mGLAttribExtraTextureCoordinate;

    private float[] mPositionTransformMatrix;
    private int mPositionTransformMatrixLocation;

    public static final String BLEND_FILTER_VERTEX_SHADER = "" +
            "attribute vec4 position;\n" +
            "attribute vec4 inputTextureCoordinate;\n" +
            "attribute vec4 inputExtraTextureCoordinate;\n" +

            " \n" +
            "varying vec2 vTextureCoord;\n" +
            "varying vec2 vExtraTextureCoord;\n" +

            " \n" +
            "void main()\n" +
            "{\n" +
            "    gl_Position = position;\n" +
            "    vTextureCoord = inputTextureCoordinate.xy;\n" +
            "    vExtraTextureCoord = inputExtraTextureCoordinate.xy;\n" +
            "}";

    public MagicBlendFilter(){
//        super(BLEND_FILTER_VERTEX_SHADER ,
//                OpenGlUtils.readShaderFromRawResource(R.raw.fragment_shader_blend));
//        super(mBlendVertexShader,
//                OpenGlUtils.readShaderFromRawResource(R.raw.fragment_shader_blend));
//        super(ZGreenMattingUtil.getBlendVertexShader(),
//                    OpenGlUtils.readShaderFromRawResource(R.raw.fragment_shader_blend));
        super(ZGreenMattingUtil.getBlendVertexShader(),
                ZGreenMattingUtil.getBlendFragmentShader());

    }

    protected void onInit() {
        super.onInit();
        mGLUniformTexture = GLES20.glGetUniformLocation(mGLProgId, "uTexture");
        mGLUniformExtraTexture = GLES20.glGetUniformLocation(mGLProgId, "uExtraTexture");
        mGLAttribExtraTextureCoordinate = GLES20.glGetAttribLocation(mGLProgId,
                "inputExtraTextureCoordinate");

        mGLTextureBuffer.put(TextureRotationUtil.getRotation(Rotation.NORMAL, false, false)).position(0);
        mPositionTransformMatrixLocation = GLES20.glGetUniformLocation(mGLProgId, "uMVPMatrix");
    }
    public void setExtraTexture(final Bitmap bitmap){

        runOnDraw(new Runnable(){
            public void run(){
                extraTextureHandle = OpenGlUtils.loadTexture(bitmap,mGLUniformExtraTexture );
            }
        });
    }

    public void setExtraTexture(final String name){

        runOnDraw(new Runnable(){
            public void run(){
                extraTextureHandle = OpenGlUtils.loadTexture(MagicParams.context, name);
            }
        });
    }

    public int onDrawFrame(final int textureId, final FloatBuffer cubeBuffer,
                           final FloatBuffer textureBuffer) {
        GLES20.glUseProgram(mGLProgId);
        runPendingOnDrawTasks();
        if (!mIsInitialized) {
            return OpenGlUtils.NOT_INIT;
        }

        cubeBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribPosition, 2, GLES20.GL_FLOAT, false, 0, cubeBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribPosition);
        textureBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0,
                textureBuffer);


        GLES20.glEnableVertexAttribArray(mGLAttribTextureCoordinate);
        mGLTextureBuffer.position(0);
        GLES20.glVertexAttribPointer(mGLAttribExtraTextureCoordinate, 2, GLES20.GL_FLOAT, false, 0,
                mGLTextureBuffer);
        GLES20.glEnableVertexAttribArray(mGLAttribExtraTextureCoordinate);

        if (textureId != OpenGlUtils.NO_TEXTURE) {
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
            GLES20.glUniform1i(mGLUniformTexture, 0);
        }
        onDrawArraysPre();
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDisableVertexAttribArray(mGLAttribPosition);
        GLES20.glDisableVertexAttribArray(mGLAttribTextureCoordinate);
        onDrawArraysAfter();
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
        return OpenGlUtils.ON_DRAWN;
    }
    protected void onDrawArraysAfter(){
        if(extraTextureHandle != OpenGlUtils.NO_TEXTURE){
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        }
    }

    protected void onDrawArraysPre(){
        if(extraTextureHandle != OpenGlUtils.NO_TEXTURE){
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 1);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, extraTextureHandle);
            GLES20.glUniform1i(mGLUniformExtraTexture, 1);
            GLES20.glUniformMatrix4fv(mPositionTransformMatrixLocation, 1, false, mPositionTransformMatrix, 0);
        }
    }

    protected void onInitialized(){
        super.onInitialized();
    }

    public void setPositionTransformMatrix(float[] mtx) {
        mPositionTransformMatrix = mtx;
    }
}
