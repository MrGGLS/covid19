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
        //????????????????????????
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
        if (requestCode == DEFAULT_VIEW) {
            //start ScankitActivity for scanning barcode
            ScanUtil.startScan(QRCodeActivity.this, REQUEST_CODE_SCAN, new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //                    ??????????????????????????????
        GetLocationThread getLocationThread=new GetLocationThread();
        showMsg("????????????????????????...");
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
        //???????????????
        try {
            mLocationClient = new AMapLocationClient(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        //????????????????????????
        mLocationClient.setLocationListener(this);
        //?????????AMapLocationClientOption??????
        mLocationOption = new AMapLocationClientOption();
        //?????????????????????AMapLocationMode.Hight_Accuracy?????????????????????
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //????????????3s???????????????????????????????????????
        //??????setOnceLocationLatest(boolean b)?????????true??????????????????SDK???????????????3s?????????????????????????????????????????????????????????true???setOnceLocation(boolean b)????????????????????????true???????????????????????????false???
        mLocationOption.setOnceLocationLatest(true);
        //????????????????????????????????????????????????????????????
        mLocationOption.setNeedAddress(true);
        //?????????????????????????????????????????????????????????30000???????????????????????????????????????8000?????????
        mLocationOption.setHttpTimeOut(20000);
        //??????????????????????????????????????????????????????
        mLocationOption.setLocationCacheEnable(false);
        //??????????????????????????????????????????
        mLocationClient.setLocationOption(mLocationOption);
        mLocationClient.startLocation();
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //??????
                address = aMapLocation.getAddress();
                latitude = aMapLocation.getLatitude();
                longitude = aMapLocation.getLongitude();
                showMsg("????????????????????????"+address);
                try {
                    generateCodeAndCard();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//              ??????????????????????????????
                final String[] splitAddress = address.split("???|???");
                tmb.addLocation(new TravelMap("0", splitAddress[0], splitAddress[1], latitude, longitude));
//                ?????????????????????????????????????????????????????????
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
//  ????????????
    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        mListener = onLocationChangedListener;
        mListener = onLocationChangedListener;
        if (mLocationClient == null) {
            mLocationClient.startLocation();//????????????
        }
    }
//    ????????????
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
            //true ????????? ????????????
//            showMsg("????????????????????????????????????");
            mLocationClient.startLocation();
        } else {
            //false ?????????
            EasyPermissions.requestPermissions(this, "????????????", REQUEST_PERMISSIONS, permissions);
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
                codeRes = new HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.WHITE).setBitmapColor(Color.rgb(0xF2, 0xD9, 0x0E)).setBitmapMargin(3).create();
                try {
                    qrBitmap = ScanUtil.buildBitmap("You might be safe!", type, width, height, codeRes);
                    qrRes.setImageBitmap(qrBitmap);
                } catch (WriterException e) {
                    Log.w("buildBitmap", e);
                }
                break;
            case RED:
                codeRes = new HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.WHITE).setBitmapColor(Color.RED).setBitmapMargin(3).create();
                try {
                    qrBitmap = ScanUtil.buildBitmap("You're in danger!", type, width, height, codeRes);
                    qrRes.setImageBitmap(qrBitmap);
                } catch (WriterException e) {
                    Log.w("buildBitmap", e);
                }
                break;
        }

        Toast.makeText(this, "??????????????????", Toast.LENGTH_SHORT).show();
        switch (codeRes.bimapColor) {
            case Color.GREEN:
                qrTip.setText("????????????????????????????????????~");
                qrTip.setTextColor(Color.GREEN);
                break;
            case Color.RED:
                qrTip.setText("????????????????????????");
                qrTip.setTextColor(Color.RED);
                break;
            default:
                qrTip.setText("??????????????????????????????????????????~");
                qrTip.setTextColor(Color.rgb(0xF2, 0xD9, 0x0E));
                break;
        }
        qrTipCard.setVisibility(View.VISIBLE);
        qrTipCard.setAlpha(0);
        qrTipCard.animate().translationY(-300f).alpha(1f).setDuration(1800);
    }
}
