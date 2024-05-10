package net.onest.time.api.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import net.onest.time.api.dto.TaskDto;
import net.onest.time.application.TimeApplication;
import net.onest.time.constant.SharedPreferencesConstant;
import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

public class RequestUtil {
    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private static final Gson gson = new Gson();
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    private Request.Builder requestBuilder = null;

    public static RequestUtil builder() {
        RequestUtil requestUtil = new RequestUtil();
        requestUtil.requestBuilder = new Request.Builder();

        String token = TimeApplication
                .getApplication()
                .getApplicationContext()
                .getSharedPreferences(SharedPreferencesConstant.USER_INFO, Context.MODE_PRIVATE)
                .getString("token", "");
        requestUtil.requestBuilder.header("token", token);
        return requestUtil;
    }


    public RequestUtil header(String name, String value) {
        this.requestBuilder.header(name, value);
        return this;
    }

    public RequestUtil url(String url) {
        this.requestBuilder.url(url);
        return this;
    }

    public RequestUtil get() {
        this.requestBuilder.get();
        return this;
    }

    public RequestUtil post(Object requestBody) {
        RequestBody body = RequestBody.create(
                gson.toJson(requestBody),
                MediaType.parse("application/json; charset=utf-8")
        );
        this.requestBuilder.post(body);
        return this;
    }

    public RequestUtil post() {
        this.requestBuilder.post(RequestBody.create("", MediaType.parse("application/json; charset=utf-8")));
        return this;
    }

    public RequestUtil postFile(String fileName) {
        File file = new File(fileName);
        MultipartBody body = new MultipartBody.Builder()
                .addFormDataPart(
                        "avatar", file.getName(),
                        RequestBody.create(file, MediaType.parse("image/*"))
                )
                .build();
        this.requestBuilder.post(body);
        return this;
    }

    public RequestUtil put(Object requestBody) {
        RequestBody body = RequestBody.create(
                gson.toJson(requestBody),
                MediaType.parse("application/json; charset=utf-8")
        );
        this.requestBuilder.put(body);
        return this;
    }

    public RequestUtil put() {
        this.requestBuilder.put(RequestBody.create("", MediaType.parse("application/json; charset=utf-8")));
        return this;
    }

    public RequestUtil delete(Object requestBody) {
        RequestBody body = RequestBody.create(
                gson.toJson(requestBody),
                MediaType.parse("application/json; charset=utf-8")
        );
        this.requestBuilder.delete(body);
        return this;
    }

    public RequestUtil delete() {
        this.requestBuilder.delete();
        return this;
    }

    public <T> T buildAndSend(Class<T> clazz) {
        return gson.fromJson(send(), clazz);
    }

    public <T> T buildAndSend(TypeToken<T> typeToken) {
        return gson.fromJson(send(), typeToken);
    }

    public void buildAndSend() {
        send();
    }

    private JsonElement send() {
        Future<JsonElement> future = executorService.submit(() -> {
            Request request = requestBuilder.build();
            Call call = httpClient.newCall(request);
            call.timeout().timeout(15, TimeUnit.SECONDS);

            try (Response response = call.execute()) {
                checkResponse(response);
                return gson.fromJson(response.body().string(), JsonObject.class).get("data");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        try {
            return future.get(15, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkResponse(Response response) {
        if (response == null) {
            throw new RuntimeException("Response is null");
        } else if (response.code() != 200) {
            throw new RuntimeException("Unexpected response code: " + response.code());
        }
    }

    public static String parseParams(Object o){
        if (o == null) return "?";

        StringBuilder builder = new StringBuilder();
        for (Field field : o.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object o1 = field.get(o);
                if (o1 == null || o1.toString().isEmpty())
                    continue;

                builder.append(field.getName())
                        .append("=")
                        .append(o1)
                        .append("&");
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
        builder.deleteCharAt(builder.length() - 1);
        return "?" + builder + "&";
    }

    public static String parseParams(Object o, Object... params) {
        StringBuilder builder = new StringBuilder(parseParams(o));
        for (int i = 0; i < params.length; i += 2) {
            builder.append(params[i]).append("=").append(params[i + 1]).append("&");
        }
        builder.deleteCharAt(builder.length() - 1);
        return builder.toString();
    }
}
