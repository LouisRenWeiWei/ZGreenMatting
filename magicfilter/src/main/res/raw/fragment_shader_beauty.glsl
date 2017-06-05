precision mediump float;
varying vec2 vTextureCoord;
uniform sampler2D uTexture;
uniform sampler2D uYUVTexture;

uniform float u_delta_x;
uniform float u_delta_y;
uniform float u_smoothParam;

const int GAUSSIAN_SAMPLES = 13;
const vec3 W = vec3(0.299, 0.587, 0.114);
const int SQ_SAMPLES = GAUSSIAN_SAMPLES*GAUSSIAN_SAMPLES;
vec2 blurCoordinates[SQ_SAMPLES];

float hardLight(float color)
{
	float ret = 0.0;

	if (ret <= 0.5)
	{
		ret = color * color * 2.0;
	}
	else
	{
		ret = 1.0 - ((1.0 - color)*(1.0 - color) * 2.0);
	}

	return ret;
}

void main()
{
	int i = 0;
	int j = 0;
	vec3 yuvColor = texture2D(uYUVTexture, vTextureCoord).rgb;

	float fsmoothParam = 10 + u_smoothParam * u_smoothParam * 100;
	vec3 centralColor = texture2D(uTexture, vTextureCoord).rgb;

	float blue = centralColor.r;
	float green = centralColor.g;
	float red = centralColor.b;

	if ((blue >0.37 && green >0.156 && red >0.078 && blue - red >0.058 && blue - green >0.058) || (blue >0.784 && green >0.823 && red >0.667 && abs(blue - red) <=0.058 && blue > red && green > red))
	{
		vec2 StepOffset;
		vec2 blurStep;

		int count = 0;
		int multiplierX = 0;
		int multiplierY = 0;
		vec2 pos;

		StepOffset.x = u_texelWidthOffset;
		StepOffset.y = u_texelHeightOffset;
		for (i = 0; i < GAUSSIAN_SAMPLES; i++)
		{ 
			multiplierY = (i - ((GAUSSIAN_SAMPLES - 1) / 2));
			for (j = 0; j < GAUSSIAN_SAMPLES; j++)
			{
				multiplierX = (j - ((GAUSSIAN_SAMPLES - 1) / 2));
				blurStep.x = tc.x + u_texelWidthOffset*multiplierX;
				blurStep.y = tc.y + u_texelHeightOffset*multiplierY;

				blurStep.x = clamp(blurStep.x, 0, 1);
				blurStep.y = clamp(blurStep.y, 0, 1);

				blurCoordinates[i*GAUSSIAN_SAMPLES + j] = blurStep;
			}
		}

		float fValue = 0;
		float fSum = 0;
		float fSquare = 0;
		for (i = 0;i< SQ_SAMPLES;i++)
		{
			fValue = texture2D(tex1, blurCoordinates[i]).r * 255;
			fSum += fValue;
			fSquare += fValue*fValue;
		}

		float m = fSum / SQ_SAMPLES;
		float v = fSquare / SQ_SAMPLES - m*m;
		float k = v / (v + fsmoothParam);
		float y = ceil(m - k * m + k * yuvColor.r*255);
		y = y / 255;

		gl_FragColor = vec4(y, yuvColor.g, yuvColor.b, 1.0);
	}
	else
	{
		gl_FragColor = vec4(yuvColor.rgb,1.0);
	}
}
