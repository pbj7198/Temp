package com.example.gwon.javachip.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gwon.javachip.Activity.MainActivity;
import com.example.gwon.javachip.Activity.MemoWriteActivity;
import com.example.gwon.javachip.Activity.ShowAnswerActivity;
import com.example.gwon.javachip.Activity.ShowMemoActivity;
import com.example.gwon.javachip.Adapter.Adapter_memo;
import com.example.gwon.javachip.Adapter.Adater_question;
import com.example.gwon.javachip.Javafile.MakeJsonObject;
import com.example.gwon.javachip.ListItem.Item_memo;
import com.example.gwon.javachip.ListItem.Item_question;
import com.example.gwon.javachip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gwon on 2016-06-21.
 */
public class Fragment_memo extends Fragment {

    View view_Memo;
    ListView lv_memo;
    Button bt_memowrite,bt_refresh;
    String id;
    LayoutInflater inflater;

    List<Item_memo> item_memoList;
    SharedPreferences spf_security;

     int getposition;
    final String URL = "http://168.131.152.172:8080/SoftWareProject/memo.jsp";
    final String URL_DELETE = "http://168.131.152.172:8080/SoftWareProject/deletememo.jsp";

    public Fragment_memo() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        this.inflater = inflater;
        view_Memo = inflater.inflate(R.layout.fragment_memo,container,false);


        bt_refresh = (Button) view_Memo.findViewById(R.id.bt_reflesh);


        bt_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new GetMemo().execute(URL);

            }
        });


        spf_security = getActivity()
                .getSharedPreferences("login", Context.MODE_PRIVATE);

        id =spf_security.getString("loginId",null);

        Log.i("id_memo",id);

        lv_memo = (ListView) view_Memo.findViewById(R.id.lv_memo);
        bt_memowrite = (Button) view_Memo.findViewById(R.id.bt_memwrite);

        bt_memowrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it_memowrite = new Intent(getActivity(), MemoWriteActivity.class);
                startActivity(it_memowrite);
            }
        });

        new GetMemo().execute(URL);

        return view_Memo;
    }

    public class GetMemo extends AsyncTask<String, String, List<Item_memo>> {

        @Override
        protected List<Item_memo> doInBackground(String... params) {

            HashMap<String,String> hashMap = new HashMap<String,String>();

            hashMap.put("id",id);

            JSONObject json = new MakeJsonObject(params[0],hashMap).makehttpUrlConnection();

            try {

                JSONArray result = json.getJSONArray("result");

                item_memoList = new ArrayList<Item_memo>();
                for (int i = 0; i < result.length(); i++) {
                    JSONObject questions = result.getJSONObject(i);
                    Item_memo item_memo = new Item_memo();

                    item_memo.setMinfo(questions.getString("minfo"));
                    item_memo.setMid(questions.getString("mid"));
                    item_memo.setId(id);

                    if(questions.getString("minfo").length()<10) {
                        item_memo.setTitle(questions.getString("minfo"));
                        Log.i("memo_0",questions.getString("minfo"));
                    }else{
                        Log.i("memo_10",questions.getString("minfo"));
                        item_memo.setTitle(questions.getString("minfo").substring(0,9)+"....");
                    }

                    Log.i("memo_resut",questions.getString("minfo")+questions.getString("mid"));
                    Log.i("memo",item_memo.getId()+item_memo.getMid()+item_memo.getMinfo()+item_memo.getTitle());
                    item_memoList.add(item_memo);
                }

                return item_memoList;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Item_question item_question = new Item_question();
            item_question.setQid("");
            item_question.setQuestionContents("검색된 내용이 없습니다.");
            item_question.setQuestionTitle("검색된 내용이 없습니다.");
            item_question.setQuestionWriter("");

            return item_memoList;

        }

        /**
         * 로그인 성공 여부를 확인하여 다음 동작을 하는 메소드
         *
         * @param result
         */
        protected void onPostExecute(final List<Item_memo> result) {
            super.onPostExecute(result);


            Adapter_memo adapter_memo = new Adapter_memo(inflater.getContext().getApplicationContext(), R.layout.lisview_item_question, result);
            lv_memo.setAdapter(adapter_memo);

            lv_memo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Intent it = new Intent(getActivity(), ShowMemoActivity.class);

                    it.putExtra("mid", result.get(position).getMid());
                    it.putExtra("minfo",result.get(position).getMinfo());


                    startActivity(it);

                }
            });

            lv_memo.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                    getposition = position;
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view_Memo.getContext());

                    alertDialogBuilder.setTitle("목록 삭제");

                    alertDialogBuilder.setMessage(result.get(position).getTitle()+"를 삭제 하시겠습니까?");
                    alertDialogBuilder.setCancelable(true);
                    alertDialogBuilder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            new DeleteMemo().execute(URL_DELETE,result.get(getposition).getMid());

                        }
                    });

                    alertDialogBuilder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    alertDialogBuilder.show();

                    return false;
                }
            });

        }
    }

    public class DeleteMemo extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> data = new HashMap<String, String>();

            data.put("mid", params[1]);

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
               new GetMemo().execute(URL);
            } else {

            }


        }
    }

}
