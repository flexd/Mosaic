/*
 */
package org.cognitive;

import java.util.ArrayList;
import java.util.List;
import org.cognitive.texturemanager.Sprite;
import org.cognitive.texturemanager.SpriteSheet;

/**
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Mar 3, 2012
 */
public class TextureManager {
  public List<SpriteSheet> sheets = new ArrayList<SpriteSheet>();
  private List<Sprite> sprites = new ArrayList<Sprite>();
  
  public TextureManager() {
  }

  public void load(String name, String filename, int slotSize) {
    SpriteSheet sheet = new SpriteSheet(name, filename, slotSize);
    sheets.add(sheet);
  }
  
  public void define(int sheetID, String name, int sX, int sY) {
    Sprite sprite = new Sprite(sheets.get(sheetID).getTexture(), name, sX, sY); // 
    sprites.add(sprite);
  }
  public Sprite getByName(String name) {
    for (Sprite sprite : sprites) {
      if (sprite.getName().equals(name)) {
        return sprite;
      }
    }
    return null;
  }
}
