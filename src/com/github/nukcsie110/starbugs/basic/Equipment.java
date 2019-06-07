package com.github.nukcsie110.starbugs.basic;

public enum Equipment{
    NONE(0),
    LONG_GUN(1),
    LONG_BOW(2),
    SHORT_SWORD(3),
    ARMOR_LV1(4);

    private final int val;
    private Equipment(int x){
        this.val = x;
    }
    public int getID(){
        return this.val;
    }
    public static Equipment getName(int _id){
            Equipment[] As = Equipment.values();
            for(int i = 0; i < As.length; i++){
                if(As[i].getID()==_id)
                    return As[i];
            }
            return Equipment.NONE;
    }
}