package com.github.nukcsie110.starbugs.packet;

import java.util.ArrayList;
import com.github.nukcsie110.starbugs.basic.User;

/**
 * @version 0.1
 * @since 0.1
 * Union data type of all possiblly field
 */
public class union{
    private byte pkID;
    private byte state;
    private short userID;
    private byte cnt;
    private ArrayList<User> nameTable;

}