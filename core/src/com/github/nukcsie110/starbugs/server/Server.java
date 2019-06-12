package com.github.nukcsie110.starbugs.server;

import com.github.nukcsie110.starbugs.server.Game;
import com.github.nukcsie110.starbugs.packet.Handler;
import com.github.nukcsie110.starbugs.util.Logger;
import java.nio.channels.*;
import java.io.IOException;
import java.util.Iterator;
import java.net.ServerSocket;
import java.net.InetSocketAddress;

public class Server{
    private final static int LISTEN_PORT = 8787;
    private final static int SELECTOR_TIMEOUT = 1;
    private static Selector selector;
    private static Game game;

    public static void main(String[] args) throws IOException {  
  
        selector = Selector.open();  
        game = new Game();
        initServer(selector);  
  
        while (true) {  
            selector.select(SELECTOR_TIMEOUT);  
    
            for (Iterator<SelectionKey> itor = selector.selectedKeys().iterator(); itor.hasNext();) {  
                SelectionKey key = (SelectionKey) itor.next();  
                itor.remove();  
                Handler handler = (Handler) key.attachment();  
                handler.execute(selector, key);  
            }  
        }  
    }  
  
    private static void initServer(Selector selector) throws IOException,ClosedChannelException {  
        Logger.log("Listening for connection on port " + LISTEN_PORT);  
        ServerSocketChannel serverChannel = ServerSocketChannel.open();  
        ServerSocket ss = serverChannel.socket();  
        ss.bind(new InetSocketAddress(LISTEN_PORT));  
        serverChannel.configureBlocking(false);  
        SelectionKey serverKey = serverChannel.register(selector, SelectionKey.OP_ACCEPT);  
        serverKey.attach(new ServerHandler(game));  
    }
}