package com.github.nukcsie110.starbugs.packet;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.ArrayList;
import com.github.nukcsie110.starbugs.basic.User;
import com.github.nukcsie110.starbugs.basic.Item;
import com.github.nukcsie110.starbugs.basic.Coordinate;
import com.github.nukcsie110.starbugs.basic.Equipment;
import com.github.nukcsie110.starbugs.basic.Map;
import com.github.nukcsie110.starbugs.util.Logger;
import java.io.UnsupportedEncodingException;

public class Parser{
    public static Union toUnion(ByteBuffer x){
        byte[] bytes = new byte[x.remaining()];
        x.get(bytes, 0, bytes.length);
        return toUnion(bytes);
    }

    public static Union toUnion(byte[] x){
        Union rtVal = new Union();
        if(x.length < 5){
            rtVal.pkID = -1;
            Logger.log("Invaild packet length");
        }else if(x.length != (x[1]<<24)+(x[2]<<16)+(x[3]<<8)+x[4]+5){
            rtVal.pkID = -1;
            Logger.log("Invaild packet length");
        }else{
            rtVal.pkID = x[0];
        }

        //Get rid of header
        x = Arrays.copyOfRange(x, 5, x.length);
        switch(rtVal.pkID){
            case 0x00: _join(x, rtVal); break;
            case 0x01: _joinReply(x, rtVal); break;
            case 0x02: _updateNameTable(x, rtVal); break;
            case 0x03: _updateGlobalItem(x, rtVal); break;
            case 0x04: _updateSinglePlayer(x, rtVal); break;
            case 0x05: _updateYou(x, rtVal); break;
            case 0x06: _updateMap(x, rtVal); break;
            case 0x07: _keyPress(x, rtVal); break;
            case 0x08: _keyPress(x, rtVal); break;
            case 0x09: _updateDirection(x, rtVal); break;
            case 0x10: _gameOver(x, rtVal); break;
            default:
                Logger.log("Invaild packet ID: "+ rtVal.pkID);
                rtVal.pkID = -1;
        }
        return rtVal;
    }

    private static void _join(byte[] x, Union y){
        int len = Math.min(x.length, 32);
        try{
            y.player = new User();
            y.player.setName(new String(x, "US-ASCII"));
        }catch(UnsupportedEncodingException e){
            Logger.log("Error: UnsupportedEncodingException");
        }
    }
    public static byte[] join(String name){
        name = trimAndPadName(name);
        return makePacket((byte)0x00, name.getBytes());
    }

    private static void _joinReply(byte[] x, Union y){
        if(x.length != 3){
            Logger.log("Illigle joinReply packet size");
            y.pkID = -1;
            return;
        }
        ByteBuffer buf = ByteBuffer.wrap(x);
        y.state = buf.get();

        y.player = new User();
        //Big-endian player id
        y.player.setID(buf.getShort());
    }

    public static byte[] joinReply(byte state, short playerID){
        byte[] buf = new byte[3];
        buf[0] = state;
        //Big-endian player id
        buf[1] = (byte)((playerID>>8)&0xFF);
        buf[2] = (byte)(playerID&0xFF);

        return makePacket((byte)0x01, buf);
    }

    private static void _updateNameTable(byte[] x, Union y){
        byte cnt = x[0];
        int elementSize = 34;
        if(x.length != 1+cnt*elementSize){
            Logger.log("Illigle updateNameTable packet length");
            y.pkID = -1;
            return;
        }
        ByteBuffer buf = ByteBuffer.wrap(x, 1, x.length-1);
        y.nameTable = new ArrayList<User>();
        while(cnt-- != 0){
            User tmpUser = new User();
            tmpUser.setID(buf.getShort());
            byte[] tmpName = new byte[32];
            buf.get(tmpName);
            try{
               tmpUser.setName(
                   new String(tmpName, "US-ASCII")
               );
            }catch(UnsupportedEncodingException e){
                Logger.log("Error: UnsupportedEncodingException");
            }
            y.nameTable.add(tmpUser);
        }
    }

