/*
 */
package entities;

import org.cognitive.texturemanager.Sprite;
import org.cognitive.ScrollGame;
import static org.lwjgl.opengl.GL11.*;

/*
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Mar 2, 2012
 */
public class AbstractTexturedMoveableEntity extends AbstractMoveableEntity {

  private Sprite sprite;

  public AbstractTexturedMoveableEntity(double x, double y, String spriteName) {
    super(x, y, 32, 32);

    sprite = ScrollGame.tManager.getByName(spriteName);
    
    this.width = ScrollGame.tManager.sheets.get(0).getWidth();
    this.height = ScrollGame.tManager.sheets.get(0).getHeight();

  }

  @Override
  public void draw() {
    this.sprite.getTexture().bind();
    int x1 = sprite.getsX() * 1/((int)this.width/32); // 1/40 (40 32x32 tiles per sheet in width)
    int x2  = x1 + 1/((int)this.width/32);
    glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
    System.out.println("Spritesheet width: " + this.width + ", height: " + this.height);
    System.out.println("sX: " + sprite.getsX() + ", sY: " + sprite.getsY() + " x1: " + x1 + ", x2: " + x2);
    //glColor4f(colorRed,colorGreen,colorBlue, 1.0f);
    glBegin(GL_QUADS);
      glTexCoord2d(x1, 0);
      glVertex2d(x, y);
      glTexCoord2d(x2, 0);
      glVertex2d(x + 32, y);
      glTexCoord2d(x2, 1);
      glVertex2d(x + 32, y + 32);
      glTexCoord2d(x1, 1);
      glVertex2d(x, y + 32);
     
    glEnd();
  }
 /*
       * glTexCoord2d(0, 0);
      glVertex2d(x, y);
      glTexCoord2d(1, 0);
      glVertex2d(x + width, y);
      glTexCoord2d(1, 1);
      glVertex2d(x + width, y + height);
      glTexCoord2d(0, 1);
      glVertex2d(x, y + height);
       */
}