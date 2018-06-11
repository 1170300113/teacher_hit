package com.example.klzwii.teacher_hit;

import org.greenrobot.eventbus.EventBus;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

public class get_my_clas extends Thread{
    public boolean ju =true;
    @Override
    public void run(){
        EventBus.getDefault().register(this);
        while(ju){
            JSONObject jsont = get_clas.get_cla();
            message_event msg = new message_event("change");
            msg.set_json(jsont);
            EventBus.getDefault().post(msg);
            try {
                Thread.sleep(5000);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        EventBus.getDefault().unregister(this);
    }
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void Event(message_event Message){
        if(Message.getMessage().equals("stop")){
            ju=false;
        }
    }

}
