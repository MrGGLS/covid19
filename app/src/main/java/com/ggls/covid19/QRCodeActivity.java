package com.ggls.covid19;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapsInitializer;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class QRCodeActivity extends AppCompatActivity implements AMapLocationListener,LocationSource {
    public static final int DEFAULT_VIEW = 0x22;
    private static final int REQUEST_CODE_SCAN = 0X01;
    private static final String TAG = "QRCodeActivity";
    private static final int REQUEST_PERMISSIONS = 7777;
    public AMapLocationClient mLocationClient = null;
    public AMapLocationClientOption mLocationOption = null;
    private LocationSource.OnLocationChangedListener mListener;
    private TravelMapDataBase tmb = new TravelMapDataBase();
    private UserDataBase udb = new UserDataBase();
    private String address;
    private Double latitude;
    private Double longitude;
    private String qrResOriginal;
    private class GetLocationThread extends Thread{
        @Override
        public void run() {
            initLocation();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        MapsInitializer.updatePrivacyAgree(this,true);
        MapsInitializer.updatePrivacyShow(this,true,true);
        Button scanButton=findViewById(R.id.tap_to_scan);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newViewBtnClick(v);
            }
        });
    }

    public void newViewBtnClick(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.requestPermissions(
                    new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE},
                    DEFAULT_VIEW);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions == null || grantResults == null || grantResults.length < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //设置权限请求结果
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        if (requestCode == DEFAULT_VIEW) {
            //start ScankitActivity for scanning barcode
            ScanUtil.startScan(QRCodeActivity.this, REQUEST_CODE_SCAN, new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //                    开启线程获取当前位置
        GetLocationThread getLocationThread=new GetLocationThread();
        showMsg("正在获取当前位置...");
        getLocationThread.start();
        try {
            getLocationThread.sleep(2000);
            getLocationThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //receive result after your activity finished scanning
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        // Obtain the return value of HmsScan from the value returned by the onActivityResult method by using ScanUtil.RESULT as the key value.
        if (requestCode == REQUEST_CODE_SCAN) {
            Object obj = data.getParcelableExtra(ScanUtil.RESULT);
            if (obj instanceof HmsScan) {
                if (!TextUtils.isEmpty(((HmsScan) obj).getOriginalValue())) {
                    qrResOriginal=((HmsScan) obj).getOriginalValue();
                }
                return;
            }
        }
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
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //地址
                address = aMapLocation.getAddress();
                latitude = aMapLocation.getLatitude();
                longitude = aMapLocation.getLongitude();
                showMsg("当前地理位置是："+address);
                try {
                    generateCodeAndCard();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//              上传地理信息到数据库
                final String[] splitAddress = address.split("省|市");
                tmb.addLocation(new TravelMap("0", splitAddress[0], splitAddress[1], latitude, longitude));
//                只获取一次定位信息，后面的语句可以去掉
                mLocationClient.stopLocation();
                if(mListener!=null){
                    mListener.onLocationChanged(aMapLocation);
                }
            } else {
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }

    public void showMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mLocationClient){
            mLocationClient.onDestroy();
        }
    }
//  启动定位
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient.startLocation();//启动定位
        }
    }
//    停止定位
    @Override
    public void deactivate() {
        mListener = null;
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
            mLocationClient.onDestroy();
        }
        mLocationClient = null;
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
//            showMsg("已获得权限，可以定位啦！");
            mLocationClient.startLocation();
        } else {
            //false 无权限
            EasyPermissions.requestPermissions(this, "需要权限", REQUEST_PERMISSIONS, permissions);
        }
    }

    private void generateCodeAndCard() throws InterruptedException {
        int type = HmsScan.QRCODE_SCAN_TYPE;
        int width = 750;
        int height = 750;
        ImageView qrRes = findViewById(R.id.qr_res);
        Bitmap qrBitmap;
        HmsBuildBitmapOption codeRes = new HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.WHITE).setBitmapColor(Color.GREEN).setBitmapMargin(3).create();
        CardView qrTipCard = findViewById(R.id.qr_res_tip_card);
        TextView qrTip = findViewById(R.id.qr_res_tip);
        switch (udb.getStatus()) {
            case GREEN:
                codeRes = new HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.WHITE).setBitmapColor(Color.GREEN).setBitmapMargin(3).create();
                try {
                    qrBitmap = ScanUtil.buildBitmap("You're safe!", type, width, height, codeRes);
                    qrRes.setImageBitmap(qrBitmap);
                } catch (WriterException e) {
                    Log.w("buildBitmap", e);
                }
                break;
            case YELLOW:
                codeRes = new HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.WHITE).setBitmapColor(Color.RED).setBitmapMargin(3).create();
                try {
                    qrBitmap = ScanUtil.buildBitmap("You're in danger!", type, width, height, codeRes);
                    qrRes.setImageBitmap(qrBitmap);
                } catch (WriterException e) {
                    Log.w("buildBitmap", e);
                }
                break;
            case RED:
                codeRes = new HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.WHITE).setBitmapColor(Color.rgb(0xF2, 0xD9, 0x0E)).setBitmapMargin(3).create();
                try {
                    qrBitmap = ScanUtil.buildBitmap("You might be safe!", type, width, height, codeRes);
                    qrRes.setImageBitmap(qrBitmap);
                } catch (WriterException e) {
                    Log.w("buildBitmap", e);
                }
                break;
        }

        Toast.makeText(this, "广哥拉斯牛逼", Toast.LENGTH_SHORT).show();
        switch (codeRes.bimapColor) {
            case Color.GREEN:
                qrTip.setText("哎哟，不错哦。请继续保持~");
                qrTip.setTextColor(Color.GREEN);
                break;
            case Color.RED:
                qrTip.setText("隔离！马上隔离！");
                qrTip.setTextColor(Color.RED);
                break;
            default:
                qrTip.setText("我觉得你还是需要做一下核酸哦~");
                qrTip.setTextColor(Color.rgb(0xF2, 0xD9, 0x0E));
                break;
        }
        qrTipCard.setVisibility(View.VISIBLE);
        qrTipCard.setAlpha(0);
        qrTipCard.animate().translationY(-300f).alpha(1f).setDuration(1800);
    }
}
