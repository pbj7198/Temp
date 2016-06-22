package com.example.gwon.javachip.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.gwon.javachip.Javafile.MakeJsonObject;
import com.example.gwon.javachip.ListItem.Item_lecture;
import com.example.gwon.javachip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 학습하기 창화면을 띄우는 클래스
 * 강의 단원과 내용 그리고 동영상 강의밑 문제풀기 기능이 있다.
 */
public class StudyActivity extends AppCompatActivity {

    String cid;
    TextView tv_ctitle, tv_cinfo;
    Button bt_lecture, bt_problem;

    String lecture_url;
    final String URL = "http://168.131.152.172:8080/SoftWareProject/concept2.jsp";

    /**
     * 강의화면에 대한 화면을 작성하는 메소드
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);


        cid = getIntent().getIntExtra("cid", 2) + "";

        Log.i("cid", cid);
        tv_cinfo = (TextView) findViewById(R.id.tv_cinfo);
        tv_ctitle = (TextView) findViewById(R.id.tv_ctitle);
        bt_lecture = (Button) findViewById(R.id.bt_lecture);
        bt_problem = (Button) findViewById(R.id.bt_problem);

        new GetLecture().execute(URL);

        /**
         * 동영상 url를 받아 인터넷 브라우저를 실행하는 메소드
         */
        bt_lecture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                Uri u = Uri.parse(lecture_url);
                i.setData(u);
                startActivity(i);
            }
        });


        /**
         *해당 단원의 문제플기화면으로 넘어가는 클래스
         */
        bt_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudyActivity.this, ProblemActivity.class);
                i.putExtra("cid", cid);
                startActivity(i);
            }
        });


    }

    /**
     * 로그인 사용자의 학습현황을 가져오고 자신이 듣지 않은 강의 정보를 가져온다.
     */
    public class GetLecture extends AsyncTask<String, String, Item_lecture> {


        /**
         * 사용자의 학습현황과 각 단원에대한 학습정보를 서버로부터 가져오는 메소드
         *
         * @param params 서버주소값
         * @return
         */

        @Override
        protected Item_lecture doInBackground(String... params) {

            HashMap<String, String> hashMap = new HashMap<String, String>();

            hashMap.put("cid", cid);

            JSONObject json = new MakeJsonObject(params[0], hashMap).makehttpUrlConnection();

            try {

                JSONArray result = json.getJSONArray("result");
                JSONObject questions = result.getJSONObject(0);
                Item_lecture item_lecture = new Item_lecture();
                item_lecture.setCinfo(questions.getString("cinfo"));
                item_lecture.setCtitle(questions.getString("ctitle"));
                item_lecture.setUrl(questions.getString("url"));


                return item_lecture;

            } catch (JSONException e) {
                e.printStackTrace();
                Item_lecture item_lecture = new Item_lecture();
                item_lecture.setCinfo("검색된 내용이 없습니다");
                item_lecture.setCtitle("검색된 내용이 없습니다.");
                item_lecture.setUrl("검색된 내용이 없습니다.");


                return item_lecture;
            }


        }

        /**
         * 서버로부터 받은 강의에 대한 정보와 사용자 학습현황을 화면에 출력하는 메소드
         * @param result
         */
        protected void onPostExecute(Item_lecture result) {
            super.onPostExecute(result);

            tv_cinfo.setText(result.getCinfo());
            tv_ctitle.setText(result.getCtitle());
            lecture_url = result.getUrl();

        }
    }

}
