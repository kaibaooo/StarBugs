package com.github.nukcsie110.starbugs.test;

import com.github.nukcsie110.starbugs.packet.Parser;
import com.github.nukcsie110.starbugs.packet.Union;
import com.github.nukcsie110.starbugs.basic.User;
import com.github.nukcsie110.starbugs.basic.Item;
import com.github.nukcsie110.starbugs.basic.Coordinate;
import com.github.nukcsie110.starbugs.basic.Equipment;
import com.github.nukcsie110.starbugs.basic.Map;
import com.github.nukcsie110.starbugs.util.Logger;
import java.util.ArrayList;

public class testParser{
    public static void main(String args[]){
        Logger.println("---Testing join parser---");
        byte[] joinPacket = Parser.join("test123");
        Logger.printBytes(joinPacket);
        Union parsedJoin = Parser.toUnion(joinPacket);
        Logger.println(parsedJoin.player.getName());
        
        joinPacket = Parser.join("0123456789012345678901234567890123456789");
        Logger.printBytes(joinPacket);
        parsedJoin = Parser.toUnion(joinPacket);
        Logger.println(parsedJoin.player.getName());

        Logger.println("---Testing joinReply parser---");
        byte[] joinReplyPacket = Parser.joinReply((byte)0, (short)0xbeef);
        Logger.printBytes(joinReplyPacket);
        Union parsedJoinReply = Parser.toUnion(joinReplyPacket);
        Logger.println(parsedJoinReply.state);
        Logger.println(User.getIDString(parsedJoinReply.player.getID()));

        Logger.println("---Testing updateNameTable parser---");
        ArrayList<User> userTable = new ArrayList<>();
        userTable.add(new User((short)0xDEAD, "ABC", new Coordinate(0,0,0)));
        userTable.add(new User((short)0xBEEF, "123456", new Coordinate(0,0,0)));
        byte[] updateNameTablePacket = Parser.updateNameTable(userTable);
        Logger.printBytes(updateNameTablePacket);
        Union parsedUpdateNameTable = Parser.toUnion(updateNameTablePacket);
        for(User i: parsedUpdateNameTable.nameTable){
            Logger.println(i.getDisplayName());
        }
        
        Logger.println("---Testing updateGlobalItem parser---");
        ArrayList<Item> items = new ArrayList<>();
        items.add(new Item((byte)12, new Coordinate(0.5f,0.5f,0.5f)));
        items.add(new Item((byte)87, new Coordinate(10f,10f,179.9f)));
        byte[] updateGlobalItemPacket = Parser.updateGlobalItem(items);
        Logger.printBytes(updateGlobalItemPacket);
        Union parsedUpdateGlobalItem = Parser.toUnion(updateGlobalItemPacket);
        for(Item i: parsedUpdateGlobalItem.items){
            Logger.println(i.getItemID()+" "+i.getCoordinate());
        }

        Logger.println("---Testing updateSinglePlayer parser---");
        User a = new User();
        a.setID(0x8787);
        a.getPos().moveTo(3.5f, 100.0001f);
        a.getPos().turnDir(87.45f);
        a.addEquip(Equipment.LONG_BOW);
        a.addEquip(Equipment.ARMOR_LV1);
        a.setWeaponInHand(Equipment.LONG_BOW);
        byte[] updateSinglePlayerPacket = Parser.updateSinglePlayer(a);
        Logger.printBytes(updateSinglePlayerPacket);
        Union parsedUpdateSinglePlayer = Parser.toUnion(updateSinglePlayerPacket);
        Logger.println(parsedUpdateSinglePlayer.player);

        Logger.println("---Testing updateYou parser---");
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
        Logger.printBytes(updateYouPacket);
        Union parsedUpdateYou = Parser.toUnion(updateYouPacket);
        Logger.println(parsedUpdateYou.player);
        
        Logger.println("---Testing updateMap parser---");
        Map b = new Map();
        b.setSaveZone(10f, new Coordinate(87f, 78.5f, 0));
        b.setCurrentTime(20123);
        b.setNextSaveZoneTime(40000);
        b.setCurrentPlayers(10);
        byte[] updateMapPacket = Parser.updateMap(b);
        Logger.printBytes(updateMapPacket);
        Union parsedUpdateMap = Parser.toUnion(updateMapPacket);
        Logger.println(parsedUpdateMap.map);

        Logger.println("---Testing keyDown/keyUp parser---");
        byte[] keyDownPacket = Parser.keyDown((byte)0x87);
        byte[] keyUpPacket = Parser.keyUp((byte)0x87);
        Logger.printBytes(keyDownPacket);
        Logger.printBytes(keyUpPacket);
        Union parsedKeyDown = Parser.toUnion(keyDownPacket);
        Union parsedKeyUp = Parser.toUnion(keyUpPacket);
        Logger.println(parsedKeyDown.keyCode);
        Logger.println(parsedKeyUp.keyCode);
        
        Logger.println("---Testing updateDirection parser---");
        float newDirection = 0.000087f;
        byte[] updateDirectionPacket = Parser.updateDirection(newDirection);
        Logger.printBytes(updateDirectionPacket);
        Union parsedUpdateDirection = Parser.toUnion(updateDirectionPacket);
        Logger.println(parsedUpdateDirection.newDirection);

        Logger.println("---Testing gameOver parser---");
        byte rank = (byte)1;
        byte[] gameOverPacket = Parser.gameOver(rank);
        Logger.printBytes(gameOverPacket);
        Union parsedGameOver = Parser.toUnion(gameOverPacket);
        Logger.println(parsedGameOver.rank);

    }
}