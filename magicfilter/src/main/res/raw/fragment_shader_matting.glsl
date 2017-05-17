precision mediump float;

varying vec2 vTextureCoord;
uniform sampler2D uTexture;
uniform mediump float params;

void main() 
{
	vec4 color = texture2D(uTexture, vTextureCoord);

	float r = color.r*255.0;
	float g = color.g*255.0;
	float b = color.b*255.0;
	float alpha = 0.0;

	if (g >b && g >r)
	{
		alpha = g*2.0 - (r + b);
		alpha = 255.0 - alpha;
	}
	else
	{
		alpha = 255.0;
	}

	alpha = params*1.2 * alpha;

	if (alpha >255.0)
	{
		alpha = 255.0;
	}
	if (alpha < 1.0)
	{
		alpha = 1.0;
	}

	alpha = alpha / 255.0;

	gl_FragColor = vec4(color.r, color.g, color.b, alpha);
}