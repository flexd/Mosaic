/*
 */
package org.cognitive;

import java.util.Random;
import static org.lwjgl.opengl.GL11.*;
import org.newdawn.slick.opengl.Texture;


/**
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Feb 27, 2012
 */
public class TexturedObject extends GameObject {
  public Texture texture;
  private float colorRed, colorGreen, colorBlue;

  TexturedObject(int x, int y, Texture texture) {
    super(x, y);
    this.x = x;
    this.y = y;
    this.texture = texture;
    Random randomGenerator = new Random();
    colorRed = randomGenerator.nextFloat();
    colorGreen = randomGenerator.nextFloat();
    colorBlue = randomGenerator.nextFloat();
  }

  void randomizeColors() {
    Random randomGenerator = new Random();
    colorRed = randomGenerator.nextFloat();
    colorGreen = randomGenerator.nextFloat();
    colorBlue = randomGenerator.nextFloat();
  }

  @Override
  void draw() {
    this.texture.bind();

    glColor4f(1.0f,1.0f,1.0f, 1.0f);
    //glColor4f(colorRed,colorGreen,colorBlue, 1.0f);
    glBegin(GL_QUADS);
      glTexCoord2f(0,0);
      glVertex2f(x,y);
      glTexCoord2f(1,0);
      glVertex2f(x + 50, y);
      glTexCoord2f(1,1);
      glVertex2f(x + 50, y + 50);
      glTexCoord2f(0,1);
      glVertex2f(x, y + 50);
    glEnd();
  }
  @Override
  boolean inBounds(int mousex, int mousey) {
    if (mousex > x && mousex < x + 50 && mousey > y && mousey < y + 50) {
      return true;
    }
    else {
      return false;
    }
  }
  @Override
  boolean outOfBounds() {
    if (x > Game.DISPLAY_WIDTH || y > Game.DISPLAY_HEIGHT || x < 0 || y < 0) {
      return true;
    }
    else {
      return false;
    }
  }
  @Override
  void update(int dx, int dy) {
    x += dx;
    y += dy;
  }
  @Override
  void update(int delta) {
    int dx = 1, dy = 1;
    x += delta * dx * 0.1;
    y += delta * dy * 0.1;
  }
}