package com.github.nukcsie110.starbugs.test;
import com.github.nukcsie110.starbugs.basic.User;

public class basic_User{
    public static void main(String[] argv){
        User a = new User();
        println(a.getID());
        println(a.getIDString());
        println(a.getName());
        println(a.getDisplayName());
        a.setID(0xbeef);
        a.setName("test");
        println(a.getID());
        println(a.getIDString());
        println(a.getName());
        println(a.getDisplayName());
        a.setID(0x100000);
        a.setName("dadasdasddasdasdsadsadsadasdsadsadasdasddsadasdsadasdasdsadsadsadsadasd");
        println(a.getID());
        println(a.getIDString());
        println(a.getName());
        println(a.getDisplayName());
    }
    private static void println(Object x){
        System.out.println(x);
    }
}