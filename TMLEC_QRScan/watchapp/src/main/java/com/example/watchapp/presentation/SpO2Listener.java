package com.example.watchapp.presentation;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.watchapp.R;
import com.samsung.android.service.health.tracking.HealthTracker;
import com.samsung.android.service.health.tracking.data.DataPoint;
import com.samsung.android.service.health.tracking.data.ValueKey;

import java.util.List;

public class SpO2Listener extends BaseListener {
    private final static String APP_TAG = "SpO2Listener";

    SpO2Listener() {
        final HealthTracker.TrackerEventListener trackerEventListener = new HealthTracker.TrackerEventListener() {
            @Override
            public void onDataReceived(@NonNull List<DataPoint> list) {
                Log.d(APP_TAG, "onDataReceived: SpO2 List Size = " + list.size()); // 👈 THIS LOG IS KEY
                for (DataPoint data : list) {
                    Log.d(APP_TAG, "DataPoint received: " + data.toString());
                    updateSpo2(data);
                }
            }

            @Override
            public void onFlushCompleted() {
                Log.i(APP_TAG, " onFlushCompleted called");
            }

            @Override
            public void onError(HealthTracker.TrackerError trackerError) {
                Log.e(APP_TAG, " onError called: " + trackerError);
                setHandlerRunning(false);
                if (trackerError == HealthTracker.TrackerError.PERMISSION_ERROR) {
                    TrackerDataNotifier.getInstance().notifyError(R.string.NoPermission);
                }
                if (trackerError == HealthTracker.TrackerError.SDK_POLICY_ERROR) {
                    TrackerDataNotifier.getInstance().notifyError(R.string.SdkPolicyError);
                }
            }
        };
        setTrackerEventListener(trackerEventListener);
    }

    public void updateSpo2(DataPoint dataPoint) {
        final int status = dataPoint.getValue(ValueKey.SpO2Set.STATUS);
        int spo2Value = 0;
        if (status == SpO2Status.MEASUREMENT_COMPLETED)
            spo2Value = dataPoint.getValue(ValueKey.SpO2Set.SPO2);
        TrackerDataNotifier.getInstance().notifySpO2TrackerObservers(status, spo2Value);
        Log.d(APP_TAG, dataPoint.toString());
    }

}
