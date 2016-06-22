package com.example.gwon.javachip.Activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gwon.javachip.Javafile.MakeJsonObject;
import com.example.gwon.javachip.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class QuestionWriteActivity extends AppCompatActivity {


    Button bt_back, bt_ask;
    EditText et_questionContent, et_questionTitle;
    SharedPreferences spf_security;

    String uid, title, qinfo;

    final String URL = "http://168.131.152.172:8080/SoftWareProject/insertquestion.jsp";
    final String URL_GET = "http://168.131.152.172:8080/SoftWareProject/answer.jsp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_write);

        bt_ask = (Button) findViewById(R.id.bt_question_ask);
        bt_back = (Button) findViewById(R.id.bt_question_back);

        et_questionContent = (EditText) findViewById(R.id.et_question_write);
        et_questionTitle = (EditText) findViewById(R.id.et_questionwrite_title);

        spf_security = getSharedPreferences("login", MODE_PRIVATE);
        uid = spf_security.getString("loginId", null);

        bt_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title = et_questionTitle.getText().toString();
                qinfo = et_questionContent.getText().toString();

                new PutAnswer().execute(URL);
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();

            }
        });


    }

    public class PutAnswer extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> data = new HashMap<String, String>();

            data.put("id", uid);
            data.put("qtitle", title);
            data.put("qinfo", qinfo);

            JSONObject json = new MakeJsonObject(params[0], data).makehttpUrlConnection();


            int result = -1;

            try {
                result = json.getInt("result");
                Log.i("result", result + "");
            } catch (JSONException e) {
                e.printStackTrace();
                return "실패";
            }

            if (result == 0) {
                return "성공";
            } else {
                return "실패";
            }
        }


        /**
         * 로그인 성공 여부를 확인하여 다음 동작을 하는 메소드
         *
         * @param result
         */

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("성공")) {
                setResult(RESULT_OK);
                finish();

            } else {

            }

        }


    }


}
