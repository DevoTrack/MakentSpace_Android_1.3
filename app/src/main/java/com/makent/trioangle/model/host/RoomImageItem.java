package com.makent.trioangle.model.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  model/host
 * @category    RoomImageItem
 * @author      Trioangle Product Team
 * @version     1.1
 */

import java.io.Serializable;

/**
 * Created by test on 2/27/16.
 */
public class RoomImageItem implements Serializable {
    String image;
    String id;
    String room_id;

    public RoomImageItem(String image, String id, String room_id) {
        this.image = image;
        this.id = id;
        this.room_id = room_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public RoomImageItem() {
    }

    public String getImage() {
        return image;

    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RoomImageItem(String image, String id) {
        this.image = image;
        this.id = id;
    }
}
