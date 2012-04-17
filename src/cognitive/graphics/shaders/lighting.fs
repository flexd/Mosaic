varying vec4 out_color;
varying vec3 pos;
varying vec3 out_normal;

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
    vec3 nor = out_normal;
    vec3 light = vec3(1.0, 1.0, 1.0);
 
    float con = 1.0;
    vec4 amb = vec4(0.2, 0.2, 0.2, 1.0);
    vec4 dif = vec4(1.0, 1.0, 1.0, 1.0);
    float bac = max(0.2 + 0.8*dot(nor,vec3(-light.x,light.y,-light.z)),0.0);
    vec4 spe = vec4(1.0, 1.0, 1.0, 1.0);
    vec4 color = out_color + amb * dif *spe; // + dif + spe;
    //color += vec4(0.70*dif*vec3(1.00,0.97,0.85),1.0);
    //color += vec4(0.15*bac*vec3(1.00,0.97,0.85),1.0);
    //color += vec4(0.20*amb*vec3(0.10,0.15,0.20),1.0);
  gl_FragColor = color;
}