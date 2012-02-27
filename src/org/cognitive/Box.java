/*
 */
package org.cognitive;

import static org.lwjgl.opengl.GL11.*;


/**
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Feb 27, 2012
 */
public class Box extends GameObject {
    Box(int x, int y) {
      super(x,y);
    }
    @Override
    void draw() {
      glDisable(GL_TEXTURE_2D);
      // glColor3f(1.0f, 0.0f, 0.0f);
      glColor4d(0.0, 1.0, 1.0, 1.0);
      
      glRecti(x, y, x + 30, y + 30); 
      glEnable(GL_TEXTURE_2D);
    }
  }