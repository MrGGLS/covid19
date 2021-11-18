package com.ggls.covid19;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeAddress;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;

import java.util.ArrayList;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class EPMapActivity extends AppCompatActivity implements AMapLocationListener, LocationSource, AMap.OnMapClickListener, GeocodeSearch.OnGeocodeSearchListener {
    private static final String TAG = "EPMapActivity";
    private static final int REQUEST_PERMISSIONS = 7777;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    MapView mapView;
    private AMap aMap;
    private LocationSource.OnLocationChangedListener mListener;
    private MyLocationStyle myLocationStyle=new MyLocationStyle();
//  地理编码搜索
    private GeocodeSearch geocodeSearch;
    //解析成功标识码
    private static final int PARSE_SUCCESS_CODE = 1000;
    //行踪表
    private ArrayList<String>journeyList=new ArrayList<>();
    private ArrayList<LatLng>latLngList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epmap);
        MapsInitializer.updatePrivacyAgree(this,true);
        MapsInitializer.updatePrivacyShow(this,true,true);
        initLocation();
        initMap(savedInstanceState);
        checkingAndroidVersion();
    }

    private void initMap(Bundle savedInstanceState) {
        mapView = findViewById(R.id.map_view);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        aMap = mapView.getMap();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(findViewById(R.id.location_great))));
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;
        myLocationStyle.interval(4000L);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(true);
        aMap.setOnMapClickListener(this);
        try {
            geocodeSearch=new GeocodeSearch(this);
            geocodeSearch.setOnGeocodeSearchListener(this);
        } catch (AMapException e) {
            e.printStackTrace();
        }
        // 设置定位监听
        aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        LatLng l1=new LatLng(34,108);
        LatLng l2=new LatLng(35,109);
        LatLng l3=new LatLng(36,110);
        LatLng l4=new LatLng(37,111);
        latLngList.add(l1);
        latLngList.add(l2);
        latLngList.add(l3);
        latLngList.add(l4);
        journeyList.add("北京");
        journeyList.add("上海");
        journeyList.add("深圳");
        journeyList.add("广州");
        for (int i = 0; i < 4; i++) {
            latlonToAddress(latLngList.get(i));
        }
        addMarkers(latLngList,journeyList);
    }

    private void initLocation() {
        //初始化定位
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //设置定位回调监听
        mLocationClient.setLocationListener(this);
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置定位请求超时时间，单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(20000);
        //关闭缓存机制，高精度定位会产生缓存。
        mLocationOption.setLocationCacheEnable(false);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
//                //地址
//                String address = aMapLocation.getAddress();
//                Double latitude = aMapLocation.getLatitude();
//                Double longitude = aMapLocation.getLongitude();
//                停止定位
                mLocationClient.stopLocation();
                if(mListener!=null){
                    mListener.onLocationChanged(aMapLocation);
                }
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    private void checkingAndroidVersion() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            //Android6.0及以上先获取权限再定位
            requestPermission();
        }else {
            //Android6.0以下直接定位
            mLocationClient.startLocation();
        }
    }

    @AfterPermissionGranted(REQUEST_PERMISSIONS)
    private void requestPermission() {
        String[] permissions = {
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        if (EasyPermissions.hasPermissions(this, permissions)) {
            //true 有权限 开始定位
            showMsg("已获得权限，可以定位啦！");
            mLocationClient.startLocation();
        } else {
            //false 无权限
            EasyPermissions.requestPermissions(this, "需要权限", REQUEST_PERMISSIONS, permissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //设置权限请求结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    private void showMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient.startLocation();//启动定位
        }
    }

    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        latlonToAddress(latLng);
        aMap.addMarker(new MarkerOptions().position(latLng).snippet("DefaultMarker"));
    }

    private void latlonToAddress(LatLng latLng) {
        //位置点  通过经纬度进行构建
        LatLonPoint latLonPoint = new LatLonPoint(latLng.latitude, latLng.longitude);
        //逆编码查询  第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 20, GeocodeSearch.AMAP);
        //异步获取地址信息
        geocodeSearch.getFromLocationAsyn(query);
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    private void addMarkers(ArrayList<LatLng> latLngList,ArrayList<String>journeyList) {
        //添加标点
        ImageView locationPoint=findViewById(R.id.location_danger);
        for(int i=0;i<latLngList.toArray().length;++i)
            aMap.addMarker(new MarkerOptions().position(latLngList.get(i)).snippet(journeyList.get(i)).icon(BitmapDescriptorFactory.fromBitmap(convertViewToBitmap(locationPoint))));
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
            //解析result获取地址描述信息
        if(i == PARSE_SUCCESS_CODE){
            RegeocodeAddress regeocodeAddress = regeocodeResult.getRegeocodeAddress();
            //显示解析后的地址
            showMsg("地址: "+regeocodeAddress.getFormatAddress());
        }else {
            showMsg("获取地址失败");
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}
