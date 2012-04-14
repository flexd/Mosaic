attribute vec3 vertex;
uniform vec4 position;
uniform vec4 color;
varying vec4 vColor;




void main()
{
   vColor = color;
   gl_Position = gl_ProjectionMatrix * gl_ModelViewMatrix * vertex * position; 
}
