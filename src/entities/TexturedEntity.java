/*
 */
package entities;

import cognitive.graphics.Vertex2f;
import org.cognitive.texturemanager.Sprite.Animation;
import org.cognitive.texturemanager.Sprite;
import cognitive.Window;
import static org.lwjgl.opengl.GL11.*;

/*
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Mar 2, 2012
 */
public class TexturedEntity extends AbstractEntity {

  public Sprite sprite;
  public TexturedEntity(double x, double y, String spriteName) {
    super(x, y, 32, 32); // 32, 32 does not matter
    sprite = Window.tm.getSpriteByName(spriteName);
  }
  @Override
  public void draw() {
    
  }
  public Quad dance() {
    int tileSize = sprite.getTileSize();
   
    
    Quad out = new Quad((float)pos.x, (float)pos.y, tileSize, tileSize, 1.0f, 0f, 0f, 0.0f, sprite);
    return out;
  }

  @Override
  public void update(int delta) {
    super.update(delta);
    if (sprite.animated) {

      switch (state) {
        case MOVING_LEFT:
          getAnimation(0);
          break;
        case MOVING_RIGHT:
          getAnimation(1);
          break;
        case MOVING_DOWN:
          getAnimation(2);
          break;
        case MOVING_UP:
          getAnimation(3);
          break;
        default:
          break;
      }
    }
  }

  private void getAnimation(int aniID) {
    Animation ani;
    int max;
    int sx;
    int sy;
    ani = sprite.animations.get(aniID);
    max = ani.length();
    if (ani.current == max) { ani.current = 0; }
    if (elapsedDelta > sprite.frameDelay) { // Seems to work nice.
      ani.current++;
      elapsedDelta = 0;
    }
    if (!this.validMove) {
      ani.current = 0;
    }
    sx = ani.frames.get(ani.current).x;
    sy = ani.frames.get(ani.current).y;
    sprite.frameStart = sx;
    sprite.frameOffset = sy;
  }
}