/*
 */
package org.cognitive.texturemanager;

import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Mar 3, 2012
 */

public class Sprite {

  private String name;
  private int sX;
  private int sY;
  private Texture texture;
  private int sheetID = 0;

  public Sprite(Texture texture, int sheetID, String name, int sX, int sY) {
    this.texture = texture;
    this.name = name;
    this.sX = sX;
    this.sY = sY;
    this.sheetID = sheetID;
  }

  public int getSheetID() {
    return sheetID;
  }
  
  public String getName() {
    return name;
  }

  public int getsX() {
    return sX;
  }

  public int getsY() {
    return sY;
  }

  public Texture getTexture() {
    return texture;
  }
}