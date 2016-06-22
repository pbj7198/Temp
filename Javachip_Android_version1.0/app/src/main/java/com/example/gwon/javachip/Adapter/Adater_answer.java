package com.example.gwon.javachip.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gwon.javachip.ListItem.Item_answer;
import com.example.gwon.javachip.R;

import java.util.List;

/**
 * 질문에 달린 댓글 출력을 구성한는 클래스
 * <p/>
 * Created by gwon on 2016-06-21.
 */
public class Adater_answer extends ArrayAdapter<Item_answer> {
    Context c;
    LayoutInflater inflater;
    public List<Item_answer> answerList;


    /**
     * 화면 구성을 위한 기본값을 받아오는 생성자
     *
     * @param context  컨텍스트
     * @param resource 댓글 리스트뷰를 만들 기준 틀
     * @param objects  댓글 리스트들의 정보를 담고있는 리스트
     */
    public Adater_answer(Context context, int resource, List<Item_answer> objects) {
        super(context, resource, objects);
        answerList = objects;
        c = context;
        inflater = (LayoutInflater) c.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }


    /**
     * 하나의 댓글 뷰를 작성하는 메소드
     *
     * @param position    댓글의 순서
     * @param convertView 댓글 뷰의 기준
     * @param parent      댓글 뷰를 담을 부모 뷰
     * @return 완성된 댓글 뷰
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listview_item_answer, null);
        }


        TextView tv_info = (TextView) convertView.findViewById(R.id.tv_answer_info);
        TextView tv_writer = (TextView) convertView.findViewById(R.id.tv_answer_writer);


        tv_info.setText(answerList.get(position).getAinfo());
        tv_writer.setText("작성자 : " + answerList.get(position).getId() + "");


        return convertView;
    }
}
