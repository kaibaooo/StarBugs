package com.github.nukcsie110.starbugs.test;
import com.github.nukcsie110.starbugs.basic.User;
import com.github.nukcsie110.starbugs.basic.Equipment;
import com.github.nukcsie110.starbugs.basic.EquipmentSlot;

public class basic_User{
    public static void main(String[] argv){
        User a = new User();
        println("===============Testing name and id===================");
        println(a);
        a.setID(0xbeef);
        a.setName("test");
        println(a);
        a.setID(0x100000);
        a.setName("dadasdasddasdasdsadsadsadasdsadsadasdasddsadasdsadasdasdsadsadsadsadasd");
        println(a);
        println("=============Testing weapon and armor=================");
        a = new User();
        println(a);

        Equipment rt = a.assEquip(Equipment.LONG_GUN);
        println(a);
        println("\tReturn value: "+rt);
        
        rt = a.assEquip(Equipment.LONG_BOW);
        println(a);
        println("\tReturn value: "+rt);
        
        rt = a.assEquip(Equipment.SHORT_SWORD);
        println(a);
        println("\tReturn value: "+rt);
        
        rt = a.assEquip(Equipment.ARMOR_LV1);
        println(a);
        println("\tReturn value: "+rt);
        
        rt = a.assEquip(Equipment.LONG_GUN);
        println(a);
        println("\tReturn value: "+rt);
        
        rt = a.assEquip(Equipment.NONE);
        println(a);
        println("\tReturn value: "+rt);

        rt = a.clearSlot(EquipmentSlot.LONG);
        println(a);
        println("\tReturn value: "+rt);

        rt = a.clearSlot(EquipmentSlot.SHORT);
        println(a);
        println("\tReturn value: "+rt);

        rt = a.clearSlot(EquipmentSlot.ARMOR);
        println(a);
        println("\tReturn value: "+rt);

        println("=============Testing pos=================");
        println(a);
        a.getPos().moveTo(100, 100);
        println(a);

    }
    private static void println(Object x){
        System.out.println(x);
    }
}