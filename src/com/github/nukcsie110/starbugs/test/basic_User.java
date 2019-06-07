package com.github.nukcsie110.starbugs.test;
import com.github.nukcsie110.starbugs.basic.User;
import com.github.nukcsie110.starbugs.basic.Equipment;
import com.github.nukcsie110.starbugs.basic.EquipmentSlot;

public class basic_User{
    public static void main(String[] argv){
        User a = new User();
        println("===============Testing name and id===================");
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
        println("=============Testing weapon and armor=================");
        a = new User();
        println("Long: " + a.getLongWeapon());
        println("Short: " + a.getShortWeapon());
        println("Armor: " + a.getArmor());
        println("");

        Equipment rt = a.addWeapon(Equipment.LONG_GUN);
        println("rt: " + rt);
        println("Long: " + a.getLongWeapon());
        println("Short: " + a.getShortWeapon());
        println("Armor: " + a.getArmor());
        println("");
        
        rt = a.addWeapon(Equipment.LONG_BOW);
        println("rt: " + rt);
        println("Long: " + a.getLongWeapon());
        println("Short: " + a.getShortWeapon());
        println("Armor: " + a.getArmor());
        println("");
        
        rt = a.addWeapon(Equipment.SHORT_SWORD);
        println("rt: " + rt);
        println("Long: " + a.getLongWeapon());
        println("Short: " + a.getShortWeapon());
        println("Armor: " + a.getArmor());
        println("");
        
        rt = a.addWeapon(Equipment.ARMOR_LV1);
        println("rt: " + rt);
        println("Long: " + a.getLongWeapon());
        println("Short: " + a.getShortWeapon());
        println("Armor: " + a.getArmor());
        println("");
        
        rt = a.addWeapon(Equipment.LONG_GUN);
        println("rt: " + rt);
        println("Long: " + a.getLongWeapon());
        println("Short: " + a.getShortWeapon());
        println("Armor: " + a.getArmor());
        println("");
        
        rt = a.addWeapon(Equipment.NONE);
        println("rt: " + rt);
        println("Long: " + a.getLongWeapon());
        println("Short: " + a.getShortWeapon());
        println("Armor: " + a.getArmor());
        println("");

        rt = a.clearSlot(EquipmentSlot.LONG);
        println("rt: " + rt);
        println("Long: " + a.getLongWeapon());
        println("Short: " + a.getShortWeapon());
        println("Armor: " + a.getArmor());
        println("");

        rt = a.clearSlot(EquipmentSlot.SHORT);
        println("rt: " + rt);
        println("Long: " + a.getLongWeapon());
        println("Short: " + a.getShortWeapon());
        println("Armor: " + a.getArmor());
        println("");

        rt = a.clearSlot(EquipmentSlot.ARMOR);
        println("rt: " + rt);
        println("Long: " + a.getLongWeapon());
        println("Short: " + a.getShortWeapon());
        println("Armor: " + a.getArmor());
        println("");

        println("=============Testing pos=================");
        println("X: "+a.getPos().getPosX()+",Y: "+a.getPos().getPosY());
        a.getPos().moveTo(100, 100);
        println("X: "+a.getPos().getPosX()+",Y: "+a.getPos().getPosY());

    }
    private static void println(Object x){
        System.out.println(x);
    }
}