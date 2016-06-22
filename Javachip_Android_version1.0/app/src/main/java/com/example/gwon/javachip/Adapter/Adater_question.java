package com.example.gwon.javachip.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gwon.javachip.ListItem.Item_question;
import com.example.gwon.javachip.R;

import java.util.List;

/**
 * 질문에대한 정부를 담고 질문리스트를 작성하는 클래스
 * Created by gwon on 2016-06-21.
 */
public class Adater_question extends ArrayAdapter<Item_question> {
    Context c;
    LayoutInflater inflater;
    public List<Item_question> questionLIst;


    /**
     * 리스트 뷰 작성시 필요한 기본 요소를 받아오는 생성자
     *
     * @param context  메인 엑티비티 컨텍스트
     * @param resource 리스트 뷰를 작성할 기본틀
     * @param objects  질문에 대한 정보를 담고있는 리스트
     */
    public Adater_question(Context context, int resource, List<Item_question> objects) {
        super(context, resource, objects);
        questionLIst = objects;
        c = context;
        inflater = (LayoutInflater) c.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * 질문 하나의 리스트 뷰를 작성하는 메소드
     *
     * @param position    리스트 뷰의 유치를 나타내느것
     * @param convertView 리스트 뷰의 기봍 convertview
     * @param parent      리스트 뷰들이 생성되는 부모 뷰값
     * @return 생성된 리스트 뷰값
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lisview_item_question, null);
        }


        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_qeustion_title);
        TextView tv_writer = (TextView) convertView.findViewById(R.id.tv_id_question);


        tv_title.setText(questionLIst.get(position).getQuestionTitle());
        tv_writer.setText("작성자 : " + questionLIst.get(position).getQuestionWriter() + "");


        return convertView;
    }
}
