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
public class Quad2D {
  private Vertex2f[] vertices = new Vertex2f[4];
  private Texture tex = null;

  public Quad2D(float x, float y, float width, float height, float red, float green, float blue, float alpha) {
    
    setupQuad(x, y, width, height, red, green, blue, alpha, 0, 0, 0);
    
  }
  public Quad2D(float x, float y, float width, float height, float red, float green, float blue, float alpha, Sprite sprite) {
    
    
    tex = sprite.getTexture();
    
    float row, col;
    float u0, u1, v0, v1;
    int tileSize = sprite.getTileSize();
    float pxPerU = (float)(1.0 / sprite.getSheetWidth());
    float pxPerV = (float)(1.0 / sprite.getSheetHeight());
    if (sprite.isCoords()) {

      col = sprite.getsX() + sprite.frameStart;
      row = sprite.getsY() + sprite.frameOffset;



      u0 = tileSize * col * pxPerU;
      u1 = tileSize * (col + 1) * pxPerU;

      v0 = tileSize * row * pxPerV;
      v1 = tileSize * (row + 1) * pxPerV;
    } else {
      int tilesAcross = sprite.getSheetWidth() / tileSize;
      int tilesDown = sprite.getSheetHeight() / tileSize;
      //System.out.println("Tiles per width: " + tilesAcross);
      //System.out.println("Tiles per height: " + tilesDown);

      col = (sprite.getsNo() + sprite.frameStart) / tilesAcross;
      row = (sprite.getsNo() + sprite.frameOffset) % tilesDown;

      u0 = row * tileSize / sprite.getSheetHeight();
      u1 = (row + 1) * tileSize / sprite.getSheetHeight();

      v0 = col * tileSize / sprite.getSheetWidth();
      v1 = (col + 1) / tilesAcross;
      // IT FUCKING WORKS!
    }
    vertices[0] = new Vertex2f(x, y, red, green, blue, alpha, u0, v0, 1);
    vertices[1] = new Vertex2f(x + tileSize, y, red, green, blue, alpha, u1, v0, 1);
    vertices[2] = new Vertex2f(x + tileSize, y + tileSize, red, green, blue, alpha, u1, v1, 1);
    vertices[3] = new Vertex2f(x, y + tileSize, red, green, blue, alpha, u0, v1, 1);
  }
  private void setupQuad(float x, float y, float width, float height, float red, float green, float blue, float alpha, float u, float v, float t) {
    vertices[0] = new Vertex2f(x, y, red, green, blue, alpha, u, v, t);
    vertices[1] = new Vertex2f(x + width, y, red, green, blue, alpha, u, v, t);
    vertices[2] = new Vertex2f(x + width, y + height, red, green, blue, alpha, u, v, t);
    vertices[3] = new Vertex2f(x, y + height, red, green, blue, alpha, u, v, t);
  }
  public Texture getTexture() {
    return tex;
  }
  public Vertex2f[] getVertices() {
    return vertices;
  }
  
}
