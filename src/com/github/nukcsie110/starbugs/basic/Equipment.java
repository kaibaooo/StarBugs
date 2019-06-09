package com.github.nukcsie110.starbugs.basic;

public enum Equipment{
    NONE(0),
    LONG_GUN(1),
    LONG_BOW(2),
    SHORT_SWORD(3),
    ARMOR_LV1(4),
    ARMOR_LV2(5),
    ARMOR_LV3(6);

    private final int val;
    private Equipment(int x){
        this.val = x;
    }
    public int getID(){
        return this.val;
    }
    public static Equipment getName(int _id){
        /*Equipment[] As = Equipment.values();
        for(int i = 0; i < As.length; i++){
            if(As[i].getID()==_id)
            return As[i];
        }
        return Equipment.NONE;
        */

        //Bad demostration, but good performance
        switch(_id){
            case 0:  return Equipment.NONE;
            case 1:  return Equipment.LONG_GUN;
            case 2:  return Equipment.LONG_BOW;
            case 3:  return Equipment.SHORT_SWORD;
            case 4:  return Equipment.ARMOR_LV1;
            case 5:  return Equipment.ARMOR_LV2;
            case 6:  return Equipment.ARMOR_LV3;
            default: return Equipment.NONE;
        }
    }
}