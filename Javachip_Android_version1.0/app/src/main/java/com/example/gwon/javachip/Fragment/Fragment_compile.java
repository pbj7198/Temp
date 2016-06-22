package com.example.gwon.javachip.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gwon.javachip.Javafile.MakeJsonObject;
import com.example.gwon.javachip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * 컴파일 화면을 출력하는 컴파일 프레그먼트 클래스
 * Created by gwon on 2016-06-20.
 */
public class Fragment_compile extends Fragment {

    View compile_view;
    EditText et_contents ;
    Button bt_compil;
    TextView tv_result;
    String content,output;
    String URL = "http://168.131.152.172:8080/SoftWareProject/compile.jsp";
    ProgressDialog PD;

    public Fragment_compile() {
    }

    /**
     * 뷰에대한 구성을 작성하는 메소드
     * @param inflater 메인엑티비티에서 받은 iflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {


        compile_view = inflater.inflate(R.layout.fragment_compile,container,false);

        et_contents = (EditText) compile_view.findViewById(R.id.et_content);
        tv_result = (TextView) compile_view.findViewById(R.id.tv_result);
        bt_compil = (Button) compile_view.findViewById(R.id.bt_compile);

        bt_compil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                content = et_contents.getText().toString();
                new Networking().execute(URL,content);

            }
        });

        return compile_view;
    }

    public class Networking extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            PD = new ProgressDialog(compile_view.getContext());
            PD.setMessage("Loading.....");
            PD.setCancelable(false);
            PD.show();
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... params) {

            HashMap<String, String> data = new HashMap<String, String>();

            data.put("contents", params[1]);

            try {
                JSONObject json = new MakeJsonObject(params[0], data).makehttpUrlConnection();

                JSONArray result = json.getJSONArray("result");
                StringBuilder str = new StringBuilder();

                for(int i = 0; i<result.length();i++){
                    JSONObject ob = result.getJSONObject(i);
                    output = ob.getString("result");

                    Log.i("test",output+'\t');
                    str.append(output);
                }

                return str.toString();


            } catch (Exception e) {
                return "실패";
            }

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            PD.dismiss();
           if(result.equals("실패")){
               Toast.makeText(compile_view.getContext().getApplicationContext(),"코드를 수정하세요.",Toast.LENGTH_SHORT).show();
           }else{
               tv_result.setText(result);
           }

        }


    }

    public View getView(){
        return compile_view;
    }

}
