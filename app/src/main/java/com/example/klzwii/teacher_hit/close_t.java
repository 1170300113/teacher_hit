package com.example.klzwii.teacher_hit;

import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

public class close_t extends Thread{
    private Context mcontext;
    private  String class_id;
    public close_t(Context mcontext,String class_id){
        this.mcontext=mcontext;
        this.class_id=class_id;
    }

    @Override
    public void run() {
        super.run();
        EventBus.getDefault().register(this);
        JSONObject json =clos_clas.log(class_id);
        try {
            if(json.getString("status").equals("1")){
                Intent intent=new Intent(mcontext,cls_select.class);
                mcontext.startActivity(intent);
                EventBus.getDefault().post(new message_event("finish"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void Event(message_event message){

    }
}
