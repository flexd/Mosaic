/*
 */
package entities;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.opengl.Texture;
import static org.lwjgl.opengl.GL11.*;

/*
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Mar 2, 2012
 */
public class AbstractTexturedMoveableEntity extends AbstractMoveableEntity {
    private Texture texture;
    
    public AbstractTexturedMoveableEntity(double x, double y, int sheetX, int sheetY) {
      super(x, y, 16, 16);
      texture = null;
    }

    
    @Override
    public void draw() {
      this.texture.bind();

      glColor4f(1.0f,1.0f,1.0f, 1.0f);
      //glColor4f(colorRed,colorGreen,colorBlue, 1.0f);
      glBegin(GL_QUADS);
        glTexCoord2d(0,0);
        glVertex2d(x,y);
        glTexCoord2d(1,0);
        glVertex2d(x + width, y);
        glTexCoord2d(1,1);
        glVertex2d(x + width, y + height);
        glTexCoord2d(0,1);
        glVertex2d(x, y + height);
      glEnd();
    }
    
    public final Texture loadTexture(String textureName) {
    try {
      texture = TextureLoader.getTexture("PNG", getClass().getResourceAsStream("/res/" + textureName + ".png"));
      return texture;
    } catch (IOException ex) {
      Logger.getLogger(AbstractTexturedMoveableEntity.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
    
  }