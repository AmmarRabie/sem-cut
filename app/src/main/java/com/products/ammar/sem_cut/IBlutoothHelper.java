package com.products.ammar.sem_cut;

public interface IBlutoothHelper {
    void send(byte[] message);
    void setOnReceiveData(OnReceiveDataListener callback);


    interface OnReceiveDataListener{
        void receive(byte[] data);
    }
}
