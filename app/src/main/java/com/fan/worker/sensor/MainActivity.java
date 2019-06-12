package com.fan.worker.sensor;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.util.Pools;
import android.util.Log;

import com.fan.worker.R;

import java.util.ArrayDeque;
import java.util.Arrays;

/**
 * Created by pc on 19-3-18.
 */

public class MainActivity extends Activity {

    private static final String TAG = "GyroscopeSensorListener";

    private static final float NS2S = 1.0f / 1000000000.0f;
    private static final float DISTANCE_PER_DEGREE = 1080.0f / 90;
    private static final int MAX_SIZE = 10;

    private SensorManager sensorManager = null;
    private Sensor gyroscopeSensor = null;

    private final float[] deltaRotationVector = new float[4];
    private float[] gyroOrientation = new float[3];
    private float[] gyroMatrix = new float[9];
    private float[] mLastEventOrientation = new float[3];
    private float[] mFirstOrientation = new float[3];
    private boolean mLastEventSet = false;
    public static final float EPSILON = 0.0001f;
    protected float[] mLastEventData = new float[3];
    protected float mTotalDeltaX = 540 / DISTANCE_PER_DEGREE, mTotalDeltaY = 1080 / DISTANCE_PER_DEGREE;
    private float mDeltaX = 0, mDeltaY = 0;
    private float mTimestampgyro = 0.0f;

    private boolean mGotFirstPos = false;
    private float[] mRotationMatrix = new float[9];
    private float[] orientationVals = new float[3];
    private float[] orientationFirstVals = new float[3];

    private int ref = 0;

    private GyroScopeValueQueue mGyroScopeValueQueue = new GyroScopeValueQueue(MAX_SIZE);

//    private GyroscopeCanvasView mCanvasView;

    private void initMatrixData(boolean initRefCount) {
        Arrays.fill(gyroOrientation, 0);
        // initialise gyroMatrix with identity matrix
        gyroMatrix[0] = 1.0f;
        gyroMatrix[1] = 0.0f;
        gyroMatrix[2] = 0.0f;
        gyroMatrix[3] = 0.0f;
        gyroMatrix[4] = 1.0f;
        gyroMatrix[5] = 0.0f;
        gyroMatrix[6] = 0.0f;
        gyroMatrix[7] = 0.0f;
        gyroMatrix[8] = 1.0f;
        if (initRefCount) {
            mTimestampgyro = -1L;
        }
        mGotFirstPos = false;
        mLastEventSet = false;
    }

    private void multiplication(float[] A, float[] B, float[] out) {

        float v0 = A[0] * B[0] + A[1] * B[3] + A[2] * B[6];
        float v1 = A[0] * B[1] + A[1] * B[4] + A[2] * B[7];
        float v2 = A[0] * B[2] + A[1] * B[5] + A[2] * B[8];

        float v3 = A[3] * B[0] + A[4] * B[3] + A[5] * B[6];
        float v4 = A[3] * B[1] + A[4] * B[4] + A[5] * B[7];
        float v5 = A[3] * B[2] + A[4] * B[5] + A[5] * B[8];

        float v6 = A[6] * B[0] + A[7] * B[3] + A[8] * B[6];
        float v7 = A[6] * B[1] + A[7] * B[4] + A[8] * B[7];
        float v8 = A[6] * B[2] + A[7] * B[5] + A[8] * B[8];

        out[0] = v0;
        out[1] = v1;
        out[2] = v2;
        out[3] = v3;
        out[4] = v4;
        out[5] = v5;
        out[6] = v6;
        out[7] = v7;
        out[8] = v8;
    }

    // Create a listener
    SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            // This time step's delta rotation to be multiplied by the current rotation
            // after computing it from the gyro sample data.
            if (mTimestampgyro == -1L || ref++ < 30) {
                mTimestampgyro = event.timestamp;
                return;
            }

