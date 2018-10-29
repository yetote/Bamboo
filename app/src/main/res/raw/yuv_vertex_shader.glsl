#version 120
attribute  vec2  a_Position;
attribute  vec2  a_TextureCoordinates;
varying vec2 v_aTextureCoordinates;
void main() {
    gl_Position =a_Position;
    v_aTextureCoordinates=a_TextureCoordinates;
}
