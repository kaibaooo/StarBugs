package com.github.nukcsie110.starbugs.packet;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.ArrayList;
import com.github.nukcsie110.starbugs.basic.User;
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
            log("Invaild packet length");
        }
        rtVal.pkID = x[0];
        x = Arrays.copyOfRange(x, 5, x.length);
        switch(rtVal.pkID){
            case 0x00: _join(x, rtVal); break;
            case 0x01: _joinReply(x, rtVal); break;
            case 0x02: _updateNameTable(x, rtVal); break;
            /*case 0x03: _updateGlobalItem(x, rtVal); break;
            case 0x04: _updateSinglePlayer(x, rtVal); break;
            case 0x05: _updateYou(x, rtVal); break;
            case 0x06: _updateMap(x, rtVal); break;
            case 0x07: _keyDown(x, rtVal); break;
            case 0x08: _keyUp(x, rtVal); break;
            case 0x09: _updateDirection(x, rtVal); break;
            case 0x10: _gameOver(x, rtVal); break;
            */
            default:
                log("Invaild packet ID: "+ x[0]);
                rtVal.pkID = -1;
        }
        return rtVal;
    }

    private static void _join(byte[] x, Union y){
        int len = Math.min(x.length, 32);
        try{
            y.name = new String(x, "US-ASCII");
        }catch(UnsupportedEncodingException e){
            log("Error: UnsupportedEncodingException");
        }
    }
    public static byte[] join(String name){
        name = trimAndPadName(name);
        return makePacket((byte)0x00, name.getBytes());
    }

    private static void _joinReply(byte[] x, Union y){
        if(x.length != 3){
            log("Illigle joinReply packet size");
            y.pkID = -1;
            return;
        }
        ByteBuffer buf = ByteBuffer.wrap(x);
        y.state = buf.get();

        //Big-endian player id
        y.playerID = buf.getShort();
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
        if(x.length != 1+cnt*34){
            log("Illigle updateNameTable packet length");
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
                log("Error: UnsupportedEncodingException");
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
        log("Length of data:"+data.length);
        ByteBuffer packetFactory = ByteBuffer.allocate(5+data.length);
        packetFactory.put(pkID);
        packetFactory.putInt((int)(data.length));
        packetFactory.put(data);
        return byteBuffer2byte(packetFactory);
    }
    
    private static byte[] byteBuffer2byte(ByteBuffer x){
        x.rewind();
        byte[] rtVal = new byte[x.remaining()];
        x.get(rtVal, 0, rtVal.length);
        return rtVal;
    }
    private static void log(Object x){
        System.out.println(x);
    }
}