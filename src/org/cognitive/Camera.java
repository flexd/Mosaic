/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cognitive;
import static org.lwjgl.opengl.GL11.*;
/**
 *
 * @author kristoffer
 */
public class Camera {
  private int dx = 0, dy = 0;
  private int x = 0, y = 0;
  
  public Camera(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public double getDx() {
    return dx;
  }

  public void setDx(int dx) {
    this.dx = dx;
  }

  public double getDy() {
    return dy;
  }

  public void setDy(int dy) {
    this.dy = dy;
  }
  public void addDx(int dx) {
    this.dx = this.dx + dx;
  }
  public void addDy(int dy) {
    this.dy = this.dy + dy;
  }
  public double getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }
  
  public void move(int dx, int dy) {
    this.dx = dx;
    this.dy = dy;
  }
  public void update () {
    x += dx;
    y += dy;
    glTranslated(x, y, 0); // Move the camera.
  }
}
