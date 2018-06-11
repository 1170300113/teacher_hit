package com.example.klzwii.teacher_hit;

import org.json.JSONObject;

public class message_event {
    private String message;
    private JSONObject json;
    public message_event(String meessage){
        this.message=meessage;
    }
    public void set_json(JSONObject json){
        this.json=json;
    }
    public String getMessage() {
        return message;
    }

    public JSONObject getJson() {
        return json;
    }
}
