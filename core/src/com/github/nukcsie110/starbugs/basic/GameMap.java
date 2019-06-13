package com.github.nukcsie110.starbugs.basic;

public class GameMap{
    public static final int TICK_PER_SECOND = 50;
    protected double saveZoneRadius = 0;
    protected Coordinate saveZoneCenterPos = new Coordinate(0,0,0);
    protected Long currentTick = 0l;
    protected Long nextSaveZoneTick = 0l;
    protected int currentPlayers = 0;

    public long getLeftTick(){
        return this.nextSaveZoneTick - this.currentTick;
    }
    public float getLeftTime(){
        return (this.nextSaveZoneTick - this.currentTick)/this.TICK_PER_SECOND;
    }
    public void setSaveZone(double newRadius, Coordinate newPos){
        if(newRadius>=0){
            this.saveZoneCenterPos = newPos;
            this.saveZoneRadius = newRadius;
        }
    }

    public double getSaveZoneRadius(){
        return this.saveZoneRadius;
    }
    public void setSaveZoneRadius(double x){
        this.saveZoneRadius = x;
    }
    public Coordinate getSaveZonePos(){
        return this.saveZoneCenterPos;
    }

    public void setCurrentTick(long newTick){
        synchronized(currentTick){
            if(newTick >=0 ){
                this.currentTick = newTick;
            }
        }
    }
    public void incTick(){
        synchronized(currentTick){
            this.currentTick = this.currentTick+1;
        }
    }
    public long getCurrentTick(){
        synchronized(currentTick){
            return this.currentTick;
        }
    }

    public void setNextSaveZoneTick(long newTick){
        if(newTick >= currentTick){
            this.nextSaveZoneTick = newTick;
        }
    }
    public long getNextSaveZoneTick(){
        return this.nextSaveZoneTick;
    }

    public void setCurrentPlayers(int newPlayers){
        if(newPlayers>0){
            this.currentPlayers = newPlayers;
        }
    }
    public int getCurrentPlayers(){
        return this.currentPlayers;
    }
    public String toString(){
        return String.format(
            "saveZoneRadius: %f\n"+
            "saveZoneCenterPos: %s\n"+
            "currentTick: %d\n"+
            "nextSaveZoneTick: %d\n"+
            "currentPlayers: %d\n",
            this.saveZoneRadius,
            this.saveZoneCenterPos,
            this.currentTick,
            this.nextSaveZoneTick,
            this.currentPlayers
        );
    }
}