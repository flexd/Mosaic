/*
 */
package cognitive.graphics.texturemanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import cognitive.Game;


/**
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Mar 3, 2012
 */
public class TextureManager {  
  private String rootPath = "";
  public TextureManager(String rootPath) {
    this.rootPath = rootPath;
  }

  public final Texture load(String textureName) {
    try {
      Texture texture = TextureLoader.getTexture("PNG", getClass().getResourceAsStream(rootPath + textureName + ".png"));
      return texture;
    } catch (IOException ex) {
      Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
    }
    return null;
  }
}
