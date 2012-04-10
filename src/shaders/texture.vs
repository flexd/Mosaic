uniform mat4 u_projectionMatrix;
attribute vec4 aVertexPosition;

attribute vec2 aTextureCoord;
varying vec2 vTextureCoord;
varying vec4 vColor;
attribute vec4 aColor;

varying float vUseTex;
attribute float useTex;

void main()
{
   gl_Position = gl_ProjectionMatrix * gl_ModelViewMatrix * aVertexPosition;
   vTextureCoord = aTextureCoord;
   vColor = aColor;
   vUseTex = useTex;
}
