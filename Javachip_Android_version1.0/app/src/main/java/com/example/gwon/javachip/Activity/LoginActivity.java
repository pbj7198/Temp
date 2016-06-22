package com.example.gwon.javachip.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gwon.javachip.Javafile.MakeJsonObject;
import com.example.gwon.javachip.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 어플 첫 실행 화면 사용자가 로그인 하는 엑티비티 클래스
 * 로그인이 되어 있으면 메인화면으로 바로 넘어간다.
 */
public class LoginActivity extends AppCompatActivity {

    Intent it_JoinActivity;
    Button bt_Login, bt_Join;
    final int REQUESTCODE = 1;
    Context context;
    EditText et_Id, et_Pw;
    String URL = "http://168.131.152.172:8080/SoftWareProject/login.jsp";
    String id, pw;

    SharedPreferences.Editor edit_security;
    SharedPreferences spf_security;

    /**
     * 엑티비티 실행시 화면을 구성하는 메소드
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spf_security = getSharedPreferences("login", MODE_PRIVATE);
        edit_security = spf_security.edit();

        if (spf_security.getBoolean("check", false)) {
            Intent it = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(it);
            finish();
        }

        bt_Login = (Button) findViewById(R.id.bt_login);
        bt_Join = (Button) findViewById(R.id.bt_join);
        et_Id = (EditText) findViewById(R.id.et_ID);
        et_Pw = (EditText) findViewById(R.id.et_PW);

        context = this;

        /**
         * 회원가입 창으로 넘어가는 버튼 액션 메소드
         */
        bt_Join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToJoin();
            }
        });

        /**
         * 입력정보와 회원DB에 저장된 값을 비교하여 로그인 여부를 결정하는 메소드
         */
        bt_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id = et_Id.getText().toString();
                pw = et_Pw.getText().toString();
                if (!id.equals("") && !pw.equals("")) {
                    new LoginJsonTask().execute(URL);
                } else {
                    Toast.makeText(getApplicationContext(), "아이디 또는 비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    /*
    *회원가입 창으로 넘어가는 메소드
     */

    public void goToJoin() {
        it_JoinActivity = new Intent(LoginActivity.this, JoinActivity.class);
        startActivity(it_JoinActivity);
    }


    /**
     * 입력한 회원정보를 서버에 있는 회원디비와 확인하는 클래스
     */
    public class LoginJsonTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> data = new HashMap<String, String>();

            data.put("id", id);
            data.put("password", pw);

            JSONObject json = new MakeJsonObject(params[0], data).makehttpUrlConnection();

            int result = -1;

            try {
                result = json.getInt("result");
                Log.i("result", result + "");
            } catch (JSONException e) {
                e.printStackTrace();
                return "실패";
            }

            if (result == 1) {
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
                Intent it_main = new Intent(LoginActivity.this, MainActivity.class);
                edit_security.putString("loginId", et_Id.getText() + "");
                edit_security.putBoolean("check", true);
                edit_security.commit();
                startActivityForResult(it_main,REQUESTCODE);
                Toast.makeText(context.getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context.getApplicationContext(), "비밀번호가 맞지 않습니다.", Toast.LENGTH_SHORT).show();
            }


        }

    }




}
