/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cognitive.graphics.shadermanager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL20;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
/**
 *
 * @author kristoffer
 */
public class Shader {
  private StringBuilder vertexShaderSource = new StringBuilder();
  private StringBuilder fragmentShaderSource = new StringBuilder();
  private int shaderProgram;
  private int vertexShader;
  private int fragmentShader;
  
  public Shader(String filename) {
    shaderProgram = glCreateProgram();
    
    try {
      //TODO: Fix this hardcoded crap!
      BufferedReader reader = new BufferedReader(new FileReader("shaders/"+filename+".vs"));

      String line = "";
      while ((line = reader.readLine()) != null) {
        vertexShaderSource.append(line).append("\n");
      }
      
    }
    catch (IOException e) {
      System.err.println("VertexShader: " + filename + " was not loaded!");
      Display.destroy();
      System.exit(0);
    }
    try {
      BufferedReader reader = new BufferedReader(new FileReader("shaders/"+filename+".fs"));
      String line = "";
      while ((line = reader.readLine()) != null) {
        fragmentShaderSource.append(line).append("\n");
      }
      
    }
    catch (IOException e) {
      System.err.println("FragmentShader: " + filename + " was not loaded!");
      Display.destroy();
      System.exit(0);
    }
    vertexShader   = glCreateShader(GL_VERTEX_SHADER);
 
    fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

    glShaderSource(vertexShader, vertexShaderSource);
    glShaderSource(fragmentShader, fragmentShaderSource);
    glCompileShader(vertexShader);
    glCompileShader(fragmentShader);

    if (glGetShader(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
      int length = glGetShader(vertexShader,GL_INFO_LOG_LENGTH);
      String log = glGetShaderInfoLog(vertexShader, length);
      System.err.println("VertexShader: " + filename + " was not compiled correctly!");
      System.err.println(log);
      Display.destroy();
      System.exit(0);
    }
    if (glGetShader(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
      int length = glGetShader(fragmentShader,GL_INFO_LOG_LENGTH);
      String log = glGetShaderInfoLog(fragmentShader, length);
      System.err.println("FragmentShader: " + filename + " was not compiled correctly!");
      System.err.println(log);
      Display.destroy();
      System.exit(0);
    }
    glAttachShader(shaderProgram, vertexShader);
    glAttachShader(shaderProgram, fragmentShader);
    
    glLinkProgram(shaderProgram);
    
    if (glGetProgram(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
      int length = glGetProgram(shaderProgram, GL_INFO_LOG_LENGTH);
      String log = glGetProgramInfoLog(shaderProgram, length);
      System.err.println("Problem linking shader!");
      System.err.println(log);
      Display.destroy();
      System.exit(0);
    }
    glValidateProgram(shaderProgram);
 
  }
  public int getProgram() {
    return shaderProgram;
  }
  public void use() {
    glUseProgram(shaderProgram);
  }
  public void stop() {
    glUseProgram(0);
  }
  public int uniformLocation(String name) {
    int location = glGetUniformLocation(shaderProgram, name);
    if (location == -1) {
      System.err.println("No such uniform: " + name);
      Display.destroy();
      System.exit(0);
    }
    return location;
  }
  public int attribLocation(String name) {
    int location = glGetAttribLocation(shaderProgram, name);
    if (location == -1) {
      System.err.println("No such attribute: " + name);
      Display.destroy();
      System.exit(0);
    }
    return location;
  }
  
}
