varying vec3 out_color;

vec2 map( vec3 p )
{
   vec2 d2 = vec2( p.y+0.55, 2.0 );

   p.y -= 0.75*pow(dot(p.xz,p.xz),0.2);
   vec2 d1 = vec2( length(p) - 1.0, 1.0 );

   if( d2.x<d1.x) d1=d2;
   return d1;
}
vec3 calcNormal( in vec3 pos )
{
    vec3  eps = vec3(.001,0.0,0.0);
    vec3 nor;
    nor.x = map(pos+eps.xyy).x - map(pos-eps.xyy).x;
    nor.y = map(pos+eps.yxy).x - map(pos-eps.yxy).x;
    nor.z = map(pos+eps.yyx).x - map(pos-eps.yyx).x;
    return normalize(nor);
}
void main()
{ 
  gl_FragColor = vec4(out_color, 1.0);
}