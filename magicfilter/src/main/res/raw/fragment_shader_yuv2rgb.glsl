precision mediump float;
varying vec2 vTextureCoord;
uniform sampler2D uTexture;

void main()
{
	vec4 textureColor = texture2D(uTexture, vTextureCoord);

	float y = textureColor.r;
	float cb = textureColor.g;
	float cr = textureColor.b;

	float r = 1.164*(y - 0.0625) + 1.596*(cr - 0.5);
	float g = 1.164*(y - 0.0625) - 0.392*(cb - 0.5) - 0.813*(cr - 0.5);
	float b = 1.164*(y - 0.0625) + 2.017*(cb - 0.5);

	gl_FragColor = vec4(r, g, b, 1.0);
}

