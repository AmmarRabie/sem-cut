package com.products.ammar.sem_cut;

import com.google.android.gms.maps.model.LatLng;

public interface IAppRealTime {

    void setCallbacks(OnDataChange callbacks);
    void setRpm(int value);
    void setSpeed(int value);
    void setLocation(LatLng location);
    void setStatus(boolean status);

    interface OnDataChange{
        void onRpmChange(int newValue);
        void onSpeedChange(int newValue);
        void onLocationChange(LatLng location);
        void onStatusChange(boolean isRunning);
    }
}
