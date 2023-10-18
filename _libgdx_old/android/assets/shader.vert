attribute vec4 a_position;
attribute vec4 a_color;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;
varying vec4 v_color;
varying vec2 v_texCoords;
varying vec4 myPos;

void main() {
    v_color = a_color;
    myPos = a_position;
    v_color.a = v_color.a * (255.0 / 254.0);
    v_texCoords = a_texCoord0;
    gl_Position = u_projTrans * a_position;
}