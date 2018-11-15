attribute float a_Radius;
attribute vec4 a_Position;
uniform  float u_Scale;
void main() {
    gl_Position =a_Position;
    gl_PointSize =a_Radius*u_Scale;
}
