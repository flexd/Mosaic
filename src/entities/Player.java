/*
 */
package entities;

/**
 *
 * @author Kristoffer Berdal <web@flexd.net>
 * @studnr 180212
 * @date Mar 10, 2012
 */
public class Player extends TexturedEntity {

  public Player(double x, double y, String spriteName) {
    super(x, y, spriteName);
  }
  public void move(int dx, int dy) {
    this.dx = dx * 0.30;
    this.dy = dy * 0.30;
  }
  
}
