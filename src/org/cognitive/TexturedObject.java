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


  TexturedObject(int x, int y, int width, int height, Texture texture) {
    super(x, y, width, height);
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.texture = texture;
    
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
      glVertex2f(x + width, y);
      glTexCoord2f(1,1);
      glVertex2f(x + width, y + height);
      glTexCoord2f(0,1);
      glVertex2f(x, y + height);
    glEnd();
  }
}