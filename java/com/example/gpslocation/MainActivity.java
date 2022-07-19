package com.example.gpslocation;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.maps.android.SphericalUtil;

import org.jetbrains.annotations.Nullable;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity implements SensorEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener  {
    
    private static final int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private TextView magnum;
    private TextView latie;
    private TextView longie;
    private ImageView imageView,arr;
    private ImageView fab;
    private EditText latitude;
    private EditText longitude;
    private FloatingActionButton fab2;
    private Button save,cal;
    private String logii,lat,lat_long;
    private float angle;
    
    
    private FusedLocationProviderApi fusedLocationProviderApi = LocationServices.FusedLocationApi;
    
    
    enum LocationDirection {
        UNKNOWN,
        NORTH,
        NORTH_EAST,
        EAST,
        SOUTH_EAST,
        SOUTH,
        SOUTH_WEST,
        WEST,
        NORTH_WEST
    }
    
    private SensorManager sensorManager;
    private Sensor accelerometerSensor,magnetometerSensor;
    private float[] lastAccelerometer=new float[3];
    private float[] lastMagnetometer=new float[3];
    private float[] rotationMatrix=new float[9];
    private float[] orientation=new float[3];
    boolean isLastAccelerometerArrayCopied=false;
    boolean isLastMagnetometerArrayCopied=false;
    long lastUpdateTime=0;
    float CurrentDegree=0f;
    
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private LocationManager mLocationManager;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 1 * 500;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000; /* 2 sec */
    private LocationManager locationManager;
    private Boolean got_target=false;
    private String prev_lat;
    private String prev_long ;
    
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        
        magnum = findViewById(R.id.magnitude);
        imageView = findViewById(R.id.imageView);
        fab = findViewById(R.id.fab);
        fab2=findViewById(R.id.fab2);
        cal=findViewById(R.id.calculate);
        latie=findViewById(R.id.curent_lat);
        longie=findViewById(R.id.curent_lon);
        save=findViewById(R.id.save);
        arr=findViewById(R.id.arrow);
        latitude=findViewById(R.id.latitude);
        longitude=findViewById(R.id.longitude);
        Intent iin= getIntent();
        try{
            Bundle b = iin.getExtras();
            if(b!=null)
            {
                 prev_lat =(String) b.get("Lat");
                 prev_long=(String) b.get("Long");
                latitude.setText(prev_lat);
                longitude.setText(prev_long);
                got_target=true;
                
            }
            else{
                if(!(TextUtils.isEmpty(latitude.getText().toString()))) {
                    if(!(TextUtils.isEmpty(longitude.getText().toString()))) {
                        prev_lat=latitude.getText().toString();
                        prev_long=longitude.getText().toString();
                    }}
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    
        cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                got_target=true;
                prev_lat =latitude.getText().toString() ;
                prev_long =longitude.getText().toString() ;
    
                if(!(TextUtils.isEmpty(prev_lat))) {
                    if(!(TextUtils.isEmpty(prev_long))) {
                   
                        Location startPoint=new Location("locationA");
                        startPoint.setLatitude(mLocation.getLatitude());
                        startPoint.setLongitude(mLocation.getLongitude());
    
                        Location endPoint=new Location("locationA");
                        endPoint.setLatitude(Double.parseDouble(prev_lat));
                        endPoint.setLongitude(Double.parseDouble(prev_long));
    
                        double distance=startPoint.distanceTo(endPoint);
                        //Log.d("mgnum",mag);
                        magnum.setText(Double.toString(distance)+" m");
                }}
                
                
                
            }
        });
    
        sensorManager=(SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometerSensor=sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mLocationManager = (LocationManager)this.getSystemService(Context.LOCATION_SERVICE);
        checkLocation();
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent opt = new Intent(getApplicationContext(),option.class);
                startActivity(opt);
    
            }
        });
        
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(String.valueOf(mLocation.getLongitude()) != null)
                {
                    if(String.valueOf(mLocation.getLatitude()) != null)
                    {
                Intent i = new Intent(getApplicationContext(),save_page.class);
                lat=(String.valueOf(mLocation.getLatitude()));
                logii=(String.valueOf(mLocation.getLongitude()));
                lat_long=lat +","+logii;
                i.putExtra("loca_loca",lat_long );
                startActivity(i);}}
                else{
                    Toast.makeText(getApplicationContext(), "Wait For Location to appear",Toast.LENGTH_SHORT).show();
    
                }
            }
        });
    
    
    }
    
    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, this, 0).show();
            return false;
        }
    }
    
    private String but_magnitude(String late,String new_long) {
        double x= mLocation.getLatitude();
        double y= mLocation.getLongitude();
        double x2=Double.parseDouble(late);
        double y2=Double.parseDouble(new_long);
        double ma;
        ma = Math.pow((x2-x),2) +   Math.pow((y2-y),2);
        double mag=  Math.sqrt(ma);
        double mag2=  Math.round(mag);
        String real_mag= String.valueOf(mag2);
        return real_mag;
        
    }
    private String magnitude(double la, double lo) {
        double x= la;
        double y= lo;
        double x2=Double.parseDouble(prev_lat);
        double y2=Double.parseDouble(prev_long);
        double ma;
        ma = Math.pow((x2-x),2) +   Math.pow((y2-y),2);
        double mag=  Math.sqrt(ma);
        double mag2=  Math.round(mag);
        String real_mag= String.valueOf(mag2);
        return real_mag;
      
    }
     
        
        
        private boolean checkLocation() {
        if(!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }
    
    private boolean isLocationEnabled() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    
    }
    
    private void showAlert() {
        Toast.makeText(this, "jhamela hogya ",Toast.LENGTH_SHORT).show();
    
    }
    
    
    public static Bitmap textAsBitmap(String text, float textSize, int textColor) {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(textColor);
        paint.setTextAlign(Paint.Align.LEFT);
        float baseline = -paint.ascent(); // ascent() is negative
        int width = (int) (paint.measureText(text) + 0.0f); // round
        int height = (int) (baseline + paint.descent() + 0.0f);
        Bitmap image = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        
        Canvas canvas = new Canvas(image);
        canvas.drawText(text, 0, baseline, paint);
        return image;
    }
    
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor==accelerometerSensor){
            System.arraycopy(sensorEvent.values,0,lastAccelerometer,0, sensorEvent.values.length);
            isLastAccelerometerArrayCopied=true;
            
        }
        else if(sensorEvent.sensor==magnetometerSensor){
            System.arraycopy(sensorEvent.values,0,lastMagnetometer,0, sensorEvent.values.length);
            isLastMagnetometerArrayCopied=true;
            
        }
        if (isLastAccelerometerArrayCopied && isLastMagnetometerArrayCopied && System.currentTimeMillis()-lastUpdateTime>1050)
        {
            SensorManager.getRotationMatrix(rotationMatrix,null,lastAccelerometer,lastMagnetometer);
            SensorManager.getOrientation(rotationMatrix,orientation);
            
            float azimuthrad=orientation[0];
            float azimuthdeg= (float) Math.toDegrees(azimuthrad);
            RotateAnimation rotateAnimation=new RotateAnimation(CurrentDegree,-azimuthdeg, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f);
            rotateAnimation.setDuration(1050);
            rotateAnimation.setFillAfter(true);
            imageView.startAnimation(rotateAnimation);
            
            float bismu = CurrentDegree;
            CurrentDegree=-azimuthdeg;
            lastUpdateTime=System.currentTimeMillis();
            
            int x=(int)azimuthdeg;
            if (x<0){
                x=180+Math.abs(x);
            }
            String y=String.valueOf(x);
            fab.setImageBitmap(textAsBitmap((y+"°"), 60, Color.WHITE));
            try {
                float x_axis = azimuthdeg + angle;
                RotateAnimation rotate = new RotateAnimation(CurrentDegree-angle, -x_axis, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                rotate.setDuration(500);
                rotate.setFillAfter(true);
                arr.startAnimation(rotate);
            } catch (Exception e) {
                e.printStackTrace();
            }
    
        }
    }
    
    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        Toast.makeText(this, "Accuracy increasing..", Toast.LENGTH_SHORT).show();
    
    }
    
    /**
     * {@inheritDoc}
     * <p>
     * Dispatch onResume() to fragments.  Note that for better inter-operation
     * with older versions of the platform, at the point of this call the
     * fragments attached to the activity are <em>not</em> resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,accelerometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,magnetometerSensor,SensorManager.SENSOR_DELAY_NORMAL);
        
    }
    
    /**
     * {@inheritDoc}
     * <p>
     * Dispatch onPause() to fragments.
     */
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this,accelerometerSensor);
        sensorManager.unregisterListener(this,magnetometerSensor);
    }
    
    @Override
    public void onConnected(@Nullable  Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED  ){
            requestPermissions(new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return ;
        }
        startLocationUpdates();
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if(mLocation == null){
            startLocationUpdates();
        }
        if (mLocation != null) {
             latie.setText((String.valueOf(mLocation.getLatitude())));
            longie.setText((String.valueOf(mLocation.getLongitude())));
        } else {
            Toast.makeText(this, "Location not Detected", Toast.LENGTH_SHORT).show();
        }
    
    }
    private double angle() {
        double x= mLocation.getLatitude();
        double y= mLocation.getLongitude();
        double x2=Double.parseDouble(prev_lat);
        double y2=Double.parseDouble(prev_long);
        double tan=(y-y2) /(x-x2);
       double angle= Math.atan(tan);
       Log.e("directed", String.valueOf(angle));
       return angle;
    }
    
    
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)
                .setFastestInterval(FASTEST_INTERVAL);
        // 	Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, this);
        Log.d("reque", "--->>>>");
    }
    
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }
    
    @Override
    public void onConnectionFailed(ConnectionResult  connectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode());
    
    }
    
    @Override
    public void onLocationChanged(Location location) {
        
        latie.setText("Latitude= "+(String.valueOf(location.getLatitude())));
        longie.setText("Longitude= "+(String.valueOf(location.getLongitude() )));
        // You can now create a LatLng Object for use with maps
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        
        
       // double la= location.getLatitude();
       // double lo=location.getLongitude();
        if (got_target==true){
            Location startPoint1=new Location("locationA");
            startPoint1.setLatitude(location.getLatitude());
            startPoint1.setLongitude(location.getLongitude());
            LatLng latLng1 = new LatLng(location.getLatitude(), location.getLongitude());
    
            Location endPoint1=new Location("locationA");
            endPoint1.setLatitude(Double.parseDouble(prev_lat));
            endPoint1.setLongitude(Double.parseDouble(prev_long));
            LatLng latLng2 = new LatLng(Double.parseDouble(prev_lat), Double.parseDouble(prev_long));
    
            double distance=startPoint1.distanceTo(endPoint1);
            //Log.d("mgnum",mag);
            double a = distance;
    
            double roundOff = Math.round(a*100)/100;
            magnum.setText(Double.toString(roundOff)+" m");
            double heading = SphericalUtil.computeHeading(latLng1, latLng2);
            LocationDirection side = direction(heading);
            Log.d("direction ", String.valueOf(side));
            String ang=String.valueOf(side);
            switch (ang){
            
                case "NORTH":
                    angle=90;
                    break;
                case "NORTH_EAST":
                    angle=45;
                    break;
                    
                case "EAST":
                    angle=0;
                    break;
                    
                case "SOUTH_EAST":
                    angle=-45;
                    break;
                    
                case "SOUTH":
                    angle=-90;
                    break;
    
                case "SOUTH_WEST":
                     angle=-135;
                    break;
    
                case "WEST":
                    angle=-180;
                    break;
    
                case "NORTH_WEST":
                    angle=-225;
                    break;
                default:
                    break;
    
                
            
            }
            
        }
       
    
    }
    
    public static LocationDirection direction(double head) {
        double delta = 22.5;
        LocationDirection direction = LocationDirection.UNKNOWN;
        double heading = head;
        
        if ((heading >= 0 && heading < delta) || (heading < 0 && heading >= -delta)) {
            direction = LocationDirection.NORTH;
        } else if (heading >= delta && heading < 90 - delta) {
            direction = LocationDirection.NORTH_EAST;
        } else if (heading >= 90 - delta && heading < 90 + delta) {
            direction = LocationDirection.EAST;
        } else if (heading >= 90 + delta && heading < 180 - delta) {
            direction = LocationDirection.SOUTH_EAST;
        } else if (heading >= 180 - delta || heading <= -180 + delta) {
            direction = LocationDirection.SOUTH;
        } else if (heading >= -180 + delta && heading < -90 - delta) {
            direction = LocationDirection.SOUTH_WEST;
        } else if (heading >= -90 - delta && heading < -90 + delta) {
            direction = LocationDirection.WEST;
        } else if (heading >= -90 + delta && heading < -delta) {
            direction = LocationDirection.NORTH_WEST;
        }
        
        return direction;
    }
    
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}
