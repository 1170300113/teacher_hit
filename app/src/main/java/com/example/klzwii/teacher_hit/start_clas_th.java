package com.example.klzwii.teacher_hit;

import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

public class start_clas_th extends Thread {
    private Context mcontext=null;
    private double lat,lot;
    private String clas_id,pass_wd;
    public start_clas_th(Context mcontext,double lat,double lot,String clas_id,String pass_wd){
        this.mcontext=mcontext;
        this.lat=lat;
        this.lot=lot;
        this.clas_id=clas_id;
        this.pass_wd=pass_wd;
    }
    @Override
    public void run() {
        super.run();
        EventBus.getDefault().register(this);
        JSONObject json =start_clas.start_cla(clas_id,pass_wd,lat,lot);
        try {
            if(json.getString("status").equals("1")){
                Intent intent = new Intent(mcontext,clas_main.class);
                intent.putExtra("class_id",clas_id);
                intent.putExtra("times",json.getString("times"));
                mcontext.startActivity(intent);
                EventBus.getDefault().post(new message_event("finish"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        EventBus.getDefault().post(new message_event("stop"));
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void Event(message_event message){

    }
}
