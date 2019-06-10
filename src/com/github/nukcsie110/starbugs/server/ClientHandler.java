package com.github.nukcsie110.starbugs.server;

import com.github.nukcsie110.starbugs.packet.Handler;
import com.github.nukcsie110.starbugs.server.ServerUser;
import com.github.nukcsie110.starbugs.server.RecvBuffer;
import java.nio.channels.*;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ClientHandler implements Handler {  
    private final static int BUF_SIZE=1024;
    private RecvBuffer recvBuf;
    //private int readCnt;
    //private int exceptedPacketLength;
    //private int headOfPacket;
    //private ByteBuffer packetBuf;
    private ByteBuffer writeBuf;
    private ServerUser player;  
      
    public ClientHandler(ServerUser _player) {  
    //public ClientHandler() {  
        this.recvBuf = new RecvBuffer(BUF_SIZE);
        this.writeBuf = ByteBuffer.allocate(BUF_SIZE);
        //this.player = _player;
        //this.readCnt = 0;
        //this.exceptedPacketLength = -1;
        //this.recvBuf.mark();
        //this.headOfPacket = 0;
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
            log("Close connection from: "+client.getRemoteAddress());
            client.close();
            client.keyFor(selector).cancel();
        }
        while(recvBuf.hasPacket()){
            packetHandle(recvBuf.getPacket());
        }
    }

    private void packetHandle(byte[] packet){
        printBytes(packet);
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
    private void log(Object x){
        System.out.println(x);
    }
    private static void printBytes(byte[] x){
        for(byte i:x){
            System.out.printf("0x%02X ", i);
        }
        System.out.println(" "+x.length+" bytes");
    }
    private void printByteBuffer(ByteBuffer x){
        int oldPos = x.position();
        int oldLim = x.limit();

        x.rewind();
        
        while(x.hasRemaining()){
            System.out.printf("%02X ", x.get());
        }
        System.out.println("");

        x.position(oldPos);
        x.limit(oldLim);
    }
}  
