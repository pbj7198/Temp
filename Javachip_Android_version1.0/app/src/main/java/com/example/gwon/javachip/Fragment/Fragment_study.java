package com.example.gwon.javachip.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gwon.javachip.Activity.ShowAnswerActivity;
import com.example.gwon.javachip.Activity.StudyActivity;
import com.example.gwon.javachip.Adapter.Adapter_view;
import com.example.gwon.javachip.Adapter.Adater_question;
import com.example.gwon.javachip.Javafile.MakeJsonObject;
import com.example.gwon.javachip.ListItem.Item_question;
import com.example.gwon.javachip.ListItem.Item_view;
import com.example.gwon.javachip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gwon on 2016-06-20.
 */
public class Fragment_study extends Fragment {

    Context context;
    LayoutInflater layoutContext;
    GridView gridView;
    List<Item_view> item_viewList;
    SharedPreferences sf_id;

    int getposition;
    View view_study;
    String id;
    String percent;

    TextView tv_select,tv_progress;

    final String URL = "http://168.131.152.172:8080/SoftWareProject/concept.jsp";
    public Fragment_study() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        layoutContext = inflater;
        sf_id = getActivity().getSharedPreferences("login", Context.MODE_PRIVATE);

        id = sf_id.getString("loginId", null);

        view_study = layoutContext.inflate(R.layout.fragment_study, container, false);
        gridView = (GridView) view_study.findViewById(R.id.gridView1);
        tv_select = (TextView) view_study.findViewById(R.id.tv_select);
        tv_progress = (TextView) view_study.findViewById(R.id.tv_progress);


        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        item_viewList = new ArrayList<Item_view>();

        for(int i = 0 ; i<12; i++){
            Item_view item_view = new Item_view();
            item_view.setNum(i+1);
            item_view.setCheck(false);
            item_view.setId(id);
            item_viewList.add(item_view);
        }
        Adapter_view adapter_view = new Adapter_view(view_study.getContext(), R.layout.gridlayout, item_viewList);

        gridView.setAdapter(adapter_view);

        tv_progress.setText(percent);

        new GetTitle().execute(URL);

        return view_study;
    }
    public class GetTitle extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            HashMap<String,String> hashMap = new HashMap<String,String>();

            hashMap.put("id",id);

            JSONObject json = new MakeJsonObject(params[0],hashMap).makehttpUrlConnection();



            try {



                return json.getString("percent");
            } catch (JSONException e) {
                e.printStackTrace();
                return "%";
            }



        }

        /**
         * 로그인 성공 여부를 확인하여 다음 동작을 하는 메소드
         *
         * @param result
         */
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

               tv_progress.setText(result);
        }
    }


}
