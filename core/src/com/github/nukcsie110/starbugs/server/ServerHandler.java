package com.github.nukcsie110.starbugs.server;

import com.github.nukcsie110.starbugs.packet.Handler;
import com.github.nukcsie110.starbugs.packet.Parser;
import com.github.nukcsie110.starbugs.server.ClientHandler;
import com.github.nukcsie110.starbugs.util.Logger;
import com.github.nukcsie110.starbugs.server.Game;
import com.github.nukcsie110.starbugs.basic.Coordinate;
import java.nio.channels.*;
import java.io.IOException;

public class ServerHandler implements Handler {  
    private Game game;
    public ServerHandler(Game _game){
        game = _game;
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

            //Generate new player and give random position
            ServerUser newPlayer  = new ServerUser(0,"", Coordinate.genRandomPos());
            ClientHandler handler = new ClientHandler(newPlayer, game, clientKey);
            newPlayer.setHandler(handler);

            //Generate new id
            newPlayer.setID((int)(Math.random()*0x10000));

            clientKey.attach(newPlayer.getHandler());  

        } catch (IOException e) {  
            if (clientKey != null)  
                clientKey.cancel();  
            try { client.close(); } catch (IOException ioe) { }  
        }  
    }  
}  