package com.github.nukcsie110.starbugs.util;

import java.nio.ByteBuffer;

public class Logger{
    public static void log(Object x){
        System.out.println(x);
    }
    public static void printBytes(byte[] x){
        for(byte i:x){
            System.out.printf("0x%02X ", i);
        }
        System.out.println(" "+x.length+" bytes");
    }
    public static void printByteBuffer(ByteBuffer x){
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
    public static void println(Object x){
        System.out.println(x);
    }

}