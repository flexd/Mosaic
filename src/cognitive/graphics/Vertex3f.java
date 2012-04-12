/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive.graphics;

/**
 *
 * @author kristoffer
 */
public class Vertex3f {

  public float x, y, z, r, g, b, a, u, v, tex;
  
  public Vertex3f() {
    
  }
  public Vertex3f(float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, float tex) {
    this.x = x;
    this.y = y;
    this.z = z;
    r = red;
    g = green;
    b = blue;
    a = alpha;
    this.u = u;
    this.v = v;
    this.tex = tex;
  }
  
}
