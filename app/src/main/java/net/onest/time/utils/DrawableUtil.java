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
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class DrawableUtil {
    //任务item背景图
    private static int[] itemDrawableArray={
        R.drawable.art_card2,
        R.drawable.art_card22,
        R.drawable.art_card73,
        R.drawable.art_card77,
        R.drawable.new_card_bg_1,
        R.drawable.new_card_bg_2,
        R.drawable.new_card_bg_3,
        R.drawable.new_card_bg_4,
        R.drawable.new_card_bg_5,
        R.drawable.new_card_bg_6,
        R.drawable.new_card_bg_7,
        R.drawable.new_card_bg_8,
        R.drawable.record_bg_night,
        R.drawable.ic_bgm_custom,
        R.drawable.ic_bgm_quite,
        R.drawable.ic_bgm_rain,
        R.drawable.ic_bgm_rain2,
        R.drawable.ic_bgm_spring1,
        R.drawable.ic_bgm_streams
    };
    private static Set<Integer> selectedImages = new HashSet<>();
    //时钟背景图源
    private static int[] drawableArray = {
            R.drawable.timer_background_one,
            R.drawable.timer_background_two,
            R.drawable.timer_background_four,
            R.drawable.timer_background_seven,
            R.drawable.timer_background_eight,
            R.drawable.timer_background_ten,
            R.drawable.default_background_new_2,
            R.drawable.default_background_new_3,
            R.drawable.default_background_new_4,
            R.drawable.default_background_new_5,
            R.drawable.default_background_new_6,
            R.drawable.default_background_new_7,
            R.drawable.default_background_new_8,
    } ;

    //返回时钟的背景图
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

//---------------------------------------------------------------

    //随机不重复返回item背景图：
    public static Drawable getRandomImage(Context context){
        Random random = new Random();
        int randomIndex = random.nextInt(itemDrawableArray.length);

        while (selectedImages.contains(itemDrawableArray[randomIndex])) {
            randomIndex = random.nextInt(itemDrawableArray.length);
        }

        selectedImages.add(itemDrawableArray[randomIndex]);

        if (selectedImages.size() == itemDrawableArray.length) {
            // 重新开始，清空已选图片集合
            selectedImages.clear();
        }

        return context.getResources().getDrawable(itemDrawableArray[randomIndex]);
    }

    //随机返回item背景图：
    public static Drawable getRandomImageTwo(Context context){
        Random random = new Random();
        int index = random.nextInt(itemDrawableArray.length);
        return context.getResources().getDrawable(itemDrawableArray[index]);
    }

}
