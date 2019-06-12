package com.github.nukcsie110.starbugs.packet;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.io.IOException;
import com.github.nukcsie110.starbugs.util.Logger;

public class WriteBuffer{
    private ByteBuffer writeBuf;
    private int wrotedIndex;
    private int endOfBuffer;
    private boolean empty;
    public WriteBuffer(int _len){
        writeBuf = ByteBuffer.allocate(_len);
        wrotedIndex = 0;
        endOfBuffer = 0;
    }

    /**
     * Put data into buffer
     */
    public synchronized void put(byte[] x){
        //writeBuf.position(endOfBuffer);
        //writeBuf.limit(writeBuf.capacity());
        writeBuf.put(x);
        empty = false;
        //endOfBuffer = writeBuf.position();
    }

    /**
     * @return Whether buffer is empty
     */
    public synchronized boolean isEmpty(){
        return empty;
        //return (endOfBuffer-wrotedIndex)==0;
    }
    
    /**
     * Flush buffer to target
     * @return True if and only if no data in buffer
     */
    public synchronized void write(SocketChannel target) throws IOException{
            //if(!isEmpty()){
                writeBuf.flip();
                //writeBuf.position(wrotedIndex);
                //writeBuf.limit(endOfBuffer);
                target.write(writeBuf);
                writeBuf.compact();
                if(writeBuf.position() == 0) {  // write finished, switch to OP_READ  
                    empty = true;
                    writeBuf.clear();
                    //endOfBuffer = 0;
                    //wrotedIndex = 0;
                }else{
                    //wrotedIndex = writeBuf.position();
                }
            //}
    }
}