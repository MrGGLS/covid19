/*
 *  Copyright 2020. Huawei Technologies Co., Ltd. All rights reserved.
 *  This subclass is created by HMS Core Toolkit
 *  and used to receive token information or messages returned by HMS server
 *
 */
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
import android.widget.Toast;
import com.huawei.hms.hmsscankit.ScanUtil;
import com.huawei.hms.hmsscankit.WriterException;
import com.huawei.hms.ml.scan.HmsBuildBitmapOption;
import com.huawei.hms.ml.scan.HmsScan;
import com.huawei.hms.ml.scan.HmsScanAnalyzerOptions;

public class QRCodeActivity extends Activity {
    public static final int DEFAULT_VIEW = 0x22;
    private static final int REQUEST_CODE_SCAN = 0X01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
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
        if (permissions == null || grantResults == null || grantResults.length < 2 || grantResults[0] != PackageManager.PERMISSION_GRANTED || grantResults[1] != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (requestCode == DEFAULT_VIEW) {
            //start ScankitActivity for scanning barcode
            ScanUtil.startScan(QRCodeActivity.this, REQUEST_CODE_SCAN, new HmsScanAnalyzerOptions.Creator().setHmsScanTypes(HmsScan.ALL_SCAN_TYPE).create());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                    int type = HmsScan.QRCODE_SCAN_TYPE;
                    int width = 750;
                    int height = 750;
                    ImageView qrRes=findViewById(R.id.qr_res);
                    String res=((HmsScan) obj).getOriginalValue();
                    Bitmap qrBitmap;
                    HmsBuildBitmapOption codeRes;
                    Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
                    if(res.equals("China")){
                        codeRes = new HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.WHITE).setBitmapColor(Color.GREEN).setBitmapMargin(3).create();
                        try {
                            qrBitmap = ScanUtil.buildBitmap("You're safe!", type, width, height, codeRes);
                            qrRes.setImageBitmap(qrBitmap);
                        } catch (WriterException e) {
                            Log.w("buildBitmap", e);
                        }
                    }else if(res.equals("America")){
                        codeRes = new HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.WHITE).setBitmapColor(Color.RED).setBitmapMargin(3).create();
                        try {
                            qrBitmap = ScanUtil.buildBitmap("You're in danger!", type, width, height, codeRes);
                            qrRes.setImageBitmap(qrBitmap);
                        } catch (WriterException e) {
                            Log.w("buildBitmap", e);
                        }
                    }else{
                        codeRes = new HmsBuildBitmapOption.Creator().setBitmapBackgroundColor(Color.WHITE).setBitmapColor(Color.rgb(0xF2,0xD9,0x0E)).setBitmapMargin(3).create();
                        try {
                            qrBitmap = ScanUtil.buildBitmap("You might be safe!", type, width, height, codeRes);
                            qrRes.setImageBitmap(qrBitmap);
                        } catch (WriterException e) {
                            Log.w("buildBitmap", e);
                        }
                    }
                }
                return;
            }
        }
    }
}
