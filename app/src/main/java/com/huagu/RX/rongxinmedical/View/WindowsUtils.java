package com.huagu.RX.rongxinmedical.View;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.huagu.RX.rongxinmedical.Adapter.CommonStringAdapter;
import com.huagu.RX.rongxinmedical.Entity.GoodsNameAndId;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.ScreenUtils;
import com.huahan.utils.tools.DensityUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 窗口管理工具
 *
 * @author wjh
 */
public class WindowsUtils {
    /**
     * 全屏带背景的ListView PopupWindow展示框
     *
     * @param locationView
     * @param list
     * @param listener
     */
    public static void showStringListPopupWindow(View locationView, List<GoodsNameAndId> list, final OnStringItemClickListener listener) {
        Log.i("cyb", "创建showStringListPopupWindow");
        if (list == null || list.size() == 0) {
            return;
        }
        Context context = locationView.getContext();
        final PopupWindow window = new PopupWindow(context);
        window.setWidth(LayoutParams.MATCH_PARENT);
        window.setHeight(LayoutParams.MATCH_PARENT);
        View view = View.inflate(context, R.layout.popup_window_listview_with_bg, null);
        ListView listView = (ListView) view.findViewById(R.id.lv_pw_with_bg);
        listView.setAdapter(new CommonStringAdapter(context, list));
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                window.dismiss();
                if (listener != null) {
                    listener.onStringItemClick(position);
                }
            }

        });
        window.setContentView(view);
        window.setFocusable(true);
        window.setOutsideTouchable(true);
        ColorDrawable colorDrawable = new ColorDrawable(000000);
        window.setBackgroundDrawable(colorDrawable);
        window.setAnimationStyle(R.style.anim_window_select);
//        window.showAsDropDown(locationView, ScreenUtils.getScreenWidth(context) - window.getWidth(), DensityUtils.dip2px(context, 1));
        window.showAtLocation(locationView, Gravity.BOTTOM,ScreenUtils.getScreenWidth(context) - window.getWidth(), DensityUtils.dip2px(context, 1));
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
    }

    public interface OnStringItemClickListener {
        void onStringItemClick(int position);
    }

    public interface OnMoreFilterListener {
        void OnFilter(double min, double max, List<Option> optionList);
    }


    private static List<Option> optionList = new ArrayList<Option>();







    public static class Option implements Serializable {
        private String option;
        private boolean is_selected = false;
        private int position;

        public int getPosition() {
            return position;
        }
        public void setPosition(int position) {
            this.position = position;
        }
        public Option(String str, boolean b,int index) {
            option = str;
            is_selected = b;
            position = index;
        }
        public String getOption() {
            return option;
        }
        public void setOption(String option) {
            this.option = option;
        }
        public boolean is_selected() {
            return is_selected;
        }
        public void setIs_selected(boolean is_selected) {
            this.is_selected = is_selected;
        }
    }


}
