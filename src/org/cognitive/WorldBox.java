/*
 */
package org.cognitive;

/**
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Mar 2, 2012
 */
public class WorldBox {
  public int width = 0, height = 0;

  public WorldBox(int width, int height) {
    this.width = width;
    this.height = height;
  }
  
  public int getY() {
    return height;
  }

  public int getX() {
    return width;
  }
  
  
  
}
