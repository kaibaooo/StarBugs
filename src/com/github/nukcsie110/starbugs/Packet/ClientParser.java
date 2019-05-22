package com.github.nukcsie110.starbugs.Packet;
import com.github.nukcsie110.starbugs.basic.Item;
import java.util.Arrays;
import java.util.HashMap;
/**
 * @version    0.1
 * @since      0.1
 */
public class ClientParser{
    private static byte type;
    private static byte[] data;
    public void parse(byte[] src){
        type = src[0];
        data = Arrays.copyOfRange(src, 1, src.length-1);
        switch(type){
            case 0x00:
                // statement
                break;
            case 0x01:
                // statement
                break;
            case 0x02:
                // statement
                break;
            case 0x03:
                // statement
                break;
            case 0x04:
                // statement
                break;
            case 0x05:
                // statement
                break;
            case 0x06:
                // statement
                break;
            case 0x07:
                // statement
                break;
            case 0x08:
                // statement
                break;
            case 0x09:
                // statement
                break;
        }
    }
    // login - Server packet parser section
    //=========================================
    /**
     * 0x01    |    state{BYTE}    |    ID {userID}
     * - state
     *      - 0x00: OK
     *      - 0x01: Rejected
     * - ID: Univers Unique ID generated by server for current client
     * @since  0.1
     */
    private static HashMap<String, Object> parseJoinReply(){
        HashMap<String, Object> result = new HashMap<>();
        byte state = data[0];
        byte[] id = new byte[2];
        //TODO:
        //I dont know if this correct
        id[0] = data[2];
        id[1] = data[1];
        result.put("state", state);
        result.put("userID", id);
        return result;
    }
    /**
     * 0x02    |    cnt{BYTE}    |    nameMapping * cnt {userID:name}
     * - cnt: How many players online
     * - nameMapping:
     * - e.g. playerID=0x1234, name=bug, then nameMapping = 
     *      playerID little endian      padding of name     name align with MSB
     *      0x34,0x12                   [0x00] * (32-4)     0x00 'g','u','b'
     * @since  0.1
     */
    private static HashMap<String, Object> parseUpdateNameTable(){
        HashMap<String, Object> result = new HashMap<>();
        // TODO: implement it
        return result;
    }

    // Gaming - Server packet parser section
    //=========================================
    /**
     * Update all items in map
     * 0x03    |    cnt{BYTE}    |    item * cnt {item}
     * @since  0.1
     */
    private static HashMap<String, Object> parseUpdateGlobleItem(){
        HashMap<String, Object> result = new HashMap<>();
        byte cnt = data[0];
        // TODO: implement it
        return result;
    }
     /**
     * Update one player information
     * 0x04    |    playerID{DWORD}    |    coordinate{coordinate}    |    equipment{BYTE}
     * @since  0.1
     */
    private static HashMap<String, Object> parseUpdateSinglePlayer(){
        HashMap<String, Object> result = new HashMap<>();
        // TODO: implement it
        return result;
    }
    /**
     * Update the user current player
     * 0x05    |    coordinate{coordinate}    |    item status {WORD}    |    Blood{DWORD}
     * - item status
     * - [15:12] : Short weapon type
     * - [11:8]  : Long weapon type
     * - [7:4] : Armor type
     * - [3:0] : Amount of posion
     * - Blood : Max 100
     * @since  0.1
     */
    private static HashMap<String, Object> parseUpdateYou(){
        HashMap<String, Object> result = new HashMap<>();
        // TODO: implement it
        return result;
    }
    /**
     * Update game informations
     * 0x06    |    Current Lives{BYTE}    |    saveZone{coordinate}    |    timeLeft{DWORD}
     * - saveZone: Current save zone position. X,Y,dir(Radius)
     * - timeLeft: Time left of next saveZone in seconds
     * @since  0.1
     */
    private static HashMap<String, Object> parseUpdateMap(){
        HashMap<String, Object> result = new HashMap<>();
        // TODO: implement it
        return result;
    }
    /**
     * Game over
     * 0x10    |    rank{BYTE}
     * - Liggle key code: WSADE1234, mouse click left, mouse move
     * @since  0.1
     */
    private static HashMap<String, Object> parseGameOver(){
        HashMap<String, Object> result = new HashMap<>();
        // TODO: implement it
        return result;
    }
}