package com.makent.trioangle.model.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DeleteAccountModel  implements Serializable {
    @SerializedName("status_code")
    @Expose
    private String statusCode;
    @SerializedName("success_message")
    @Expose
    private String successMessage;

    @SerializedName("status_message")
    @Expose
    private String statusMessage;

    @SerializedName("is_payout")
    @Expose
    public boolean isPayout;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getSuccessMessage() {
        return successMessage;
    }
    public String getStatusMessage() {
        return statusMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }


    public boolean isPayout() {
        return isPayout;
    }

    public void setisPayout(boolean isPayout) {
        this.isPayout = isPayout;
    }
}
