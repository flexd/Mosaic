/*
 */
package entities;

import org.newdawn.slick.opengl.Texture;
import org.cognitive.texturemanager.Sprite;
import org.cognitive.ScrollGame;
import static org.lwjgl.opengl.GL11.*;

/*
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Mar 2, 2012
 */
public class TexturedEntity extends AbstractEntity {

  private Sprite sprite;
  
  public TexturedEntity(double x, double y, String spriteName) {
    super(x, y, 32, 32); // 32, 32 does not matter
    sprite = ScrollGame.textureManager.getSpriteByName(spriteName);
  }

  @Override
  public void draw() {
    this.sprite.getTexture().bind();
    
    double row, col;
    double u0, u1, v0,v1;
    int tileSize = sprite.getTileSize();
    
    if (sprite.isCoords()) {
      
      col = this.sprite.getsX();
      row = this.sprite.getsY();

      double pxPerU = 1.0/sprite.getSheetWidth();
      double pxPerV = 1.0/sprite.getSheetHeight();

      u0 = tileSize * col * pxPerU;
      u1 = tileSize * (col + 1) * pxPerU;

      v0 = tileSize * row * pxPerV;
      v1 = tileSize * (row + 1) * pxPerV;
    }
    else {
      double tilesPerWidth = sprite.getSheetWidth()/tileSize;
      double tilesPerHeight = sprite.getSheetHeight()/tileSize;
            
      row = (tilesPerWidth - sprite.getsNo()) % tilesPerWidth;
      col = sprite.getsNo() / tilesPerWidth;
      u0 = row / tilesPerWidth;
      u1 = (row + 1) / tilesPerWidth;

      v0 = col / tilesPerHeight;
      v1 = (col + 1) / tilesPerHeight; 
      
    }
    
    
    
    //System.out.println("tilesize: " + tileSize + " row: " + row + " col: " + col + " width: " + sheetWidth + " height: " + sheetHeight);
    //System.out.println("u0: " + u0 + " u1: " + u1 + " v0: " + v0 + " v1: " + v1);
    glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    
    glBegin(GL_QUADS);
      glTexCoord2d(u0, v0); // upper left
      glVertex2d(x, y);
      glTexCoord2d(u1, v0); // upper right
      glVertex2d(x + tileSize, y);
      glTexCoord2d(u1, v1); // bottom right
      glVertex2d(x + tileSize, y + tileSize);
      glTexCoord2d(u0, v1); // bottom left
      glVertex2d(x, y + tileSize);
    glEnd();
    
  }
}