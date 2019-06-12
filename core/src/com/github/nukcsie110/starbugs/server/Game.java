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
    protected ArrayList<Item> itemList = new ArrayList<Item>();
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
    public ArrayList<Item> getItemList(){
        return this.itemList;
    }

    public synchronized void broadcast(byte[] msg){
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
        for(int i=0; i<10; i++){
            this.itemList.add(new Item(ItemID.SWORD, Coordinate.genRandomPos()));
        }
        for(int i=0; i<10; i++){
            this.itemList.add(new Item(ItemID.BOW, Coordinate.genRandomPos()));
        }
        for(int i=0; i<10; i++){
            this.itemList.add(new Item(ItemID.ARMOR_LV1, Coordinate.genRandomPos()));
        }
        for(int i=0; i<10; i++){
            this.itemList.add(new Item(ItemID.ARMOR_LV2, Coordinate.genRandomPos()));
        }
        for(int i=0; i<10; i++){
            this.itemList.add(new Item(ItemID.ARMOR_LV3, Coordinate.genRandomPos()));
        }
        for(int i=0; i<10; i++){
            this.itemList.add(new Item(ItemID.POISON, Coordinate.genRandomPos()));
        }
        
        //Broadcast game start
        byte[] startGamePacket = Parser.gameOver((byte)0xFF);
        this.broadcast(startGamePacket);
        
        //Start main game loop
        Timer timer = new Timer();
        this.updater = new MapUpdater(this);
        timer.schedule(this.updater, 100, 1000/GameMap.TICK_PER_SECOND);
        
        Logger.log("[Server] Game started");
    }

    public void resetGame(){

        //Stop update
        this.updater.cancel();

        //Inform every client and close connection
        Iterator<Map.Entry<Integer, ServerUser>> iter
            = this.playerList.entrySet().iterator();
        byte[] gameOverPacket = Parser.gameOver((byte)1);
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
    
    public void attackDetection(ServerUser player){
        int zoneSize = 200;

        Iterator<Map.Entry<Integer, ServerUser>> iter
            = this.getOnlinePlayers().entrySet().iterator();
        while(iter.hasNext()){
            ServerUser ele = iter.next().getValue();
            if(ele==player) continue;
            if((Math.abs(player.getPos().getPosX()-ele.getPos().getPosX())
              +Math.abs(player.getPos().getPosX()-ele.getPos().getPosX()))<=zoneSize){
                int damage = damageCalc(player, ele);
                Logger.log(player.getName()+" hit "+ele.getName()+" -"+damage);
                ele.setBlood(ele.getBlood()-damage);
            }
            // N
            /*if(player.getPos().getDir()>=135 && player.getPos().getDir()<225){
                if(ele.getPos().getPosX()<player.getPos().getPosX()+zoneSize
                    && ele.getPos().getPosX()>player.getPos().getPosX()-zoneSize
                    && ele.getPos().getPosY()<player.getPos().getPosY()+zoneSize
                    && ele.getPos().getPosY()>=player.getPos().getPosY()){
                    Logger.log(player.getName()+" hit "+ele.getName());
                }
            }
            else if(player.getPos().getDir()>=225 && player.getPos().getDir()<315){
                if(ele.getPos().getPosY()<player.getPos().getPosY()-zoneSize
                        && ele.getPos().getPosY()<player.getPos().getPosY()+zoneSize
                        && ele.getPos().getPosY()>player.getPos().getPosX()-zoneSize
                        && ele.getPos().getPosY()<=player.getPos().getPosX()){
                    Logger.log(player.getName()+" hit "+ele.getName());
                }
            }
            else if(player.getPos().getDir()>=315 && player.getPos().getDir()<405){
                if(ele.getPos().getPosX()<player.getPos().getPosX()+zoneSize
                        && ele.getPos().getPosX()<player.getPos().getPosX()-zoneSize
                        && ele.getPos().getPosY()>player.getPos().getPosY()-zoneSize
                        && ele.getPos().getPosY()<=player.getPos().getPosY()){
                    Logger.log(player.getName()+" hit "+ele.getName());
                }
            }
            else if(player.getPos().getDir()>=405 || player.getPos().getDir()<135){
                if(ele.getPos().getPosY()<player.getPos().getPosY()-zoneSize
                        && ele.getPos().getPosY()<player.getPos().getPosY()+zoneSize
                        && ele.getPos().getPosY()<player.getPos().getPosX()+zoneSize
                        && ele.getPos().getPosY()>=player.getPos().getPosX()){
                    Logger.log(player.getName()+" hit "+ele.getName());
                }
            }*/
        }
    }
    private int damageCalc(ServerUser attacker, ServerUser victim){
        if(victim.getArmor()!=Equipment.NONE){
            return attacker.getWeapon().getPoint()*(1-victim.getArmor().getPoint()/100);
        }else{
            return attacker.getWeapon().getPoint();
        }
    }
    
    public void itemCollisionDetect(ServerUser player){
        double r=50;
        Iterator<Item> iter = itemList.iterator(); 
        while(iter.hasNext()){
            Item i = iter.next();
            double x1 = i.getPos().getPosX();
            double x2 = player.getPos().getPosX();
            double y1 = i.getPos().getPosX();
            double y2 = player.getPos().getPosX();

            double result = Math.sqrt((x1-x2)*(x1-x2)+(y1-y2)*(y1-y2));
            if(result<=2*r){
                switch(i.getItemID()){
                    case SWORD:
                        player.addEquip(Equipment.SHORT_SWORD);
                    break;
                    case BOW:
                        player.addEquip(Equipment.LONG_BOW);
                    break;
                    case ARMOR_LV1:
                        if(player.getArmor()!=Equipment.ARMOR_LV2 ||
                           player.getArmor()!=Equipment.ARMOR_LV3){
                            player.addEquip(Equipment.ARMOR_LV1);
                        }
                    break;
                    case ARMOR_LV2:
                        if(player.getArmor()!=Equipment.ARMOR_LV3){
                            player.addEquip(Equipment.ARMOR_LV2);
                        }
                    break;
                    case ARMOR_LV3:
                        player.addEquip(Equipment.ARMOR_LV3);
                    break;
                }
                if(i.getItemID()==ItemID.POISON){
                    if(player.getPoison()>=3){
                        continue;
                    }else{
                        player.setPoison(player.getPoison()+1);
                    }
                }
                Logger.log(player);
                iter.remove();
            }
        }
    }
}

