precision highp float; //指定默认精度

varying vec2 vTextureCoord;
uniform sampler2D uTexture;

uniform float u_delta_x;//u_delta_x = 1.0/width
uniform float u_delta_y;//u_delta_y = 1.0/height

float weight0 = 0.437674104;//0.073922237;
vec2 weight1 = vec2(1.48717949, 0.141705169);
vec2 weight2 = vec2(3.47008547, 0.119598863);
vec2 weight3 = vec2(5.45299146, 0.088097377);
vec2 weight4 = vec2(7.43589744, 0.056595891);
vec2 weight5 = vec2(9.41880342, 0.031676804);

void main() 
{
	vec4 src = texture2D(uTexture, vTextureCoord);
	vec4 ret = src * weight0;

	ret += texture2D(uTexture, vTextureCoord + vec2(weight1.x * u_delta_x, 0.0)) * weight1.y;
	ret += texture2D(uTexture, vTextureCoord + vec2(weight2.x * u_delta_x, 0.0)) * weight2.y;
	ret += texture2D(uTexture, vTextureCoord + vec2(weight3.x * u_delta_x, 0.0)) * weight3.y;
	ret += texture2D(uTexture, vTextureCoord + vec2(weight4.x * u_delta_x, 0.0)) * weight4.y;
	ret += texture2D(uTexture, vTextureCoord + vec2(weight5.x * u_delta_x, 0.0)) * weight5.y;

	ret += texture2D(uTexture, vTextureCoord - vec2(weight1.x * u_delta_x, 0.0)) * weight1.y;
	ret += texture2D(uTexture, vTextureCoord - vec2(weight2.x * u_delta_x, 0.0)) * weight2.y;
	ret += texture2D(uTexture, vTextureCoord - vec2(weight3.x * u_delta_x, 0.0)) * weight3.y;
	ret += texture2D(uTexture, vTextureCoord - vec2(weight4.x * u_delta_x, 0.0)) * weight4.y;
	ret += texture2D(uTexture, vTextureCoord - vec2(weight5.x * u_delta_x, 0.0)) * weight5.y;

	//原始的，针对alpha图像
	gl_FragColor =  vec4(src.rgb, ret.a);
}