package com.github.nukcsie110.starbugs.basic;

/**
 * @author     kaibao
 * @version    0.1
 * @since      0.1
 */

public class User{
    protected int id; //Unsigned short actually(16-bits long)
    protected String name;

    /**
     * Default constructor
     */
    public User(){
        this(0, "");
    }

    /**
     * Fully functional constructor
     */
    public User(int _id, String _name){
        setID(_id);
        setName(_name);
    }

    /**
     * Check id which is going to set and set it
     */
    public void setID(int x){
        assert x>=0 && x<=0xffff;
        this.id = x;
    }

    /**
     * Get raw user id
     */
    public int getID(){
        return this.id;
    }

    /**
     * Get standard formatted user id
     */
    public String getIDString(){
        return String.format("0x%04X", this.id);
    }

    /**
     * Check length of user name and set it
     */
    public void setName(String x){
        assert x.length()<=32-1;
        this.name = x;
    }

    /**
     * Get copy of raw user name
     */
    public String getName(){
        String rtVal = this.name; //Deep copy
        return rtVal;
    }

    /**
     * Get the standard displing name combining user id and user name
     */
    public String getDisplayName(){
        return String.format("%s#0x%04X", this.name, this.id);
    }
    
}