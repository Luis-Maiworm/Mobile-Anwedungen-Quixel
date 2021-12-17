package com.example.quizz.viewControl;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quizz.R;

import java.util.List;

public class BluetoothDevicesRVAdapter extends RecyclerView.Adapter<BluetoothDevicesRVAdapter.BluetoothDeviceViewHolder>{

    Context c;
    List<BluetoothDevice> mDevices;
    LayoutInflater inflater;


    public BluetoothDevicesRVAdapter(Context c, List<BluetoothDevice> mDevices){
        this.c = c;
        this.mDevices = mDevices;
        this.inflater = LayoutInflater.from(c);
    }



    @NonNull
    @Override
    public BluetoothDeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.rv_bluetooth_device_grid_layout, parent, false);


        return new BluetoothDeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BluetoothDeviceViewHolder holder, int position) {
        holder.names.setText(mDevices.get(position).getName());
        holder.adresses.setText(mDevices.get(position).getAddress());


    }

    @Override
    public int getItemCount() {
        if(mDevices != null) return mDevices.size();
        return 0;
    }



    public class BluetoothDeviceViewHolder extends RecyclerView.ViewHolder {

        ImageView icons;
        ConstraintLayout mainLayout;

        TextView adresses;
        TextView names;


        public BluetoothDeviceViewHolder(@NonNull View itemView) {
            super(itemView);

            adresses = itemView.findViewById(R.id.btAdress);
            names = itemView.findViewById(R.id.btName);

         //   icons = itemView.findViewById(R.id.profileIcon);
            mainLayout = itemView.findViewById(R.id.btDeviceLayout);
        }
    }
}
