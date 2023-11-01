package com.makent.trioangle.model.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by trioangle on 3/8/18.
 */

public class CurrencyListModel {

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("symbol")
    @Expose
    private String symbol;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
