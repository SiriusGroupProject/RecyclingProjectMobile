package com.sirius.android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;

public class ScanBarcodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private static final int REQUEST_CAMERA=1;
    private ZXingScannerView ScannerView;
    private  static int camID = Camera.CameraInfo.CAMERA_FACING_BACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);
        int currentApiVersion = Build.VERSION.SDK_INT;

        if(currentApiVersion == Build.VERSION_CODES.LOLLIPOP){
            if(checkPermission()){
                Toast.makeText(getApplicationContext(),"Onay verildi!",Toast.LENGTH_LONG).show();
            }
            else{
                requestPermission();
            }
        }
    }

    private boolean checkPermission(){
        return (ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA)== PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission(){
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},REQUEST_CAMERA);
    }
    public void onRequestPermissionsResult(int requestCode, String permissions[], int grantResults[]){
        switch (requestCode){
            case REQUEST_CAMERA :
                if(grantResults.length > 0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted){
                        Toast.makeText(getApplicationContext(),"Onay verildi, kameraya erişebilirsiniz.",Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"İstek reddedildi, kameraya erişemezsiniz.",Toast.LENGTH_LONG).show();
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                            /*if(shouldShowRequestPermissionRationale(CAMERA)){
                                showMessageOKCancel("Kameraya erişmek için izin vermelisiniz",
                                        new DialogInterface.OnClickListener(){
                                            @Override
                                            public void onClick(DialogInterface dialog, int which){
                                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                                                    requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                                }
                                            }
                                        });
                                return;
                            }
*/
                        }
                    }
                }
                break;
        }
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener){
        new androidx.appcompat.app.AlertDialog.Builder(ScanBarcodeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK",okListener)
                .setNegativeButton("İptal",null)
                .create()
                .show();
    }

    @Override
    public void handleResult(Result result) { //Deliver the result to get info about the automat
        final String myResult = result.getText();
        Log.d("QRCodeScanner", result.getText());
        Log.d("QRCodeScanner", result.getBarcodeFormat().toString());

        ScannerView.stopCamera();

        Bundle b = getIntent().getExtras();
        String userId = ""; // or other values
        String automatId = "";
        double balance = 0.0;
        if(b != null) {
            userId = b.getString("userID");
            automatId = b.getString("automatID");
            balance = b.getDouble("balance");
        }
        Intent intent = new Intent(ScanBarcodeActivity.this, WaitingScreenBarcode.class);
        Bundle bu = new Bundle();
        bu.putString("userID",userId); //Your id
        bu.putString("automatID",automatId);
        bu.putString("barcode",result.getText());
        bu.putDouble("balance",balance);
        intent.putExtras(bu);
        startActivity(intent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    public void onResume() {
        super.onResume();
        int currentApiVersion = Build.VERSION.SDK_INT;
        if(currentApiVersion >= Build.VERSION_CODES.LOLLIPOP){
            if(checkPermission()){
                if(ScannerView == null){
                    ScannerView = new ZXingScannerView(this);
                    setContentView(ScannerView);
                }
            }
            ScannerView.setResultHandler(this);
            ScannerView.startCamera();
        }
        else{
            requestPermission();
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        ScannerView.stopCamera();
    }
}

