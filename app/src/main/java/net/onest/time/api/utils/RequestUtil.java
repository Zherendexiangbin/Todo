package net.onest.time.api.utils;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import net.onest.time.api.dto.MessageDto;
import net.onest.time.application.TimeApplication;
import net.onest.time.constant.SharedPreferencesConstant;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.WebSocket;

public class RequestUtil {
    private static final OkHttpClient httpClient = new OkHttpClient.Builder().build();
    private static final Gson gson;
    private static final ExecutorService executorService = Executors.newCachedThreadPool();

    private Request.Builder requestBuilder = null;

    private static WebSocket webSocket;

    static {
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(Date.class, new DateTimeSerializer())
                .create();
    }

    public static RequestUtil builder() {
        RequestUtil requestUtil = new RequestUtil();
        requestUtil.requestBuilder = new Request.Builder();

        String token = getToken();
        requestUtil.requestBuilder.header("token", token);
        return requestUtil;
    }

    public static void webSocketConnect(String url, MessageListener messageListener) {
        String token = getToken();
        webSocket = httpClient.newWebSocket(
                new Request.Builder()
                        .url(url + "?token=" + token)
                        .header("token", token)
                        .build(),
                messageListener
        );
    }

    @NonNull
    private static String getToken() {
        return TimeApplication
                .getApplication()
                .getApplicationContext()
                .getSharedPreferences(SharedPreferencesConstant.USER_INFO, Context.MODE_PRIVATE)
                .getString("token", "");
    }

    public static void sendMessage(MessageDto messageDto) {
        webSocket.send(gson.toJson(messageDto));
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
                String body = response.body().string();
                Result<?> result = gson.fromJson(body, Result.class);
                checkResult(result);
                return gson.fromJson(body, JsonObject.class).get("data");
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

    private void checkResult(Result<?> result) {
        if (result == null) {
            throw new RuntimeException("Result is null");
        } else if (!result.getCode().equals("200")) {
            throw new RuntimeException(result.getMsg());
        }
    }

    public static String parseParams(Object o){
        if (o == null) return "?";

        StringBuilder builder = new StringBuilder();
        for (Field field : o.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object o1 = field.get(o);
                if (o1 == null)
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

    public static Gson getGson(){
        return gson;
    }

    static class DateTimeSerializer implements JsonSerializer<Date>, JsonDeserializer<Date> {

        static {
            TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
            TimeZone.setDefault(timeZone);
        }

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new Date(json.getAsLong());
        }

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getTime());
        }
    }
}
