package com.github.nukcsie110.starbugs.basic;

public enum ItemID{
    NONE(0), SWORD(1), BOW(2),
    ARMOR_LV1(3), ARMOR_LV2(4), ARMOR_LV3(5),
    POISON(6), ARROW(7);
    private final int val;
    private ItemID(int x){
        this.val = x;
    }
    public byte getID(){
        return (byte)(this.val);
    }
    public static ItemID getName(int _id){
        ItemID[] As = ItemID.values();
        for(int i = 0; i < As.length; i++){
            if(As[i].getID()==_id)
            return As[i];
        }
        return ItemID.NONE;
    }

}