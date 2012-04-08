/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cognitive.shadermanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
/**
 *
 * @author kristoffer
 */
public class Shader {
  private StringBuilder shaderSource = new StringBuilder();
  private int shaderProgram;
  private int shader;
  
  public Shader(String filename) {
    shaderProgram = glCreateProgram();
    
    try {
      BufferedReader reader = new BufferedReader(new FileReader("src/shaders/"+filename));
      String line = "";
      while ((line = reader.readLine()) != null) {
        shaderSource.append(line).append("\n");
      }
      
    }
    catch (IOException e) {
      System.err.println("Shader: " + filename + " was not loaded!");
    }
    if (filename.endsWith(".vs")) {
      shader = glCreateShader(GL_VERTEX_SHADER);
    }
    else if (filename.endsWith(".fs")) {
      shader = glCreateShader(GL_FRAGMENT_SHADER);
    }
    glShaderSource(shader, shaderSource);
    glCompileShader(shader);
    
    if (glGetShader(shader, GL_COMPILE_STATUS) == GL_FALSE) {
      System.err.println("Shader: " + filename + " was not compiled correctly!");
    }
    glAttachShader(shaderProgram, shader);
  }
  public void begin() {
    glUseProgram(shaderProgram);
  }
  public void end() {
    glUseProgram(0);
  }
  
}
