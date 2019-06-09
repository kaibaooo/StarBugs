package com.github.nukcsie110.starbugs.test;

import com.github.nukcsie110.starbugs.packet.Parser;
import com.github.nukcsie110.starbugs.packet.Union;
import com.github.nukcsie110.starbugs.basic.User;
import com.github.nukcsie110.starbugs.basic.Item;
import com.github.nukcsie110.starbugs.basic.Coordinate;
import com.github.nukcsie110.starbugs.basic.Equipment;
import com.github.nukcsie110.starbugs.basic.Map;
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

        println("---Testing updateYou parser---");
        a = new User();
        a.setID(0x8787);
        a.getPos().moveTo(3.5f, 100.0001f);
        a.getPos().turnDir(87.45f);
        a.addEquip(Equipment.LONG_BOW);
        a.addEquip(Equipment.SHORT_SWORD);
        a.addEquip(Equipment.ARMOR_LV2);
        a.setWeaponInHand(Equipment.LONG_BOW);
        a.setBlood(87);
        a.setPoison(3);
        byte[] updateYouPacket = Parser.updateYou(a);
        printBytes(updateYouPacket);
        Union parsedUpdateYou = Parser.toUnion(updateYouPacket);
        println(parsedUpdateYou.player);
        
        println("---Testing updateMap parser---");
        Map b = new Map();
        b.setSaveZone(10f, new Coordinate(87f, 78.5f, 0));
        b.setCurrentTime(20123);
        b.setNextSaveZoneTime(40000);
        b.setCurrentPlayers(10);
        byte[] updateMapPacket = Parser.updateMap(b);
        printBytes(updateMapPacket);
        Union parsedUpdateMap = Parser.toUnion(updateMapPacket);
        println(parsedUpdateMap.map);

        println("---Testing keyDown/keyUp parser---");
        byte[] keyDownPacket = Parser.keyDown((byte)0x87);
        byte[] keyUpPacket = Parser.keyUp((byte)0x87);
        printBytes(keyDownPacket);
        printBytes(keyUpPacket);
        Union parsedKeyDown = Parser.toUnion(keyDownPacket);
        Union parsedKeyUp = Parser.toUnion(keyUpPacket);
        println(parsedKeyDown.keyCode);
        println(parsedKeyUp.keyCode);
        
        println("---Testing updateDirection parser---");
        float newDirection = 0.000087f;
        byte[] updateDirectionPacket = Parser.updateDirection(newDirection);
        printBytes(updateDirectionPacket);
        Union parsedUpdateDirection = Parser.toUnion(updateDirectionPacket);
        println(parsedUpdateDirection.newDirection);

        println("---Testing gameOver parser---");
        byte rank = (byte)1;
        byte[] gameOverPacket = Parser.gameOver(rank);
        printBytes(gameOverPacket);
        Union parsedGameOver = Parser.toUnion(gameOverPacket);
        println(parsedGameOver.rank);

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