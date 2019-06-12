package com.shopping.Base;

public class GameJudger {
    private int mapMinX = 780;
    private int mapMinY = 425;
    private int mapMaxX = 10880;
    private int mapMaxY = 10545;

    public boolean judgeUserMoveIllegal(float currentX, float currentY, float offsetX, float offsetY){
        return true;
    }
    public boolean judgeUserMoveIllegal(float targetX, float targetY){
        // Map size check
        if(targetX < mapMinX-50 || targetY < mapMinY-50 || targetX > mapMaxX + 50 || targetY > mapMaxY + 50){
            return false;
        }
        return true;
    }
}
