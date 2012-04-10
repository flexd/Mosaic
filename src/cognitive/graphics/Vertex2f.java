/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive.graphics;

/**
 *
 * @author kristoffer
 */
public class Vertex2f {

  public float x, y, r, g, b, a, u, v, tex;
  
  public Vertex2f() {
    
  }
  public Vertex2f(float x, float y, float red, float green, float blue, float alpha, float u, float v, float tex) {
    this.x = x;
    this.y = y;
    r = red;
    g = green;
    b = blue;
    a = alpha;
    this.u = u;
    this.v = v;
    this.tex = tex;
  }
  
}
