package com.example.klzwii.teacher_hit;

import android.app.usage.UsageEvents;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

public class referesh_student_th extends Thread{
    private String class_id;
    referesh_student_th(String class_id){
        this.class_id=class_id;
    }
    private boolean stopp=true;
    @Override
    public void run() {
        super.run();
        EventBus.getDefault().register(this);
        while (stopp){
            JSONObject json = refresh_student.log(class_id);
            message_event messageEvent = new message_event("change");
            messageEvent.set_json(json);
            EventBus.getDefault().post(messageEvent);
            try{
                Thread.sleep(10000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void Event(message_event messageEvent){
        if(messageEvent.getMessage().equals("stop")){
            stopp=false;
        }
    }
}
