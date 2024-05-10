package net.onest.time.utils;

import android.graphics.Color;

import java.util.Random;

public class ColorUtil {
    //随机颜色【各种颜色】
    /**
     * 根据RGB色值获取整型颜色
     *
     * @param rgb rgb色值,空代表获取随机颜色
     * @return int型色值
     */
    public static int getColorByRgb(String rgb) {
        int color = Color.WHITE;
        try {
            if (rgb != null) {
                color = Color.parseColor(rgb);
            } else {
                color = Color.parseColor(getRanDomColor());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return color;
    }


    /**
     * 获取随机颜色
     *
     * @return 随机六位色值
     */
    public static String getRanDomColor() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("#");
        for (int i = 0; i < 6; i++) {
            stringBuffer.append(getRandomBeen());
        }
        return String.valueOf(stringBuffer);
    }

    /**
     * 获取色值单元
     *
     * @return 单个色值单元值
     */
    public static String getRandomBeen() {
        String been = "";
        int random = getRandom(16);
        if (random > 9) {
            switch (random) {
                case 10:
                    been = "a";
                    break;
                case 11:
                    been = "b";
                    break;
                case 12:
                    been = "c";
                    break;
                case 13:
                    been = "d";
                    break;
                case 14:
                    been = "e";
                    break;
                case 15:
                    been = "f";
                    break;
            }
        } else {
            been = String.valueOf(random);
        }
        return been;
    }

    /**
     * 获取随机整形数字
     *
     * @return 随机数
     */
    public static int getRandom(int range) {
        Random random = new Random();
        return random.nextInt(range);
    }


    //第二种方式：字符串
    /**
     * 获取十六进制的颜色代码.例如  "#5A6677"
     * 分别取R、G、B的随机值，然后加起来即可
     *
     * @return String
     */
    public static String getRandColor() {
        String R, G, B;
        Random random = new Random();
        R = Integer.toHexString(random.nextInt(256)).toUpperCase();
        G = Integer.toHexString(random.nextInt(256)).toUpperCase();
        B = Integer.toHexString(random.nextInt(256)).toUpperCase();

        R = R.length() == 1 ? "0" + R : R;
        G = G.length() == 1 ? "0" + G : G;
        B = B.length() == 1 ? "0" + B : B;

        return "#" + R + G + B;
    }

    //柔和颜色:
    static final Random mRandom = new Random(System.currentTimeMillis());

    public static int generateRandomColor() {
        // This is the base color which will be mixed with the generated one
        final int baseColor = Color.LTGRAY;

        final int baseRed = Color.red(baseColor);
        final int baseGreen = Color.green(baseColor);
        final int baseBlue = Color.blue(baseColor);

        final int red = (baseRed + mRandom.nextInt(256)) / 2;
        final int green = (baseGreen + mRandom.nextInt(256)) / 2;
        final int blue = (baseBlue + mRandom.nextInt(256)) / 2;

        return Color.rgb(red, green, blue);
    }
}
