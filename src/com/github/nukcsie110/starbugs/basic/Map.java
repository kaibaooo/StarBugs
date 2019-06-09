pcakage com.github.nukcsie110.starbugs.basic;

public class Map{
    protected float saveZoneRadius;
    protected Coordinate saveZoneCenterPos = new Coordinate(0,0,0);
    protected float currentTime;
    protected float nextSaveZoneTime;
    protected int currentPlayer;
    public Map(){
        
    }
    public float getLeftTime(){
        return this.nextSaveZoneTime - this.currentTime;
    }
}