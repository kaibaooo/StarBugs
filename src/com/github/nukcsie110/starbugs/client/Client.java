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


public class Client{
    private static final int BUFFER_LEN = 65536;
    private InetSocketAddress serverAddr; 
    private RecvBuffer recvBuf;
    private ByteBuffer writeBuf;
    private boolean writeBusy;
    private int tailOfLastPacket;
    private SocketChannel client;
    private Selector selector;
    private SelectionKey key;
    
    public Client(){
        this.recvBuf = new RecvBuffer(BUFFER_LEN);
        this.writeBuf = ByteBuffer.allocate(BUFFER_LEN);
        this.serverAddr = new InetSocketAddress("127.0.0.1", 8787);
        this.writeBusy = false;
        this.tailOfLastPacket = 0;
    }

    public void initConnection(){
        try{
            this.selector = Selector.open();
            this.client = SocketChannel.open(serverAddr);
            this.client.configureBlocking(false);  
            this.key = this.client.register(this.selector, SelectionKey.OP_READ);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void select(int timeout){
        try{
            this.selector.select(timeout);  
            if(this.key.isReadable()){
                if(!recvBuf.read(this.client)){
                    Logger.log("Close connection from: "+this.client.getRemoteAddress());
                    this.client.close();
                    this.client.keyFor(selector).cancel();
                }
            }else if(this.key.isWritable()){
                client.write(writeBuf);  
                if (writeBuf.remaining() == 0) {  // write finished, switch to OP_READ  
                    writeBuf.clear(); 
                    this.tailOfLastPacket = 0;
                    key.interestOps(SelectionKey.OP_READ);
                    this.writeBusy = false;
                }  
            }
        
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

    public boolean isWritable(){
        return !this.writeBusy;
    }
    public void write(byte[] packet){
        int oldPos = this.writeBuf.position();
        this.writeBuf.limit(this.writeBuf.capacity());
        this.writeBuf.position(tailOfLastPacket); //Restore tail of data
        this.writeBuf.put(packet); //Append packet to tail
        this.tailOfLastPacket = this.writeBuf.position(); //Mark tail of data
        if(this.writeBusy){
            //Restore writing state
            this.writeBuf.limit(this.writeBuf.position());
            this.writeBuf.position(oldPos);
        }
    }
    public void flush(){
        this.writeBuf.limit(this.writeBuf.position());
        this.writeBuf.position(0);
        this.key.interestOps(SelectionKey.OP_WRITE); //Switch to write mode
        this.writeBusy = true;
    }
}