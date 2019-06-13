package com.github.nukcsie110.starbugs.client;

import com.github.nukcsie110.starbugs.packet.RecvBuffer;
import com.github.nukcsie110.starbugs.packet.WriteBuffer;
import com.github.nukcsie110.starbugs.packet.Union;
import com.github.nukcsie110.starbugs.util.Logger;
import com.github.nukcsie110.starbugs.packet.Parser;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.nio.channels.*;
import java.util.*;


public class Client extends Thread{
    private static final int BUFFER_LEN = 819200;
    private static final int SELECTOR_TIMEOUT = 1;
    private InetSocketAddress serverAddr; 
    private RecvBuffer recvBuf;
    private WriteBuffer writeBuf;
    private SocketChannel client;
    private Selector selector;
    private SelectionKey key;
    private Boolean gameEnded;
    private Boolean ready;
    
    public Client(){
        this.recvBuf = new RecvBuffer(BUFFER_LEN);
        this.writeBuf = new WriteBuffer(BUFFER_LEN);
        this.serverAddr = new InetSocketAddress("127.0.0.1", 8787);
        this.ready = false;
        this.gameEnded = false;
    }

    private void initConnection() throws IOException{
        this.selector = Selector.open();
        this.client = SocketChannel.open();
        this.client.configureBlocking(false);
        boolean connected = this.client.connect(this.serverAddr);
        if(!connected){
            this.key = this.client.register(this.selector, SelectionKey.OP_CONNECT);
            Logger.log("Connection pending...");
        }else{
            this.key = this.client.register(this.selector, SelectionKey.OP_READ);
            synchronized(this.ready){
                this.ready = true;
            }
            Logger.log("Connected to "+this.serverAddr);
        }
    }

    private void closeConnection() throws IOException{
        Logger.log("Disconnect with "+this.client.getRemoteAddress());
        this.client.close();
        this.client.keyFor(selector).cancel();
        this.selector.selectNow(); //Update selector keys
    }

    public synchronized void close(){
        this.gameEnded = true;
    }

    public boolean isReady(){
        synchronized(this.ready){
            return this.ready;
        }
    }

    public void run(){
        try{
            this.initConnection();

            while(gameEnded!=true){
                
                //Determine write mode or read mode
                if(this.ready){
                    if(this.writeBuf.isEmpty()){
                        this.key.interestOps(SelectionKey.OP_READ);
                    }else{
                        this.key.interestOps(SelectionKey.OP_WRITE);
                    }
                }

                this.selector.select(this.SELECTOR_TIMEOUT);
                if(this.key.isConnectable()){ //Not connect yet
                    if(this.client.isConnectionPending()){
                        this.client.finishConnect();
                        this.key = this.client.register(this.selector, SelectionKey.OP_READ);
                        synchronized(this.ready){
                            this.ready = true;
                        }
                        Logger.log("Connected to "+this.serverAddr);
                    }
                }else if(this.key.isReadable()){
                    if(!this.recvBuf.read(this.client)){
                        Logger.log("Connection reset by peer "+this.client.getRemoteAddress());
                        this.gameEnded = true;
                    }
                }else if(this.key.isWritable()){
                    //Logger.log("is writable");
                    this.writeBuf.write(this.client);
                }
            }

            this.closeConnection();
        
        } catch (IOException e) {  
            this.key.cancel();  
            try { this.key.channel().close(); } catch (IOException ioe) { }  
        }
    }  

    public boolean isReadable(){
        return this.recvBuf.hasPacket();
    }

    public Union read(){
        byte[] packet = this.recvBuf.getPacket();
        //Logger.printBytes(packet);
        return Parser.toUnion(packet);
    }

    public void join(String name){
        this.write(Parser.join(name));
    }
    
    public void keyDown(byte keyCode){
        this.write(Parser.keyDown(keyCode));
    }
    
    public void keyUp(byte keyCode){
        this.write(Parser.keyUp(keyCode));
    }
    
    public void updateDirection(float newDir){
        this.write(Parser.updateDirection(newDir));
    }

    public void write(byte[] packet){
        this.writeBuf.put(packet);
    }
}