class MapUpdater extends TimerTask{
    private Game game;
    private GameMap map;
    
    public MapUpdater(Game _game){
        this.game = _game;
        this.map = this.game.getMap();
        Thread.currentThread().setPriority(Thread.MIN_PRIORITY);  
    }

    public void run(){
        //Logger.log(this.map.getCurrentTick());

        //Update items(Collision detect & arrow fly?)
        Iterator<Map.Entry<Integer, ServerUser>> iter
            = game.getOnlinePlayers().entrySet().iterator();
        while(iter.hasNext()){
            ServerUser me = iter.next().getValue();
            game.itemCollisionDetect(me);
        }


        iter = game.getOnlinePlayers().entrySet().iterator();
        while(iter.hasNext()){
            ServerUser me = iter.next().getValue();
            ClientHandler myHandler = me.getHandler();
            
            //Logger.log(me);

            //Game over behavior
            if(me.getBlood()<=0){
                Logger.log(me.getDisplayName()+" died");
                byte[] gameOverPacket = Parser.gameOver((byte)game.getOnlinePlayerCount());
                myHandler.send(gameOverPacket);
                //myHandler.terminate();
                iter.remove();
                continue;
            }

            //Update me 
            byte[] updateYouPacket = Parser.updateYou(me);
            myHandler.send(updateYouPacket);
            
            //Update other player
            ArrayList<ServerUser> online = new ArrayList<ServerUser>();
            Iterator<Map.Entry<Integer, ServerUser>> iter_other
                = game.getOnlinePlayers().entrySet().iterator();
            while(iter_other.hasNext()){
                ServerUser other = iter_other.next().getValue();
                if(other==me) continue;
                online.add(other);
            }
            byte[] updateSinglePlayerPacket = Parser.updateSinglePlayer(online);
            myHandler.send(updateSinglePlayerPacket);

            //updateGlobalItems
            byte[] updateGlobalItemsPacket = Parser.updateGlobalItem(game.getItemList());
            myHandler.send(updateGlobalItemsPacket);

            //Update map(1 TPS)
            if(map.getCurrentTick()%map.TICK_PER_SECOND == 0){
                byte[] updateMapPacket = Parser.updateMap(map);
                myHandler.send(updateMapPacket);
            }
        }
        
        this.map.incTick();

        //End condition
        if(game.getOnlinePlayerCount()<=1){
            game.resetGame();
        }

        //Flush buffer
        /*iter = game.getOnlinePlayers().entrySet().iterator();
        while(iter.hasNext()){
            ServerUser player = iter.next().getValue();
            player.getHandler().flush();
        }*/
    }

}