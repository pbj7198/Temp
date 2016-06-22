package com.example.gwon.javachip.Activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.gwon.javachip.Javafile.MakeJsonObject;
import com.example.gwon.javachip.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 *회원 가입하는 클래스 엑티비티
 */
public class JoinActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    EditText et_Id, et_Pw, et_CheckPw, et_Email,et_Name;
    Button bt_Ok, bt_Cancle;
    RadioGroup Rg_gender;
    String id, pw, checkPw, email, name, gender;

    public HashMap<String, String> data;
    Context context;

    final String URL = "http://168.131.152.172:8080/SoftWareProject/join.jsp";

    @Override

    /**
     * 클래스 생성시 뷰를 생성하는 자동호출 메소드
     */
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        context = this;

        et_Id = (EditText) findViewById(R.id.et_id);
        et_Pw = (EditText) findViewById(R.id.et_pw);
        et_CheckPw = (EditText) findViewById(R.id.et_cpw);
        et_Email = (EditText) findViewById(R.id.et_email);
        et_Name = (EditText)findViewById(R.id.et_name);

        bt_Ok = (Button) findViewById(R.id.bt_ok);
        bt_Cancle = (Button) findViewById(R.id.bt_cancle);

        Rg_gender = (RadioGroup) findViewById(R.id.rg_jender);

        /**
         * 회원가입정보를 입력하고 회원 가입을 하는 메소드
         * 회원 정보가 입력 되어 있지 않거나 아이디가 중복되면은 회원가입이 불가능 하다.
         */
        bt_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = et_Id.getText().toString();
                pw = et_Pw.getText().toString();
                email = et_Email.getText().toString();
                checkPw = et_CheckPw.getText().toString();
                name = et_Name.getText().toString();

                if (id.equals("")) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (pw.equals("")) {
                    Toast.makeText(getApplicationContext(), "비밀번호 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (email.equals("")) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력하세요", Toast.LENGTH_SHORT).show();
                } else if (gender.equals("null")) {
                    Toast.makeText(getApplicationContext(), "성별을 선택하세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (!pw.equals(checkPw)) {
                        Toast.makeText(getApplicationContext(), "비밀번호와 확인 번호가 다릅니다", Toast.LENGTH_SHORT).show();
                    } else {
                        new JoginJsonTask().execute(URL);
                    }
                }

            }
        });

        /**
         * 취소 버튼 메소드
         * 회원가입화면에서 로그인 화면으로 넘어간다.
         */
        bt_Cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * 라디오체크박스에서 사용자가 선택한 성별을 확인한다.
         */
        Rg_gender.setOnCheckedChangeListener(this);
    }


    /*
    라디오버튼에 체크된 값에 따라 성별을 설정 하는 메소드
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId) {
            case R.id.rb_men:
                gender = "men";
                break;
            case R.id.rb_girl:
                gender = "women";
                break;
        }
    }

    /**
     * 서버로 입력한 사용자 정보를 전송하는 클래스
     *
     */
    public class JoginJsonTask extends AsyncTask<String, String, String> {


        /**
         * 서버로 정보를 보내고 그에 따른 결과 값을 받아내는 메소드
         * @param params URL주소값
         * @return 성공 실패 여부
         */
        @Override
        protected String doInBackground(String... params) {
            data = new HashMap<String, String>();
            data.put("id", id);
            data.put("password", pw);
            data.put("email", email);
            data.put("gender", gender);
            data.put("name", name);



            JSONObject json = new MakeJsonObject(params[0], data).makehttpUrlConnection();

            int result = -1;

            try {
                result = json.getInt("result");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (result == 1) {
                return "실패";
            } else {
                return "성공";
            }


        }

        /**
         * 로그인 성공 여부를 확인하여 다음 동작을 하는 메소드
         *
         * @param result 성공여부
         */
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("성공")) {
                finish();
                Toast.makeText(context.getApplicationContext(), "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context.getApplicationContext(), "아이디가 중복 됩니다.", Toast.LENGTH_SHORT).show();

            }

        }
    }

}
