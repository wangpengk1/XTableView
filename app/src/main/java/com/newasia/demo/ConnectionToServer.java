package com.newasia.demo;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ConnectionToServer {


    public interface OnQueryResult
    {
        void onResult(JsonArray array);
    }

    public final static String BASEURL = "http://39.98.80.91:8088/";
    private static OkHttpClient s_HttpClient = null;


    public static OkHttpClient getHttpClient()
    {
        if(s_HttpClient==null)
        {
            s_HttpClient = new OkHttpClient.Builder().pingInterval(60, TimeUnit.SECONDS).build();
        }

        return s_HttpClient;
    }


    public static void query(String page,OnQueryResult result)
    {
        final Handler handler = new Handler(Looper.getMainLooper());
        try
        {
            //MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
            MediaType mediaType = MediaType.parse("application/octet-stream,charset=utf-8");
            Request request = new Request.Builder()
                    .url(BASEURL+page)
                    .post(RequestBody.create("test".getBytes(),mediaType))
                    .build();
            getHttpClient().newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    handler.post(()-> result.onResult(null));

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    byte[] retData = response.body().bytes();
                    if(retData!=null && retData.length>0) {
                        String strRet = new String(retData, "utf-8");
                        Gson gson = new Gson();
                        try
                        {
                            handler.post(()-> result.onResult(gson.fromJson(strRet,JsonArray.class)));
                        }catch (JsonSyntaxException e){e.printStackTrace();}

                    }
                }
            });
        }catch (Exception e){e.printStackTrace();}
    }
}
