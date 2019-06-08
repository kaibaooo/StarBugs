package com.github.nukcsie110.starbugs.test;

import com.github.nukcsie110.starbugs.packet.Parser;
import com.github.nukcsie110.starbugs.packet.Union;
import com.github.nukcsie110.starbugs.basic.User;
import com.github.nukcsie110.starbugs.basic.Item;
import com.github.nukcsie110.starbugs.basic.Coordinate;
import com.github.nukcsie110.starbugs.basic.Equipment;
import java.util.ArrayList;

public class testParser{
    public static void main(String args[]){
        println("---Testing join parser---");
        byte[] joinPacket = Parser.join("test123");
        printBytes(joinPacket);
        Union parsedJoin = Parser.toUnion(joinPacket);
        println(parsedJoin.player.getName());
        
        joinPacket = Parser.join("0123456789012345678901234567890123456789");
        printBytes(joinPacket);
        parsedJoin = Parser.toUnion(joinPacket);
        println(parsedJoin.player.getName());

        println("---Testing joinReply parser---");
        byte[] joinReplyPacket = Parser.joinReply((byte)0, (short)0xbeef);
        printBytes(joinReplyPacket);
        Union parsedJoinReply = Parser.toUnion(joinReplyPacket);
        println(parsedJoinReply.state);
        println(User.getIDString(parsedJoinReply.player.getID()));

        println("---Testing updateNameTable parser---");
        ArrayList<User> userTable = new ArrayList<>();
        userTable.add(new User((short)0xDEAD, "ABC", new Coordinate(0,0,0)));
        userTable.add(new User((short)0xBEEF, "123456", new Coordinate(0,0,0)));
        byte[] updateNameTablePacket = Parser.updateNameTable(userTable);
        printBytes(updateNameTablePacket);
        Union parsedUpdateNameTable = Parser.toUnion(updateNameTablePacket);
        for(User i: parsedUpdateNameTable.nameTable){
            println(i.getDisplayName());
        }
        
        println("---Testing updateGlobalItem parser---");
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item((byte)12, new Coordinate(0.5f,0.5f,0.5f)));
        items.add(new Item((byte)87, new Coordinate(10f,10f,179.9f)));
        byte[] updateGlobalItemPacket = Parser.updateGlobalItem(items);
        printBytes(updateGlobalItemPacket);
        Union parsedUpdateGlobalItem = Parser.toUnion(updateGlobalItemPacket);
        for(Item i: parsedUpdateGlobalItem.items){
            println(i.getItemID()+" "+i.getCoordinate());
        }

        println("---Testing updateSinglePlayer parser---");
        User a = new User();
        a.setID(0x8787);
        a.getPos().moveTo(3.5f, 100.0001f);
        a.getPos().turnDir(87.45f);
        a.addEquip(Equipment.LONG_BOW);
        a.addEquip(Equipment.ARMOR_LV1);
        a.setWeaponInHand(Equipment.LONG_BOW);
        byte[] updateSinglePlayerPacket = Parser.updateSinglePlayer(a);
        printBytes(updateSinglePlayerPacket);
        Union parsedUpdateSinglePlayer = Parser.toUnion(updateSinglePlayerPacket);
        println(parsedUpdateSinglePlayer.player);
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