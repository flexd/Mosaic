#version 120
uniform mat4 modelProjectionMatrix;
uniform vec4 in_color;
attribute vec3 in_vertex;
varying vec4 out_color;

void main()
{
  out_color = in_color;
  gl_Position = modelProjectionMatrix * vec4(in_vertex, 1.0);
}
