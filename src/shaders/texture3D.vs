
attribute vec3 aVertexPosition;
varying vec4 vColor;
attribute vec4 aColor;
attribute vec2 aTextureCoord;
varying vec2 vTextureCoord;
varying float vUseTex;
attribute float useTex;

void main()
{
   vTextureCoord = aTextureCoord;
   vColor = aColor;
   vUseTex = useTex;
   gl_Position = gl_ProjectionMatrix * gl_ModelViewMatrix * vec4(aVertexPosition, 1.0); 
}
