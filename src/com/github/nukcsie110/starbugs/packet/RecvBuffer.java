package com.github.nukcsie110.starbugs.packet;

import java.nio.channels.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import com.github.nukcsie110.starbugs.util.Logger;

public class RecvBuffer{
    private ByteBuffer recvBuf;
    private int recvCnt;
    private int expectPacketLength;
    private int headOfPacket;
    private static final int HEADER_LEN = 5;

    /**
     * @param bufLength How many bytes you want to allocate to recvBuf
     */
    public RecvBuffer(int bufLength){
        this.recvBuf = ByteBuffer.allocate(bufLength);
        this.recvCnt = 0;
        this.expectPacketLength = -1;
        this.headOfPacket = 0;
    }

    /**
     * Read available bytes from target channel
     * @param target Target channel
     * @return Whether read successful
     */
    public synchronized boolean read(SocketChannel target) throws IOException{
        int n = target.read(recvBuf);  
        if(n!=-1 && n!=0){ //not reach End-Of-Stream
            Logger.log("Recived: "+n+" bytes");
            this.recvCnt += n;
        }
        return n!=-1;
    }

    /**
     * @return Whether there are complete packet in buffer
     */
    public synchronized boolean hasPacket(){
        boolean rtVal = false;
        //Read packet length
        if(this.expectPacketLength == -1 && this.recvCnt>=5){
            int oldPos = this.recvBuf.position();
            this.recvBuf.position(headOfPacket); //Restore starting point of this packet
            this.recvBuf.get(); //Skip 1 byte (pkID)
            this.expectPacketLength = HEADER_LEN+this.recvBuf.getInt();
            recvBuf.position(oldPos); //Restore position
            Logger.log("Got header; expected length:"+ this.expectPacketLength);
        }

        if(this.expectPacketLength != -1 && this.recvCnt>=this.expectPacketLength){
            rtVal = true;
        }else{
            rtVal = false;
        }
        return rtVal;
    }

    /**
     * Return the first most complete packet in buffer
     * and move pointer to next packet
     * @return The first most complete packet in buffer
     */
    public synchronized byte[] getPacket(){
        byte[] rtVal;
        if(!this.hasPacket()){
            rtVal = null;
        }else{
            this.recvBuf.position(headOfPacket); //Restore starting point of this packet
            //Read only expected length
            rtVal = new byte[this.expectPacketLength];
            //Logger.log("Packet size: "+ this.expectPacketLength);
            this.recvBuf.get(rtVal);
            this.headOfPacket = this.recvBuf.position(); //Mark the starting point of next packet
            this.recvCnt -= this.expectPacketLength;
            this.expectPacketLength = -1; //Indicate to read length of next packet

            //if no data in buffer, reset the buffer
            if(this.recvCnt == 0){
                this.recvBuf.clear();
                this.headOfPacket = 0;
            }
        }
        return rtVal;
    }
}