package com.nerv.quickweather;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nerv.quickweather.gson.Forecast;
import com.nerv.quickweather.gson.Lifestyle;
import com.nerv.quickweather.gson.Weather;
import com.nerv.quickweather.service.AutoUpdateService;
import com.nerv.quickweather.util.HttpUtil;
import com.nerv.quickweather.util.Utility;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;


public class WeatherActivity extends AppCompatActivity {

    public DrawerLayout drawerLayout;

    private Button navButton;

    public SwipeRefreshLayout swipeRefresh;

    private String mWeatherId;

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView degreeText;

    private TextView weatherCondText;

    private TextView updateTime;

    private TextView feelTemperature;

    private TextView weatherWindLv;

    private TextView weatherWindSpd;

    private TextView weatherWindDir;

    private LinearLayout forecastLayout;

    private LinearLayout lifestyleLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView pm10Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;

    public LocationClient mLocationClient=null;

//    private TextView positionText;


    private Button locateBtn;


    private String key="4632ca196b9e494b92cc93e3821aafc0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //判断系统版本，Android6.0以上状态栏沉浸
        if(Build.VERSION.SDK_INT>=21){
            View decorView=getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);

        //初始化各个控件
        weatherLayout = (ScrollView) findViewById(R.id.weather_layout);
        titleCity=(TextView) findViewById(R.id.title_city);
        updateTime=(TextView) findViewById(R.id.update_time);
        degreeText=(TextView) findViewById(R.id.degree_text);
        feelTemperature=(TextView) findViewById(R.id.feel_temperature);
        weatherCondText=(TextView) findViewById(R.id.weather_cond_text);
        weatherWindLv=(TextView) findViewById(R.id.weather_wind_lv);
        weatherWindSpd=(TextView) findViewById(R.id.weather_wind_spd);
        weatherWindDir=(TextView) findViewById(R.id.weather_wind_dir);
        forecastLayout=(LinearLayout) findViewById(R.id.forecast_layout);
        lifestyleLayout=(LinearLayout) findViewById(R.id.lifestyle_layout);
        aqiText=(TextView) findViewById(R.id.aqi_text);
        pm25Text=(TextView) findViewById(R.id.pm25_text);
        pm10Text=(TextView) findViewById(R.id.pm10_text);
        comfortText = (TextView) findViewById(R.id.outgoing_text);
        carWashText = (TextView) findViewById(R.id.carwash_text);
        sportText = (TextView) findViewById(R.id.sport_text);
        bingPicImg =(ImageView) findViewById(R.id.bing_pic_img);
        swipeRefresh=(SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        navButton=(Button) findViewById(R.id.nav_button);
        locateBtn=(Button) findViewById(R.id.locate_btn);
//        positionText=(TextView) findViewById(R.id.position_text_view);

        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString=preferences.getString("weather",null);

        //有缓存时直接解析天气，无缓存时去访问服务器查询天气
        if(weatherString!=null){
            Weather weather= Utility.handleWeatherResponse(weatherString);
            mWeatherId=weather.basic.weatherId;
            showWeatherInfo(weather);
        }else {
            mWeatherId=getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });

        //侧滑菜单
        navButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        /**
         * 定位
         * 点击按钮定位
         */

        locateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * 动态权限申请
                 * 将没有申请到的权限放入List集合中，再将List转换成数组，调用ActivityCompat.requestPermissions()一次申请所需权限
                 */

                List<String> permissionList = new ArrayList<>();
                if (ContextCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                if (ContextCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(Manifest.permission.READ_PHONE_STATE);
                }
                if (ContextCompat.checkSelfPermission(WeatherActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (!permissionList.isEmpty()) {
                    String [] permissions = permissionList.toArray(new String[permissionList.size()]);
                    ActivityCompat.requestPermissions(WeatherActivity.this, permissions, 1);
                } else {
                    /*
                    if(mLocationClient.isStarted()==true){
                        mLocationClient.stop();
                    }*/
                    MyLocationListener myListener=new MyLocationListener();

                    mLocationClient=new LocationClient(getApplicationContext());
                    mLocationClient.registerLocationListener(myListener);
                    requestLocation();
                }
            }
        });

        /**
         * 获取bing每日图片
         * 尝试从SharedPreferences读取缓存图片 如果有缓存就用Glide加载图片 如果没有则用loadBingPic()方法获取图片
         */
        String bingPic=preferences.getString("bing_pic",null);
        if(bingPic!=null){
            Glide.with(this).load(bingPic).into(bingPicImg);
        }else {
            loadBingPic();
        }


    }

    private void requestLocation()
    {
        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
//        option.setScanSpan(5000);
//        option.setOpenGps(true);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
    }

    /**
    * 判断是否获得权限
    */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用定位功能", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "定位时发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    /**
     * 实现定位
     */
    public class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            StringBuilder currentPosition = new StringBuilder();
            //获取定位经纬度
            double longitude=bdLocation.getLongitude();
            double latitude=bdLocation.getLatitude();
            String coordinate=longitude+","+latitude;

