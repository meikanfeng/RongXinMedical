package com.huagu.RX.rongxinmedical.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.huagu.RX.rongxinmedical.Adapter.MyInfoAdapter;
import com.huagu.RX.rongxinmedical.Interface.RequestListener;
import com.huagu.RX.rongxinmedical.Interface.RightClickCallBack;
import com.huagu.RX.rongxinmedical.MyApplication;
import com.huagu.RX.rongxinmedical.R;
import com.huagu.RX.rongxinmedical.Utils.Constant;
import com.huagu.RX.rongxinmedical.Utils.HttpRequest;
import com.huagu.RX.rongxinmedical.Utils.ImageUtils;
import com.huagu.RX.rongxinmedical.Utils.ScreenUtils;
import com.huagu.RX.rongxinmedical.widget.CircleImageView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class MyInfoActivity extends BaseActivity implements RightClickCallBack, View.OnClickListener {

    private TextView tcd;
    private ImageView back, refresh;
    private Context context;
    private ListView infolist;

    private String pid, type;

    private HashMap<String, String> map;
    private static String[] names;

    private String photo;
    private CircleImageView avatar;
    private AlertDialog alert;
    private View photoselectview;
    private TextView select_album, select_photograph, select_cancel;

    private final int OPENALBUM = 101;
    private final int PHOTOGRAPH = 102;
    private final int REQUESTCODE = 103;
    private final int AVATARCROP = 104;
    private final String AVATAR_NAME = "rongxin.png";//头像图片名

    private Uri avatarOutUri;//头像输出的位置

    private int selectposition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);

        type = usershared.getString("TYPE", "");
        pid = usershared.getString("USER_ID", "");

        context = this;
        InitView();

        map = new HashMap<String, String>();
        if ("3".equals(type)) {
            avatar.setImageResource(R.mipmap.def_patient);
            names = context.getResources().getStringArray(R.array.patient_info);
            lins = 11;
            map.put("pid", pid);
            getData("patientUserInformation", map);
        } else if ("2".equals(type)) {
            avatar.setImageResource(R.mipmap.def_doctor);
            names = context.getResources().getStringArray(R.array.doctor_info);
            lins = 5;
            map.put("did", pid);
            getData("doctorUserInformation", map);
        }
    }

    private int lins;
    private HashMap<String, String> hashMap;
    private MyInfoAdapter mia;

    private void getData(String method, HashMap<String, String> map) {
        HttpRequest.getInstance().Request(method, map, new RequestListener() {
            @Override
            public void Success(String method, JSONObject result) throws JSONException {
                Log.e(method, result.toString());
                JSONObject obj = result.getJSONObject("data");
                switch (method) {
                    case "patientUserInformation":
                        hashMap = new HashMap<String, String>();
                        hashMap.put("Phone:", obj.getString("PHONE"));
                        hashMap.put("First Name:", obj.getString("FIRST_NAME"));
                        hashMap.put("Last Name:", obj.getString("LAST_NAME"));
                        hashMap.put("Age:", obj.getString("AGE"));
                        hashMap.put("ID:", obj.getString("USER_ID"));
                        hashMap.put("Weight:", obj.getString("WEIGHT"));
                        hashMap.put("Instant Messaging Account", obj.getString("INSTANT_MESSAGING_ACCOUNT"));
                        hashMap.put("Sex:", obj.getString("SEX"));
                        hashMap.put("Institutuin ID:", obj.getString("INSTITUTUIN_ID"));
                        hashMap.put("Height:", obj.getString("HEIGHT"));
                        hashMap.put("Treating Doctor ID:", obj.has("TREATING_DOCTOR_ID") ? obj.getString("TREATING_DOCTOR_ID") : "");
                        hashMap.put("Email:", obj.getString("EMAIL"));
                        setData(obj);
                        break;
                    case "doctorUserInformation":
                        hashMap = new HashMap<String, String>();
                        hashMap.put("Phone:", obj.getString("PHONE"));
                        hashMap.put("First Name:", obj.getString("FIRST_NAME"));
                        hashMap.put("Last Name:", obj.getString("LAST_NAME"));
                        hashMap.put("ID:", obj.getString("USER_ID"));
                        hashMap.put("Institutuin ID:", obj.getString("INSTITUTUIN_ID"));
                        hashMap.put("Department:", obj.getString("DEPARTMENT"));
                        setData(obj);
                        break;
                    case "upload_head":
                        String avatar_path = obj.getString("result");
                        usershared.edit().putString("PHOTO",avatar_path).commit();
                        Toast.makeText(context,R.string.avatar_upLoad_Tips_success,Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void Failure(String str, String method, String errorStr) {
                Log.e(method, str.toString());
                if (method.equals("upload_head")){
                    Toast.makeText(context,R.string.avatar_upLoad_Tips_fail,Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void Error(String str, String method, Throwable ex) {
                Log.e(method, str.toString());
            }
        });
    }

    public void setData(JSONObject obj) throws JSONException {
        mia = new MyInfoAdapter(context, names, hashMap);
        mia.setRightClick(MyInfoActivity.this);
        infolist.setAdapter(mia);
        photo = obj.getString("HEAD_PHOTO");
        x.image().bind(avatar, usershared.getString("IMAGE_PATH", "") + "/" + photo, MyApplication.getInstance().imageOptions);
        usershared.edit().putString("PHOTO",photo).commit();
    }

    private void InitView() {
        tcd = (TextView) this.findViewById(R.id.title);
        back = (ImageView) this.findViewById(R.id.menu);
        refresh = (ImageView) this.findViewById(R.id.refresh);

        tcd.setText(R.string.MyInfo);
        refresh.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        infolist = (ListView) this.findViewById(R.id.infolist);
        View view = LayoutInflater.from(context).inflate(R.layout.myinfo_header, null, false);
        infolist.addHeaderView(view);

        avatar = (CircleImageView) view.findViewById(R.id.avatar);

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowPhotoSelect();
            }
        });
    }

    /**
     * 裁剪图片方法实现
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, AVATARCROP);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUESTCODE:
                if (resultCode == RESULT_OK) {
                    if (data == null) return;
                    String result = data.getStringExtra("result");
                    if (!TextUtils.isEmpty(result)) {
                        if ("Name:".equals(names[selectposition])){
                            hashMap.put("First Name:", result.split("#")[0]);
                            hashMap.put("Last Name:", result.split("#")[1]);
                        }else{
                            hashMap.put(names[selectposition], result);
                        }
                        mia.notifyDataSetChanged();
                    }
                }
                break;
            case OPENALBUM:
                if (data == null) return;
                startPhotoZoom(data.getData());
                break;
            case PHOTOGRAPH:
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    try {
                        File file = ImageUtils.Bitmap2Vertical(Constant.APP_ROOTPATH + "/rongxin.png");
                        startPhotoZoom(avatarOutUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(context, "未找到存储卡，无法存储照片！", Toast.LENGTH_LONG).show();
                }
                break;
            case AVATARCROP:
                if (data == null) return;
                Bundle extras = data.getExtras();
                Bitmap avatarbitmap = extras.getParcelable("data");
                avatar.setImageBitmap(avatarbitmap);
                String path = ImageUtils.getImageToView(avatarbitmap, AVATAR_NAME, 80).getPath();

                HashMap<String,String> avatarmap = new HashMap<String,String>();
                avatarmap.put("uid",usershared.getString("USER_ID",""));
                avatarmap.put("file", path);
                getData("upload_head",avatarmap);
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void RightClick(String str, int i) {
        if (i == 0) return;
        if ("Treating Doctor ID:".equals(str) || "Institutuin ID:".equals(str)) return;
        selectposition = i;
        Intent intent = new Intent(this, EditMyInfoActivity.class);
        intent.putExtra("title", str);
        intent.putExtra("value", (!"Name:".equals(str))?hashMap.get(str):hashMap.get("First Name:")+"#"+hashMap.get("Last Name:"));
        intent.putExtra("type", type);
        startActivityForResult(intent, REQUESTCODE);
    }

    private void ShowPhotoSelect() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        dialog.create();
        alert = dialog.show();
        photoselectview = getLayoutInflater().inflate(R.layout.select_avatar_layout, null, false);
        InitSelectView();
        alert.setContentView(photoselectview);

        Window window = alert.getWindow();
        window.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams wmlp = window.getAttributes();
        wmlp.width = ScreenUtils.getScreenWidth(context);
        window.setAttributes(wmlp);
    }

    private void InitSelectView() {
        select_album = (TextView) photoselectview.findViewById(R.id.select_album);
        select_photograph = (TextView) photoselectview.findViewById(R.id.select_photograph);
        select_cancel = (TextView) photoselectview.findViewById(R.id.select_cancel);

        select_album.setOnClickListener(this);
        select_photograph.setOnClickListener(this);
        select_cancel.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_album:
                Intent openAlbumIntent = new Intent(Intent.ACTION_PICK);
                openAlbumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(openAlbumIntent, OPENALBUM);
                alert.dismiss();
                break;
            case R.id.select_photograph:
                photograph();
                alert.dismiss();
                break;
            case R.id.select_cancel:
                alert.dismiss();
                break;
        }
    }

    public void photograph() {
        String path = Constant.APP_ROOTPATH;
        File filepath = new File(path);
        if (!filepath.exists()) {
            filepath.mkdirs();
        }
        File file = new File(filepath, AVATAR_NAME);
        avatarOutUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
        intent.putExtra(MediaStore.EXTRA_OUTPUT, avatarOutUri);
        startActivityForResult(intent, PHOTOGRAPH);//头像拍照
    }

}
