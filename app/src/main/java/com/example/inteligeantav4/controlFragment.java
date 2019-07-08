package com.example.inteligeantav4;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class controlFragment extends Fragment {
    Button buttonChoseColor;
    TextView WeightRead;
    ListView ListTags;
    private int colorButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        BluetoothActivity activity =(BluetoothActivity) getActivity();
        View view = inflater.inflate(R.layout.fragment_control, container, false);
        buttonChoseColor = (Button)view.findViewById(R.id.buttonChooseColor);
        WeightRead =(TextView)view.findViewById(R.id.sensorWeight);
        ListTags = (ListView)view.findViewById(R.id.rfidListItems) ;

        buttonChoseColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothActivity activity = (BluetoothActivity) getActivity();
                activity.chooseColor();
                activity.setOnSelectedColorListener(new BluetoothActivity.OnSelectedColorListener() {
                    @Override
                    public void onSelectColor(int color) {
                        Toast.makeText(getActivity(), "Color #" + color, Toast.LENGTH_SHORT).show();
                        int red = Color.red(color);
                        int green = Color.green(color);
                        int blue = Color.blue(color);

                        String message = String.format("color_led:%s;%s;%s", red, green, blue);
                        BluetoothConnect.send(message);
                        //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

                    }
                });

                }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

}

