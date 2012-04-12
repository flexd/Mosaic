/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import cognitive.graphics.Vertex2f;
import cognitive.graphics.Vertex3f;

import org.cognitive.texturemanager.Sprite;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author kristoffer
 */
public class Quad3D {
  private Vertex3f[] vertices = new Vertex3f[24];
  private Texture tex = null;

  public Quad3D(float x, float y, float z, float width, float height, float depth, float red, float green, float blue, float alpha) {
    
    setupQuad(x, y, z, width, height, depth, red, green, blue, alpha);
    
  }
  public Quad3D(float x, float y, float z, float width, float height, float depth, float red, float green, float blue, float alpha, Sprite sprite) {
    
    
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
      //TODO: setup shit!
      
    }
 // surface 1
 	  vertices[0] = new Vertex3f(x, y, z, red, green, blue, alpha, u0, v0, 1);
 	  vertices[1] = new Vertex3f(x + width, y, z, red, green, blue, alpha, u1, v0, 1);
 	  vertices[2] = new Vertex3f(x + width, y + height, z, red, green, blue, alpha, u1, v1, 1);
 	  vertices[3] = new Vertex3f(x, y + height, z, red, green, blue, alpha, u0, v1, 1);
 	  
 	  // surface 2
 	  vertices[4] = new Vertex3f(x + width, y, z, red, green, blue, alpha, u0, v0, 1);
 	  vertices[5] = new Vertex3f(x + width, y, z - depth, red, green, blue, alpha, u1, v0, 1);
 	  vertices[6] = new Vertex3f(x + width, y + height, z - depth, red, green, blue, alpha, u1, v1, 1);
 	  vertices[7] = new Vertex3f(x + width, y + height, z, red, green, blue, alpha, u0, v1, 1);
 	  
 	  // surface 3
 	  vertices[8] = new Vertex3f(x + width, y, z - depth, red, green, blue, alpha, u0, v0, 1);
 	  vertices[9] = new Vertex3f(x, y, z - depth, red, green, blue, alpha, u1, v0, 1);
 	  vertices[10] = new Vertex3f(x, y + height, z - depth, red, green, blue, alpha, u1, v1, 1);
 	  vertices[11] = new Vertex3f(x + width, y + height, z - depth, red, green, blue, alpha, u0, v1, 1);
 	  
 	  // surface 4
 	  vertices[12] = new Vertex3f(x, y, z - depth, red, green, blue, alpha, u0, v0, 1);
 	  vertices[13] = new Vertex3f(x, y, z, red, green, blue, alpha, u1, v0, 1);
 	  vertices[14] = new Vertex3f(x, y + height, z, red, green, blue, alpha, u1, v1, 1);
 	  vertices[15] = new Vertex3f(x, y + height, z, red, green, blue, alpha, u0, v1, 1);
 	  
 	  // surface 5
 	  vertices[16] = new Vertex3f(x, y + height, z, red, green, blue, alpha, u0, v0, 1);
 	  vertices[17] = new Vertex3f(x + width, y + height, z, red, green, blue, alpha, u1, v0, 1);
 	  vertices[18] = new Vertex3f(x + width, y + height, z - depth, red, green, blue, alpha, u1, v1, 1);
 	  vertices[19] = new Vertex3f(x, y + height, z - depth, red, green, blue, alpha, u0, v1, 1);
 	  
 	  // surface 6
 	  vertices[20] = new Vertex3f(x, y, z, red, green, blue, alpha, u0, v0, 1);
 	  vertices[21] = new Vertex3f(x, y, z - depth, red, green, blue, alpha, u1, v0, 1);
 	  vertices[22] = new Vertex3f(x + width, y, z - depth, red, green, blue, alpha, u1, v1, 1);
 	  vertices[23] = new Vertex3f(x + width, y, z, red, green, blue, alpha, u0, v1, 1);
    
 
  }
  private void setupQuad(float x, float y, float z, float width, float height, float depth, float red, float green, float blue, float alpha) {
	  // surface 1
	  vertices[0] = new Vertex3f(x, y, z, red, green, blue, alpha, 0, 0, 0);
	  vertices[1] = new Vertex3f(x + width, y, z, red, green, blue, alpha, 0, 0, 0);
	  vertices[2] = new Vertex3f(x + width, y + height, z, red, green, blue, alpha, 0, 0, 0);
	  vertices[3] = new Vertex3f(x, y + height, z, red, green, blue, alpha, 0, 0, 0);
	  
	  red = 0;
	  blue = 1;
	  green = 0;
	  // surface 2
	  vertices[4] = new Vertex3f(x + width, y, z, red, green, blue, alpha, 0, 0, 0);
	  vertices[5] = new Vertex3f(x + width, y, z - depth, red, green, blue, alpha, 0, 0, 0);
	  vertices[6] = new Vertex3f(x + width, y + height, z - depth, red, green, blue, alpha, 0, 0, 0);
	  vertices[7] = new Vertex3f(x + width, y + height, z, red, green, blue, alpha, 0, 0, 0);
	  
	  red = 0;
	  blue = 0;
	  green = 1;
	  // surface 3
	  vertices[8] = new Vertex3f(x + width, y, z - depth, red, green, blue, alpha, 0, 0, 0);
	  vertices[9] = new Vertex3f(x, y, z - depth, red, green, blue, alpha, 0, 0, 0);
	  vertices[10] = new Vertex3f(x, y + height, z - depth, red, green, blue, alpha, 0, 0, 0);
	  vertices[11] = new Vertex3f(x + width, y + height, z - depth, red, green, blue, alpha, 0, 0, 0);
	  
	  red = 0;
	  blue = 1;
	  green = 0.5f;
	  // surface 4
	  vertices[12] = new Vertex3f(x, y, z - depth, red, green, blue, alpha, 0, 0, 0);
	  vertices[13] = new Vertex3f(x, y, z, red, green, blue, alpha, 0, 0, 0);
	  vertices[14] = new Vertex3f(x, y + height, z, red, green, blue, alpha, 0, 0, 0);
	  vertices[15] = new Vertex3f(x, y + height, z, red, green, blue, alpha, 0, 0, 0);
	  
	  red = 0;
	  blue = 0.5f;
	  green = 0.2f;
	  // surface 5
	  vertices[16] = new Vertex3f(x, y + height, z, red, green, blue, alpha, 0, 0, 0);
	  vertices[17] = new Vertex3f(x + width, y + height, z, red, green, blue, alpha, 0, 0, 0);
	  vertices[18] = new Vertex3f(x + width, y + height, z - depth, red, green, blue, alpha, 0, 0, 0);
	  vertices[19] = new Vertex3f(x, y + height, z - depth, red, green, blue, alpha, 0, 0, 0);
	  
	  red = 0.2f;
	  blue = 0.3f;
	  green = 1;
	  // surface 6
	  vertices[20] = new Vertex3f(x, y, z, red, green, blue, alpha, 0, 0, 0);
	  vertices[21] = new Vertex3f(x, y, z - depth, red, green, blue, alpha, 0, 0, 0);
	  vertices[22] = new Vertex3f(x + width, y, z - depth, red, green, blue, alpha, 0, 0, 0);
	  vertices[23] = new Vertex3f(x + width, y, z, red, green, blue, alpha, 0, 0, 0);
  }

  public Texture getTexture() {
    return tex;
  }
  public Vertex3f[] getVertices() {
    return vertices;
  }
  
}
