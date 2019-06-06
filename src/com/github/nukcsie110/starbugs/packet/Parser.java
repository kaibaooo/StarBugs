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
        if(x.length == 0){
            rtVal.pkID = -1;
            log("Invaild packet length");
        }
        rtVal.pkID = x[0];
        switch(x[0]){
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
        int len = Math.min(x.length-1, 32);
        try{
            y.name = new String(Arrays.copyOfRange(x, 1, len), "US-ASCII");
        }catch(UnsupportedEncodingException e){
            log("Error: UnsupportedEncodingException");
        }
    }
    public static byte[] join(String name){
        name = trimAndPadName(name);

        ByteBuffer buf = ByteBuffer.allocate(33);
        buf.put((byte)0x00);
        buf.put(name.getBytes());
        return byteBuffer2byte(buf);
    }

    private static void _joinReply(byte[] x, Union y){
        if(x.length != 4){
            log("Illigle joinReply packet size");
            y.pkID = -1;
            return;
        }
        ByteBuffer buf = ByteBuffer.wrap(x, 1, 3);
        y.state = buf.get();

        //Big-endian player id
        y.playerID = buf.getShort();
    }

    public static byte[] joinReply(byte state, short playerID){
        ByteBuffer buf = ByteBuffer.allocate(4);
        buf.put((byte)0x01);
        buf.put(state);
        //Big-endian player id
        buf.putShort(playerID);

        return byteBuffer2byte(buf);
    }

    private static void _updateNameTable(byte[] x, Union y){
        byte cnt = x[1];
        if(x.length != cnt*34+2){
            log("Illigle updateNameTable packet length");
            y.pkID = -1;
            return;
        }
        ByteBuffer buf = ByteBuffer.wrap(x, 2, x.length-2);
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
        ByteBuffer buf = ByteBuffer.allocate(2+table.size()*34);
        buf.put((byte)0x02);
        buf.put((byte)table.size());
        for(User i:table){
            buf.putShort(i.getID());
            buf.put(
                trimAndPadName(i.getName()).getBytes()
                );
        }
        return byteBuffer2byte(buf);
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