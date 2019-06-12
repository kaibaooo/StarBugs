package com.github.nukcsie110.starbugs.basic;

public enum EquipmentSlot{
    NONE(0), LONG(2), SHORT(1), ARMOR(3);
    private int val;
    EquipmentSlot(int x){
        this.val = x;
    }
    public int getID(){
        return this.val;
    }
    
    public static EquipmentSlot getName(int _id){
        EquipmentSlot[] As = EquipmentSlot.values();
        for(int i = 0; i < As.length; i++){
            if(As[i].getID()==_id)
            return As[i];
        }
        return EquipmentSlot.NONE;
    }
}