//            currentPosition.append("纬度:").append(bdLocation.getLatitude()).append("\n");
//            currentPosition.append("经度:").append(bdLocation.getLongitude()).append("\n");
//            currentPosition.append("国家:").append(bdLocation.getCountry()).append("\n");
//            currentPosition.append("省:").append(bdLocation.getProvince()).append("\n");
//            currentPosition.append("市:").append(bdLocation.getCity()).append("\n");
//            currentPosition.append("区:").append(bdLocation.getDistrict()).append("\n");
//            currentPosition.append("街道:").append(bdLocation.getStreet()).append("\n");
//            currentPosition.append("定位方式:");
//            if(bdLocation.getLocType()==BDLocation.TypeGpsLocation){
//                currentPosition.append("GPS");
//            } else if (bdLocation.getLocType()==BDLocation.TypeNetWorkLocation){
//                currentPosition.append("网络");
//            }
//            positionText.setText(currentPosition);
            requestWeather(coordinate);
        }
    }

    /**
     * 根据天气id请求城市天气信息。
     */
    public void requestWeather(final String weatherId){
//        String weatherUrl="http://guolin.tech/api/weather?cityid=" + weatherId + "&key=4632ca196b9e494b92cc93e3821aafc0";
        String weatherUrl="https://free-api.heweather.com/s6/weather?key=" + key + "&location=" + weatherId;
        HttpUtil.sendOkHttpRequest(weatherUrl, new Callback() {

            //对异常进行处理
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"获取天气信息错误",Toast.LENGTH_LONG).show();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }

            //得到服务器返回的具体内容
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText=response.body().string();

                final Weather weather=Utility.handleWeatherResponse(responseText);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(weather!=null&&"ok".equals(weather.status)){
                            SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseText);
                            editor.apply();
                            mWeatherId=weather.basic.weatherId;
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
                        }
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        });
        loadBingPic();
    }

    /**
     * 加载bing每日图片
     */
    public void loadBingPic(){
        String requestBingPic="http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic=response.body().string();
                SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
                    }
                });

            }
        });
    }

    /**
     * 处理并展示Weather实体类中的数据。
     */
    private void showWeatherInfo(Weather weather){
        if(weather!=null&&"ok".equals(weather.status)){
        String cityName = weather.basic.cityName;
        String strUpdateTime = weather.update.updateTime.split(" ")[1];
        String strDegree = weather.now.temperature + "℃";
        String strFeelTemperature=weather.now.feelTemperature + "℃";
        String strCondInfo=weather.now.condInfo;
        String strWindLv=weather.now.windlv;
        String strWindSpd=weather.now.windSpd;
        String strWindDir=weather.now.windDir;
        titleCity.setText(cityName);
        updateTime.setText("发布时间:"+strUpdateTime);
        degreeText.setText(strDegree);
        feelTemperature.setText("体感温度:"+strFeelTemperature);
        weatherCondText.setText(strCondInfo);
        weatherWindLv.setText("风力:"+strWindLv);
        weatherWindSpd.setText("风速:"+strWindSpd+"km/h");
        weatherWindDir.setText("风向:"+strWindDir);
        forecastLayout.removeAllViews();
        for(Forecast forecast:weather.forecastList){
            View view= LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView temperature_range = (TextView) view.findViewById(R.id.temperature_range);
            dateText.setText(forecast.date);
            infoText.setText(forecast.info_d+"/"+forecast.info_n);
            temperature_range.setText(forecast.tmp_max+ "℃"+"/"+forecast.tmp_min+ "℃");
            forecastLayout.addView(view);
        }
        lifestyleLayout.removeAllViews();
        for(Lifestyle lifestyle:weather.lifestyleList){
            View view=LayoutInflater.from(this).inflate(R.layout.lifestyle_item,lifestyleLayout,false);
            TextView lifeType=(TextView) view.findViewById(R.id.life_type);
            TextView lifeText=(TextView) view.findViewById(R.id.life_text);
            lifeType.setText(lifestyle.life_brf);
            lifeText.setText(lifestyle.life_txt);
            lifestyleLayout.addView(view);
        }
//        if(weather.aqi!=null){
//            aqiText.setText(weather.aqi.city.aqi);
//            pm25Text.setText(weather.aqi.city.pm25);
//            pm10Text.setText(weather.aqi.city.pm10);
//        }

//        String comfort = weather.lifestyle.comfort.info;
//        String carWash =  weather.lifestyle.carWash.info;
//        String sport = weather.lifestyle.sport.info;
//        comfortText.setText(comfort);
//        carWashText.setText(carWash);
//        sportText.setText(sport);

        weatherLayout.setVisibility(View.VISIBLE);
//        lifestyleLayout.setVisibility(View.VISIBLE);

        Intent intent = new Intent(this, AutoUpdateService.class);
        startService(intent);

        Toast.makeText(WeatherActivity.this,"获取天气信息成功",Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(WeatherActivity.this,"获取天气信息失败",Toast.LENGTH_SHORT).show();
        }
    }
}