            // todo 10ge average
            double thetaOverTwo;
            if (mTimestampgyro != 0) {
                final double dT = (event.timestamp - mTimestampgyro) * NS2S;
                // Axis of the rotation sample, not normalized yet.
                //float[] smooth = LowPassFilter.filter(event.values, mLastEventData , 1.0f);
                mLastEventData[0] = event.values[0];
                mLastEventData[1] = event.values[1];
                mLastEventData[2] = event.values[2];

                double axisX = (double) mLastEventData[0];
                double axisY = (double) mLastEventData[1];
                double axisZ = (double) mLastEventData[2];
                // Calculate the angular speed of the sample
                double omegaMagnitude = Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

                //Log.d("GSensorDetector", "generateRaw: " + Arrays.toString(smooth) + " mLastEventData:" + Arrays.toString(mLastEventData));

                // Normalize the rotation vector if it's big enough to get the axis
                if (omegaMagnitude > EPSILON) {
                    axisX /= omegaMagnitude;
                    axisY /= omegaMagnitude;
                    axisZ /= omegaMagnitude;
                }

//                Log.d(TAG, "axisX : " + axisX + " ,axisY :  " + axisY +  " ,axisZ : "  + axisZ);
                // Integrate around this axis with the angular speed by the time step
                // in order to get a delta rotation from this sample over the time step
                // We will convert this axis-angle representation of the delta rotation
                // into a quaternion before turning it into the rotation matrix.
                thetaOverTwo = omegaMagnitude * dT / 2.0f;
                double sinThetaOverTwo = Math.sin(thetaOverTwo);
                double cosThetaOverTwo = Math.cos(thetaOverTwo);
                deltaRotationVector[0] = (float) (sinThetaOverTwo * axisX);
                deltaRotationVector[1] = (float) (sinThetaOverTwo * axisY);
                deltaRotationVector[2] = (float) (sinThetaOverTwo * axisZ);
                deltaRotationVector[3] = (float) (cosThetaOverTwo);
            }
            mTimestampgyro = event.timestamp;
            SensorManager.getRotationMatrixFromVector(mRotationMatrix, deltaRotationVector);

            /*Log.d(TAG, " deltaRotationVector[0]: " + deltaRotationVector[0] + " deltaRotationVector[1]: " + deltaRotationVector[1] + " deltaRotationVector[2] : " + deltaRotationVector[2] + "" +
                    " deltaRotationVector[3] : " + deltaRotationVector[3]);*/
            if (!mGotFirstPos) {
                Arrays.fill(orientationFirstVals, 0);
                Arrays.fill(mLastEventOrientation, 0);
                mGotFirstPos = true;
                mLastEventSet = false;
                //log(" First ----> Yaw A0: " + Arrays.toString(gyroMatrix));
                multiplication(gyroMatrix, mRotationMatrix, gyroMatrix);

                SensorManager.getOrientation(gyroMatrix, gyroOrientation);
                // Optionally convert the result from radians to degrees
                orientationVals[0] = (float) Math.toDegrees(gyroOrientation[0]);
                orientationVals[1] = (float) Math.toDegrees(gyroOrientation[1]);
                orientationVals[2] = (float) Math.toDegrees(gyroOrientation[2]);
                System.arraycopy(orientationVals, 0, mFirstOrientation, 0, 3);
                //log(" First ----> Yaw A: " + Arrays.toString(mRotationMatrix) + " B:" + Arrays.toString(gyroMatrix) + " rotate:" + Arrays.toString(gyroOrientation));
                return;
            }

            multiplication(gyroMatrix, mRotationMatrix, gyroMatrix);

            SensorManager.getOrientation(gyroMatrix, gyroOrientation);
            // Optionally convert the result from radians to degrees
            orientationVals[0] = (float) Math.toDegrees(gyroOrientation[0]);
            orientationVals[1] = (float) Math.toDegrees(gyroOrientation[1]);
            orientationVals[2] = (float) Math.toDegrees(gyroOrientation[2]);

//            Log.d(TAG, " Yaw: " + orientationVals[0] + " Pitch: " + orientationVals[1] + " Roll : " + orientationVals[2]);


            float x = orientationVals[0];
            float y = orientationVals[1];
            float z = orientationVals[2];

