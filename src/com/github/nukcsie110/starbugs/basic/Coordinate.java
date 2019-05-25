package com.github.nukcsie110.starbugs.basic;

/**
 * @author     kaibao  
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
     * @param dir_ initial object face direction in degree
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
     * @return true move successed, move failed
     * @since             0.1
     */
    public boolean moveTo(float posX_, float posY_){
        // TODO: 
        // Check the posX and posY if illegle with map and other object collision
        posX = posX_;
        posY = posY_;
        return true;
    }
    /**
     * check position illegle
     * @param offsetX offset of X with current position X 
     * @param offsetY offset of Y with current position Y
     * @return true move successed, move failed
     * @since             0.1
     */
    public boolean moveOffset(float offsetX, float offsetY){
        // TODO: 
        // Check the posX and posY if illegle with map and other object collision
        posX += offsetX;
        posY += offsetY;
        return true;
    }
    /**
     * turn the object
     * @param dir_ new direction of object, in 0 ~ 359 degree
     * @since             0.1
     */
    public void turnDir(float dir_){
        dir = dir_ % 360;
    }
    /**
     * get the position X of object
     * @return posX
     * @since             0.1
     */
    public float getPosX(){
        return posX;
    }
    /**
     * get the position of object
     * @return posY
     * @since             0.1
     */
    public float getPosY(){
        return posY;
    }
    /**
     * get the direction of object
     * @return dir
     * @since             0.1
     */
    public float getDir(){
        return dir;
    }
    /**
     * set the position X of object
     * @since             0.1
     */
    public void setPosX(float newX){
        posX = newX;
    }
    /**
     * set the position of object
     * @since             0.1
     */
    public void setPosY(float newY){
        posY = newY;
    }
    /**
     * set the direction of object in degree
     * @since             0.1
     */
    public void setDir(float newDir){
        // I believe the newDir wont be greater than 2 PI
        dir = newDir;
    }

}