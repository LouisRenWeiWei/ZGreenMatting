precision mediump float;

varying vec2 vTextureCoord;
varying vec2 vExtraTextureCoord;

uniform sampler2D uTexture;
uniform sampler2D uExtraTexture;

void main() {
    vec4 base = texture2D(uTexture, vTextureCoord);
    vec4 overlay = texture2D(uExtraTexture, vExtraTextureCoord);
    vec4 outputColor;

    outputColor.r = base.a*base.r + overlay.r * (1.0 - base.a);
    outputColor.g = base.a*base.g + overlay.g * (1.0 - base.a);
    outputColor.b = base.a*base.b + overlay.b * (1.0 - base.a);
	outputColor.a = base.a;

    gl_FragColor = outputColor;
}