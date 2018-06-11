package com.example.klzwii.teacher_hit;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class clas_main extends AppCompatActivity {
    View getlistview;
    String[] mlistText = { "全选", "A", "B", "C", "D", "E", "F", "G" };
    ArrayList<Map<String, Object>> mData = new ArrayList<Map<String, Object>>();
    AlertDialog.Builder builder;
    AlertDialog builder2;
    SimpleAdapter adapter;
    Boolean[] bl = { false, false, false, false, false, false, false, false };
    private ListView listView;
    private List<Map<String, String>> list;
    JSONObject json =new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clas_main);
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.fabb);
        int lengh = mlistText.length;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        for (int i = 0; i < lengh; i++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("text", mlistText[i]);
            mData.add(item);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateDialog();// 点击创建Dialog
            }
        });
        FloatingActionButton button1 = findViewById(R.id.fabbb);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread mthread = new close_t(clas_main.this,getIntent().getExtras().getString("class_id"));
                mthread.start();
            }
        });
        listView=findViewById(R.id.clas_listt);
        list=new ArrayList<Map<String,String>>();
        listView.setAdapter(new list_array(clas_main.this, list));
        Thread mthread = new referesh_student_th(getIntent().getStringExtra("class_id"));
        mthread.start();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        toolbar.setTitle("第"+getIntent().getStringExtra("times")+"次课程");
    }

    class ItemOnClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
            CheckBox cBox = (CheckBox) view.findViewById(R.id.X_checkbox);
            if (cBox.isChecked()) {
                cBox.setChecked(false);
            } else {
                Log.i("TAG", "取消该选项");
                cBox.setChecked(true);
            }

            if (position == 0 && (cBox.isChecked())) {
                //如果是选中 全选  就把所有的都选上 然后更新
                for (int i = 0; i < bl.length; i++) {
                    bl[i] = true;
                }
                adapter.notifyDataSetChanged();
            } else if (position == 0 && (!cBox.isChecked())) {
                //如果是取消全选 就把所有的都取消 然后更新
                for (int i = 0; i < bl.length; i++) {
                    bl[i] = false;
                }
                adapter.notifyDataSetChanged();
            }
            if (position != 0 && (!cBox.isChecked())) {
                // 如果把其它的选项取消   把全选取消
                bl[0] = false;
                bl[position]=false;
                adapter.notifyDataSetChanged();
            } else if (position != 0 && (cBox.isChecked())) {
                //如果选择其它的选项，看是否全部选择
                //先把该选项选中 设置为true
                bl[position]=true;
                int a = 0;
                for (int i = 1; i < bl.length; i++) {
                    if (!bl[i]) {
                        //如果有一个没选中  就不是全选 直接跳出循环
                        break;
                    } else {
                        //计算有多少个选中的
                        a++;
                        if (a == bl.length - 1) {
                            //如果选项都选中，就把全选 选中，然后更新
                            bl[0] = true;
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(message_event messageEvent){
        if(messageEvent.getMessage().equals("change")){
            if(messageEvent.getJson()!=json&&messageEvent.getJson()!=null){
                list=new ArrayList<Map<String,String>>();
                Log.i("asdad","sthhappen");
                json = messageEvent.getJson();
                try{
                    String tots = json.getString("tot");
                    int tot = Integer.parseInt(tots);
                    for(int i=0;i<tot;i++){
                        Map<String,String>map= new HashMap<>();
                        String a = json.getString("hit_num"+i);
                        map.put("info",a);
                        list.add(map);
                        }
                }catch (JSONException E){
                    E.printStackTrace();
                }
                listView.setAdapter(new list_array(clas_main.this, list));
            }

        }
        if(messageEvent.getMessage().equals("finish")){
            clas_main.this.finish();
        }
    }
    protected void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().post(new message_event("stop"));
        EventBus.getDefault().unregister(this);
    }
    public void CreateDialog() {

        // 动态加载一个listview的布局文件进来
        LayoutInflater inflater = LayoutInflater.from(clas_main.this);
        getlistview = inflater.inflate(R.layout.list_view_main, null);

        // 给ListView绑定内容
        ListView listview = (ListView) getlistview.findViewById(R.id.X_listview);
        adapter = new SetSimpleAdapter(clas_main.this, mData, R.layout.activity_clas_main, new String[] { "text" },
                new int[] { R.id.X_item_text });
        // 给listview加入适配器
        listview.setAdapter(adapter);
        listview.setItemsCanFocus(false);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listview.setOnItemClickListener(new ItemOnClick());

        builder = new AlertDialog.Builder(this);
        builder.setTitle("请选择正确选项");
        builder.setIcon(R.drawable.ic_launcher_background);
        //设置加载的listview
        builder.setView(getlistview);
        builder.setPositiveButton("发送", new DialogOnClick());
        builder.setNegativeButton("取消", new DialogOnClick());
        builder.create().show();
    }

    class DialogOnClick implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case Dialog.BUTTON_POSITIVE:
                    int k=0;
                    for(int i=1;i<bl.length;i++){
                        k<<=1;
                        if(bl[i])
                            k+=1;
                    }
                    String clas_id=getIntent().getExtras().getString("class_id");
                    Thread a = new send_t(k+"",clas_id);
                    a.start();
                    break;
                case Dialog.BUTTON_NEGATIVE:
                    //取消按钮的事件
                    break;
                default:
                    break;
            }
        }
    }

    //重写simpleadapterd的getview方法
    class SetSimpleAdapter extends SimpleAdapter {

        public SetSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from,
                                int[] to) {
            super(context, data, resource, from, to);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LinearLayout.inflate(getBaseContext(), R.layout.x_checkbox, null);
            }
            CheckBox ckBox = (CheckBox) convertView.findViewById(R.id.X_checkbox);
            //每次都根据 bl[]来更新checkbox
            if (bl[position]) {
                ckBox.setChecked(true);
            } else if (!bl[position]) {
                ckBox.setChecked(false);
            }
            return super.getView(position, convertView, parent);
        }
    }
}