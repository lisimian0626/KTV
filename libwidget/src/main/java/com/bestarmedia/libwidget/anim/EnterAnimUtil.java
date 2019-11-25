package com.bestarmedia.libwidget.anim;

import com.bestarmedia.libwidget.layout.EnterAnimLayout;

import java.util.Random;

public class EnterAnimUtil {

    public static Anim getRandomEnterAnim(EnterAnimLayout enterAnimLayout) {
        Random random = new Random();
        int nextInt = random.nextInt(12);
        switch (nextInt) {
            case 0:
                return new AnimBaiYeChuang(enterAnimLayout);
            case 1:
                return new AnimCaChu(enterAnimLayout);
            case 2:
                return new AnimHeZhuang(enterAnimLayout);
            case 3:
//                return new AnimJieTi(enterAnimLayout);//阶梯效果会卡住
                return new AnimBaiYeChuang(enterAnimLayout);
            case 4:
                return new AnimLingXing(enterAnimLayout);
            case 5:
                return new AnimLunZi(enterAnimLayout);
            case 6:
                return new AnimPiLie(enterAnimLayout);
            case 7:
                return new AnimQieRu(enterAnimLayout);
            case 8:
                return new AnimQiPan(enterAnimLayout);
            case 9:
                return new AnimShanXingZhanKai(enterAnimLayout);
            case 10:
                return new AnimShiZiXingKuoZhan(enterAnimLayout);
            case 11:
                return new AnimSuiJiXianTiao(enterAnimLayout);
            case 12:
                return new AnimXiangNeiRongJie(enterAnimLayout);
            default:
                return new AnimYuanXingKuoZhan(enterAnimLayout);
        }
    }
}
