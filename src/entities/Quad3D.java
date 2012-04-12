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
  private Vertex3f[] vertices = new Vertex3f[4];
  private Texture tex = null;

  public Quad3D(float x, float y, float z, float width, float height, float red, float green, float blue, float alpha) {
    
    setupQuad(x, y, z, width, height, red, green, blue, alpha, 0, 0, 0);
    
  }
  public Quad3D(float x, float y, float z, float width, float height, float red, float green, float blue, float alpha, Sprite sprite) {
    
    
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
    vertices[0] = new Vertex3f(x, y, z, red, green, blue, alpha, u0, v0, 1);
	vertices[1] = new Vertex3f(x + width, y, z, red, green, blue, alpha, u1, v0, 1);
	vertices[2] = new Vertex3f(x + width, y + height, z, red, green, blue, alpha, u1, v1, 1);
	vertices[3] = new Vertex3f(x, y + height, z, red, green, blue, alpha, u0, v1, 1);
    
 
  }
  private void setupQuad(float x, float y, float z, float width, float height, float red, float green, float blue, float alpha, float u, float v, float t) {
	  // surface 1
	  vertices[0] = new Vertex3f(x, y, z, red, green, blue, alpha, 0, 0, 0);
	  vertices[1] = new Vertex3f(x + width, y, z, red, green, blue, alpha, 0, 0, 0);
	  vertices[2] = new Vertex3f(x + width, y + height, z, red, green, blue, alpha, 0, 0, 0);
	  vertices[3] = new Vertex3f(x, y + height, z, red, green, blue, alpha, 0, 0, 0);
	  
//	  // surface 2
//	  vertices[4] = new Vertex3f(x + width, y, z, red, green, blue, alpha, 0, 0, 0);
//	  vertices[5] = new Vertex3f(x + width, y, z, red, green, blue, alpha, 0, 0, 0);
  }
//  vertices[0] = new Vertex2f(x, y, red, green, blue, alpha, u, v, t);
//  vertices[1] = new Vertex2f(x + width, y, red, green, blue, alpha, u, v, t);
//  vertices[2] = new Vertex2f(x + width, y + height, red, green, blue, alpha, u, v, t);
//  vertices[3] = new Vertex2f(x, y + height, red, green, blue, alpha, u, v, t);
//  GL11.glVertex3i(0, 0, 0); // Upper left
//  GL11.glVertex3i(WORLD_WIDTH, 0, 0); // Upper right
//  GL11.glVertex3i(WORLD_WIDTH, WORLD_HEIGHT, 0); // bottom right
//  GL11.glVertex3i(0, WORLD_HEIGHT, 0); // bottom left
//  GL11.glColor3f(1.0f, 0.0f, 0.0f);
//
//  //surface 2
//  GL11.glVertex3i(WORLD_WIDTH, 0, 0); // Upper left
//  GL11.glVertex3i(WORLD_WIDTH, 0, -WORLD_DEPTH); // Upper right
//  GL11.glVertex3i(WORLD_WIDTH, WORLD_HEIGHT, -WORLD_DEPTH); // bottom right
//  GL11.glVertex3i(WORLD_WIDTH, WORLD_HEIGHT, 0); // bottom left
//  
//  // surface 3
//  GL11.glColor3f(0.0f, 1.0f, 1.0f);
//
//  GL11.glVertex3i(WORLD_WIDTH, 0, -WORLD_HEIGHT); // Upper right
//  GL11.glVertex3i(0, 0, -WORLD_DEPTH); // Upper left
//  GL11.glVertex3i(0, WORLD_HEIGHT, -WORLD_DEPTH); // bottom right
//  GL11.glVertex3i(WORLD_WIDTH, WORLD_HEIGHT, -WORLD_DEPTH); // bottom left
//  
//  // surface 4
//  GL11.glColor3f(0.0f, 0.0f, 1.0f);
//
//  GL11.glVertex3i(0, 0, -WORLD_HEIGHT); // Upper right
//  GL11.glVertex3i(0, 0, 0); // Upper left
//  GL11.glVertex3i(0, WORLD_HEIGHT, 0); // bottom right
//  GL11.glVertex3i(0, WORLD_HEIGHT, -WORLD_DEPTH); // bottom left
//  
//  // surface 5
//  GL11.glColor3f(1.0f, 0.0f, 1.0f);
//
//  GL11.glVertex3i(0, WORLD_HEIGHT, 0); // Upper right
//  GL11.glVertex3i(WORLD_WIDTH, WORLD_HEIGHT, 0); // Upper left
//  GL11.glVertex3i(WORLD_WIDTH, WORLD_HEIGHT, -WORLD_DEPTH); // bottom left
//  GL11.glVertex3i(0, WORLD_HEIGHT, -WORLD_DEPTH); // bottom right
//   
//  // surface 6
//  GL11.glColor3f(0.5f, 0.0f, 1.0f);
//
//  GL11.glVertex3i(0, 0, 0); // Upper right
//  GL11.glVertex3i(0, 0, -WORLD_DEPTH); // Upper left
//  GL11.glVertex3i(WORLD_WIDTH, 0, -WORLD_DEPTH); // bottom left
//  GL11.glVertex3i(WORLD_WIDTH, 0, 0); // bottom right
  public Texture getTexture() {
    return tex;
  }
  public Vertex3f[] getVertices() {
    return vertices;
  }
  
}
