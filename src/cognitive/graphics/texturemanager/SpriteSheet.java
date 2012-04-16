/*
 */
package cognitive.graphics.texturemanager;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import cognitive.ThreeDee;
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
  private int tileSize;
  private int width, height;

  public SpriteSheet(String name, String filename, int tileSize) {
    this.name = name;
    this.tileSize = tileSize;
    this.sheet = load(filename);
    this.width = upper_power_of_two(sheet.getImageWidth());
    this.height = upper_power_of_two(sheet.getImageHeight());
  }

  public final Texture load(String textureName) {
    try {
      Texture texture = TextureLoader.getTexture("PNG", getClass().getResourceAsStream("/res/" + textureName + ".png"));
      return texture;
    } catch (IOException ex) {
      Logger.getLogger(ThreeDee.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
  
  public int getTileSize() {
    return tileSize;
  }
  
  public int getWidth() {
    return width;
  }
  public int getHeight() {
    return height;
  }
  public Texture getTexture() {
    return this.sheet;
  }

  public Object getName() {
    return this.name;
  }
  private int upper_power_of_two(int v) {
    v--;
    v |= v >> 1;
    v |= v >> 2;
    v |= v >> 4;
    v |= v >> 8;
    v |= v >> 16;
    v++;
    return v;
  }
}