varying vec2 vTextureCoord;
varying vec4 vColor;
uniform sampler2D mytex;
varying float vUseTex;

void main()
{
    if ( vUseTex == 1.0 )
    {
        //gl_FragColor = vColor; // TESTING, this makes it green, and it works
        gl_FragColor = texture2D (mytex, vec2 (vTextureCoord.s, vTextureCoord.t)).rgba * vColor;
         // <- This just makes the quad not be drawn/be visible.
    }
    else
    {
        gl_FragColor = vColor;
    }
}