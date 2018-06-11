package com.example.klzwii.teacher_hit;

public class send_t extends Thread{
    private String ans;
    private String clas_id;
    public send_t(String  ans,String clas_id){
        this.ans=ans;
        this.clas_id=clas_id;
    }

    @Override
    public void run() {
        super.run();
        send_message.get_cla(clas_id,ans);
    }
}
