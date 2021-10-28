package com.example.sacudeycome;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

public class ShakeDetector implements SensorEventListener {

    private static final float SHAKE_THRESHOLD_GRAVITY = 2.2F;
    private static final int SHAKE_SLOP_TIME_MS = 100;
    private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

    private OnShakeListener mListener;
    private long mShakeTimestamp;
    private int mShakeCount;
    public boolean right;

    public void setOnShakeListener(OnShakeListener listener) {
        this.mListener = listener;
    }

    public interface OnShakeListener {
        public void onShake(int count, boolean right);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // ignore
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (mListener != null) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];


            Float gX = x / SensorManager.GRAVITY_EARTH;
            Float gY = y / SensorManager.GRAVITY_EARTH;
            Float gZ = z / SensorManager.GRAVITY_EARTH;

            // gForce will be close to 1 when there is no movement.
            float gForce = (float)Math.sqrt(gX * gX + gY * gY + gZ * gZ);


            if (gForce > SHAKE_THRESHOLD_GRAVITY) {
                final long now = System.currentTimeMillis();
                // ignore shake events too close to each other (500ms)
                if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
                    return;
                }

                if(Round(x,4)>10.0000){
                    Log.d("SENSOR IZQUIERDA", "X Right axis: " + x);
                    right = false;
                }
                else if(Round(x,4)<-10.0000){
                    Log.d("SENSOR DERECHA", "X Left axis: " + x);
                    right = true;
                }

                // reset the shake count after 3 seconds of no shakes
                if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
                    mShakeCount = 0;
                }

                mShakeTimestamp = now;
                mShakeCount++;

                Log.d("VISUALIZARVALOR SHAKE X", gX.toString());
                mListener.onShake(mShakeCount,right);
            }
        }
    }
    public static float Round(float Rval, int Rpl) {
        float p = (float)Math.pow(10,Rpl);
        Rval = Rval * p;
        float tmp = Math.round(Rval);
        return (float)tmp/p;
    }
}