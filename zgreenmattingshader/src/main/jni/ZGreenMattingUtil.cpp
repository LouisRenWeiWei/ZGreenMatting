//
// Created by guowei on 30/05/2017.
//

#include <jni.h>
#include <android/log.h>
#include <stdio.h>

#define  LOG_TAG    "ZGreenMattingUtil"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG, __VA_ARGS__)

#ifdef __cplusplus
extern "C" {
#endif

const char *BlendVertexShader = "attribute vec4 position; \n"
        "attribute vec4 inputTextureCoordinate; \n"
        "attribute vec4 inputExtraTextureCoordinate; \n"
        "\n"
        "uniform mat4 uMVPMatrix; \n"
        "varying vec2 vTextureCoord; \n"
        "varying vec2 vExtraTextureCoord; \n"
        "\n"
        "void main() \n"
        "{ \n"
        "    gl_Position = uMVPMatrix * position; \n"
        "    vTextureCoord = inputTextureCoordinate.xy; \n"
        "    vExtraTextureCoord = inputExtraTextureCoordinate.xy; \n"
        "}";

const char *BlendFragmentShader = "precision mediump float;\n"
        "\n"
        "varying vec2 vTextureCoord;\n"
        "varying vec2 vExtraTextureCoord;\n"
        "\n"
        "uniform sampler2D uTexture;\n"
        "uniform sampler2D uExtraTexture;\n"
        "\n"
        "void main() {\n"
        "    vec4 base = texture2D(uTexture, vTextureCoord);\n"
        "    vec4 overlay = texture2D(uExtraTexture, vExtraTextureCoord);\n"
        "    vec4 outputColor;\n"
        "\n"
        "    outputColor.r = base.a*base.r + overlay.r * (1.0 - base.a);\n"
        "    outputColor.g = base.a*base.g + overlay.g * (1.0 - base.a);\n"
        "    outputColor.b = base.a*base.b + overlay.b * (1.0 - base.a);\n"
        "    outputColor.a = base.a;\n"
        "\n"
        "    gl_FragColor = outputColor;\n"
        "}";

const char *BlurVertexShader = "attribute vec4 position;\n"
        "attribute vec4 inputTextureCoordinate;\n"
        "uniform mat4 uMVPMatrix;\n"
        "varying vec2 vTextureCoord;\n"
        "void main()\n"
        "{\n"
        "    gl_Position = uMVPMatrix * position;\n"
        "    vTextureCoord = inputTextureCoordinate.xy;\n"
        "}";

const char *BlurFragmentShader = "precision highp float; //指定默认精度\n"
        "\n"
        "varying vec2 vTextureCoord;\n"
        "uniform sampler2D uTexture;\n"
        "\n"
        "uniform float u_delta_x;//u_delta_x = 1.0/width\n"
        "uniform float u_delta_y;//u_delta_y = 1.0/height\n"
        "\n"
        "float weight0 = 0.437674104;//0.073922237;\n"
        "vec2 weight1 = vec2(1.48717949, 0.141705169);\n"
        "vec2 weight2 = vec2(3.47008547, 0.119598863);\n"
        "vec2 weight3 = vec2(5.45299146, 0.088097377);\n"
        "vec2 weight4 = vec2(7.43589744, 0.056595891);\n"
        "vec2 weight5 = vec2(9.41880342, 0.031676804);\n"
        "\n"
        "void main() \n"
        "{\n"
        "\tvec4 src = texture2D(uTexture, vTextureCoord);\n"
        "\tvec4 ret = src * weight0;\n"
        "\n"
        "\tret += texture2D(uTexture, vTextureCoord + vec2(weight1.x * u_delta_x, 0.0)) * weight1.y;\n"
        "\tret += texture2D(uTexture, vTextureCoord + vec2(weight2.x * u_delta_x, 0.0)) * weight2.y;\n"
        "\tret += texture2D(uTexture, vTextureCoord + vec2(weight3.x * u_delta_x, 0.0)) * weight3.y;\n"
        "\tret += texture2D(uTexture, vTextureCoord + vec2(weight4.x * u_delta_x, 0.0)) * weight4.y;\n"
        "\tret += texture2D(uTexture, vTextureCoord + vec2(weight5.x * u_delta_x, 0.0)) * weight5.y;\n"
        "\n"
        "\tret += texture2D(uTexture, vTextureCoord - vec2(weight1.x * u_delta_x, 0.0)) * weight1.y;\n"
        "\tret += texture2D(uTexture, vTextureCoord - vec2(weight2.x * u_delta_x, 0.0)) * weight2.y;\n"
        "\tret += texture2D(uTexture, vTextureCoord - vec2(weight3.x * u_delta_x, 0.0)) * weight3.y;\n"
        "\tret += texture2D(uTexture, vTextureCoord - vec2(weight4.x * u_delta_x, 0.0)) * weight4.y;\n"
        "\tret += texture2D(uTexture, vTextureCoord - vec2(weight5.x * u_delta_x, 0.0)) * weight5.y;\n"
        "\n"
        "\t//原始的，针对alpha图像\n"
        "\tgl_FragColor =  vec4(src.rgb, ret.a);\n"
        "}";

const char *MattingVertexShader = "attribute vec4 position;\n"
        "attribute vec4 inputTextureCoordinate;\n"
        "uniform mat4 uMVPMatrix;\n"
        "varying vec2 vTextureCoord;\n"
        "void main()\n"
        "{\n"
        "    gl_Position = uMVPMatrix * position;\n"
        "    vTextureCoord = inputTextureCoordinate.xy;\n"
        "}";

const char *MattingFragmentShader = "precision mediump float;\n"
        "\n"
        "varying vec2 vTextureCoord;\n"
        "uniform sampler2D uTexture;\n"
        "uniform mediump float params;\n"
        "\n"
        "void main() \n"
        "{\n"
        "\tvec4 color = texture2D(uTexture, vTextureCoord);\n"
        "\tfloat r = color.r*255.0;\n"
        "\tfloat g = color.g*255.0;\n"
        "\tfloat b = color.b*255.0;\n"
        "\tfloat alpha = 0.0;\n"
        "\t\n"
        "\tif(color.a < 0.1)\n"
        "\t{\n"
        "\t\talpha = 0.0;\n"
        "\t}\n"
        "\telse\n"
        "\t{\n"
        "\t\tif (g >b && g >r)\n"
        "\t\t{\n"
        "\t\t\talpha = g*2.0 - (r + b);\n"
        "\t\t\talpha = 255.0 - alpha;\n"
        "\t\t}\n"
        "\t\telse\n"
        "\t\t{\n"
        "\t\t\talpha = 255.0;\n"
        "\t\t}\n"
        "\n"
        "\t\talpha = params*1.2 * alpha;\n"
        "\n"
        "\t\tif (alpha >255.0)\n"
        "\t\t{\n"
        "\t\t\talpha = 255.0;\n"
        "\t\t}\n"
        "\t\tif (alpha < 1.0)\n"
        "\t\t{\n"
        "\t\t\talpha = 1.0;\n"
        "\t\t}\n"
        "\n"
        "\t\talpha = alpha / 255.0;\n"
        "\t}\n"
        "\n"
        "\tgl_FragColor = vec4(color.r, color.g, color.b, alpha);\n"
        "}";


// Blend Filter Shaders
JNIEXPORT jobject JNICALL
Java_com_momo_zgreenmattingshader_ZGreenMattingUtil_getBlendVertexShader(JNIEnv *env,
                                                                        jobject instance){
    LOGD("%s\n", BlendVertexShader);
    return env->NewStringUTF(BlendVertexShader);
}

JNIEXPORT jobject JNICALL
Java_com_momo_zgreenmattingshader_ZGreenMattingUtil_getBlendFragmentShader(JNIEnv *env,
                                                                        jobject instance){
    return env->NewStringUTF(BlendFragmentShader);
}


// Blur Filter Shaders

JNIEXPORT jobject JNICALL
Java_com_momo_zgreenmattingshader_ZGreenMattingUtil_getBlurVertexShader(JNIEnv *env,
                                                                        jobject instance){
    return env->NewStringUTF(BlurVertexShader);
}

JNIEXPORT jobject JNICALL
Java_com_momo_zgreenmattingshader_ZGreenMattingUtil_getBlurFragmentShader(JNIEnv *env,
                                                                          jobject instance){
    return env->NewStringUTF(BlurFragmentShader);
}


// Matting Filter Shader

JNIEXPORT jobject JNICALL
Java_com_momo_zgreenmattingshader_ZGreenMattingUtil_getMattingVertexShader(JNIEnv *env,
                                                                        jobject instance){
    return env->NewStringUTF(MattingVertexShader);
}

JNIEXPORT jobject JNICALL
Java_com_momo_zgreenmattingshader_ZGreenMattingUtil_getMattingFragmentShader(JNIEnv *env,
                                                                          jobject instance){
    return env->NewStringUTF(MattingFragmentShader);
}


#ifdef __cplusplus
}
#endif