/*
 */
package entities;

import org.cognitive.texturemanager.Sprite.Animation;
import org.cognitive.texturemanager.Sprite;
import org.cognitive.Window;
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
    this.sprite.getTexture().bind();

    double row, col;
    double u0, u1, v0, v1;
    int tileSize = sprite.getTileSize();
    double pxPerU = 1.0 / sprite.getSheetWidth();
    double pxPerV = 1.0 / sprite.getSheetHeight();
    if (sprite.isCoords()) {

      col = this.sprite.getsX() + sprite.frameStart;
      row = this.sprite.getsY() + sprite.frameOffset;



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



    //System.out.println("tilesize: " + tileSize + " row: " + row + " col: " + col + " width: " + sheetWidth + " height: " + sheetHeight);
    //System.out.println("u0: " + u0 + " u1: " + u1 + " v0: " + v0 + " v1: " + v1);
    glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

    glBegin(GL_QUADS);
      glTexCoord2d(u0, v0); // upper left
      glVertex2d(pos.x, pos.y);
      glTexCoord2d(u1, v0); // upper right
      glVertex2d(pos.x + tileSize, pos.y);
      glTexCoord2d(u1, v1); // bottom right
      glVertex2d(pos.x + tileSize, pos.y + tileSize);
      glTexCoord2d(u0, v1); // bottom left
      glVertex2d(pos.x, pos.y + tileSize);
    glEnd();


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