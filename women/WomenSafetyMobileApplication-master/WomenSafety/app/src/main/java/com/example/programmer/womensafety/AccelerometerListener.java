package com.example.programmer.womensafety;

/**
 * Created by Programmer on 12/14/2017.
 */

interface AccelerometerListener {
    public void onAccelerationChanged(float x,float y,float z);
    public  void onShake(float force);
}
