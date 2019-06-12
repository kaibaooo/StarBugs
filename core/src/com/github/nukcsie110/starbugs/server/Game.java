package com.github.nukcsie110.starbugs.server;


import java.util.*;

import com.github.nukcsie110.starbugs.basic.*;
import com.github.nukcsie110.starbugs.server.*;
import com.github.nukcsie110.starbugs.packet.*;
import com.github.nukcsie110.starbugs.util.Logger;


public class Game{
    private ServerState state;
    private GameMap map = new GameMap();
    public static final int MAX_PLAYER = 2;
    protected final long MAX_TICK = 60*GameMap.TICK_PER_SECOND;
    protected HashMap<Integer, ServerUser> playerList = new HashMap<Integer, ServerUser>();
    protected HashSet<Item> itemList = new HashSet<Item>();
    private Random randGenerater = new Random();
    private MapUpdater updater;
    public Game(){
        state = ServerState.WAITTING;
        map.setCurrentPlayers(0);
        Coordinate newPos = new Coordinate(0,0,0);
        newPos.setPosX((float)randGenerater.nextInt(5000) + 2500); 
        newPos.setPosY((float)randGenerater.nextInt(5000) + 2500); 
        map.setSaveZone(5000, newPos);
        map.setCurrentTick(0);
    }
    public boolean addPlayer(ServerUser player){
        if(this.state==ServerState.WAITTING && this.playerList.size()<this.MAX_PLAYER){
            this.playerList.put((int)player.getID(), player);
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

    public GameMap getMap(){
        return this.map;
    }

    public void broadcast(byte[] msg){
        Iterator<Map.Entry<Integer, ServerUser>> iter
            = this.playerList.entrySet().iterator();
        while(iter.hasNext()){
            Map.Entry<Integer, ServerUser> enrty = iter.next();
            enrty.getValue().getHandler().send(msg);
        }
    }

    public void startGame(){
        this.state=ServerState.GAMMING;
        
        //Prepare map, items;
        
        //Start main game loop
        Timer timer = new Timer();
        this.updater = new MapUpdater(this);
        timer.schedule(this.updater, 0, 1000/GameMap.TICK_PER_SECOND);
        
        //Broadcast game start
        byte[] startGamePacket = Parser.gameOver((byte)0xFF);
        this.broadcast(startGamePacket);
        
        Logger.log("[Server] Game started");
    }

    public void resetGame(){

        //Stop update
        this.updater.cancel();

        //Inform every client and close connection
        Iterator<Map.Entry<Integer, ServerUser>> iter
            = this.playerList.entrySet().iterator();
        byte[] gameOverPacket = Parser.gameOver((byte)0);
        while(iter.hasNext()){
            Map.Entry<Integer, ServerUser> enrty = iter.next();
            enrty.getValue().getHandler().send(gameOverPacket);
            enrty.getValue().getHandler().terminate();
        }

        //Reset state
        this.playerList.clear();
        this.map = new GameMap();
        this.state = ServerState.WAITTING;
        Logger.log("[Server] Game reseted");
    }

}

class MapUpdater extends TimerTask{
    private Game game;
    private GameMap map;
    
    public MapUpdater(Game _game){
        this.game = _game;
        this.map = this.game.getMap();
    }

    public void run(){
        //Update map(1 TPS)
        if(map.getCurrentTick()%map.TICK_PER_SECOND == 0){
            byte[] updateMapPacket = Parser.updateMap(map);
            this.game.broadcast(updateMapPacket);
        }
        this.map.incTick();

        //Update me
        Iterator<Map.Entry<Integer, ServerUser>> iter
            = game.getOnlinePlayers().entrySet().iterator();
        //Logger.log("1");
        while(iter.hasNext()){
            ServerUser me = iter.next().getValue();
            //Logger.log(me);
            byte[] updateYouPacket = Parser.updateYou(me);
            me.getHandler().send(updateYouPacket);
        }
    }

}