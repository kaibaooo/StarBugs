package com.github.nukcsie110.starbugs.basic;

public class Map{
    protected float saveZoneRadius = 0;
    protected Coordinate saveZoneCenterPos = new Coordinate(0,0,0);
    protected int currentTime = 0;
    protected int nextSaveZoneTime = 0;
    protected int currentPlayers = 0;

    public int getLeftTime(){
        return this.nextSaveZoneTime - this.currentTime;
    }
    public void setSaveZone(float newRadius, Coordinate newPos){
        if(newRadius>=0){
            this.saveZoneCenterPos = newPos;
            this.saveZoneRadius = newRadius;
        }
    }

    public float getSaveZoneRadius(){
        return this.saveZoneRadius;
    }
    public Coordinate getSaveZoneCenterPos(){
        return this.saveZoneCenterPos;
    }

    public void setCurrentTime(int newTime){
        if(newTime >=0 ){
            this.currentTime = newTime;
        }
    }
    public int getCurrentTime(){
        return this.currentTime;
    }

    public void setNextSaveZoneTime(int newTime){
        if(newTime >= currentTime){
            this.nextSaveZoneTime = newTime;
        }
    }
    public int getNextSaveZoneTime(){
        return this.nextSaveZoneTime;
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
            "currentTime: %d\n"+
            "nextSaveZoneTime: %d\n"+
            "currentPlayers: %d\n",
            this.saveZoneRadius,
            this.saveZoneCenterPos,
            this.currentTime,
            this.nextSaveZoneTime,
            this.currentPlayers
        );
    }
}