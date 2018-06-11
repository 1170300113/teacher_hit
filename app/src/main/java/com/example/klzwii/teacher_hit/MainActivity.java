package com.example.klzwii.teacher_hit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String TAG ="perimission_request";
    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    // 声明一个集合，在后面的代码中用来存储用户拒绝授权的权
    List<String> mPermissionList = new ArrayList<>();
    private ZLoadingDialog dialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了

        } else {//请求权限方法
            String[] permissions = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
            ActivityCompat.requestPermissions(MainActivity.this, permissions, 2);
        }
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);
            }
        }
        if(!mPermissionList.isEmpty()){
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        }
        /*Intent intent = new Intent(MainActivity.this,clas_main.class);
        MainActivity.this.startActivity(intent);
        MainActivity.this.finish();*/
        Button button = findViewById(R.id.button3);
        button.setOnClickListener(click);
    }

    private View.OnClickListener click = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialog=new ZLoadingDialog(MainActivity.this);
            dialog.setLoadingBuilder(Z_TYPE.SNAKE_CIRCLE).setLoadingColor(Color.BLACK).setHintText("Loading...").setHintTextSize(16);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            String a=((EditText)findViewById(R.id.lous)).getText().toString();
            String b=((EditText)findViewById(R.id.editText4)).getText().toString();
            data.ins_data(a,b);
            Thread mthread = new log_thread(MainActivity.this);
            mthread.start();
        }
    };
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(dialog!=null)
        dialog.dismiss();
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(message_event message){
        if(message.getMessage().equals("stop"))
            dialog.cancel();
        if(message.getMessage().equals("finish"))
            MainActivity.this.finish();
    }
}
