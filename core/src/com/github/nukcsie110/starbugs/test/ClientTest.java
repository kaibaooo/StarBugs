package com.github.nukcsie110.starbugs.test;

import com.github.nukcsie110.starbugs.client.Client;
import com.github.nukcsie110.starbugs.packet.Union;
import com.github.nukcsie110.starbugs.basic.User;
import com.github.nukcsie110.starbugs.util.Logger;
import java.util.Scanner;

public class ClientTest{
    public static void main(String argv[]){
        Scanner cin = new Scanner(System.in);
        Client client = new Client();
        client.start();
        System.out.print("Your player name:");
        String playerName = cin.next();
       
        while(!client.isReady());
        client.join(playerName);
        client.keyDown((byte)0x87);
        client.updateDirection(0.5f);
        boolean gameOver = false;
        while(!gameOver){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {}
            if(client.isReadable()){
                Union ops = client.read();
                switch(ops.pkID){
                    case 0x01:
                        Logger.log("Recived joinReply");
                        if(ops.state==0){
                            Logger.log("My ID is: "+ops.player.getIDString());
                        }else{
                            Logger.log("Failed to join. Abort.");
                            gameOver = true;
                        }
                    break;
                    case 0x02:
                        Logger.log("Recived updateNameTable");
                        Logger.log("Max player:"+ops.maxPlayer);
                        for(User i:ops.nameTable){
                            Logger.log(i.getDisplayName());
                        }
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