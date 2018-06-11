package com.example.klzwii.teacher_hit;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class clas_begin_prep extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_clas_begin_prep);
        Button okButton = findViewById(R.id.button);
        okButton.setOnClickListener(click);
    }
    private ZLoadingDialog dialog = null;
    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog=new ZLoadingDialog(clas_begin_prep.this);
            dialog.setLoadingBuilder(Z_TYPE.SNAKE_CIRCLE).setLoadingColor(Color.BLACK).setHintText("Loading...").setHintTextSize(16);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            EditText editText = findViewById(R.id.editText);
            String pass_wd = editText.getText().toString();
            Location location = null;
            if (clas_begin_prep.this.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == PackageManager.PERMISSION_GRANTED) {// 获取的是位置服务
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                String Gproviders = LocationManager.GPS_PROVIDER;
                String Nproviders = LocationManager.NETWORK_PROVIDER;
                if (locationManager != null) {
                    LocationProvider Gprovider = locationManager.getProvider(Gproviders);
                    LocationProvider Nprovider = locationManager.getProvider(Nproviders);
                    if (Gprovider != null) {
                        location = locationManager.getLastKnownLocation(Gproviders);
                    }
                    if (location == null)
                        location = locationManager.getLastKnownLocation(Nproviders);
                }
            }
            if(location!=null){
                Thread mthread =new start_clas_th(clas_begin_prep.this,location.getLatitude(),location.getLongitude(),getIntent().getExtras().getString("class_id")+"",pass_wd);
                mthread.start();
            }
            else
            dialog.cancel();
        }
    };
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(message_event messageEvent){
        if(messageEvent.getMessage().equals("stop")&&dialog!=null)
            dialog.cancel();
        if(messageEvent.getMessage().equals("finish"))
            clas_begin_prep.this.finish();
    }
    protected void onDestroy(){
        super.onDestroy();
        if(dialog!=null)
            dialog.dismiss();
        EventBus.getDefault().unregister(this);
    }
}
