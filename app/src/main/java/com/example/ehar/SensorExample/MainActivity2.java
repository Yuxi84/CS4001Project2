package com.example.ehar.SensorExample;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by ehar on 8/15/16.
 */
public class MainActivity2
        extends AppCompatActivity
        implements Observer {

    // textviews
    private TextView accel_x_view = null;
    private TextView accel_y_view = null;
    private TextView accel_z_view = null;
    private TextView locate_lat = null;
    private TextView locate_lon = null;

    private Observable accel;
    private Observable locate;

    @Override
    public void update(Observable observable, Object o) {
        if (observable instanceof AccelerometerHandler){
            //update from accelerometer
            float [] values = (float []) o;
            accel_x_view.setText(Float.toString(values[0]));
            accel_y_view.setText(Float.toString(values[1]));
            accel_z_view.setText(Float.toString(values[2]));
        }else if (observable instanceof LocationTracker){
            //update from LocationTracker
            Location location = (Location) o;
            double latValue = location.getLatitude();
            double lonValue = location.getLongitude();
            locate_lat.setText(Double.toString(latValue));
            locate_lon.setText(Double.toString(lonValue));

        }

    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accel_x_view = (TextView) findViewById(R.id.accel_x);
        accel_y_view = (TextView) findViewById(R.id.accel_y);
        accel_z_view = (TextView) findViewById(R.id.accel_z);
        this.accel = new AccelerometerHandler(500, this);
        this.accel.addObserver(this);
        locate_lat = (TextView) findViewById(R.id.latValue);
        locate_lon = (TextView) findViewById(R.id.lonValue);
        this.locate = new LocationTracker(this);
        this.locate.addObserver(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
