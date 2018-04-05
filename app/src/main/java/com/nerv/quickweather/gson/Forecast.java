package com.nerv.quickweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NERV on 2017/12/5.
 */

public class Forecast {

    public String date;

//    @SerializedName("tmp")
//    public Temperature temperature;
//
//    public class Temperature {

    public String tmp_max;

    public String tmp_min;

//    }

//    @SerializedName("cond")
//    public More more;
//
//    public class More {

    @SerializedName("cond_txt_d")
    public String info_d;

    @SerializedName("cond_txt_n")
    public String info_n;
//    }

}
