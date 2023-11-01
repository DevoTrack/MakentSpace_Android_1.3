package com.makent.trioangle.host.optionaldetails.amenities_nested_recycleview;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  host/optionaldetails/amenties_nested_recycleview
 * @category    AmenitiesParentChild
 * @author      Trioangle Product Team
 * @version     1.1
 */

import java.util.ArrayList;

/**
 * Created by cliqers on 23/1/2016.
 */
public class AmenitiesParentChild {
    String header;
    ArrayList<AmenitiesChild> child;

    public AmenitiesParentChild() {
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public ArrayList<AmenitiesChild> getChild() {
        return child;
    }

    public void setChild(ArrayList<AmenitiesChild> child) {
        this.child = child;
    }
}
