package com.github.nukcsie110.starbugs.packet;

import java.nio.channels.*;

public interface Handler {  
    void execute(Selector selector, SelectionKey key);  
} 