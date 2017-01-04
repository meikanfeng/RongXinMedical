package com.huagu.RX.rongxinmedical.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.Entity.Message;
import com.huagu.RX.rongxinmedical.MyApplication;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.DateUtils;

import org.xutils.x;

import java.util.List;

/**
 * Created by fff on 2016/8/17.
 */
public class MessageListAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messagelist;
    private String photo;
    public MessageListAdapter(Context context, List<Message> messagelist) {
        this.context = context;
        this.messagelist = messagelist;
    }

    @Override
    public int getCount() {
        if (messagelist==null)
            return 0;
        return messagelist.size();
    }

    @Override
    public Object getItem(int i) {
        return messagelist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.messagelist_item,null,false);
            holder = new ViewHolder();
            holder.message_avatar = (ImageView) view.findViewById(R.id.message_avatar);
            holder.message_content = (TextView) view.findViewById(R.id.message_content);
            holder.message_time = (TextView) view.findViewById(R.id.message_time);
            view.setTag(holder);
        }else{
            holder = (ViewHolder) view.getTag();
        }
        Message message = (Message) getItem(i);
        holder.message_content.setText(message.getCONTENT());
        holder.message_time.setText(DateUtils.getDateToString(Long.valueOf(message.getADD_TIME()),"yyyy-MM-dd HH:mm:ss"));
        x.image().bind(holder.message_avatar,context.getSharedPreferences("user",Context.MODE_PRIVATE).getString("IMAGE_PATH","")+"/"+photo, MyApplication.getInstance().imageOptions);
        return view;
    }

    public void setAvatar(String photo) {
        this.photo = photo;
    }

    class ViewHolder{
        ImageView message_avatar;
        TextView message_content,message_time;
    }

}
