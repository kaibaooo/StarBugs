package com.github.nukcsie110.starbugs.basic;

import com.github.nukcsie110.starbugs.basic.Equipment;
import com.github.nukcsie110.starbugs.basic.EquipmentSlot;

/**
 * @author     kaibao
 * @version    0.1
 * @since      0.1
 */

public class User{
    protected int id; //Unsigned short actually(16-bits long)
    protected String name;
    protected Equipment shortWeapon;
    protected Equipment longWeapon;
    protected EquipmentSlot weaponInHand;
    protected Equipment armor;
    protected Coordinate pos;
    protected int blood;
    protected int cntPoison;
    public static final int MAX_BLOOD = 100;
    protected static final float velocityX = 15;
    protected static final float velocityY = 15;


    /**
     * Default constructor
     */
    public User(){
        this(0, "bug", new Coordinate(0,0,0));
    }

    /**
     * Fully functional constructor
     */
    public User(int _id, String _name, Coordinate _pos){
        setID(_id);
        setName(_name);
        
        this.shortWeapon = Equipment.NONE;
        this.longWeapon = Equipment.NONE;
        this.weaponInHand = EquipmentSlot.NONE;
        this.armor = Equipment.NONE;
        
        this.blood = this.MAX_BLOOD;
        this.cntPoison = 0;
        
        this.pos = _pos;
    }

    /**
     * Check id which is going to set and set it
     */
    public void setID(int x){
        assert x>=0 && x<=0xffff;
        this.id = x&0xffff;
    }

    /**
     * Get raw user id
     */
    public short getID(){
        return (short)(this.id&0xFFFF);
    }

    /**
     * Get standard formatted user id
     */
    public String getIDString(){
        return String.format("0x%04X", this.id);
    }

    /**
     * Tool for standard formatted user id
     */
    public static String getIDString(short x){
        return String.format("0x%04X", x);
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
     * Get the standard disply name combining user id and user name
     * e.g. ifTNT#0xBEEF
     */
    public String getDisplayName(){
        return String.format("%s#0x%04X", this.name, this.id);
    }

    /**
     * @param newEquip the equipment you want to add(Either short, long or armor)
     * @return Old equipment in that slot
     */
    public Equipment addEquip(Equipment newEquip){
        Equipment rtVal = Equipment.NONE;
        switch(newEquip){
            case LONG_GUN:
            case LONG_BOW:
                rtVal = this.longWeapon;
                this.longWeapon = newEquip;
            break;
            case SHORT_SWORD:
                rtVal = this.shortWeapon;
                this.shortWeapon = newEquip;
            break;
            case ARMOR_LV1:
            case ARMOR_LV2:
            case ARMOR_LV3:
                rtVal = this.armor;
                this.armor = newEquip;
            break;
        }
        return rtVal;
    }

    public Equipment clearSlot(EquipmentSlot target){
        Equipment rtVal = Equipment.NONE;
        switch(target){
            case LONG:
                rtVal = this.longWeapon;
                this.longWeapon = Equipment.NONE;
            break;
            case SHORT:
                rtVal = this.shortWeapon;
                this.shortWeapon = Equipment.NONE;
            break;
            case ARMOR:
                rtVal = this.armor;
                this.armor = Equipment.NONE;
            break;
        }
        return rtVal;
    }

    public Equipment getLongWeapon(){
        return this.longWeapon;
    }
    public Equipment getShortWeapon(){
        return this.shortWeapon;
    }
    public Equipment getArmor(){
        return this.armor;
    }

    /**
     * Check wheather user has such weapon and set weapon in hand
     */
    public void setWeaponInHand(EquipmentSlot weapon){
        this.weaponInHand = weapon;
        /*if(this.longWeapon==weapon || this.shortWeapon==weapon || weapon==Equipment.NONE){
            this.weaponInHand = weapon;
        }*/
    }
    public EquipmentSlot getWeaponInHand(){
        return this.weaponInHand;
    }

    public Equipment getWeapon(){
        if(this.weaponInHand==EquipmentSlot.NONE){
            return Equipment.NONE;
        }else if(this.weaponInHand==EquipmentSlot.SHORT){
            return this.shortWeapon;
        }else{
            return this.longWeapon;
        }
    }

    /*
      Notice: setPos is not needed
      if you want to set position or direction, 
      just use User.getPos().moveTo()
    */
    public void moveTop(){
        this.pos.moveOffset(0, velocityY);
    }
    public void moveDown(){
        this.pos.moveOffset(0, -velocityY);
    }
    public void moveRight(){
        this.pos.moveOffset(velocityX, 0);
    }
    public void moveLeft(){
        this.pos.moveOffset(-velocityX, 0);
    }

    public Coordinate getPos(){
        //Return the reference of position
        return this.pos;
    }
    public void setBlood(int x){
        if(x<=this.MAX_BLOOD){
            this.blood = x;
        }
    }
    public int getBlood(){
        return this.blood;
    }
    public void setPoison(int x){
        if(x>=0 && x<=3){
            this.cntPoison = x;
        }
    }
    public int getPoison(){
        return this.cntPoison;
    }

    public void usePoison(){
        if(cntPoison>0){
            cntPoison -= 1;
            blood += (MAX_BLOOD-blood)/2;
        }
    }

    public String toString(){
        return String.format(
            "%s:\n"+
            "\tLong weapon: %s\n"+
            "\tShort weapon: %s\n"+
            "\tWeapon in hand: %s\n"+
            "\tArmor: %s\n"+
            "\tBlood: %d\n"+
            "\tCount of poison: %d\n"+
            "\tPosition: %s\n",
            this.getDisplayName(),
            longWeapon, shortWeapon, weaponInHand,
            armor, blood, cntPoison, pos
        );
    }
}