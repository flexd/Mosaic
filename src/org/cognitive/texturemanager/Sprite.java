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
  private int sX = 0;
  private int sY = 0;
  private int sNo = 0;
  private int tileSize = 32; // A default
  private SpriteSheet sheet;
  private boolean coords = true;

  public Sprite(SpriteSheet sheet, String name, int sX, int sY) {
    this.sheet = sheet;
    this.name = name;
    this.sX = sX;
    this.sY = sY;
    this.tileSize = sheet.getTileSize();
    coords = true;
  }
  
  public Sprite(SpriteSheet sheet, String name, int sNo) {
    this.sheet = sheet;
    this.name = name;
    this.sNo = sNo;
    this.tileSize = sheet.getTileSize();
    coords = false;
  }

  public boolean isCoords() {
    return coords;
  }

  public int getsNo() {
    return sNo;
  }
  
  public int getTileSize() {
    return tileSize;
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
  public int getSheetWidth() {
    return sheet.getWidth();
  }
  public int getSheetHeight() {
    return sheet.getHeight();
  }
  public Texture getTexture() {
    return sheet.getTexture();
  }
}