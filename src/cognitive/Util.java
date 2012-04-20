/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive;

import org.lwjgl.Sys;

/**
 *
 * @author kristoffer
 */
public class Util {
  public static final int FLOAT_SIZE = 4;
  public static long getTime() {
    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
  }
}
