package com.example.ls.lsn11_materiadesign_palette;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private ImageView iv;
    private TextView tv, tv1, tv2, tv3, tv4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv = (ImageView) findViewById(R.id.iv_id);
        tv = (TextView) findViewById(R.id.title_tv);
        tv1 = (TextView) findViewById(R.id.title_tv1);
        tv2 = (TextView) findViewById(R.id.title_tv2);
        tv3 = (TextView) findViewById(R.id.title_tv3);
        tv4 = (TextView) findViewById(R.id.title_tv4);

        BitmapDrawable drawable = (BitmapDrawable) iv.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //得到bitmap里面的所有的信息 通过Palette类分析出来
        //Palette palette=Palette.generate(bitmap);//这个方法会分析可能分析特别耗时
        //异步任务可能分析的图片会比较大或者颜色分布比较复杂。耗时比较久，防止卡死主线程。
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                int darkMutedColor = palette.getDarkMutedColor(Color.BLUE);
                //暗 柔和
                int lightMutedColor = palette.getLightMutedColor(Color.BLUE);
                //暗 鲜艳
                int vibrantcolor = palette.getDarkVibrantColor(Color.BLUE);
                //量 鲜艳
                int lightvibrantcolro = palette.getLightVibrantColor(Color.BLUE);
                //柔和
                int mutedColor = palette.getMutedColor(Color.BLUE);
                int verberColor = palette.getVibrantColor(Color.BLUE);
                tv.setBackgroundColor(lightvibrantcolro);
                tv1.setBackgroundColor(vibrantcolor);
                tv2.setBackgroundColor(lightMutedColor);
                tv3.setBackgroundColor(mutedColor);
                tv4.setBackgroundColor(verberColor);
                tv1.setText("vibrantcolor");
                tv2.setText("lightMutedColor");
                tv3.setText("mutedColor");
                tv4.setText("verberColor");


                Palette.Swatch lightVibrantSwatch = palette.getLightVibrantSwatch();
                //谷歌推荐的:图片的整体的颜色rgb的混合痔---主色调
                int rgb = lightVibrantSwatch.getRgb();
                int bodyTextColor = lightVibrantSwatch.getBodyTextColor();
                //谷歌推荐：作为标题的颜色(有一定的和图片对比度的颜色)
                int titleTextColor = lightVibrantSwatch.getTitleTextColor();
                //颜色向量
                float[] hsl = lightVibrantSwatch.getHsl();
                //分析该颜色在图片中所占的像素多少值
                int population = lightVibrantSwatch.getPopulation();

                tv.setTextColor(titleTextColor);
                tv.setBackgroundColor(getTranslucentColor(0.7f, rgb));


            }
        });


    }

    /**
     * rgb & 0xff运算如下:
     *    a           r       g          b
     * 11011010  01111010  10001010  10111010
     *                               11111111 做运算
     *                               10111010这样就取出了blue了
     * int green = rgb>> 8 & 0xff运算如下:
     *           rgb>>8运算:
     *           11011010  01111010  10001010  这里砍掉10111010
     *           和0xff运算
     *                               11111111 做运算
     *                               100001010  这样就取出了g了
     *
     * @param
     * @param rgb
     * @return
     */
    private int getTranslucentColor(float percent, int rgb) {
        //10101011110001111
        int blue = rgb & 0xff;//源码就是这样玩的
        int green = rgb>> 8 & 0xff;
        int red = rgb>> 16 & 0xff;
        int alpha = rgb>>>24;
        alpha=Math.round(alpha*percent);
        //int blue=Color.blue(rgb);//会自动给你分析出颜色
        return Color.argb(alpha,red,green,blue);
    }
}