    public static byte[] updateNameTable(ArrayList<User> table){
        ByteBuffer buf = ByteBuffer.allocate(1+table.size()*34);
        buf.put((byte)table.size());
        for(User i:table){
            buf.putShort(i.getID());
            buf.put(
                trimAndPadName(i.getName()).getBytes()
                );
        }
        return makePacket((byte)0x02, buf);
    }
    
    private static void _updateGlobalItem(byte[] x, Union y){
        byte cnt = x[0];
        int elementSize = 13;
        if(x.length != 1+cnt*elementSize){
            Logger.log("Illigle updateGlobalItem packet length");
            y.pkID = -1;
            return;
        }
        ByteBuffer buf = ByteBuffer.wrap(x, 1, x.length-1);
        y.items = new ArrayList<Item>();
        while(cnt-- != 0){
            byte itemID = buf.get();
            float posX = buf.getFloat();
            float posY = buf.getFloat();
            float dir = buf.getFloat();
            Coordinate tmpPos = new Coordinate(posX, posY, dir);
            Item tmpItem = new Item(itemID, tmpPos);
            y.items.add(tmpItem);
        }
    }

    public static byte[] updateGlobalItem(ArrayList<Item> items){
        ByteBuffer buf = ByteBuffer.allocate(1+items.size()*13);
        buf.put((byte)items.size());
        for(Item i:items){
            buf.put(i.getItemID());
            buf.putFloat(i.getCoordinate().getPosX());
            buf.putFloat(i.getCoordinate().getPosY());
            buf.putFloat(i.getCoordinate().getDir());
        }
        return makePacket((byte)0x03, buf);
    }

    private static void _updateSinglePlayer(byte[] x, Union y){
        ByteBuffer buf = ByteBuffer.wrap(x);
        int playerID = (int)buf.getShort();
        float posX = buf.getFloat();
        float posY = buf.getFloat();
        float dir = buf.getFloat();
        Coordinate pos = new Coordinate(posX, posY, dir);
        y.player = new User(playerID, "", pos);
        byte equipment = buf.get();
        Equipment weaponInHand = Equipment.getName((equipment>>4)&0xF);
        Equipment armor = Equipment.getName((equipment)&0xF);
        y.player.addEquip(weaponInHand);
        y.player.addEquip(armor);
        y.player.setWeaponInHand(weaponInHand);
    }

    public static byte[] updateSinglePlayer(User target){
        ByteBuffer buf = ByteBuffer.allocate(15);
        buf.putShort(target.getID());
        buf.putFloat(target.getPos().getPosX());
        buf.putFloat(target.getPos().getPosY());
        buf.putFloat(target.getPos().getDir());

        byte equipment = 0;
        equipment |= (((target.getWeaponInHand().getID())&0xF)<<4);
        equipment |= ((target.getArmor().getID())&0xF);
        buf.put(equipment);

        return makePacket((byte)0x04, buf);
    }
    private static void _keyPress(byte[] x, Union y){
        y.keyCode = x[0];
    }
    public static byte[] keyUp(byte keyCode){
        byte[] buf = new byte[1];
        buf[0] = keyCode;
        return makePacket((byte)0x07, buf);
    }
    public static byte[] keyDown(byte keyCode){
        byte[] buf = new byte[1];
        buf[0] = keyCode;
        return makePacket((byte)0x08, buf);
    }

    private static void _updateYou(byte[] x, Union y){
        ByteBuffer buf = ByteBuffer.wrap(x);
        float posX = buf.getFloat();
        float posY = buf.getFloat();
        float dir = buf.getFloat();
        Coordinate pos = new Coordinate(posX, posY, dir);
        short itemStatus = buf.getShort();
        User newPlayer = new User(0, "You", pos);
        newPlayer.addEquip(Equipment.getName(itemStatus>>13&0x07)); //Short weapon
        newPlayer.addEquip(Equipment.getName(itemStatus>>10&0x07)); //Long weapon
        newPlayer.addEquip(Equipment.getName(itemStatus>>7&0x07)); //Armor
        newPlayer.setWeaponInHand(Equipment.getName(itemStatus>>4&0x07));
        newPlayer.setPoison(itemStatus&0x0F);
        newPlayer.setBlood(buf.getShort());
        y.player = newPlayer;
    }

