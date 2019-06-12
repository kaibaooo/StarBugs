package com.shopping.Base;

public class GameJudger {
    private int mapMinX = 800;
    private int mapMinY = 435;
    private int mapMaxX = 10880;
    private int mapMaxY = 10545;

    public boolean judgeUserMoveIllegal(float currentX, float currentY, float offsetX, float offsetY){
        return true;
    }
    public boolean judgeUserMoveIllegal(float targetX, float targetY){
        // Map size check
        if(targetX < mapMinX || targetY < mapMinY || targetX > mapMaxX || targetY > mapMaxY){
            return false;
        }
        return true;
    }
}
