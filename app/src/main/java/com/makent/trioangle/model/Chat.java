package com.makent.trioangle.model;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  model
 * @category    Chat
 * @author      Trioangle Product Team
 * @version     1.1
 */

public class Chat {
    private int usersId;
    private String message,msgtripstatus,msgtripamt,msgtripguest;
    private String sentAt;
    private String name;
    private String profile;
    private int status;

    public Chat(String message, String sentAt, String name,String profile, int status) {
        //this.usersId = usersId;
        this.message = message;
        this.sentAt = sentAt;
        this.name = name;
        this.profile=profile;
        this.status = status;
    }

    public Chat(String msgtripstatus, String msgtripamt,String msgtripguest,int status) {
        this.msgtripstatus = msgtripstatus;
        this.msgtripamt = msgtripamt;
        this.msgtripguest = msgtripguest;
        this.status = status;
    }


    public int getUsersId() {
        return usersId;
    }

    public String getMessage() {
        return message;
    }

    public String getSentAt() {
        return sentAt;
    }

    public String getName() { return name;  }

    public String getImage() { return profile;   }

    public int getStatus() {
        return status;
    }

    public String getMsgTripStatus() {   return msgtripstatus;   }

    public String getMSgTripAmt() {
        return msgtripamt;
    }

    public String getMSgTripGuest() {
        return msgtripguest;
    }

}
