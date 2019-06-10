package com.github.nukcsie110.starbugs.packet;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.io.IOException;

public class WriteBuffer{
    private ByteBuffer writeBuf;
    private int wrotedIndex;
    private int endOfBuffer;
    public WriteBuffer(int _len){
        writeBuf = ByteBuffer.allocate(_len);
        wrotedIndex = 0;
        endOfBuffer = 0;
    }

    /**
     * Put data into buffer
     */
    public synchronized void put(byte[] x){
        writeBuf.position(endOfBuffer);
        writeBuf.limit(writeBuf.capacity());
        writeBuf.put(x);
        endOfBuffer = writeBuf.position();
    }

    /**
     * @return Whether buffer is empty
     */
    public synchronized boolean isEmpty(){
        return (endOfBuffer-wrotedIndex)==0;
    }
    
    /**
     * Flush buffer to target
     * @return True if and only if no data in buffer
     */
    public synchronized void write(SocketChannel target) throws IOException{
            if(!isEmpty()){
                writeBuf.position(wrotedIndex);
                writeBuf.limit(endOfBuffer);
                target.write(writeBuf);
                if(writeBuf.remaining() == 0) {  // write finished, switch to OP_READ  
                    writeBuf.clear();
                    endOfBuffer = 0;
                    wrotedIndex = 0;
                }else{
                    wrotedIndex = writeBuf.position();
                }
            }
    }
}