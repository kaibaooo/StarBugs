package com.github.nukcsie110.starbugs.server;

import com.github.nukcsie110.starbugs.packet.Handler;
import com.github.nukcsie110.starbugs.packet.Parser;
import com.github.nukcsie110.starbugs.packet.Union;
import com.github.nukcsie110.starbugs.server.ServerUser;
import com.github.nukcsie110.starbugs.server.RecvBuffer;
import com.github.nukcsie110.starbugs.util.Logger;
import java.nio.channels.*;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ClientHandler implements Handler {  
    private final static int BUF_SIZE=65536;
    private RecvBuffer recvBuf;
    private ByteBuffer writeBuf;
    private ServerUser player;  
      
    public ClientHandler(ServerUser _player) {  
    //public ClientHandler() {  
        this.recvBuf = new RecvBuffer(BUF_SIZE);
        this.writeBuf = ByteBuffer.allocate(BUF_SIZE);
        this.player = _player;
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
            Logger.log("Close connection from: "+client.getRemoteAddress());
            client.close();
            client.keyFor(selector).cancel();
        }
        while(recvBuf.hasPacket()){
            packetHandle(recvBuf.getPacket(), key);
        }
    }

    private void packetHandle(byte[] packet, SelectionKey key){
        Logger.printBytes(packet);
        Union parsedPacket = Parser.toUnion(packet);
        switch(parsedPacket.pkID){
            case 0x00:
                this.player.setName(parsedPacket.player.getName());
                Logger.log("New player joined");
                Logger.log(this.player);
                byte[] joinReplyPacket = Parser.joinReply((byte)0x00, this.player.getID());
                this.writeBuf.put(joinReplyPacket);
                this.writeBuf.flip();
                key.interestOps(SelectionKey.OP_WRITE); //Switch to write mode
            break;
            default:
        }
    }
      
    private void flushBuf(Selector selector, SelectionKey key) throws IOException {  
        // System.out.println("is writable...");  
        SocketChannel client = (SocketChannel) key.channel();
        client.write(writeBuf);  
        if (writeBuf.remaining() == 0) {  // write finished, switch to OP_READ  
            writeBuf.clear(); 
            key.interestOps(SelectionKey.OP_READ);  
        }  
    }
}  
