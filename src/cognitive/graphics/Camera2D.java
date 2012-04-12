/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive.graphics;
import cognitive.Window;
import static org.lwjgl.opengl.GL11.*;
/**
 *
 * @author kristoffer
 */
public class Camera2D {
  private int targetX = 0, targetY = 0;
  private int originalX = 0, originalY = 0;
  public int offsetX = 0, offsetY = 0;
  private static double cameraMovementRate = 2;
  private boolean followMouse;

  public void followMouse(boolean followMouse) {
    this.followMouse = followMouse;
  }

  
  public Camera2D(int targetX, int targetY) {
    originalX = targetX;
    originalY = targetY;
  }

  public double getCameraX() {
    return originalX + offsetX;
  }
  public double getCameraY() {
    return originalY + offsetY;
  }
  public void setCameraX(int x) {
    targetX = originalX + x - Window.DISPLAY_WIDTH/2;
  }
  public void setCameraY(int y) {
    targetY = originalY + y - Window.DISPLAY_HEIGHT/2;
  }
  
  public void followEntity(int targetX, int targetY) {
    //System.out.println("Given target: " + targetX + ", " + targetY + ", Mouse: " + Window.mouseX + ", " + Window.mouseY);
    setCameraX(targetX);
    setCameraY(targetY);
  }
  
  public void centerOnMouse() {
    setCameraX(Window.mouseX);
    setCameraY(Window.mouseY);
  }
  public void update () {

    if (followMouse) {
      setCameraX(Window.mouseX);
      setCameraY(Window.mouseY);
    }
    //System.out.println("Camera2D: " + originalX + ", " + originalY + ", " + "Camera2D offset: " + offsetX + ", " + offsetY + ", Target: " + targetX + ", " + targetY);
  
    if (originalX + offsetX < targetX) {
      glTranslated(cameraMovementRate, 0, 0); // Move the camera right
      offsetX += cameraMovementRate;
    }
    if (originalX + offsetX > targetX) {
      glTranslated(-cameraMovementRate, 0, 0); // Move the camera left
      offsetX -= cameraMovementRate;
    }
    if (originalY + offsetY < targetY) {
      glTranslated(0, cameraMovementRate, 0); // Move the camera down
      offsetY += cameraMovementRate;
    }
    if (originalY + offsetY > targetY) {
      glTranslated(0, -cameraMovementRate, 0); // Move the camera up
      offsetY -= cameraMovementRate;
    }
  }
}
