package com.nerv.quickweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NERV on 2017/12/5.
 */

public class Basic {

    @SerializedName("location")
    public String cityName;

    @SerializedName("cid")
    public String weatherId;

//    public Update update;

//    public class Update{
//
//        @SerializedName("loc")
//        public String updateTime;
//    }
}
