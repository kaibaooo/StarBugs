package com.github.nukcsie110.starbugs.test;
import com.github.nukcsie110.starbugs.basic.User;
import com.github.nukcsie110.starbugs.basic.Equipment;
import com.github.nukcsie110.starbugs.basic.EquipmentSlot;
import com.github.nukcsie110.starbugs.util.Logger;

public class basic_User{
    public static void main(String[] argv){
        User a = new User();
        Logger.log("===============Testing name and id===================");
        Logger.log(a);
        a.setID(0xbeef);
        a.setName("test");
        Logger.log(a);
        a.setID(0x100000);
        a.setName("dadasdasddasdasdsadsadsadasdsadsadasdasddsadasdsadasdasdsadsadsadsadasd");
        Logger.log(a);
        Logger.log("=============Testing weapon and armor=================");
        a = new User();
        Logger.log(a);

        Equipment rt = a.assEquip(Equipment.LONG_GUN);
        Logger.log(a);
        Logger.log("\tReturn value: "+rt);
        
        rt = a.assEquip(Equipment.LONG_BOW);
        Logger.log(a);
        Logger.log("\tReturn value: "+rt);
        
        rt = a.assEquip(Equipment.SHORT_SWORD);
        Logger.log(a);
        Logger.log("\tReturn value: "+rt);
        
        rt = a.assEquip(Equipment.ARMOR_LV1);
        Logger.log(a);
        Logger.log("\tReturn value: "+rt);
        
        rt = a.assEquip(Equipment.LONG_GUN);
        Logger.log(a);
        Logger.log("\tReturn value: "+rt);
        
        rt = a.assEquip(Equipment.NONE);
        Logger.log(a);
        Logger.log("\tReturn value: "+rt);

        rt = a.clearSlot(EquipmentSlot.LONG);
        Logger.log(a);
        Logger.log("\tReturn value: "+rt);

        rt = a.clearSlot(EquipmentSlot.SHORT);
        Logger.log(a);
        Logger.log("\tReturn value: "+rt);

        rt = a.clearSlot(EquipmentSlot.ARMOR);
        Logger.log(a);
        Logger.log("\tReturn value: "+rt);

        Logger.log("=============Testing pos=================");
        Logger.log(a);
        a.getPos().moveTo(100, 100);
        Logger.log(a);

    }
}