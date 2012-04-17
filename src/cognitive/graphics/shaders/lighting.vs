#version 120
uniform mat4 projectionMatrix;
attribute vec4 in_vertex;
attribute vec3 in_normal;
attribute vec4 in_color;
varying vec4 out_color;
varying vec3 out_normal;
void main()
{
  out_color = in_color;
  out_normal = in_normal;
  gl_Position = projectionMatrix * in_vertex;
}
