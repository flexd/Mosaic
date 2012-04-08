/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cognitive;

/**
 *
 * @author kristoffer
 */


public class GamePlay {
  
  public static enum GameState {

    INTRO, MAIN_MENU, GAME;
  }
  private GameState state = GameState.GAME; // INTRO should be default.

  public GameState getState() {
    return state;
  }

  public void setState(GameState state) {
    this.state = state;
  }
  
  public void update(int delta) {
    
  }
}
