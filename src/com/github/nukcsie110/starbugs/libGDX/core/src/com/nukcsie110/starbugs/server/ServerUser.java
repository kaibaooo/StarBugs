package com.github.nukcsie110.starbugs.server;

import com.github.nukcsie110.starbugs.basic.User;
//import com.github.nukcsie110.starbugs.packet.ClientHandler;
import java.nio.channels.*;

public class ServerUser extends User{
    private ClientHandler handler;
    public void setHandler(ClientHandler newHandler){
        this.handler = newHandler;
    }
    public ClientHandler getHandler(){
        return this.handler;
    }
}