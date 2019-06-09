package com.github.nukcsie110.starbugs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import com.github.nukcsie110.starbugs.basic.*;

public class Game{
    private byte state;
    private byte maximumUser;
    private Map map = new Map();
    protected final float maxTime = 6000;
    protected HashMap<String, User> userList = new HashMap<>();
    protected HashSet<Item> itemList = new HashSet<>();
    private Random randGenerater = new Random();
    public Game(){
        state = 0;
        map.setCurrentPlayers(0);
        Coordinate newPos = new Coordinate(0,0,0);
        newPos.setPosX((float)randGenerater.nextInt(5000) + 2500); 
        newPos.setPosY((float)randGenerater.nextInt(5000) + 2500); 
        map.setSaveZone(5000, newPos);
        map.setCurrentTime(0);
        maximumUser = 5;
    }

}