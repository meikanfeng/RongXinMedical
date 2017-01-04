package com.huagu.RX.rongxinmedical;

import android.app.Application;
import android.widget.ImageView;

import com.litesuits.bluetooth.LiteBluetooth;
import com.tencent.bugly.crashreport.CrashReport;

import org.xutils.image.ImageOptions;
import org.xutils.x;

import cn.jpush.android.api.JPushInterface;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by fff on 2016/7/26.
 */
public class MyApplication extends Application {


    public ImageOptions imageOptions;

    private static MyApplication application;

    public static MyApplication getInstance(){
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        application = this;

        CrashReport.initCrashReport(getApplicationContext(), "900051356", true);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/nina.ttf").setFontAttrId(R.attr.fontPath).build());

        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);

        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);



        imageOptions = new ImageOptions.Builder()
                // 加载中或错误图片的ScaleType
                .setPlaceholderScaleType(ImageView.ScaleType.CENTER_CROP)
                .setIgnoreGif(true)
                .setFailureDrawable(getResources().getDrawable(R.mipmap.def_patient))
                .setLoadingDrawable(getResources().getDrawable(R.mipmap.def_patient))
                .setLoadingDrawableId(R.mipmap.logo)
                // 默认自动适应大小
                // .setSize(...)
                .setIgnoreGif(false)
                // 如果使用本地文件url, 添加这个设置可以在本地文件更新后刷新立即生效.
                .setUseMemCache(false)
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP).build();
    }






}
