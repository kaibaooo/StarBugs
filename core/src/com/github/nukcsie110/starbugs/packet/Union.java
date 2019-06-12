package com.github.nukcsie110.starbugs.packet;

import java.util.ArrayList;
import com.github.nukcsie110.starbugs.basic.User;
import com.github.nukcsie110.starbugs.basic.Item;
import com.github.nukcsie110.starbugs.basic.Coordinate;
import com.github.nukcsie110.starbugs.basic.Equipment;
import com.github.nukcsie110.starbugs.basic.GameMap;

/**
 * @version 0.1
 * @since 0.1
 * Union data type of all possible field
 * Every data member is public
 * Bacause the instance of this class should be use only one time
 */
public final class Union{
    public byte pkID;
    public byte state;
    public int maxPlayer;
    public User player;
    //public short playerID;
    public byte cnt;
    public ArrayList<User> nameTable;
    //public String name;
    public ArrayList<Item> items;
    //public Equipment weaponInHand;
    //public Equipment shortWeapon;
    //public Equipment longWeapon;
    //public Equipment ArmorType;
    //public int posionCnt;
    //public Coordinate coordinate;
    //public int blood;
    public GameMap map;
    public byte rank;
    public byte keyCode;
    public float newDirection;
}