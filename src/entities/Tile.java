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
public class Tile extends AbstractMoveableEntity {

  private Sprite sprite;
  private int sheetWidth, sheetHeight;
  public Tile(double x, double y, String spriteName) {
    super(x, y, 32, 32); // 32, 32 does not matter
    sprite = ScrollGame.tManager.getSpriteByName(spriteName);
   
    sheetWidth = ScrollGame.tManager.sheets.get(sprite.getSheetID()).getWidth();
    sheetHeight = ScrollGame.tManager.sheets.get(sprite.getSheetID()).getHeight();

  }

  @Override
  public void draw() {
    this.sprite.getTexture().bind();
    
    int tileSize = ScrollGame.tManager.sheets.get(sprite.getSheetID()).getTileSize();
    double col = this.sprite.getsX();
    double row = this.sprite.getsY();
    
    double pxPerU = 1.0/sheetWidth;
    double pxPerV = 1.0/sheetHeight;
    
    double u0 = tileSize * col * pxPerU;
    double u1 = tileSize * (col + 1) * pxPerU;
    
    double v0 = tileSize * row * pxPerV;
    double v1 = tileSize * (row + 1) * pxPerV;
    
    
    
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