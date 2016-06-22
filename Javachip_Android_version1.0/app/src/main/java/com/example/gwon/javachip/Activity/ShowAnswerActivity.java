package com.example.gwon.javachip.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.gwon.javachip.Adapter.Adater_answer;
import com.example.gwon.javachip.Javafile.MakeJsonObject;
import com.example.gwon.javachip.ListItem.Item_answer;
import com.example.gwon.javachip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 질문에 대한 내용과 해당 질문에 달린 댓글을 확인하고 댓글을 다는 메소드
 */
public class ShowAnswerActivity extends AppCompatActivity {

    String qid, contents, title;
    String loginId, ainfo;

    List<Item_answer> answerList;

    TextView tv_title, tv_contents, tv_comment;
    EditText et_comment;

    ListView lv_comments;
    Button bt_back, bt_answer;

    SharedPreferences spf_security;

    final String URL = "http://168.131.152.172:8080/SoftWareProject/answer.jsp";
    final String URL_PUT = "http://168.131.152.172:8080/SoftWareProject/insertanswer.jsp";

    /**전체 화면을 구성하는 메소드
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_showquestion);

        spf_security = getSharedPreferences("login", MODE_PRIVATE);

        Intent it_get = getIntent();

        loginId = spf_security.getString("loginId", null);
        qid = it_get.getStringExtra("qid");
        contents = it_get.getStringExtra("questioncontent");
        title = it_get.getStringExtra("questiontitle");

        answerList = new ArrayList<Item_answer>();

        tv_title = (TextView) findViewById(R.id.tv_showquestion_title);
        tv_contents = (TextView) findViewById(R.id.tv_showquestion_content);
        tv_comment = (TextView) findViewById(R.id.tv_showquestion_comment);

        et_comment = (EditText) findViewById(R.id.et_comment_content);

        tv_title.setText(title);
        tv_contents.setText(contents);

        lv_comments = (ListView) findViewById(R.id.lv_showquestion_comments);

        bt_back = (Button) findViewById(R.id.bt_back);
        bt_answer = (Button) findViewById(R.id.bt_comment);

        /**
         * 질문리스트 화면으로 돌아가는 버튼 메소드
         */
        bt_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         *질문에 대한 댓글 입력 버튼메소드
         */
        bt_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ainfo = et_comment.getText().toString();

                if (!ainfo.equals("")) {
                    new PutAnswer().execute(URL_PUT);
                    et_comment.setText("");
                }

            }
        });

        /**
         * 질문 리스트를 작성하는 생성자호출
         */
        new GetAnswer().execute(URL);

    }

    /**
     * 전체 질문들을 가져와 리스트뷰로 작성하는 클래스
     */
    public class GetAnswer extends AsyncTask<String, String, List<Item_answer>> {

        /**
         * 해당질문에 대한 정보를 가져오는 메소드
         * @param params 서버 주소값
         * @return
         */
        @Override
        protected List<Item_answer> doInBackground(String... params) {

            HashMap<String, String> data = new HashMap<String, String>();

            data.put("qid", qid);
            Log.i("qid", qid);

            JSONObject json = new MakeJsonObject(params[0], data).makehttpUrlConnection();

            try {
                JSONArray result = json.getJSONArray("result");


                for (int i = 0; i < result.length(); i++) {

                    JSONObject answer = result.getJSONObject(i);
                    Item_answer item_answer = new Item_answer();
                    item_answer.setAid(answer.getString("aid"));
                    item_answer.setAinfo(answer.getString("ainfo"));
                    item_answer.setId(answer.getString("id"));
                    item_answer.setQid(answer.getString("qid"));

                    Log.i("answervlaue", answer.getString("aid") + answer.getString("ainfo") + answer.getString("qid"));

                    answerList.add(item_answer);

                }

                return answerList;

            } catch (JSONException e) {

                e.printStackTrace();
                Item_answer item_answer = new Item_answer();
                item_answer.setAid("");
                item_answer.setAinfo("댓글이 없습니다.");
                item_answer.setId("");
                item_answer.setQid("");

                answerList.add(item_answer);
                Log.i("answervlaue", answerList.size() + "");

                return answerList;

            }

        }

        /**
         * 전체 질문을 가져와 질문 내용을 담고 제목과 작성자로 리스트 뷰를 작성하는 메소드
         *
         * @param result
         */
        protected void onPostExecute(List<Item_answer> result) {
            super.onPostExecute(result);


            if (result.size() == 0) {

                tv_comment.setText("댓글 갯수 : " + 0);

            } else {

                tv_comment.setText("댓글 갯수 : " + result.size());

                Adater_answer adater_question = new Adater_answer(getApplicationContext(), R.layout.listview_item_answer, result);
                lv_comments.setAdapter(adater_question);
                lv_comments.setSelection(adater_question.getCount() - 1);
                lv_comments.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                   /* Intent it = new Intent(getActivity(), ShowAnswerActivity.class);

                    it.putExtra("questiontitle", item_questionList.get(position).getQuestionTitle());
                    it.putExtra("questioncontent", item_questionList.get(position).getQuestionContents());
                    it.putExtra("qid", item_questionList.get(position).getQid());


                    startActivity(it);*/

                    }
                });
            }

        }

    }

    /**
     * 질문에 대한 댓글을 서버로 전송하는 클래스
     */
    public class PutAnswer extends AsyncTask<String, String, String> {


        /**
         * 질문 내용을 입력받고 내용을 서버로 보내는 메소드
         * @param params 서버 주소값
         * @return
         */
        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> data = new HashMap<String, String>();

            data.put("qid", qid);
            data.put("id", loginId);
            data.put("ainfo", ainfo);

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
         * 댓글 작성이 끝나고 그 에대한 성공여부를 알려주는 메소드
         *
         * @param result
         */

        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.equals("성공")) {
                new GetAnswer().execute(URL);

            } else {

            }

        }
    }
}



