package com.github.nukcsie110.starbugs.basic;

/**
 * @author     kaibao  
 * @version    0.1
 * @since      0.1
 */

public class Item{
    /**
     * Represent item unique ID
     */
    protected byte itemID;
    /**
     * Item position
     */
    protected Coordinate coordinate;
    /**
     * Constructer of Item
     * @param itemID init item ID
     * @param coordinate init item position
     * @since             0.1
     */
    public Item(byte id, Coordinate pos){
        itemID = id;
        coordinate = pos;
    }
    /**
     * get the item id in byte object
     * @return  byte itemID
     * @since             0.1
     */
    public byte getItemID(){
        return itemID;
    }
    /**
     * get the position in Coordinate object
     * @return Coordinate coordinate
     * @since             0.1
     */
    public Coordinate getCoordinate(){
        return coordinate;
    }

    public String toString(){
        return "ItemID: "+itemID+" "+coordinate.toString();
    }
}