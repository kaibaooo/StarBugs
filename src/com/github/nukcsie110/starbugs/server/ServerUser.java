package com.github.nukcsie110.starbugs.server;

import com.github.nukcsie110.starbugs.basic.User;
//import com.github.nukcsie110.starbugs.packet.ClientHandler;
import java.nio.channels.*;

public class ServerUser extends User{
    private SelectionKey selKey;
    public void setSelKey(SelectionKey newSelKey){
        this.selKey = newSelKey;
    }
    public SelectionKey getSelKey(){
        return this.selKey;
    }
}