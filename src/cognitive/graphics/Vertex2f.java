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

  public float x, y, r, g, b, a, tex;
  
  public Vertex2f() {
    
  }
  public Vertex2f(float x, float y, float red, float green, float blue, float alpha) {
    this.x = x;
    this.y = y;
    r = red;
    g = green;
    b = blue;
    a = alpha;
  }
  
}
