package com.github.nukcsie110.starbugs.basic;

/**
 * @author     kaibao
 * @version    0.1
 * @since      0.1
 */

public class User{
    private int id; //Unsigned short actually(16-bits long)
    private String name;

    public User(){
        this(0, "");
    }
    public User(int _id, String _name){
        setID(_id);
        setName(_name);
    }

    public void setID(int x){
        assert x>=0 && x<=0xffff;
        this.id = x;
    }
    public int getID(){
        return this.id;
    }
    public String getIDString(){
        return String.format("0x%04x", this.id);
    }
    public void setName(String x){
        assert x.length()<=32-1;
        this.name = x;
    }
    public String getName(){
        String rtVal = this.name; //Deep copy
        return rtVal;
    }

    
}