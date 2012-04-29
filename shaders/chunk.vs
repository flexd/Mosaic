#version 120
uniform mat4 modelProjectionMatrix;
uniform mat4 modelViewMatrix;
uniform vec3 lightPosition;
uniform float diffuseIntensityModifier;
attribute vec3 in_vertex;
attribute vec3 in_normal;
attribute vec4 in_color;
attribute vec3 in_position;
varying vec3 out_color;

void main()
{  
  vec4 ambient = vec4(0.2, 0.2, 0.2, 1.0);
  vec3 color = in_color.rgb;
  float shininess = 128;
  // Retrieves the position of the vertex in eye space by 
  // multiplying the vertex in object space with the 
  // modelview matrix and stores it in a 3D vertex.
  vec3 vertexPosition = vec3(modelViewMatrix * vec4(in_vertex, 1.0));
  // Retrieves the direction of the light and stores it in a 
  // normalized 3D vector (normalized = length of 1).
  vec3 lightDirection = normalize(lightPosition - vertexPosition);

  vec3 surfaceNormal  = (gl_NormalMatrix * in_normal).xyz;

  float diffuseLightIntensity = diffuseIntensityModifier * max(0, dot(surfaceNormal, lightDirection));

  out_color.rgb = diffuseLightIntensity * in_color.rgb;

  out_color += ambient.rgb;


  vec3 reflectionDirection = normalize(reflect(-lightDirection, surfaceNormal));

  float specular = max(0.0, dot(surfaceNormal, reflectionDirection));
  if (diffuseLightIntensity != 0) {

    float fspecular = pow(specular, shininess);

    out_color.rgb += vec3(fspecular, fspecular, fspecular);
  }
  gl_Position = modelProjectionMatrix * vec4(in_vertex * in_position, 1.0);
}