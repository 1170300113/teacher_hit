package com.example.klzwii.teacher_hit;

public  class data {
    private static String user_name;
    private static  String pass_wd;
    public static void ins_data(String a,String b){
        user_name=a;
        pass_wd=b;
    }
    public static String getUser_name(){
        return user_name;
    }
    public static  String getPass_wd(){
        return pass_wd;
    }
}
