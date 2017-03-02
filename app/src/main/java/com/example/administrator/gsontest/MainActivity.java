package com.example.administrator.gsontest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final int MY_CALL_PHONE = 1001;
    private EditText mEditText;
    private TextView mTextView;
    private Button btnPhone, btnJson1, btnJson2, btnGson;
    private String num;
    private String jsonStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEditText = (EditText) findViewById(R.id.edt_text);
        mTextView= (TextView) findViewById(R.id.tv_txt);
        btnGson = (Button) findViewById(R.id.btn_gson);
        btnJson1 = (Button) findViewById(R.id.btn_json1);
        btnJson2 = (Button) findViewById(R.id.btn_json2);
        btnPhone = (Button) findViewById(R.id.btn_phone);

        initCall();


        //JSON组装json
        initPackageJson();

        //JSON解析json
        initResolveJson();

        //Gson解析json
        initGsonResolve();

    }

    private void initGsonResolve() {
        btnGson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson=new Gson();
                String jsonStr = "{\"id\":227,\"name\":\"zhang\",\"age\":20}";
                Student s=gson.fromJson(jsonStr,Student.class);
                mTextView.setText(s.getId()+"  "+s.getName()+"  "+s.getAge());
            }
        });


    }

    private void initResolveJson() {
        btnJson2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                        JSONObject obj = new JSONObject(jsonStr);
                        JSONArray array=obj.getJSONArray("stuList");
                        ArrayList<Student> stuList=new ArrayList<Student>();
                        for(int i=0;i<array.length();i++){
                            JSONObject stuobj=array.getJSONObject(i);
                            int id=stuobj.getInt("id");
                            String name=stuobj.getString("name");
                            int age=stuobj.getInt("age");
                            mTextView.setText(id+"  "+name + "   "+age +"\n");
                            Student s=new Student(id,name,age);
                            stuList.add(s);
                        }

                } catch (JSONException e){
                    Log.i("MainActivity", e.toString());
                }
            }
        });




    }


    private void initPackageJson() {
        Student s1 = new Student(226, "zou", 22);
        Student s2 = new Student(227, "zhang", 20);
        Student s3 = new Student(228, "huang", 21);
        final Student[] stu = {s1, s2, s3};

        btnJson1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONArray array = new JSONArray();
                for (int i = 0; i < stu.length; i++) {
                    JSONObject obj = getStudentJson(stu[i]);
                    array.put(obj);
                }
                JSONObject json=new JSONObject();
                try{
                    json.put("stuList",array);
                    jsonStr=json.toString();
                    mTextView.setText(jsonStr);
                } catch (JSONException e) {
                    Log.i("MainActivity", e.toString());
                }

            }
        });


    }

    private JSONObject getStudentJson(Student stu) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", stu.getId());
            obj.put("name", stu.getName());
            obj.put("age", stu.getAge());
        } catch (JSONException e) {
            Log.i("MainActivity", e.toString());
        }
        return obj;
    }


    private void initCall() {
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                num = mEditText.getText().toString().trim();

                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE}, MY_CALL_PHONE);
                } else {
                    CallPhone(num);
                }
            }
        });
    }


    private void CallPhone(String num) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + num));
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                CallPhone(num);
            } else {
                Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }



}
