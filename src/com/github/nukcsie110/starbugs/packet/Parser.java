package com.github.nukcsie110.starbugs.packet;

import java.nio.ByteBuffer;

public class Parser{
    public static Union toUnion(ByteBuffer x){
        byte[] bytes = new byte[x.remaining()];
        x.get(bytes, 0, bytes.length);
        return toUnion(bytes);
    }

    public static Union toUnion(byte[] x){
        Union rtVal;
        if(x.length == 0){
            rtVal.pkID = -1;
            log("Invaild packet length");
        }
        rtVal.pkID = x[0];
        switch(x[0]){
            case 0x00: _join(rtVal); break;
            case 0x01: _joinReply(rtVal); break;
            case 0x02: _updateNameTable(rtVal); break;
            case 0x03: _updateGlobalItem(rtVal); break;
            case 0x04: _updateSinglePlayer(rtVal); break;
            case 0x05: _updateYou(rtVal); break;
            case 0x06: _updateMap(rtVal); break;
            case 0x07: _keyDown(rtVal); break;
            case 0x08: _keyUp(rtVal); break;
            case 0x09: _updateDirection(rtVal); break;
            case 0x10: _gameOver(rtVal); break;
            default:
                log("Invaild packet ID: "+ x[0]);
                rtVal.pkID = -1;
        }
        return rtVal;
    }
    private static void log(Object x){
        System.out.println(x);
    }
}