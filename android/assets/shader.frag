#ifdef GL_ES
#define LOWP lowp
precision mediump float;
#else
#define LOWP
#endif

varying LOWP vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
varying vec2 myPos;
uniform vec4 colorA;
uniform vec4 colorB;

void main()
{
   float period = 25.0;
   float x = mod(myPos.x + myPos.y, period) / period;
   float mult = abs(mod(x + 0.5, 1) - 0.5) * 2;

   gl_FragColor = mix(colorA, colorB, mult) * texture2D(u_texture, v_texCoords);
}