package com.example.gwon.javachip.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gwon.javachip.Activity.QuestionWriteActivity;
import com.example.gwon.javachip.Activity.ShowAnswerActivity;
import com.example.gwon.javachip.Adapter.Adater_question;
import com.example.gwon.javachip.Javafile.MakeJsonObject;
import com.example.gwon.javachip.ListItem.Item_question;
import com.example.gwon.javachip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gwon on 2016-06-20.
 */
public class Fragment_question extends Fragment {
    LayoutInflater inflater;
    ListView lv_question;
    Button bt_question_write, bt_refresh;
    List<Item_question> item_questionList;
    View view_question;
    final int REQUEST_CODE = 1;
    String URL = "http://168.131.152.172:8080/SoftWareProject/question.jsp";
    Activity activity;

    public Fragment_question() {

    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        activity = getActivity();
        view_question = inflater.inflate(R.layout.fragment_question, container, false);
        this.inflater = inflater;

        lv_question = (ListView) view_question.findViewById(R.id.lv_question);

        bt_question_write = (Button) view_question.findViewById(R.id.bt_problem);
        bt_refresh = (Button) view_question.findViewById(R.id.bt_reflesh);

        item_questionList = new ArrayList<Item_question>();

        new GetQuestion().execute(URL);

        bt_question_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_questionWrite = new Intent(getActivity(), QuestionWriteActivity.class);

                getActivity().startActivityForResult(it_questionWrite, REQUEST_CODE);
            }
        });

        bt_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetQuestion().execute(URL);
            }
        });

        return view_question;
    }


    public class GetQuestion extends AsyncTask<String, String, List<Item_question>> {

        @Override
        protected List<Item_question> doInBackground(String... params) {

            JSONObject json = new MakeJsonObject(params[0]).makemakehttpUrlConnection_no_params();

            try {
                JSONArray result = json.getJSONArray("result");


                for (int i = 0; i < result.length(); i++) {
                    JSONObject questions = result.getJSONObject(i);
                    Item_question item_question = new Item_question();
                    item_question.setQid(questions.getString("qid"));
                    item_question.setQuestionContents(questions.getString("qinfo"));
                    item_question.setQuestionTitle(questions.getString("qtitle"));
                    item_question.setQuestionWriter(questions.getString("id"));
                    Log.i("values", questions.getInt("qid") + questions.getString("qinfo") + questions.getString("qtitle") + questions.getString("id"));
                    item_questionList.add(item_question);

                }

                return item_questionList;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Item_question item_question = new Item_question();
            item_question.setQid("");
            item_question.setQuestionContents("검색된 내용이 없습니다.");
            item_question.setQuestionTitle("검색된 내용이 없습니다.");
            item_question.setQuestionWriter("");

            return item_questionList;

        }

        /**
         * 로그인 성공 여부를 확인하여 다음 동작을 하는 메소드
         *
         * @param result
         */
        protected void onPostExecute(List<Item_question> result) {
            super.onPostExecute(result);


            Adater_question adater_question = new Adater_question(inflater.getContext().getApplicationContext(), R.layout.lisview_item_question, result);
            lv_question.setAdapter(adater_question);
            lv_question.setSelection(adater_question.getCount() - 1);
            lv_question.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent it = new Intent(getActivity(), ShowAnswerActivity.class);

                    it.putExtra("questiontitle", item_questionList.get(position).getQuestionTitle());
                    it.putExtra("questioncontent", item_questionList.get(position).getQuestionContents());
                    it.putExtra("qid", item_questionList.get(position).getQid());

                    startActivity(it);

                }
            });
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if((resultCode)==getActivity().RESULT_OK) // 액티비티가 정상적으로 종료되었을 경우
        {
            if(requestCode==REQUEST_CODE)
            {
                Toast.makeText(view_question.getContext().getApplicationContext(),"성공",Toast.LENGTH_SHORT).show();
               new GetQuestion().execute(URL);
            }
        }
    }

}
