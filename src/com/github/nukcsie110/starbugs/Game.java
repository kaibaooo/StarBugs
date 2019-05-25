package com.github.nukcsie110.starbugs;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

import com.github.nukcsie110.starbugs.basic.*;

public class Game{
    private byte state;
    private byte maximumUser;
    protected byte currentUser;
    protected float saveZoneRadius;
    protected float currentTime;
    protected final float maxTime = 6000;
    protected HashMap<String, User> userList = new HashMap<>();
    protected HashSet<Item> itemList = new HashSet<>();
    protected Coordinate saveZoneCenterPos = new Coordinate(0,0,0);
    private Random randGenerater = new Random();
    Game(){
        state = 0;
        currentUser = 0;
        saveZoneRadius = 5000;
        maximumUser = 5;
        currentTime = 0;
        saveZoneCenterPos.setPosX((float)randGenerater.nextInt(5000) + 2500);
        saveZoneCenterPos.setPosY((float)randGenerater.nextInt(5000) + 2500);
    }
    public void updtaeState(byte newState){
        state = newState;
    }
    public void setCurrentUser(byte newUser){
        if(newUser <= maximumUser){
            currentUser = newUser;
        }
    }

}