package com.huagu.RX.rongxinmedical.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.huagu.RX.rongxinmedical.Activity.HomeActivity;
import com.huagu.RX.rongxinmedical.Activity.SleepQualityRankingsActivity;
import com.huagu.RX.rongxinmedical.Adapter.HomeListAdapter;
import com.huagu.RX.rongxinmedical.Adapter.SleepQualityRankingsAdapter;
import com.huagu.RX.rongxinmedical.Entity.Ahi;
import com.huagu.RX.rongxinmedical.Entity.CircleScheduleModel;
import com.huagu.RX.rongxinmedical.Entity.Date_YMD;
import com.huagu.RX.rongxinmedical.Entity.Leak;
import com.huagu.RX.rongxinmedical.Entity.Press;
import com.huagu.RX.rongxinmedical.Entity.Usa;
import com.huagu.RX.rongxinmedical.Interface.OnDateSelectListener;
import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.DateUtils;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;
import com.huagu.RX.rongxinmedical.View.ChartView;
import com.huagu.RX.rongxinmedical.View.CircleScheduleView;
import com.huagu.RX.rongxinmedical.View.TextClickDateView;
import com.huagu.RX.rongxinmedical.widget.CustomTextView;
import com.huagu.RX.rongxinmedical.widget.MyListView;
import com.huagu.RX.rongxinmedical.widget.materialmenuview.MaterialMenuDrawable;
import com.huagu.RX.rongxinmedical.widget.materialmenuview.MaterialMenuDrawable.IconState;
import com.huagu.RX.rongxinmedical.widget.materialmenuview.MaterialMenuView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements OnDateSelectListener {

    private MyListView mylistview;
    private CircleScheduleView cirscview;
    private List<CircleScheduleModel> CircleScheduleList;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    private HashMap<String,String> map1;
    private HashMap<String,String> map;

    private static HomeFragment fragment;

    public static HomeFragment newInstance(Date_YMD dy) {
//        if (fragment==null)
            fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("timestamp",dy.getTimestamp());
        bundle.putInt("count", dy.getPosition());
        fragment.setArguments(bundle);
        return fragment;
    }

    private ImageView refresh,menu;
    private CustomTextView percent4,percent3,percent2,percent1;

    private long timestamp;
    private int count;
    public long getTimestamp(){
        return this.timestamp;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        InitView(view);

        return view;
    }
    private TextClickDateView dateclick;

    /**
     * 初始化控件
     */
    private void InitView(View view){
        cirscview = (CircleScheduleView) view.findViewById(R.id.cirscview);
        cirscview.setName("Score", 0);
        cirscview.setPosition(0);

        cirscview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), SleepQualityRankingsActivity.class));
            }
        });

        mylistview = (MyListView) view.findViewById(R.id.mylistview);

        dateclick = (TextClickDateView) view.findViewById(R.id.dateclick);
        dateclick.setPopupEnabled(true);
        dateclick.setStartDate(HomeActivity.getInstance().reg_time);
        dateclick.setDateSelectListener(this);

        menu = (ImageView) view.findViewById(R.id.menu);
        if ("2".equals(HomeActivity.getInstance().type)){
            menu.setImageResource(R.drawable.back);
        }
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("3".equals(HomeActivity.getInstance().type)){
                    HomeActivity.getInstance().closeoropenMenu();
                }else if ("2".equals(HomeActivity.getInstance().type)){
                    getActivity().finish();
                }
            }
        });

        refresh = (ImageView) view.findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData("home", map);
                getData("weekDataStatistic",map1);
            }
        });

        percent4 = (CustomTextView) view.findViewById(R.id.percent4);
        percent3 = (CustomTextView) view.findViewById(R.id.percent3);
        percent2 = (CustomTextView) view.findViewById(R.id.percent2);
        percent1 = (CustomTextView) view.findViewById(R.id.percent1);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        timestamp = getArguments().getLong("timestamp");//获取此页面（时间戳）标记。也是这个页面该显示的哪天
        count = getArguments().getInt("count");
        Calendar calend = Calendar.getInstance();
        calend.setTimeInMillis(timestamp);
        int y = calend.get(Calendar.YEAR);
        int m = calend.get(Calendar.MONTH);
        int d = calend.get(Calendar.DAY_OF_MONTH);
        int w = calend.get(Calendar.DAY_OF_WEEK);
        dateclick.setText(String.format("%1$s %2$s %3$s,%4$d", dateclick.weeks[w - 1], dateclick.months[m], ((d < 10) ? "0" + d : "" + d), y));

        map = new HashMap<String,String>();
        map.put("id", HomeActivity.getInstance().pid);
        map.put("date", timestamp + "");
        getData("home", map);

        map1 = new HashMap<String,String>();
        map1.put("pid",HomeActivity.getInstance().pid);
        map1.put("time", timestamp + "");
        getData("weekDataStatistic",map1);

    }

    /**
     * 获取网络数据
     * @param method    请求方法名
     * @param map       请求的参数集合
     */
    private void getData(String method,HashMap<String,String> map){//date : yyyy-mm-dd 12:00:00

        HttpRequest.getInstance().Request(method, map, new RequestListener() {
            @Override
            public void Success(String method, JSONObject result) throws JSONException {
                if ("home".equals(method)) {
                    int event = result.getJSONObject("userInfo").getInt("event");
                    int humidifier = result.getJSONObject("userInfo").getInt("humidifier");
                    int maskfit = result.getJSONObject("userInfo").getInt("maskfit");
                    double score = result.getJSONObject("userInfo").getInt("score");
                    double usage = result.getJSONObject("userInfo").getDouble("usage");

                    percent1.setText(String.format("%d/60", (int) usage));
                    percent2.setText(String.format("%d/15", maskfit));
                    percent3.setText(String.format("%d/20", event));
                    percent4.setText(String.format("%d/5", humidifier));

                    //列表对应不同数据， 分别加入列表
                    CircleScheduleList = new ArrayList<CircleScheduleModel>();
                    CircleScheduleModel csm1 = new CircleScheduleModel("usage", 100, usage, R.color.thinblue);
                    CircleScheduleList.add(csm1);
                    CircleScheduleModel csm2 = new CircleScheduleModel("maskfit", 100, maskfit, R.color.thinorange);
                    CircleScheduleList.add(csm2);
                    CircleScheduleModel csm3 = new CircleScheduleModel("event", 100, event, R.color.thingray);
                    CircleScheduleList.add(csm3);
                    CircleScheduleModel csm4 = new CircleScheduleModel("humidifier", 100, humidifier, R.color.thinyellow);
                    CircleScheduleList.add(csm4);
                    cirscview.setCircleScheduleList(CircleScheduleList);
                    cirscview.setName("Score", 0);
                    cirscview.setPosition(score);
                    cirscview.start(false);//绘制动画

                    Log.e(method, result.toString());
                } else if ("weekDataStatistic".equals(method)) {
                    Log.e(method, result.toString());
                    JSONArray ja = result.getJSONArray("data");
                    ChartData(ja);
                }
            }

            @Override
            public void Failure(String str, String method, String errorStr) {
                Log.e(method, str.toString());
            }

            @Override
            public void Error(String str, String method, Throwable ex) {
                Log.e(method, str.toString());
            }
        });
    }

    //图表的绘制数据处理
    public void ChartData(JSONArray ja) throws JSONException {
        double[] max = new double[7];
        double[] middle = new double[7];
        double[] p95 = new double[7];
        double[] max1 = new double[7];
        double[] middle1 = new double[7];
        double[] p951 = new double[7];
        double[] ahi = new double[7];
        double[] usa = new double[7];
        for (int i=0;i<ja.length();i++){
            JSONObject jsonobj = ja.getJSONObject(i);
            if (jsonobj.has("press")){
                JSONObject press = jsonobj.optJSONObject("press");
                if (press.has("max")) max[6-i] = press.getDouble("max");
                if (press.has("middle")) middle[6-i] = press.getDouble("middle");
                if (press.has("p95")) p95[6-i] = press.getDouble("p95");
            }
            if (jsonobj.has("leak")){
                JSONObject leak = jsonobj.optJSONObject("leak");
                if (leak.has("max")) max1[6-i] = leak.getDouble("max");
                if (leak.has("middle")) middle1[6-i] = leak.getDouble("middle");
                if (leak.has("p95")) p951[6-i] = leak.getDouble("p95");
            }
            if (jsonobj.has("ahi")){
                ahi[6-i] = jsonobj.optDouble("ahi");
            }
            if (jsonobj.has("useTime")){
                usa[6-i] = jsonobj.optDouble("useTime");
            }
        }
        double[][] press = new double[][]{max,p95,middle};
        double[][] leak = new double[][]{max1,p951,middle1};
        HomeListAdapter ha = new HomeListAdapter(getActivity(),timestamp);
        ha.setData(press, leak, ahi, usa);
        mylistview.setAdapter(ha);
        mylistview.setFocusable(false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDateSelect(int year, int month, int day, int week) {
        long time = DateUtils.getTimeStamp(year, month, day,12);
        HomeActivity.getInstance().selectpager(time);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
