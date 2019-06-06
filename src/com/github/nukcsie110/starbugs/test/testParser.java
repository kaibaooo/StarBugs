package com.github.nukcsie110.starbugs.test;

import com.github.nukcsie110.starbugs.packet.Parser;
import com.github.nukcsie110.starbugs.packet.Union;
import com.github.nukcsie110.starbugs.basic.User;
import java.util.ArrayList;

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
        byte[] joinReplyPacket = Parser.joinReply((byte)0, (short)0xbeef);
        printBytes(joinReplyPacket);
        Union parsedJoinReply = Parser.toUnion(joinReplyPacket);
        println(parsedJoinReply.state);
        println(User.getIDString(parsedJoinReply.playerID));

        println("---Testing updateNameTable parser---");
        ArrayList<User> userTable = new ArrayList<>();
        userTable.add(new User((short)0xDEAD, "ABC"));
        userTable.add(new User((short)0xBEEF, "123456"));
        byte[] updateNameTablePacket = Parser.updateNameTable(userTable);
        printBytes(updateNameTablePacket);
        Union parsedUpdateNameTable = Parser.toUnion(updateNameTablePacket);
        for(User i: parsedUpdateNameTable.nameTable){
            println(i.getDisplayName());
        }
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