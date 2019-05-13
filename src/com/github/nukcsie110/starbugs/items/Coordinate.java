package com.github.nukcsie110.starbugs.items;

/**
 * @author     kaibao  <a1065514@mail.nuk.edu.tw>
 * @version    0.1
 * @since      0.1
 */

public class Coordinate{
    /**
     * Represent the position X of objects, float type
     */
    protected float posX;
    /**
     * Represent the position Y of objects, float type
     */
    protected float posY;
    /**
     * Represent the face direction of objects, float type
     */
    protected float dir;
    /**
     * Constructer of Coordinate
     * @param posX_ initial object X coordinate
     * @param posY_ initial object Y coordinate
     * @param dir_ initial object face direction
     * @since             0.1
     */
    public Coordinate(float posX_, float posY_, float dir_){
        posX = posX_;
        posY = posY_;
        dir = dir_;
    }
    /**
     * move the object
     * @param posX_ new coordinate X of object
     * @param posY_ new coordinate Y of object
     * @since             0.1
     */
    public void moveTo(float posX_, float posY_){
        // TODO: 
        // Check the posX and posY if illegle with map and other object collision
        posX = posX_;
        posY = posY_;
    }
    /**
     * turn the object
     * @param dir_ new direction of object, in 0 ~ 359 degree
     * @since             0.1
     */
    public void turnDir(float dir_){
        dir = dir_ % 360;
    }
}