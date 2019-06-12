package com.github.nukcsie110.starbugs.server;


import java.util.*;

import com.github.nukcsie110.starbugs.basic.*;
import com.github.nukcsie110.starbugs.server.*;
import com.github.nukcsie110.starbugs.packet.*;


public class Game{
    private ServerState state;
    public static final int MAX_PLAYER = 2;
    private GameMap map = new GameMap();
    protected final float maxTime = 6000;
    protected HashMap<Integer, ServerUser> playerList = new HashMap<Integer, ServerUser>();
    protected HashSet<Item> itemList = new HashSet<Item>();
    private Random randGenerater = new Random();
    public Game(){
        state = ServerState.WAITTING;
        map.setCurrentPlayers(0);
        Coordinate newPos = new Coordinate(0,0,0);
        newPos.setPosX((float)randGenerater.nextInt(5000) + 2500); 
        newPos.setPosY((float)randGenerater.nextInt(5000) + 2500); 
        map.setSaveZone(5000, newPos);
        map.setCurrentTime(0);
    }
    public boolean addPlayer(ServerUser player){
        if(this.state==ServerState.WAITTING && this.playerList.size()<this.MAX_PLAYER){
            this.playerList.put((int)player.getID(), player);
            if(this.playerList.size()==this.MAX_PLAYER){
                this.state=ServerState.GAMMING;
                this.startGame();
            }
            return true;
        }else{
            return false;
        }
    }

    public int getOnlinePlayerCount(){
        return this.playerList.size();
    }

    public HashMap<Integer, ServerUser> getOnlinePlayers(){
        return this.playerList;
    }

    public void broadcast(byte[] msg){
        Iterator<Map.Entry<Integer, ServerUser>> iter
            = this.playerList.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<Integer, ServerUser> enrty = iter.next();
            enrty.getValue().getHandler().send(msg);
        }
    }

    private void startGame(){
        byte[] startGamePacket = Parser.gameOver((byte)0xFF);
        this.broadcast(startGamePacket);
    }

}