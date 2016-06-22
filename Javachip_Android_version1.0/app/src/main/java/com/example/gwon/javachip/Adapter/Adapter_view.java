package com.example.gwon.javachip.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.gwon.javachip.Activity.StudyActivity;
import com.example.gwon.javachip.ListItem.Item_view;
import com.example.gwon.javachip.R;

import java.util.List;

/**
 * 각단원에 대한 버튼을 작성할때 그리드 뷰로 작성
 * 각 단원에대한 클릭리스너가 작성됨
 * Created by gwon on 2016-06-22.
 */
public class Adapter_view extends ArrayAdapter<Item_view> {

    List<Item_view> item_views;
    Context context;
    LayoutInflater layoutInflater;


    /**
     * 그리드 뷰를 작성하는 기본조건을 작성하는 생성자
     *
     * @param context  context
     * @param resource 리스트뷰를 작성할 item
     * @param objects  각 단원에 대한 정보를 답고 있는 값
     */
    public Adapter_view(Context context, int resource, List<Item_view> objects) {
        super(context, resource, objects);
        this.context = context;
        item_views = objects;
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * 해당 단원의 크기를 알려주는 메소드
     *
     * @return 총 단원의 갯수를 리턴
     */
    @Override
    public int getCount() {
        return item_views.size();
    }

    /**
     * 해당 단원의 정보를 리턴
     *
     * @param position 해당 단원의 단원
     * @return 해당단원의 정보 값
     */
    @Override
    public Item_view getItem(int position) {
        return item_views.get(position);
    }

    /**
     * 해당 단원에 대한 단원장 값
     *
     * @param position 선택한 단원의 단원 값
     * @return 선택 단원에 대한 단원값 리턴
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 해당 단원에 대한 뷰를 작성한ㄴ 메소드
     *
     * @param position    뷰가 위치할 번호
     * @param convertView 그리드 뷰
     * @param parent      부모 뷰
     * @return 작성된 그리드뷰
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View contextView = convertView;

        if (contextView == null) {
            contextView = layoutInflater.inflate(R.layout.gridlayout, null);
        }

        TextView tv_date = (TextView) contextView.findViewById(R.id.tv_date);

        if (position == 11) {
            tv_date.setText("");
        } else {
            tv_date.setText(item_views.get(position).getNum() + "");
        }

        final View view_study = layoutInflater.inflate(R.layout.fragment_study, parent, false);

        final View finalContextView = contextView;
        tv_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it_study = new Intent(finalContextView.getContext(), StudyActivity.class);
                it_study.putExtra("cid", position + 1);

                finalContextView.getContext().startActivity(it_study);
            }
        });
        return contextView;

    }
}
