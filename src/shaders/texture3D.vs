#version 120
uniform mat4 in_position;
uniform vec4 in_color;
uniform mat4 projectionMatrix;
attribute vec3 in_vertex;

vec4 out_position;

varying vec4 out_color;

void main()
{
  out_color = in_color;
  out_position = in_position * vec4(in_vertex, 1.0); // Add position to the vertex, and scale it?
  gl_Position = projectionMatrix  * out_position;
}
