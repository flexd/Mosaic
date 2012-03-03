/*
 */
package org.cognitive;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Mar 2, 2012
 */
public class SpriteSheet {
  
  public SpriteSheet(int dimension_x, int dimension_y, String filename) {
    Texture sheet = load(filename);
  }
  
  public final Texture load(String textureName) {
    try {
      Texture texture = TextureLoader.getTexture("PNG", getClass().getResourceAsStream("/res/" + textureName + ".png"));
      return texture;
    } catch (IOException ex) {
      Logger.getLogger(ScrollGame.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
  
  public Texture getTexture(int x, int y) {
    return null;
  }
}
