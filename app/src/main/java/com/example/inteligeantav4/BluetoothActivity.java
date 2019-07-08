package com.example.inteligeantav4;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

public class BluetoothActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    private static final String TAG = "MainActivity";
    private View mainContent;
    public Button BtConnect;
    public TextView BtState, WeightRead;
    private int colorButton;
    Button buttonChoseColor;

    public static boolean connect = false;
    private BluetoothConnect bluetooth = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        buttonChoseColor = findViewById(R.id.buttonChooseColor);
        mainContent = findViewById(R.id.contentMain);
        BtConnect = findViewById(R.id.connect_btn);
        BtState = findViewById(R.id.status);
        WeightRead = findViewById(R.id.sensorWeight);
        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainContent.setVisibility(View.VISIBLE);
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
            }
        });

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;
        int id = item.getItemId();
        if (id == R.id.personal_information) {
            mainContent.setVisibility(View.GONE);
            fragment = new userInformationFragment();
        } else if (id == R.id.open_bag) {
            if(connect){
                mainContent.setVisibility(View.GONE);
                fragment = new controlFragment();}
            else
            {
                Toast.makeText(BluetoothActivity.this, "Conectati Bluetooth", Toast.LENGTH_SHORT).show();
            }
        }
        else if (id == R.id.about_section) {
            mainContent.setVisibility(View.GONE);
            fragment = new aboutInformationFragment();
        }
        else if (id == R.id.location_option) {//It should be implemented in the future of the project
            Toast.makeText(BluetoothActivity.this, "Opţiunea urmează a fi implementată", Toast.LENGTH_SHORT).show();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.fragment_container, fragment);
            ft.commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onConnectPressed() {
        BtState.setText("connecting");
        final String macAddress = "20:18:08:23:51:75"; // the Mac address of the HC-05
        connect = bluetooth.connect(macAddress);
        if (connect) {
            BtState.setText("Conectat");
        } else {
            BtState.setText("Conexiune esuata");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(BtConnect)) {
            onConnectPressed();
        }
    }
       public void chooseColor() {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Alegeti o culoare")
                .initialColor(colorButton)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)// show as a Flower
                .density(12)                                   // diameter of the circles
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    // called when the color circle has been selected
                    @Override
                    public void onColorSelected(int selectedColor) {
                        Toast.makeText(BluetoothActivity.this, "Ati ales culoarea: 0x" + Integer.toHexString(selectedColor), Toast.LENGTH_SHORT).show();
                    }
                })
                // color selected
                .setPositiveButton("OK", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface d, int lastSelectedColor, Integer[] allColors) {
                        if (selectedColorListener != null)
                            selectedColorListener.onSelectColor(lastSelectedColor);

                    }
                })
                // Close the dialog fragment
                .setNegativeButton("Anulare", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                })
                .build()
                .show();
    }//Read weight measurements from Arduino, error at reading String
    //Should show data in controlFragment
   /* public String ShowWeight(BluetoothConnect bluetooth){
        String data = bluetooth.
        return data;
    }*/

    private OnSelectedColorListener selectedColorListener;
    private OnSlectedWeightMeasurements slectedWeightMeasurements;

    public void setOnSelectedColorListener(OnSelectedColorListener listener) {
        selectedColorListener = listener;
    }

    public interface OnSelectedColorListener {
        void onSelectColor(int color);
    }
    public interface OnSlectedWeightMeasurements{
        void onMeausredWeight();
    }


}

