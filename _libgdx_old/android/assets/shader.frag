#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
varying vec4 myPos;
uniform vec4 colorA;
uniform vec4 colorB;

void main()
{
   float period = 25.0;
   float offset = mod(myPos.x + myPos.y, period) / period;
   float modresult = mod(offset + 0.5, 1.0);
   float mult = abs(modresult - 0.5) * 2.0;

   gl_FragColor = mix(colorA, colorB, mult) * texture2D(u_texture, v_texCoords);
}