package com.makent.trioangle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makent.trioangle.host.RoomsBeds.BedTypesList;
import com.makent.trioangle.model.homeModels.BedRoomBed;

import java.util.ArrayList;

public class BedTypeList {

    @SerializedName("common_beds")
    @Expose
    private ArrayList<BedTypesList> commonBeds;

    @SerializedName("bed_room_beds")
    @Expose
    private ArrayList<ArrayList<BedTypesList>> bedRoomBeds = null;


    public ArrayList<ArrayList<BedTypesList>> getBedRoomBeds() {
        return bedRoomBeds;
    }

    public void setBedRoomBeds(ArrayList<ArrayList<BedTypesList>> bedRoomBeds) {
        this.bedRoomBeds = bedRoomBeds;
    }

    public ArrayList<BedTypesList> getCommonBeds() {
        return commonBeds;
    }

    public void setCommonBeds(ArrayList<BedTypesList> commonBeds) {
        this.commonBeds = commonBeds;
    }
}


