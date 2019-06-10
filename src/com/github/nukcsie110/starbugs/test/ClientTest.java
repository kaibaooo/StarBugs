package com.github.nukcsie110.starbugs.test;

import com.github.nukcsie110.starbugs.client.Client;
import com.github.nukcsie110.starbugs.packet.Union;
import com.github.nukcsie110.starbugs.util.Logger;

public class ClientTest{
    public static void main(String argv[]){
        Client client = new Client();
        client.start();
        while(!client.isReady());
        client.join("ifTNT");
        //client.keyDown((byte)0x87);
        //client.updateDirection(0.5f);
        boolean gameOver = false;
        while(!gameOver){
            if(client.isReadable()){
                Union ops = client.read();
                switch(ops.pkID){
                    case 0x01:
                        Logger.log("Recived joinReply");
                        Logger.log("My ID is: "+ops.player.getIDString());
                    break;
                    case 0x10:
                        Logger.log("Game over");
                        client.close();
                        gameOver = true;
                    default:
                }
            }
        }
    }
}