    public static byte[] updateYou(User target){
        ByteBuffer buf = ByteBuffer.allocate(16);
        buf.putFloat(target.getPos().getPosX());
        buf.putFloat(target.getPos().getPosY());
        buf.putFloat(target.getPos().getDir());
        short itemStatus = (short)0;
        itemStatus |= (target.getShortWeapon().getID()&0x07) << 13;
        itemStatus |= (target.getLongWeapon().getID()&0x07) << 10;
        itemStatus |= (target.getArmor().getID()&0x07) << 7;
        itemStatus |= (target.getWeaponInHand().getID()&0x07) << 4;
        itemStatus |= target.getPoison() & 0x0F;
        buf.putShort(itemStatus);
        buf.putShort((short)target.getBlood());
        return makePacket((byte)0x05, buf);
    }

    private static void _updateMap(byte[] x, Union y){
        ByteBuffer buf = ByteBuffer.wrap(x);
        Map newMap = new Map();
        byte currentLives = buf.get();
        float posX = buf.getFloat();
        float posY = buf.getFloat();
        float radius = buf.getFloat();
        int currentTime = buf.getInt();
        int timeLeft = buf.getInt();

        Coordinate tmpPos = new Coordinate(posX, posY, 0);
        newMap.setSaveZone(radius, tmpPos);
        newMap.setCurrentTime(currentTime);
        newMap.setNextSaveZoneTime(timeLeft);
        newMap.setCurrentPlayers(currentLives);

        y.map = newMap;
    }

    public static byte[] updateMap(Map target){
        ByteBuffer buf = ByteBuffer.allocate(21);
        buf.put((byte)(target.getCurrentPlayers()&0xFF));
        buf.putFloat(target.getSaveZoneCenterPos().getPosX());
        buf.putFloat(target.getSaveZoneCenterPos().getPosY());
        buf.putFloat(target.getSaveZoneRadius());
        buf.putInt(target.getCurrentTime());
        buf.putInt(target.getNextSaveZoneTime());

        return makePacket((byte)0x06, buf);
    }
    
    private static void _updateDirection(byte[] x, Union y){
        y.newDirection = ByteBuffer.wrap(x).getFloat();
    }

    public static byte[] updateDirection(float newDirection){
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.putFloat(newDirection);
        return makePacket((byte)0x09, buf);
    } 

    private static void _gameOver(byte[] x, Union y){
        y.rank = x[0];
    }
    public static byte[] gameOver(byte rank){
        byte[] buf = new byte[1];
        buf[0] = rank;
        return makePacket((byte)0x10, buf);
    }

    private static String trimAndPadName(String name){
        name = name.trim();
        if(name.length()>=32){
            //Trim to 32 bytes
            name = name.substring(0, 31)+"\0";

        }else{
            //Padding to 32 bytes
            name += new String(new char[32-name.length()]);
        }
        return name;
    }
    private static byte[] makePacket(byte pkID, ByteBuffer _data){
        byte[] data = byteBuffer2byte(_data);
        return makePacket(pkID, data);
    }

    //Encapsulate data segment with pkID and len
    private static byte[] makePacket(byte pkID, byte[] data){
        //Logger.log("Length of data:"+data.length);
        ByteBuffer packetFactory = ByteBuffer.allocate(5+data.length);
        packetFactory.put(pkID);
        packetFactory.putInt((int)(data.length));
        packetFactory.put(data);
        return byteBuffer2byte(packetFactory);
    }
    
    private static byte[] byteBuffer2byte(ByteBuffer x){
        x.flip();
        byte[] rtVal = new byte[x.remaining()];
        x.get(rtVal, 0, rtVal.length);
        return rtVal;
    }
}