/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import cognitive.graphics.Vertex2f;
import org.cognitive.texturemanager.Sprite;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author kristoffer
 */
public class Quad {
  private boolean textured = false;
  private Vertex2f[] vertices = new Vertex2f[4];
  private Texture tex;

  public Quad(float x, float y, float width, float height, float red, float green, float blue, float alpha) {
    
    setupQuad(x, y, width, height, red, green, blue, alpha);
    
  }
  public Quad(float x, float y, float width, float height, float red, float green, float blue, float alpha, Sprite sprite) {
    
    setupQuad(x, y, width, height, red, green, blue, alpha);
    tex = sprite.getTexture();
  }
  private void setupQuad(float x, float y, float width, float height, float red, float green, float blue, float alpha) {
    vertices[0] = new Vertex2f(x, y, red, green, blue, alpha);
    vertices[1] = new Vertex2f(x + width, y, red, green, blue, alpha);
    vertices[2] = new Vertex2f(x + width, y + height, red, green, blue, alpha);
    vertices[3] = new Vertex2f(x, y + height, red, green, blue, alpha);
  }
  public Vertex2f[] getVertices() {
    return vertices;
  }
  
}
