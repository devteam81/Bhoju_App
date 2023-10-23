package com.bhoju.app.util;

import android.content.Context;
import android.content.res.Resources;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.bhoju.app.util.sharedpref.Utils;


public class ResponsivenessUtils {


    public static FrameLayout.LayoutParams getLayoutParmsForContinueWatcingAdapter(Context context){
        int width = Utils.getScreenWidth(context);

        FrameLayout.LayoutParams params;
        int calculatedWidth = (int) (width/1.6);
        int calculateHeight = (calculatedWidth*9) / 16;
        params = new FrameLayout.LayoutParams(calculatedWidth, calculateHeight);

//        params.setMargins(10,10,10,10);

        return params;
    }

    public static RelativeLayout.LayoutParams getLayoutParamsForHorizontalAdapter(Context context, boolean isPortrait){
        int width = Utils.getScreenWidth(context);

        RelativeLayout.LayoutParams params;
        int calculatedWidth;
        int calculatedHeight;
        if(isPortrait){

            calculatedWidth = width / 3;
            calculatedHeight = (calculatedWidth * 3) / 2;
        }else{

            calculatedWidth = (int) (width / 1.5);
            calculatedHeight = (calculatedWidth * 9) / 16;
        }


        params = new RelativeLayout.LayoutParams(calculatedWidth, calculatedHeight);

        if(isPortrait)
            params.setMargins(10, 0, 10, 0);
        else
            params.setMargins(10, 0, 10, 0);
        //  params.setMargins(3,0,3,0);

        return params;

    }

    public static RelativeLayout.LayoutParams getLayoutParamsForOrignalAdapter(Context context, boolean isportrait){
        int width = Utils.getScreenWidth(context);

        RelativeLayout.LayoutParams params;
        int calculatedWidth = 0;
        int calculatedHeight = 0;
        if(isportrait){

            calculatedWidth = (int) (width / 2.3);
            calculatedHeight = (calculatedWidth * 3) / 2 ;
        }


        params = new RelativeLayout.LayoutParams(calculatedWidth, calculatedHeight);


        //  params.setMargins(3,0,3,0);

        return params;

    }


    public static RelativeLayout.LayoutParams getLayoutParmsForSearchScreenAdapter(Context context){

        int width = Utils.getScreenWidth(context);

        RelativeLayout.LayoutParams params;
        int calculatedWidth;
        int calculatedHeight;
        /*calculatedWidth = (int) (width / 2);
        calculatedHeight = (calculatedWidth * 2) / 3 ;*/

        calculatedWidth = (int) (width / 2);
        calculatedHeight = (calculatedWidth * 9) / 16;

        params = new RelativeLayout.LayoutParams(calculatedWidth, calculatedHeight);

        params.setMargins(20, 5, 20, 0);

        /*int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);


        if(dp>400 && dp<800)
            params.setMargins(20, 0, 0, 0);
        else if(dp>800)
            params.setMargins(40, 0, 0, 0);*/

        return params;
    }



    public static RelativeLayout.LayoutParams getLayoutParmsForPosterActivitySeasonView(Context context){



        int width = Utils.getScreenWidth(context);

        RelativeLayout.LayoutParams params;
        int calculatedWidth = (int) (width/1.5);
        int calculateHeight = (calculatedWidth*9) / 16;
        params = new RelativeLayout.LayoutParams(calculatedWidth, calculateHeight);


        params.setMargins(10,0,10,0);

        return params;



    }

    public static RelativeLayout.LayoutParams getLayoutParamsForTrailerAndExtrasView(Context context){



        int width = Utils.getScreenWidth(context);

        RelativeLayout.LayoutParams params;
        int calculateHeight = (width*9) / 16;
        params = new RelativeLayout.LayoutParams(width, calculateHeight);


        params.setMargins(10,10,10,10);

        return params;


    }

    public static RelativeLayout.LayoutParams getLayoutParamsForFullWidthPlayerView(Context context){



        int width = Utils.getScreenWidth(context);

        RelativeLayout.LayoutParams params;
        int calculateHeight = (width*9) / 16;
        params = new RelativeLayout.LayoutParams(width, calculateHeight);


        //params.setMargins(10,10,10,10);

        return params;


    }


