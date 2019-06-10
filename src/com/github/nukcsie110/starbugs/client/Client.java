package com.github.nukcsie110.starbugs.client;

import com.github.nukcsie110.starbugs.packet.RecvBuffer;
import com.github.nukcsie110.starbugs.packet.Union;
import com.github.nukcsie110.starbugs.util.Logger;
import com.github.nukcsie110.starbugs.packet.Parser;
import java.nio.ByteBuffer;
import java.net.InetSocketAddress;
import java.io.IOException;
import java.nio.channels.*;
import java.util.*;


public class Client extends Thread{
    private static final int BUFFER_LEN = 65536;
    private static final int SELECTOR_TIMEOUT = 1;
    private InetSocketAddress serverAddr; 
    private RecvBuffer recvBuf;
    private ByteBuffer writeBuf;
    private Boolean writeEnable; //Lock for writing
    private int tailOfLastPacket;
    private SocketChannel client;
    private Selector selector;
    private SelectionKey key;
    private Boolean gameEnded;
    private Boolean ready;
    
    public Client(){
        this.recvBuf = new RecvBuffer(BUFFER_LEN);
        this.writeBuf = ByteBuffer.allocate(BUFFER_LEN);
        this.serverAddr = new InetSocketAddress("127.0.0.1", 8787);
        this.tailOfLastPacket = 0;
        this.ready = false;
        this.writeEnable = false;
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
        Logger.log("Close connection from: "+this.client.getRemoteAddress());
        this.client.close();
        this.client.keyFor(selector).cancel();
        this.selector.selectNow();
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
                    if(!recvBuf.read(this.client)){
                        this.gameEnded = true;
                    }
                }else if(this.key.isWritable()){
                    synchronized(writeEnable){
                        if(writeEnable){
                            client.write(writeBuf);
                        }
                        if(!writeEnable | writeBuf.remaining() == 0) {  // write finished, switch to OP_READ  
                            writeBuf.clear(); 
                            //this.tailOfLastPacket = 0;
                            this.writeEnable = false;
                            this.key.interestOps(SelectionKey.OP_READ);
                        }  
                    }
                }
            }

            this.closeConnection();
        
        } catch (IOException e) {  
            key.cancel();  
            try { key.channel().close(); } catch (IOException ioe) { }  
        }
    }  

    public boolean isReadable(){
        return this.recvBuf.hasPacket();
    }

    public Union read(){
        return Parser.toUnion(this.recvBuf.getPacket());
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
        //int oldPos = this.writeBuf.position();
        //this.writeBuf.limit(this.writeBuf.capacity());
        //this.writeBuf.position(tailOfLastPacket); //Restore tail of data
        synchronized(this.writeEnable){
            this.writeBuf.put(packet); //Append packet to tail
        //this.tailOfLastPacket = this.writeBuf.position(); //Mark tail of data
        /*if(this.writeBusy){
            //Restore writing state
            this.writeBuf.limit(this.writeBuf.position());
            this.writeBuf.position(oldPos);
        }*/
            this.writeBuf.flip();
            this.writeEnable = true;
            this.key.interestOps(SelectionKey.OP_WRITE); //Switch to write mode
        }
        //this.flush();
    }
    private void flush(){
        this.writeBuf.flip();
        //this.writeBuf.limit(this.writeBuf.position());
        //this.writeBuf.position(0);
        this.key.interestOps(SelectionKey.OP_WRITE); //Switch to write mode
    }
}