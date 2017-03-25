package experiment.diary;
//
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.LabeledIntent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

// * Created by Administrator on 2016/12/28 0028.


public class MyAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Diary> list;
    Integer[] noImgs = new Integer[] {R.drawable.noimagever1,R.drawable.noimagever2,R.drawable.noimagever3};
    public MyAdapter(Context context, ArrayList<Diary> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        if (list == null) {
            return null;
        }
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView;
        ViewHolder viewHolder;

        if (view == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.diary_item, null);
            viewHolder = new ViewHolder();
            viewHolder.record_date = (TextView) convertView.findViewById(R.id.record_date);
            viewHolder.record_content = (TextView) convertView.findViewById(R.id.record_content);
            viewHolder.record_img = (ImageView) convertView.findViewById(R.id.record_img);
            convertView.setTag(viewHolder);
        } else {
            convertView = view;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.record_date.setText(String.valueOf(list.get(i).getDay()));
        if(list.get(i).getContent().length()>20) {
            viewHolder.record_content.setText(list.get(i).getContent().substring(0,20)+"…");
        } else {
            viewHolder.record_content.setText(list.get(i).getContent());
        }
        if(list.get(i).getPicture() == null) {
            int date = list.get(i).getDay();
            viewHolder.record_img.setImageResource(noImgs[date%3]);
        } else {
            viewHolder.record_img.setImageBitmap(Diary.bytesToBimap(list.get(i).getPicture()));
        }


        return convertView;
    }
    private class ViewHolder {
            public TextView record_date;
            public TextView record_content;
            public ImageView record_img;
    }
}
