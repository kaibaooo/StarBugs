package com.github.nukcsie110.starbugs.server;

import com.github.nukcsie110.starbugs.packet.Handler;
import com.github.nukcsie110.starbugs.packet.Parser;
import com.github.nukcsie110.starbugs.packet.Union;
import com.github.nukcsie110.starbugs.server.ServerUser;
import com.github.nukcsie110.starbugs.packet.RecvBuffer;
import com.github.nukcsie110.starbugs.packet.WriteBuffer;
import com.github.nukcsie110.starbugs.util.Logger;
import java.nio.channels.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

public class ClientHandler implements Handler {  
    private final static int BUF_SIZE=655360;
    private RecvBuffer recvBuf;
    private WriteBuffer writeBuf;
    private ServerUser player;
    private Game game;
    private boolean kill;
    private SelectionKey myKey;
    private boolean joined;
    private String logPrefix;
      
    public ClientHandler(ServerUser _player, Game _game, SelectionKey _myKey) {  
    //public ClientHandler() {  
        this.recvBuf = new RecvBuffer(BUF_SIZE);
        this.writeBuf = new WriteBuffer(BUF_SIZE);
        this.player = _player;
        this.game = _game;
        this.kill = false;
        this.joined = false;
        this.myKey = _myKey;
        try{
            this.logPrefix = "["+((SocketChannel)(myKey.channel())).getRemoteAddress()+"] ";
        }catch(IOException e){}
    }  

    public void execute(Selector selector, SelectionKey key) {  
        try {  
            if (key.isReadable()) {  
                readPacket(selector, key);  
            } else if (key.isWritable()) {  
                flushBuf(selector, key);  
            }
        } catch (IOException e) {  
            key.cancel();  
            try { key.channel().close(); } catch (IOException ioe) { }  
        }  
    }  
      
    private void readPacket(Selector selector, SelectionKey key) throws IOException {  
        SocketChannel client = (SocketChannel) key.channel();  
        if(!recvBuf.read(client)){
            Logger.log("Connection reset by peer "+client.getRemoteAddress());
            client.close();
            client.keyFor(selector).cancel();
        }
        while(recvBuf.hasPacket()){
            packetHandle(recvBuf.getPacket(), key);
        }
    }

    private void packetHandle(byte[] packet, SelectionKey key)throws IOException{
        //Logger.printBytes(packet);
        Union parsedPacket = Parser.toUnion(packet);
        switch(parsedPacket.pkID){
            case 0x00:
                //Ignore logined packet
                if(this.joined){
                    break;
                }

                //If cannot join (Game started or queue is full)
                if(!game.addPlayer(this.player)){
                    Logger.log(logPrefix+"Reject join");
                    byte[] joinReplyPacket = Parser.joinReply((byte)0x01, (short)0);
                    this.send(joinReplyPacket);
                    this.kill = true;
                    break;
                }

                //Set basic information
                this.player.setName(parsedPacket.player.getName());
                Logger.log(logPrefix+"New player joined: "+this.player.getDisplayName());
                this.joined = true;

                //Send join reply
                byte[] joinReplyPacket = Parser.joinReply((byte)0x00, this.player.getID());
                this.send(joinReplyPacket);

                Logger.log("Online players: "+game.getOnlinePlayerCount());
                
                //Sent name table of online players
                HashMap<Integer, ServerUser> onlinePlayer = game.getOnlinePlayers();
                ArrayList<ServerUser> playerList = new ArrayList<ServerUser>(onlinePlayer.values());
                for(ServerUser i:playerList){
                    Logger.log("\t"+i.getDisplayName());
                }
                byte[] nameTablePacket = Parser.updateNameTable(playerList, game.MAX_PLAYER);
                game.broadcast(nameTablePacket);

                
                //if queue is full
                if(game.getOnlinePlayerCount()==game.MAX_PLAYER){
                    game.startGame();
                }

            break;
            case 0x07:
                Logger.log(logPrefix+"Recived keyDown: "+(char)(parsedPacket.keyCode));
                synchronized(player){
                    switch((char)(parsedPacket.keyCode)){
                        case 'W': player.moveTop(); break;
                        case 'S': player.moveDown(); break;
                        case 'A': player.moveLeft(); break;
                        case 'D': player.moveRight(); break;
                    }
                }
                //byte[] gameOverPacket = Parser.gameOver((byte)0);
                //this.send(gameOverPacket);
                //this.kill = true;
                
            break;
            case 0x08:
                Logger.log(logPrefix+"Recived keyUp: "+parsedPacket.keyCode);
            break;
            case 0x09:
                Logger.log(logPrefix+"Recived updateDirection: "+parsedPacket.newDirection);
                player.getPos().setDir(parsedPacket.newDirection);
            break;
            default:
        }
    }
      
    private void flushBuf(Selector selector, SelectionKey key) throws IOException {  
        // System.out.println("is writable...");  
        SocketChannel client = (SocketChannel) key.channel();
        writeBuf.write(client);
        if (writeBuf.isEmpty()) {  // write finished, switch to OP_READ  
            if(this.kill){
                //Wait flush data then close connection
                Logger.log("Disconnect with "+client.getRemoteAddress());
                client.close();
            }else{
                key.interestOps(SelectionKey.OP_READ);
            }  
        }  
    }

    public synchronized void send(byte[] x){
        try{
            if(this.myKey.isValid()){
                this.writeBuf.put(x);
            }
            if(this.myKey.isValid() && this.myKey.interestOps()!=SelectionKey.OP_WRITE){
                this.myKey.interestOps(SelectionKey.OP_WRITE); //Switch to write mode
            }
        }catch(CancelledKeyException e){
            Logger.log(this.logPrefix+"CancelledKeyException");
        }
    }

    public synchronized void terminate(){
        this.kill = true;
    }
}  
