package com.power.doc.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.power.doc.model.ApiDoc;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class ClientUploadUtils {

    static long timeout = 60;

    public static ResponseBody upload(String url, String filePath, String fileName, String docName) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(timeout, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(timeout, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        ;
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName,
                        RequestBody.create(MediaType.parse("multipart/form-data"), new File(filePath)))
                .addFormDataPart("docName", docName)
                .build();

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + UUID.randomUUID())
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body();
    }

    public static ResponseBody uploadData(String url, String projectName, List<ApiDoc> apiDocList) throws Exception {
        OkHttpClient client = new OkHttpClient.Builder().connectTimeout(timeout, TimeUnit.SECONDS)//设置连接超时时间
                .readTimeout(timeout, TimeUnit.SECONDS)//设置读取超时时间
                .build();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("projectName", projectName);
        jsonObject.add("apiDocList", new Gson().toJsonTree(apiDocList));
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), new Gson().toJson(jsonObject));

        Request request = new Request.Builder()
                .header("Authorization", "Client-ID " + UUID.randomUUID())
                .url(url)
                .post(requestBody)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        return response.body();
    }

    public static void main(String[] args) throws IOException {
        try {
            String fileName = "index.html";
            String filePath = "D:/md3/index.html";
            String url = "http://localhost:8888/anobody/manage/doc/upload";
            System.out.println(upload(url, filePath, fileName, "").string());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}