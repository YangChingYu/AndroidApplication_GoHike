package com.ankan.android.hikerswatch;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.widget.Toast;





import java.util.List;
import java.util.Locale;

//AppCompatActivity,
@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends Activity implements SensorEventListener{

    private ImageView imageView;
    private float currentDegree = 0f;
    private Button button;
    LocationManager locationManager;
    LocationListener locationListener;
    TextView latTextView;
    TextView lonTextView;
    TextView accuracyTextView;
    TextView speedTextView;
    TextView addressTextView;
    Button switchOff,switchOn;
    Camera camera;
    Button btnFlashLight, btnBlinkFlashLight;
    private static final int CAMERA_REQUEST = 123;
    boolean hasCameraFlash = false;
    ScrollView background;
    public   SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        latTextView = findViewById(R.id.latTextView);
        background = findViewById(R.id.background);

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSetting();
            }
        });

        try {
            Intent intent = getIntent();
            int colorCode = Integer.parseInt(intent.getStringExtra("ColorChoice"));
            setTextColor(colorCode);

        } catch (Exception e) { //catch when getIntent is null
            System.out.print("No Intent found!");
        }







        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);

        hasCameraFlash = getPackageManager().
                hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        btnFlashLight = findViewById(R.id.btnFlashLightToggle);
        btnBlinkFlashLight = findViewById(R.id.btnBlinkFlashLight);

        btnFlashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCameraFlash) {
                    if (btnFlashLight.getText().toString().contains("ON")) {
                        btnFlashLight.setText("FLASHLIGHT OFF");
                        btnBlinkFlashLight.setText("BLINK FLASHLIGHT OFF");
                        flashLightOff();
                    } else {
                        btnBlinkFlashLight.setText("BLINK FLASHLIGHT ON");
                        btnFlashLight.setText("FLASHLIGHT ON");
                        flashLightOn();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "No flash available on your device",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        switchOff = findViewById(R.id.btn_close);
        switchOn = findViewById(R.id.btn_open);

        camera = Camera.open();
        final Camera.Parameters parameters = camera.getParameters();

        switchOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                camera.setParameters(parameters);
                camera.startPreview();
            }
        });

        switchOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                camera.setParameters(parameters);
                camera.stopPreview();

            }
        });


        btnBlinkFlashLight.setOnClickListener(new View.OnClickListener() {
                                                  //        @Override
                                                  public void onClick(View view) {

                                                      if(btnFlashLight.getText().toString().contains("ON"))
                                                      {
                                                          blinkFlash();
                                                      }
                                                      else{
                                                          Toast.makeText(MainActivity.this, "Press the above button first.",
                                                                  Toast.LENGTH_SHORT).show();
                                                      }

                                                  }
                                              });










        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        imageView = (ImageView) findViewById(R.id.compass_imageView);

        // 传感器管理器
        SensorManager sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        // 注册传感器(Sensor.TYPE_ORIENTATION(方向传感器);SENSOR_DELAY_FASTEST(0毫秒延迟);
        // SENSOR_DELAY_GAME(20,000毫秒延迟)、SENSOR_DELAY_UI(60,000毫秒延迟))
        sm.registerListener(MainActivity.this,
                sm.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_FASTEST);
        latTextView = findViewById(R.id.latTextView);
        lonTextView = findViewById(R.id.lonTextView);
        accuracyTextView = findViewById(R.id.accuracyTextView);
        speedTextView = findViewById(R.id.speedTextView);
        addressTextView = findViewById(R.id.addressTextView);





        locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                updateLocationInfo(location);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.i("Status Changed", "Ok");
            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.i("Provider Enabled", "Ok");

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.i("Provider Disabled", "Ok");

            }
        };

        //asking for permission
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { //if permission is not given
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1); //request for permission
        } else {
                locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
                Location lastKnownLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
                if(lastKnownLocation != null) {
                    updateLocationInfo(lastKnownLocation);
                } else {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }

        }
    }
