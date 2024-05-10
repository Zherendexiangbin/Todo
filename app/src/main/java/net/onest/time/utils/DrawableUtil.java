package net.onest.time.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

import net.onest.time.R;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class DrawableUtil {
    private static int[] drawableArray = {
            R.drawable.timer_background_one,
            R.drawable.timer_background_two,
            R.drawable.timer_background_three,
            R.drawable.timer_background_four,
            R.drawable.timer_background_five,
            R.drawable.timer_background_seven,
            R.drawable.timer_background_eight,
            R.drawable.timer_background_ten
    } ;

    public static Drawable randomDrawableBack(Context context){
        Random random = new Random();
        int index = random.nextInt(drawableArray.length);
        return context.getResources().getDrawable(drawableArray[index]);
    }

    public static int randomIntBack(Context context){
        Random random = new Random();
        int index = random.nextInt(drawableArray.length);
        return drawableArray[index];
    }


}
