package com.example.watchapp.presentation;

public interface TrackerDataObserver {
    void onHeartRateTrackerDataChanged(HeartRateData hrData);

    void onSpO2TrackerDataChanged(int status, int spO2Value);

    void onError(int errorResourceId);
}
