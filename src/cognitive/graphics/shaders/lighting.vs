#version 120
uniform mat4 modelProjectionMatrix;
uniform vec4 in_color;
attribute vec3 in_vertex;
attribute vec3 in_normal;
varying vec4 out_color;
varying vec3 pos;
varying vec3 out_normal;
void main()
{
  out_color = in_color;
  out_normal = in_normal;
  pos = in_vertex;
  gl_Position = modelProjectionMatrix * vec4(in_vertex, 1.0);
}
