attribute vec4 position;
attribute vec4 inputTextureCoordinate;

uniform mat4 textureTransform;
varying vec2 textureCoordinate;
uniform mat4 uMVPMatrix;

void main()
{
	textureCoordinate = (textureTransform * inputTextureCoordinate).xy;
	gl_Position = uMVPMatrix * position;
}
