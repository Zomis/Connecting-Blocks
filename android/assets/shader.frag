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

void main()
{
   float mult = mod(myPos.x, 25.0) / 25.0;


   gl_FragColor = mult * texture2D(u_texture, v_texCoords);
}