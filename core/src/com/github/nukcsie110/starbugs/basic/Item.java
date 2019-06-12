package com.github.nukcsie110.starbugs.basic;

import com.github.nukcsie110.starbugs.basic.ItemID;

/**
 * @author     kaibao  
 * @version    0.1
 * @since      0.1
 */

public class Item{
    /**
     * Represent item unique ID
     */
    private ItemID itemID;
    /**
     * Item position
     */
    private Coordinate coordinate;
    private int extra;
    /**
     * Constructer of Item
     * @param itemID init item ID
     * @param coordinate init item position
     * @since             0.1
     */
    public Item(ItemID id, Coordinate pos){
        itemID = id;
        coordinate = pos;
    }
    /**
     * get the item id in byte object
     * @return  byte itemID
     * @since             0.1
     */
    public ItemID getItemID(){
        return itemID;
    }
    /**
     * get the position in Coordinate object
     * @return Coordinate coordinate
     * @since             0.1
     */
    public Coordinate getPos(){
        return coordinate;
    }

    public void setExtra(int x){
        extra = x;
    }
    public int getExtra(){
        return extra;
    }

    public String toString(){
        return "ItemID: "+itemID+" "+coordinate.toString();
    }
}