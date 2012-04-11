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
  private StringBuilder vertexShaderSource = new StringBuilder();
  private StringBuilder fragmentShaderSource = new StringBuilder();
  private int shaderProgram;
  private int vertexShader;
  private int fragmentShader;
  
  public Shader(String filename) {
    shaderProgram = glCreateProgram();
    
    try {
      BufferedReader reader = new BufferedReader(new FileReader("src/shaders/"+filename+".vs"));
      String line = "";
      while ((line = reader.readLine()) != null) {
        vertexShaderSource.append(line).append("\n");
      }
      
    }
    catch (IOException e) {
      System.err.println("VertexShader: " + filename + " was not loaded!");
    }
    try {
      BufferedReader reader = new BufferedReader(new FileReader("src/shaders/"+filename+".fs"));
      String line = "";
      while ((line = reader.readLine()) != null) {
        fragmentShaderSource.append(line).append("\n");
      }
      
    }
    catch (IOException e) {
      System.err.println("FragmentShader: " + filename + " was not loaded!");
    }
    vertexShader   = glCreateShader(GL_VERTEX_SHADER);
 
    fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);

    glShaderSource(vertexShader, vertexShaderSource);
    glShaderSource(fragmentShader, fragmentShaderSource);
    glCompileShader(vertexShader);
    glCompileShader(fragmentShader);

    if (glGetShader(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
      System.err.println("VertexShader: " + filename + " was not compiled correctly!");
    }
    if (glGetShader(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
      System.err.println("FragmentShader: " + filename + " was not compiled correctly!");
    }
    glAttachShader(shaderProgram, vertexShader);
    glAttachShader(shaderProgram, fragmentShader);
    
    glLinkProgram(shaderProgram);
    glValidateProgram(shaderProgram);
    
    glBindAttribLocation(shaderProgram, 0, "aVertexPosition");
    glBindAttribLocation(shaderProgram, 1, "aColor");
    glBindAttribLocation(shaderProgram, 2, "aTextureCoord");
    glBindAttribLocation(shaderProgram, 3, "useTex");
  }
  public void use() {
    glUseProgram(shaderProgram);
  }
  public void stop() {
    glUseProgram(0);
  }
  public int uniformLocation(String name) {
    return glGetUniformLocation(shaderProgram, name);
  }
  public int attribLocation(String name) {
    return glGetAttribLocation(shaderProgram, name);
  }
  
}
