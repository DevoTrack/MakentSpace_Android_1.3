package com.makent.trioangle.host.RoomsBeds;



import java.util.ArrayList;
import java.util.List;
public class AdditionalBedModel {


    private String id;

    private String bedName;

    private String bedTypeName;

    private boolean bedTypeShow;

    private List<BedTypesList> bedTypesLists;

    private List<BedTypesList> bedTypesListsTemp;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBedName() {
        return bedName;
    }

    public void setBedName(String bedName) {
        this.bedName = bedName;
    }

    public String getBedTypeName() {
        return bedTypeName;
    }

    public void setBedTypeName(String bedTypeName) {
        this.bedTypeName = bedTypeName;
    }

    public boolean isBedTypeShow() {
        return bedTypeShow;
    }

    public void setBedTypeShow(boolean bedTypeShow) {
        this.bedTypeShow = bedTypeShow;
    }

    public List<BedTypesList> getBedTypesLists() {
        return bedTypesLists;
    }

    public void setBedTypesLists(List<BedTypesList> bedTypesLists) {
        this.bedTypesLists = bedTypesLists;
    }

    public List<BedTypesList> getBedTypesListsTemp() {
        return bedTypesListsTemp;
    }

    public void setBedTypesListsTemp(List<BedTypesList> bedTypesListsTemp) {
        this.bedTypesListsTemp = bedTypesListsTemp;
    }
}
