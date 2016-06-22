package com.example.gwon.javachip.Activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gwon.javachip.Javafile.MakeJsonObject;
import com.example.gwon.javachip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 문제푸는 엑티비티 클래스
 */
public class ProblemActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    TextView tv_content;
    RadioGroup rg_numgrp;

    String select_answer, cid;
    String[] answer = new String[5];

    RadioButton rb_num1, rb_num2, rb_num3, rb_num4;
    boolean bool = false;

    String id;
    SharedPreferences sf_id;

    Button bt_select, bt_cancle;
    final String URL = "http://168.131.152.172:8080/SoftWareProject/problem.jsp";
    final String URL_CHECK = "http://168.131.152.172:8080/SoftWareProject/solveproblem.jsp";

    /**
     * 문제푸는 화면을 구성하는 메소드
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problem);

        sf_id = getSharedPreferences("login", MODE_PRIVATE);

        id = sf_id.getString("loginId", null);
        cid = getIntent().getStringExtra("cid");

        tv_content = (TextView) findViewById(R.id.tv_problemcontent);
        rg_numgrp = (RadioGroup) findViewById(R.id.rg_numgrp);

        rb_num1 = (RadioButton) findViewById(R.id.rb_num1);
        rb_num2 = (RadioButton) findViewById(R.id.rb_num2);
        rb_num3 = (RadioButton) findViewById(R.id.rv_num3);
        rb_num4 = (RadioButton) findViewById(R.id.rv_num4);
        bt_cancle = (Button) findViewById(R.id.bt_problem_cancle);
        bt_select = (Button) findViewById(R.id.bt_select);

        rg_numgrp.setOnCheckedChangeListener(this);

        /**
         * 문제풀기를 취소하고 개념과 동영상을 볼 수 있는 창으로 넘어가는 메소드
         */
        bt_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 답을  확인하는 메소드
         * 해당 단원의 문제가 없을 시 해당단원의 문제는 없습니다 라는 토스트바를 띄워줌
         */
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bool) {
                    new Checkanswer().execute(URL_CHECK);
                } else {
                    Toast.makeText(getApplicationContext(), "해당 단원은 문제가 없습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        new GetAnswer().execute(URL);

    }

    /**
     * 자신이 생각한 답을 정하는 메소드
     *
     * @param group     각 선택지에 대한 id값
     * @param checkedId 체크된 라디오 버튼 값
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rb_num1:
                select_answer = rb_num1.getText().toString();
                break;
            case R.id.rb_num2:
                select_answer = rb_num2.getText().toString();
                break;
            case R.id.rv_num3:
                select_answer = rb_num3.getText().toString();
                break;
            case R.id.rv_num4:
                select_answer = rb_num4.getText().toString();
                break;
        }
    }

    /**
     * 해당 단원의 문제 내용과 선택지를 가져오는 메소드
     */
    public class GetAnswer extends AsyncTask<String, String, String[]> {

        /**
         * 자신이 선택한 값과 정답을 비교하는 메소드
         *
         * @param params 값을 비교할 서버 주소
         * @return 정답 또는 오답
         */
        @Override
        protected String[] doInBackground(String... params) {

            HashMap<String, String> data = new HashMap<String, String>();

            JSONObject json = new MakeJsonObject(params[0], data).makehttpUrlConnection();

            String[] answer_array = new String[5];

            try {
                JSONArray result = json.getJSONArray("result");
                JSONObject answer_json = result.getJSONObject(0);


                if (answer_json.getString("pinfo").equals("not")) {
                    answer_array[0] = "해당 단원은 문제가 존재 하지 않습니다.";
                    answer_array[1] = "";
                    answer_array[2] = "";
                    answer_array[3] = "";
                    answer_array[4] = "";


                    return answer_array;
                } else {
                    bool = true;
                    answer_array[0] = answer_json.getString("pinfo");
                    answer_array[1] = answer_json.getString("ch1");
                    answer_array[2] = answer_json.getString("ch2");
                    answer_array[3] = answer_json.getString("ch3");
                    answer_array[4] = answer_json.getString("ch4");

                    Log.i("answerarray", answer_array[0] + answer_array[1] + answer_array[2] + answer_array[3] + answer_array[4]);
                    return answer_array;
                }


            } catch (JSONException e) {
                e.printStackTrace();
                return answer_array;

            }


        }

        /**
         * 해당 선탑지를 4지선다형으로 바꾸는 메소드
         *
         * @param result
         */
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);


            tv_content.setText(result[0]);
            rb_num1.setText(result[1]);
            rb_num2.setText(result[2]);
            rb_num3.setText(result[3]);
            rb_num4.setText(result[4]);


        }
    }

    /**
     * 자신이 선택한 답과 정답값을 비교하는 클래스
     */

    public class Checkanswer extends AsyncTask<String, String, String> {


        /**
         * 서버로 정답을 보내고 그에 답을 비교하여 결과를 리턴해주는 메소드
         *
         * @param params URL주소값
         * @return 성공 실패 여부
         */
        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<String, String>();
            data.put("cid", cid);
            Log.i("cidcidcida", select_answer);
            data.put("id", id);
            data.put("check", select_answer);

            JSONObject json = new MakeJsonObject(params[0], data).makehttpUrlConnection();

            int result = -1;

            try {
                result = json.getInt("result");

            } catch (JSONException e) {
                e.printStackTrace();
                return "에러";
            }

            if (result == 1) {
                return "실패";
            } else if (result == 0) {
                return "성공";
            } else {
                return "이미";
            }


        }

        /**
         * 정답여부를 확인하고 그에 대한 결과를 실행 하는 메소드
         * 정답일경우 "정답입니다. "토스트바 띄우기
         * "오답일 경우 "정답입니다. "토스트바 띄우기
         * 이전에 푼 문제일 경우 "이전에 푼 문제 입니다. " 출력
         *
         * @param result 성공여부
         */
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("성공")) {
                Toast.makeText(getApplicationContext(), "정답입니다.", Toast.LENGTH_SHORT).show();

            } else if (result.equals("이미")) {
                Toast.makeText(getApplicationContext(), "이전에 푼 문제 입니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "틀렸습니다..", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
