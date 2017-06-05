precision mediump float;
varying vec2 vTextureCoord;
uniform sampler2D uTexture;

void main()
{
	vec4 textureColor = texture2D(uTexture, vTextureCoord);

	float r = textureColor.r * 255;
	float g = textureColor.g * 255;
	float b = textureColor.b * 255;

	float y = 0.257*r + 0.504*g + 0.098*b + 16;
	float cb = -0.148*r - 0.291*g + 0.439*b + 128;
	float cr = 0.439*r - 0.368*g - 0.071*b + 128;

	gl_FragColor = vec4(y/255, cb/255, cr/255,1.0);
}
