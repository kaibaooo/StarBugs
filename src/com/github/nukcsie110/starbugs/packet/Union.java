package com.github.nukcsie110.starbugs.packet;

import java.util.ArrayList;
import com.github.nukcsie110.starbugs.basic.User;
import com.github.nukcsie110.starbugs.basic.Item;
import com.github.nukcsie110.starbugs.basic.Coordinate;

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
    public short playerID;
    public byte cnt;
    public ArrayList<User> nameTable;
    public String name;
    public ArrayList<Item> items;
    public byte weaponType;
    public byte shortWeaponType;
    public byte longWeaponType;
    public byte ArmorType;
    public byte posionCnt;
    public Coordinate coordinate;
    public int blood;
    public int currentLives;
    public Coordinate saveZone;
    public int timeLeft;
    public byte rank;
    public byte keyCode;
    public float newDirection;
}