uniform vec3 in_position;
uniform vec4 in_color;
attribute vec3 in_vertex;

vec4 out_position;

varying vec4 out_color;

void main()
{
  out_color = in_color;
  out_position = vec4(in_position + in_vertex, 1.0); // Add position to the vertex, and scale it?
  gl_Position = gl_ProjectionMatrix * gl_ModelViewMatrix * out_position;
}
