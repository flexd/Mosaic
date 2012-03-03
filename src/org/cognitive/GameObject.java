/*
 */
package org.cognitive;

/**
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Feb 27, 2012
 */
public abstract class GameObject {
  public int x, y, width, height;
    public boolean selected = false;

  
    GameObject(int x, int y, int width, int height) {
      this.x = x;
      this.y = y;
      this.width = width;
      this.height = height;
    }
    
   
    
    void draw() {
      
    }
    boolean inBounds(int mousex, int mousey) {
      if (mousex > x && mousex < x + width && mousey > y && mousey < y + height) {
        return true;
      }
      else {
        return false;
      }
    }
    boolean outOfBounds() {
      if (x > Game.DISPLAY_WIDTH || y > Game.DISPLAY_HEIGHT || x < 0 || y < 0) {
        return true;
      }
      else {
        return false;
      }
    }
    void update(int dx, int dy) {
      x += dx;
      y += dy;
    }
    void update(int delta) {
      int dx = 1, dy = 1;
      x += delta * dx * 0.1;
      y += delta * dy * 0.1;
    }
  }