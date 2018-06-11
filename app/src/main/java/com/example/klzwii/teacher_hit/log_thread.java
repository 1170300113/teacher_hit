package com.example.klzwii.teacher_hit;

import android.content.Context;
import android.content.Intent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

public class log_thread extends Thread{
    private Context mcontext;
    public log_thread(Context mcontext){
        this.mcontext=mcontext;
    }
    @Override
    public void run() {
        super.run();
        EventBus.getDefault().register(this);
        JSONObject json =log_t.log();
        try {
            if(json.getString("status").equals("1")){
                JSONObject jsons = is_inclas.get_cla();
                if(jsons.getString("status").equals("1")){
                    Intent intent=new Intent(mcontext,clas_main.class);
                    System.out.print(jsons.toString());
                    intent.putExtra("class_id",jsons.getString("clas_id"));
                    intent.putExtra("times",jsons.getString("times"));
                    mcontext.startActivity(intent);
                    EventBus.getDefault().post(new message_event("finish"));
                }
                else{
                    Intent intent = new Intent(mcontext,cls_select.class);
                    mcontext.startActivity(intent);
                }
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
