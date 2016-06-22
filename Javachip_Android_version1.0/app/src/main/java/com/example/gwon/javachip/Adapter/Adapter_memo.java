package com.example.gwon.javachip.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gwon.javachip.ListItem.Item_memo;
import com.example.gwon.javachip.R;

import java.util.List;

/**
 * memo리스트를 작성할때 메모 하나를 구성하는 정보를 담고 리스트 뷰 작성시 사용 되는 클래스
 */
public class Adapter_memo extends ArrayAdapter<Item_memo> {
    Context c;
    LayoutInflater inflater;
    public List<Item_memo> memoList;


    public Adapter_memo(Context context, int resource, List<Item_memo> objects) {
        super(context, resource, objects);
        memoList = objects;
        c = context;
        inflater = (LayoutInflater) c.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        if (convertView == null) {
            convertView = inflater.inflate(R.layout.lisview_item_question, null);
        }


        TextView tv_title = (TextView) convertView.findViewById(R.id.tv_qeustion_title);
        TextView tv_writer = (TextView) convertView.findViewById(R.id.tv_id_question);


        tv_title.setText(memoList.get(position).getTitle());
        tv_writer.setText(memoList.get(position).getId());


        return convertView;
    }
}
