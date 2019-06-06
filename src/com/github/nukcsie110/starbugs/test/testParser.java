package com.github.nukcsie110.starbugs.test;

import com.github.nukcsie110.starbugs.packet.Parser;
import com.github.nukcsie110.starbugs.packet.Union;
import com.github.nukcsie110.starbugs.basic.User;

public class testParser{
    public static void main(String args[]){
        println("---Testing join parser---");
        byte[] joinPacket = Parser.join("test123");
        printBytes(joinPacket);
        Union parsedJoin = Parser.toUnion(joinPacket);
        println(parsedJoin.name);
        
        joinPacket = Parser.join("0123456789012345678901234567890123456789");
        printBytes(joinPacket);
        parsedJoin = Parser.toUnion(joinPacket);
        println(parsedJoin.name);

        println("---Testing joinReply parser---");
        byte[] joinReplyPacket = Parser.joinReply((byte)0, 0xbeef);
        printBytes(joinReplyPacket);
        Union parsedJoinReply = Parser.toUnion(joinReplyPacket);
        println(parsedJoinReply.state);
        println(User.getIDString(parsedJoinReply.playerID));
    }
    private static void println(Object x){
        System.out.println(x);
    }
    private static void printBytes(byte[] x){
        for(byte i:x){
            System.out.printf("0x%02X ", i);
        }
        println(" "+x.length+" bytes");
    }
}