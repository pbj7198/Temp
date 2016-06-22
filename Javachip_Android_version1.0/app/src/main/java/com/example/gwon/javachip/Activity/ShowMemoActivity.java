package com.example.gwon.javachip.Activity;

import android.content.Intent;
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

/**
 * 작성한 메모를 출력하는 클래스
 */
public class ShowMemoActivity extends AppCompatActivity {

    EditText et_showmemo;
    Button bt_complite;

    String mid, minfo;
    String URL = "http://168.131.152.172:8080/SoftWareProject/updatememo.jsp";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_memo);


        bt_complite = (Button) findViewById(R.id.bt_complete);
        et_showmemo = (EditText) findViewById(R.id.et_showmemo);

        Intent intent = getIntent();
        mid = intent.getStringExtra("mid");
        minfo = intent.getStringExtra("minfo");

        Log.i("mid",mid);
        et_showmemo.setText(minfo);


        /**
         * 작성한 메모를 저장하는 버튼리스너 메소드
         */
        bt_complite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                minfo = et_showmemo.getText().toString();
                new UpdateMemo().execute(URL);

            }
        });

    }

    /**
     * 저장 했던 메모를 수정하는 클래스
     */
    public class UpdateMemo extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> data = new HashMap<String, String>();

            data.put("mid", mid);
            data.put("minfo", minfo);

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
         * 메모 작성에 대한 성공여부를 확인하고 그에 해당하는 액션을 취하는 메소드
         * @param result
         */
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (result.equals("성공")) {
                finish();
            }

        }


    }
}