            mGyroScopeValueQueue.add(x, y, z);
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        mCanvasView = new GyroscopeCanvasView(this);
        setContentView(R.layout.activity_main/*mCanvasView*/);

        initMatrixData(true);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (sensorManager != null) {
            gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

            if (gyroscopeSensor != null) {
                sensorManager.registerListener(gyroscopeSensorListener,
                        gyroscopeSensor, 10/**SensorManager.SENSOR_DELAY_UI*/);
            }
        }
    }

    private void resetData() {
        mTotalDeltaX = 540;
        mTotalDeltaY = 1080;
        ref = 0;
        mTimestampgyro = -1;
        mGotFirstPos = false;
        initMatrixData(true);
    }

    private void trigger() {

        GyroScopeValue average = mGyroScopeValueQueue.average();

        float x = average.x;
        float y = average.y;
        float z = average.z;

        float offsetX = x - (!mLastEventSet ? mFirstOrientation[0] : mLastEventOrientation[0]);
        float offsetY = y - (!mLastEventSet ? mFirstOrientation[1] : mLastEventOrientation[1]);
        float offsetZ = z - (!mLastEventSet ? mFirstOrientation[2] : mLastEventOrientation[2]);

        mDeltaX = offsetX;
        mDeltaY = offsetY;

        mTotalDeltaX += mDeltaX;
        mTotalDeltaY += mDeltaY;

        if (!mLastEventSet) {
            mLastEventSet = true;
        }

        mLastEventOrientation[0] = x;
        mLastEventOrientation[1] = y;
        mLastEventOrientation[2] = z;

        mGyroScopeValueQueue.clear();
//
//        if (mCanvasView != null) {
//            mCanvasView.drawPoint(mTotalDeltaX * DISTANCE_PER_DEGREE, mTotalDeltaY * DISTANCE_PER_DEGREE);
//        }
        // TODO: set bluetooth hid x: DISTANCE_PER_DEGREE * mTotalDeltaX, y: DISTANCE_PER_DEGREE * mTotalDeltaY
        Log.d(TAG, "setPos:" + offsetX + " - " + offsetY + " xyz:" + x + " " + y + " " + z);
    }

    private class GyroScopeValue {
        public float x, y, z;

        public GyroScopeValue(float x, float y, float z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private class GyroScopeValueQueue {
        private ArrayDeque<GyroScopeValue> GyroScopeQueue;
        private Pools.SimplePool<GyroScopeValue> pools;
        private int size;
        private GyroScopeValue[] array;
        private GyroScopeValue tempSmoothData = new GyroScopeValue(0, 0, 0);


        public GyroScopeValueQueue(int size) {
            this.size = size;
            array = new GyroScopeValue[size];
            GyroScopeQueue = new ArrayDeque<>(size);
            pools = new Pools.SimplePool<>(size);
        }

        public void add(float x, float y, float z) {
            GyroScopeValue av = pools.acquire();
            if (av == null) {
                av = new GyroScopeValue(x, y, z);
            }
            av.x = x;
            av.y = y;
            av.z = z;
            GyroScopeQueue.push(av);
            if (GyroScopeQueue.size() > size) {
                av = GyroScopeQueue.pollLast();
                pools.release(av);
                trigger();
            }
        }

        public GyroScopeValue average() {
            tempSmoothData.x = 0;
            tempSmoothData.y = 0;
            tempSmoothData.z = 0;
            int size = GyroScopeQueue.size();
            if (size > 0) {
                GyroScopeValue[] values = toArray();
                for (int i = 0; i < size; i++) {
                    GyroScopeValue aa = values[i];
                    tempSmoothData.x += aa.x;
                    tempSmoothData.y += aa.y;
                    tempSmoothData.z += aa.z;
                }
                tempSmoothData.x /= size;
                tempSmoothData.y /= size;
                tempSmoothData.z /= size;
            }
            return tempSmoothData;
        }

        private GyroScopeValue[] toArray() {
            GyroScopeQueue.toArray(array);
            return array;
        }

        public int size() {
            return GyroScopeQueue.size();
        }

        public void clear() {
            GyroScopeQueue.clear();
        }
    }
}
