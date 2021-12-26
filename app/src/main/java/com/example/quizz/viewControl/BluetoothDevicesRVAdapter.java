package com.example.quizz.viewControl;

import android.app.AlertDialog;
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
import com.example.quizz.network.BluetoothManager;
import com.example.quizz.exceptions.BluetoothException;
import java.util.List;

public class BluetoothDevicesRVAdapter extends RecyclerView.Adapter<BluetoothDevicesRVAdapter.BluetoothDeviceViewHolder>{

    Context c;
    List<BluetoothDevice> mDevices;
    LayoutInflater inflater;
    BluetoothManager btManager;

    public BluetoothDevicesRVAdapter(Context c, List<BluetoothDevice> mDevices, BluetoothManager btManager){
        this.c = c;
        this.mDevices = mDevices;
        this.inflater = LayoutInflater.from(c);
        this.btManager = btManager;
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

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //is mDevice setalready at this point?
                try {
                    btManager.pair(mDevices.get(holder.getAdapterPosition()));
                } catch (BluetoothException e) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(c);
                    builder.setTitle("Error");
                    builder.setMessage(e.getMessage());
                    builder.setPositiveButton("Okay", (dialog, id) -> {
                    });
                    builder.show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mDevices != null) return mDevices.size();
        return 0;
    }

    public class BluetoothDeviceViewHolder extends RecyclerView.ViewHolder {
        ImageView icons;    //todo -> icon?
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