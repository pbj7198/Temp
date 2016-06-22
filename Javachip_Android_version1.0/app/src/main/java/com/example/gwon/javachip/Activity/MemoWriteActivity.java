package com.example.gwon.javachip.Activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
 * 메모작성 화면을 구성하는 엑티비티 클래스
 */
public class MemoWriteActivity extends AppCompatActivity {

    EditText et_memowrite;
    Button bt_write;
    String id,minfo;

    SharedPreferences shared_id;
    final String URL = "http://168.131.152.172:8080/SoftWareProject/insertmemo.jsp";

    /**
     * 화면을 구성하는 메소드
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_memo_write);

        et_memowrite = (EditText) findViewById(R.id.et_memowrite);
        bt_write = (Button) findViewById(R.id.bt_write);

        shared_id = getSharedPreferences("login",MODE_PRIVATE);

        id = shared_id.getString("loginId",null);


        bt_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minfo = et_memowrite.getText().toString();
                new PutMemo().execute(URL);
                finish();
            }
        });

    }

    public class PutMemo extends AsyncTask<String, String, String> {


        /**
         * 서버로 데이터를 보내 메모를 저장 한는 메소드
         * @param params URL주소값
         * @return 성공 실패 여부
         */
        @Override
        protected String doInBackground(String... params) {
            HashMap<String,String> data = new HashMap<String, String>();
            data.put("id", id);
            data.put("minfo", minfo);

            JSONObject json = new MakeJsonObject(params[0], data).makehttpUrlConnection();

            int result = -1;

            try {
                result = json.getInt("result");
                Log.i("result",result+"");
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
         * 메모 입력에 대한 결과를 확인하고 메세지를 띄우는 메소드
         *
         * @param result 성공여부
         */
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

           if(result.equals("성공"))
               finish();

        }
    }
}
