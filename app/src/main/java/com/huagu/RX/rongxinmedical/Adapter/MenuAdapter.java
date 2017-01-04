package com.huagu.RX.rongxinmedical.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.huagu.RX.rongxinmedical.Entity.Category;
import com.huagu.RX.rongxinmedical.Entity.Item;
import com.huagu.RX.rongxinmedical.R;

import java.util.List;

public class MenuAdapter extends BaseAdapter {

    public interface MenuListener {

        void onActiveViewChanged(View v);
    }

    private Context mContext;

//    private List<Object> mItems;

    private MenuListener mListener;

    private int mActivePosition = -1;

    public MenuAdapter(Context context, List<Object> items) {
        mContext = context;
//        mItems = items;
    }

    public void setListener(MenuListener listener) {
        mListener = listener;
    }

    public void setActivePosition(int activePosition) {
        mActivePosition = activePosition;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return null/*mItems.get(position)*/;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position) instanceof Item ? 0 : 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEnabled(int position) {
        return getItem(position) instanceof Item;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        Object item = getItem(position);

        if (item instanceof Category) {
            if (v == null) {
                v = LayoutInflater.from(mContext).inflate(R.layout.layout_menu, parent, false);
            }
        } else {
            if (v == null) {
                v = LayoutInflater.from(mContext).inflate(R.layout.layout_menu, parent, false);
            }
        }

        v.setTag(R.id.mdActiveViewPosition, position);

        if (position == mActivePosition) {
            mListener.onActiveViewChanged(v);
        }

        return v;
    }
}