//    public void setBackgroundColor(int colorCode) {
//        TextView mDisplayValue = findViewById(R.id.mDisplayValue);
//        mDisplayValue.setBackgroundColor(colorCode);
//    }
//

    private void openSetting() {

        Intent intent = new Intent(this, Setting2.class);
        startActivity(intent);
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasCameraFlash = getPackageManager().
                            hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                } else {
                    btnFlashLight.setEnabled(false);
                    btnBlinkFlashLight.setEnabled(false);
                    Toast.makeText(MainActivity.this, "Permission Denied for the Camera", Toast.LENGTH_SHORT).show();
                }
                break;
        }

        if(requestCode == 1) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startListening();
            }
        }
    }

    public void startListening() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 0, 0, locationListener);
        }

    }

    public void updateLocationInfo(Location location) {

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        try {
            List<Address> listAddress = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

            if(listAddress != null && listAddress.size() > 0) {
                String address = listAddress.get(0).getAddressLine(0);
                addressTextView.setText(address);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        latTextView.setText("Latitude: " + String.format("%.8f", location.getLatitude()));
        lonTextView.setText("Longitude: " + String.format("%.8f", location.getLongitude()));
        accuracyTextView.setText("Accuracy: " + String.format("%.8f", location.getAccuracy()));
        speedTextView.setText("Speed: " + String.format("%.2f", location.getSpeed() * 1.852) + "km/hr");

    }

    //传感器报告新的值(方向改变)
    public void onSensorChanged(SensorEvent event) {
//        background.setBackgroundColor(Color.parseColor("Yellow"));

        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            float degree = event.values[0];

            /*
            RotateAnimation类：旋转变化动画类

            参数说明:

            fromDegrees：旋转的开始角度。
            toDegrees：旋转的结束角度。
            pivotXType：X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
            pivotXValue：X坐标的伸缩值。
            pivotYType：Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
            pivotYValue：Y坐标的伸缩值
            */
            RotateAnimation ra = new RotateAnimation(currentDegree, -degree,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f);
            //旋转过程持续时间
            ra.setDuration(200);
            //罗盘图片使用旋转动画
            imageView.startAnimation(ra);

            currentDegree = -degree;
        }
    }
    //传感器精度的改变
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
//
//
//      btnBlinkFlashLight.setOnClickListener(new View.OnClickListener() {
////        @Override
//        public void onClick(View view) {
//
//            if(btnFlashLight.getText().toString().contains("ON"))
//            {
//                blinkFlash();
//            }
//            else{
//                Toast.makeText(MainActivity.this, "Press the above button first.",
//                        Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }
//




    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, true);
            }
        } catch (CameraAccessException e) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraManager.setTorchMode(cameraId, false);
            }
        } catch (CameraAccessException e) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void blinkFlash()
    {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String myString = "0101010101";
        long blinkDelay = 50; //Delay in ms
        for (int i = 0; i < myString.length(); i++) {
            if (myString.charAt(i) == '0') {
                try {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, true);
                    }
                } catch (CameraAccessException e) {
                }
            } else {
                try {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        cameraManager.setTorchMode(cameraId, false);
                    }
                } catch (CameraAccessException e) {
                }
            }
            try {
                Thread.sleep(blinkDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private int getColor(){
        SharedPreferences msharedPreference = getSharedPreferences("LL", MODE_PRIVATE);
        int selectedColor = msharedPreference.getInt("color",getResources().getColor(R.color.colorPrimary));

        return selectedColor;
    }

    public void setTextColor(int colorCode) {
        TextView latTextView = findViewById(R.id.latTextView);
        latTextView.setTextColor(colorCode);
        TextView lonTextView = findViewById(R.id.lonTextView);
        lonTextView.setTextColor(colorCode);
        TextView accuracyTextView = findViewById(R.id.accuracyTextView);
        accuracyTextView.setTextColor(colorCode);
        TextView speedTextView = findViewById(R.id.speedTextView);
        speedTextView.setTextColor(colorCode);
        TextView addressTextView = findViewById(R.id.addressTextView);
        addressTextView.setTextColor(colorCode);


    }

}
