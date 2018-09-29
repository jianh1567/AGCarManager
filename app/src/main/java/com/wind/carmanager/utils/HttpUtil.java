package com.wind.carmanager.utils;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.wind.carmanager.R;
import com.wind.carmanager.MyApplication;
import com.wind.carmanager.model.CarLocation;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by houjian on 2018/5/22.
 */

public class HttpUtil {
    private static String TABLENAME = "carPositionInfo";
    private static HttpUtil instance;
    private OnHttpUtilListener mOnHttpUtilListener;
    private static Context mContext;
    private OkHttpClient mOkHttpClient = new OkHttpClient();
    private String TAG = "HttpUtil";
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("application/json; charset=utf-8");


    public static synchronized  HttpUtil getInstance(Context context){
        if(instance == null){
            instance = new HttpUtil();
        }
        mContext = context;
        return instance;
    }

    public void postRegistrationIDToServ(final MyApplication trackApp, final String registrationID){
        UserInfo userInfo = new UserInfo.Builder()
                                        .setTableName(TABLENAME)
                                        .setTodo("insert")
                                        .setInsertMethod("insertRegistrationId")
                                        .setUsername(trackApp.mUserName)
                                        .setPassword(trackApp.mPassword)
                                        .setRegistrationId(registrationID)
                                        .build();

        String json = new Gson().toJson(userInfo);
        createHttpCall(json).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    String str = response.body().string();

                    Log.i(TAG, "onResponse str = " + str);
                }else if(response.code() == 500){
                    mOnHttpUtilListener.OnHttpResponse(mContext.getString(R.string.server_error));
                }
            }
        });
    }

    public void getCarPositionFromServ(final MyApplication trackApp){
        UserInfo userInfo = new UserInfo.Builder()
                                        .setTableName(TABLENAME)
                                        .setTodo("query")
                                        .setMethod("queryCarPositionInfo")
                                        .setUsername("minos")
                                        .setPassword("123456")
                                        .build();

        String json = new Gson().toJson(userInfo);

        createHttpCall(json).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.code() == 200){
                    String str = response.body().string();
                    CarLocation carLocation = new Gson().fromJson(str, CarLocation.class);

                    Log.i(TAG, "carLocation.getLatitude() = " + carLocation.getLatitude()
                            + " carLocation.getLongitude() = " + carLocation.getLongitude());
                    mOnHttpUtilListener.OnPositionChange(carLocation.getLatitude(), carLocation.getLongitude());
                }else if(response.code() == 500){
                    mOnHttpUtilListener.OnHttpResponse(mContext.getString(R.string.server_error));
                }
            }
        });
    }

    public Call createHttpCall(String json){
        Request request = new Request.Builder()
                .url(UrlUtil.URL_SERV)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, json))
                .build();
        Call call = mOkHttpClient.newCall(request);
        return call;
    }

    public void setOnHttpUtilListener(OnHttpUtilListener listener){
        mOnHttpUtilListener = listener;
    }

    public interface OnHttpUtilListener{
        void OnPositionChange(double latitude, double longitude);
        void OnHttpResponse(String response);
    }
}
