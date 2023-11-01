package com.makent.trioangle.helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bowshulsheikrahaman on 12/01/18.
 */

public class RoomIdPrice {
    public List<String> RoomId = new ArrayList<String>();
    public List<String> RoomPrice = new ArrayList<String>();
    public void addRoomId(String id,String price)
    {
       if (RoomId.contains(id)){
           int index=RoomId.indexOf(id);
           RoomId.set(index,id);
           RoomPrice.set(index,price);
           System.out.println("Update Room "+ RoomId+" "+RoomPrice+" " +RoomId.size());
       }else {
           RoomId.add(id);
           RoomPrice.add(price);
           System.out.println("Add Room "+ RoomId+" "+RoomPrice+" " +RoomId.size());
       }

    }
    public void removeRoomId(String id)
    {
        if (RoomId.contains(id)) {
            int index=RoomId.indexOf(id);
            RoomPrice.remove(index);
            RoomId.remove(id);
            System.out.println("Remove Room "+ RoomId+" "+RoomPrice+" " +RoomId.size());
        }
    }
    public String getRoomId()
    {
        System.out.println("Get Room Id "+ RoomId+" "+RoomPrice+" " +RoomId.size());
        if(RoomId.size()>0)
            return RoomId.get(RoomId.size() - 1);
        else
            return null;
    }
    public String getRoomPrice()
    {
        System.out.println("Get Room Price "+ RoomId+" "+RoomPrice+" " +RoomId.size());
        if(RoomPrice.size()>0)
            return RoomPrice.get(RoomPrice.size()-1);
        else
            return null;
    }
}
