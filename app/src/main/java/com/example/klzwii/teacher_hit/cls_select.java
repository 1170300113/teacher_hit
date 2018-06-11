package com.example.klzwii.teacher_hit;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class cls_select extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private ListView listView;
    private List<Map<String, String>> list;
    JSONObject json =new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cls_select);
        EventBus.getDefault().register(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        listView=findViewById(R.id.clas_lis);
        list=new ArrayList<Map<String,String>>();
        listView.setAdapter(new list_array(cls_select.this, list));
        listView.setOnItemClickListener(this);
        Thread mthread = new get_my_clas();
        mthread.start();
    }
    @Override
    protected void onPostCreate(Bundle savedInstance){
        super.onPostCreate(savedInstance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        TextView asd = findViewById(R.id.titletext);
        asd.setText("我的班级");
    }
    public void getData(){
        list=new ArrayList<Map<String,String>>();
        SQLiteOpenHelper dbHelper = new DatabaseHelper(cls_select.this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("clas",null,null,null,null,null,null);
        cursor.moveToFirst();
        Map<String,String>map= new HashMap<>();

        while(!cursor.isAfterLast()){
            map= new HashMap<>();
            String a=cursor.getString(1);
            map.put("info",a);
            list.add(map);
            cursor.moveToNext();
        }
        cursor.close();
        db.close();
        dbHelper.close();
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(message_event messageEvent){
        if(messageEvent.getMessage().equals("change")){
            if(messageEvent.getJson()!=json&&messageEvent.getJson()!=null){
                Log.i("asdad","sthhappen");
                json = messageEvent.getJson();
                try{
                    String tots = json.getString("tot");
                    int tot = Integer.parseInt(tots);
                    SQLiteOpenHelper dbHelper = new DatabaseHelper(cls_select.this);
                    SQLiteDatabase db = dbHelper.getReadableDatabase();
                    db.execSQL("delete from clas");
                    for(int i=1;i<=tot;i++){
                        String a = json.getString("clasn"+i);
                        String bs = json.getString("clas"+i);
                        int b = Integer.parseInt(bs);
                        db.execSQL("insert into clas (id,name) values ('"+b+"','"+a+"')");
                    }
                    db.close();
                    dbHelper.close();
                }catch (JSONException E){
                    E.printStackTrace();
                }
                getData();
                listView.setAdapter(new list_array(cls_select.this, list));
                listView.setOnItemClickListener(this);
            }

        }
    }
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().post(new message_event("stop"));
        EventBus.getDefault().unregister(this);
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String asd=((TextView)view.findViewById(R.id.info)).getText().toString();
        try{
            SQLiteOpenHelper dbhelper = new DatabaseHelper(cls_select.this);
            SQLiteDatabase db=dbhelper.getWritableDatabase();
            Cursor cursor =db.rawQuery("select id from clas where name = '"+asd+"'",null);
            cursor.moveToFirst();
            int clas_id=cursor.getInt(0);
            cursor.close();
            db.close();
            dbhelper.close();
            Intent intent=new Intent(cls_select.this,clas_begin_prep.class);
            intent.putExtra("class_id",clas_id+"");
            cls_select.this.startActivity(intent);
            cls_select.this.finish();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
