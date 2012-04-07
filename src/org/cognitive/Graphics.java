/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cognitive;

import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import java.awt.Font;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author kristoffer
 */
public class Graphics {
  
  Camera camera = new Camera(0,0);
  
  private static org.newdawn.slick.Font bigFont = new TrueTypeFont(new Font("Georgia", 1, 20), false);
  private static org.newdawn.slick.Font mediumFont = new TrueTypeFont(new Font("Georgia", 1, 16), false);
  private static org.newdawn.slick.Font smallFont = new TrueTypeFont(new Font("Georgia", 1, 10), false);
  
  public Graphics() {
  
  }
  public void render() {
    
  }
  public static enum FontSize {
    Small, Medium, Large;
  }
  public static void drawString(int x, int y, String string, Graphics.FontSize size, Color color) {
    //System.out.println(getDelta());
    //texture.bind();
    //glEnable(GL_BLEND);
    switch (size) {
      case Small:
        smallFont.drawString(x, y, string, color);
        break;
      case Medium:
        mediumFont.drawString(x, y, string, color);
        break;
      case Large:
        bigFont.drawString(x, y, string, color);
        break;
      default:
        mediumFont.drawString(x, y, string, Color.yellow);
        break;
    }
    
  }
  public void drawFPS(int fps) {
    drawString(1,1, "FPS: " + fps, FontSize.Medium, Color.yellow);
  }
  public static void drawLineBox(int x0, int y0, int x1, int y1, boolean stippled) {
    glPushMatrix();
    if (stippled) {
      glLineStipple(1, (short)0xAAAA);
      glEnable(GL_LINE_STIPPLE);
    }
    glDisable(GL_TEXTURE_2D);
    glColor3d(1.0, 1.0, 1.0); // White is nice

    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
   // GL11.glRecti(iMouseX, iMouseY, mouseX , mouseY);
    glRecti(x0, y0, x1 , y1);
    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL); // restore what we had.
    glEnable(GL_TEXTURE_2D);
    glDisable(GL_LINE_STIPPLE);
    glPopMatrix();
    
  }
}
