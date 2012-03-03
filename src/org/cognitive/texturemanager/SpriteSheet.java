/*
 */
package org.cognitive.texturemanager;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.cognitive.ScrollGame;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Mar 3, 2012
 */
public class SpriteSheet {

  private Texture sheet;
  private String name;
  private int slotSize;

  public SpriteSheet(String name, String filename, int slotSize) {
    this.name = name;
    this.slotSize = slotSize;
    this.sheet = load(filename);
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
  public int getWidth() {
    return sheet.getImageWidth();
  }
  public int getHeight() {
    return sheet.getImageHeight();
  }
  public Texture getTexture() {
    return this.sheet;
  }
}