    public static RelativeLayout.LayoutParams getLayoutParamsForFullWidthSeasonView(Context context){



        int width = Utils.getScreenWidth(context);

        RelativeLayout.LayoutParams params;
        int calculateHeight = (width*9) / 16;
        params = new RelativeLayout.LayoutParams(width, calculateHeight);


        //params.setMargins(10,10,10,10);

        return params;


    }

    public static RelativeLayout.LayoutParams getLayoutParamsForFullWidthVideoThumbnailView(Context context){



        int width = Utils.getScreenWidth(context);

        RelativeLayout.LayoutParams params;
        int calculateHeight = (width*9) / 10;
        params = new RelativeLayout.LayoutParams(width, calculateHeight);

        int dp = (int) (width / Resources.getSystem().getDisplayMetrics().density);

        if(dp>800)
            params.setMargins(280,150,280,0);
        else if(dp<400)
            params.setMargins(120,70,120,0);
        else
            params.setMargins(150,60,150,0);

        return params;


    }


    public static FrameLayout.LayoutParams getLayoutParmsForPosterActivityMoreLikeThisView(Context context){


        int width = Utils.getScreenWidth(context);

        FrameLayout.LayoutParams params;
        int calculatedWidth = width/2;
        int calculateHeight = (calculatedWidth*9) / 16;
        params = new FrameLayout.LayoutParams(calculatedWidth, calculateHeight);


        params.setMargins(10,10,10,10);



        return params;
    }



    public static RelativeLayout.LayoutParams getLayoutParmsForDownloadPageAdapter(Context context){

        int height = Utils.getScreenHeight(context);
        int width = Utils.getScreenWidth(context);


        RelativeLayout.LayoutParams params1;

        if(Utils.isLongScreen(context)){
            params1=new RelativeLayout.LayoutParams((int) (width/3), (int) (height/4.7));
        }else {
            params1=new RelativeLayout.LayoutParams((int) (width/3.8), (int) (height/5));
        }
        params1.setMargins(10,10,10,10);

        return params1;
    }


    public static RelativeLayout.LayoutParams getLayoutParmsForDownloadPageAdapterEpisodes(Context context){

        int height = Utils.getScreenHeight(context);
        int width = Utils.getScreenWidth(context);


        RelativeLayout.LayoutParams params1;

        if(Utils.isLongScreen(context)){
            params1=new RelativeLayout.LayoutParams((int) (width/3), (int) (height/4.7));
        }else {
            params1=new RelativeLayout.LayoutParams((int) (width/2.7), (int) (height/7));
        }
        params1.setMargins(10,10,10,10);

        return params1;
    }


    public static FrameLayout.LayoutParams getLayoutParamsForPopularPopAdapter(Context context){

        int height = Utils.getScreenHeight(context);
        int width = Utils.getScreenWidth(context);



        FrameLayout.LayoutParams params;
        int calculatedWidth = (int) (width/1.2);
        int calculateHeight = (calculatedWidth*9) / 16;
        params = new FrameLayout.LayoutParams(calculatedWidth, calculateHeight);


//        params.setMargins(10,10,10,10);

        return params;

    }


    public static RelativeLayout.LayoutParams getLayoutForUpcomingPosters(Context context){

        int width = Utils.getScreenWidth(context);



        RelativeLayout.LayoutParams params;
        int calculatedWidth = width;
        int calculateHeight = (calculatedWidth*9) / 16;
        params = new RelativeLayout.LayoutParams(calculatedWidth, calculateHeight);


        return params;

    }

    public static RelativeLayout.LayoutParams getLayoutForSelectedItemDeletion(Context context, boolean isportrait){
        int width = Utils.getScreenWidth(context);

        RelativeLayout.LayoutParams params;
        int calculatedWidth;
        int calculatedHeight;
        if(isportrait){

            calculatedWidth = width / 3;
            calculatedHeight = (calculatedWidth * 3) / 2 - 20;
        }else{

            calculatedWidth = (int) (width / 2.5);
            calculatedHeight = (calculatedWidth * 9) / 16;
        }


        params = new RelativeLayout.LayoutParams(calculatedWidth, calculatedHeight);
        params.setMargins(10,0,10,0);

        return params;

    }



}
