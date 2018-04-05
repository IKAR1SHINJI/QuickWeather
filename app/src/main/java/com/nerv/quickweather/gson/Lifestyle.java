package com.nerv.quickweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NERV on 2017/12/5.
 */

public class Lifestyle {

    @SerializedName("type")
    public String life_type;

    @SerializedName("brf")
    public String life_brf;

    @SerializedName("txt")
    public String life_txt;

//    @SerializedName("comf")
//    public Comfort comfort;
//
//    @SerializedName("cw")
//    public CarWash carWash;
//
//    public Sport sport;
//
//    public class Comfort{
//
//        @SerializedName("txt")
//        public String info;
//    }
//
//    public class CarWash{
//
//        @SerializedName("txt")
//        public String info;
//    }
//
//    public class Sport{
//
//        @SerializedName("txt")
//        public String info;
//    }
}
