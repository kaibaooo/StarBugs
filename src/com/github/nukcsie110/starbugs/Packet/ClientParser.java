package com.github.nukcsie110.starbugs.Packet;
import java.util.Arrays;
/**
 * @version    0.1
 * @since      0.1
 */
public class ClientParser{
    private byte type;
    private byte[] data;
    public void parse(byte[] src){
        type = src[0];
        data = Arrays.copyOfRange(src, 1, src.length-1);
        switch(type){
            case 0x00:
                // statement
                break;
            case 0x01:
                // statement
                break;
        }
    }
    // login - Server packet parser section
    private static void parseJoinReply(){

    }
    private static void parseUpdateNameTable(){

    }
    // Gaming - Server packet parser section
    private static void parseUpdateGlobleItem(){

    }
    private static void parseUpdateSinglePlayer(){

    }
    private static void parseUpdateYou(){

    }
    private static void parseUpdateMap(){

    }
    private static void parseGameOver(){

    }
}