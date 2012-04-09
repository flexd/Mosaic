/*
 */
package cognitive;

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
  
  public void define(String sheetName, String name, int sX, int sY) {
    int sheetID = getSheetIDByName(sheetName);
    Sprite sprite = new Sprite(sheets.get(sheetID), name, sX, sY); // 
    sprites.add(sprite);
  }
  
  public void define(String sheetName, String name, int sNo) {
    int sheetID = getSheetIDByName(sheetName);
    Sprite sprite = new Sprite(sheets.get(sheetID), name, sNo); // 
    sprites.add(sprite);
  }
  public int getSheetIDByName(String name) {
    for (SpriteSheet sheet : sheets) {
      if (sheet.getName().equals(name)) {
        return sheets.indexOf(sheet);
      }
    }
    return 0; // default to 0
  }
  public Sprite getSpriteByName(String name) {
    for (Sprite sprite : sprites) {
      if (sprite.getName().equals(name)) {
        return sprite;
      }
    }
    return null;
  }
}
