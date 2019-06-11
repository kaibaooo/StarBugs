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

public class ClientHandler implements Handler {  
    private final static int BUF_SIZE=655360;
    private RecvBuffer recvBuf;
    private WriteBuffer writeBuf;
    private ServerUser player;
    private boolean kill;
      
    public ClientHandler(ServerUser _player) {  
    //public ClientHandler() {  
        this.recvBuf = new RecvBuffer(BUF_SIZE);
        this.writeBuf = new WriteBuffer(BUF_SIZE);
        this.player = _player;
        this.kill = false;
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
        String logPrefix = "["+((SocketChannel)(key.channel())).getRemoteAddress()+"] ";
        switch(parsedPacket.pkID){
            case 0x00:
                this.player.setName(parsedPacket.player.getName());
                Logger.log(logPrefix+"New player joined: "+this.player.getDisplayName());
                
                byte[] joinReplyPacket = Parser.joinReply((byte)0x00, this.player.getID());
                this.writeBuf.put(joinReplyPacket);

                key.interestOps(SelectionKey.OP_WRITE); //Switch to write mode
            break;
            case 0x07:
                Logger.log(logPrefix+"Recived keyDown: "+parsedPacket.keyCode);
                
                byte[] gameOverPacket = Parser.gameOver((byte)0);
                this.writeBuf.put(gameOverPacket);
                this.kill = true;
                
                key.interestOps(SelectionKey.OP_WRITE); //Switch to write mode
            break;
            case 0x08:
                Logger.log(logPrefix+"Recived keyUp: "+parsedPacket.keyCode);
            break;
            case 0x09:
                Logger.log(logPrefix+"Recived updateDirection: "+parsedPacket.newDirection);
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
}  
