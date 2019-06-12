package com.github.nukcsie110.starbugs.server;

import com.github.nukcsie110.starbugs.packet.Handler;
import com.github.nukcsie110.starbugs.server.ClientHandler;
import com.github.nukcsie110.starbugs.util.Logger;
import java.nio.channels.*;
import java.io.IOException;
import java.util.ArrayList;

public class ServerHandler implements Handler {  
    private ArrayList<ServerUser> playerQueue;
    private int onlineCnt;
    public ServerHandler(){
        playerQueue = new ArrayList<>();
        onlineCnt = 0;
    }
    public void execute(Selector selector, SelectionKey key) {  
        ServerSocketChannel server = (ServerSocketChannel) key.channel();  
        SocketChannel client = null;  
        try {  
            client = server.accept();  
            Logger.log("Accepted connection from " + client.getRemoteAddress());  
        } catch (IOException e) {  
            e.printStackTrace();  
            return;  
        }  
          
        SelectionKey clientKey = null;  
        try {  
            client.configureBlocking(false);  
            clientKey = client.register(selector, SelectionKey.OP_READ);
            ServerUser newPlayer  = new ServerUser();
            newPlayer.setSelKey(clientKey);
            newPlayer.setID((int)(Math.random()*0x10000));
            onlineCnt+=1;
            Logger.log("Connections: "+onlineCnt);
            clientKey.attach(new ClientHandler(newPlayer));
            //clientKey.attach(new ClientHandler());  
        } catch (IOException e) {  
            if (clientKey != null)  
                clientKey.cancel();  
            try { client.close(); } catch (IOException ioe) { }  
        }  
    }  
}  