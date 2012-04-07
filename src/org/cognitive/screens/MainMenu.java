/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cognitive.screens;
import org.cognitive.Window;
import static org.lwjgl.opengl.GL11.*;

/**
 *
 * @author kristoffer
 */
public class MainMenu {

  public static void render() {
    glPushMatrix();
    glDisable(GL_TEXTURE_2D);
    glColor3f(1.0f, 0f, 0f);
    glRectf(0, 0, Window.DISPLAY_WIDTH, Window.DISPLAY_HEIGHT);
    glPopMatrix();
  }
}
