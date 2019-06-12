package com.github.nukcsie110.starbugs.server;

import com.github.nukcsie110.starbugs.basic.EquipmentSlot;
import com.github.nukcsie110.starbugs.basic.User;
import com.github.nukcsie110.starbugs.basic.Coordinate;
import com.github.nukcsie110.starbugs.basic.Equipment;
//import com.github.nukcsie110.starbugs.packet.ClientHandler;
import java.nio.channels.*;

public class ServerUser extends User{
    private ClientHandler handler;
    
    /**
     * Default constructor
     */
    public ServerUser(){
        this(0, "bug", new Coordinate(0,0,0));
    }

    /**
     * Fully functional constructor
     */
    public ServerUser(int _id, String _name, Coordinate _pos){
        setID(_id);
        setName(_name);
        
        this.shortWeapon = Equipment.SHORT_SWORD;
        this.longWeapon = Equipment.LONG_BOW;
        this.weaponInHand = EquipmentSlot.NONE;
        this.armor = Equipment.NONE;
        
        this.blood = this.MAX_BLOOD;
        this.cntPoison = 0;
        
        this.pos = _pos;
    }

    public void setHandler(ClientHandler newHandler){
        this.handler = newHandler;
    }
    public ClientHandler getHandler(){
        return this.handler;
    }
}