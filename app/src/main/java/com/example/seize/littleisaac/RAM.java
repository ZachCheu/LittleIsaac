package com.example.seize.littleisaac;

import android.graphics.Bitmap;
import android.graphics.Paint;

public class RAM {
    //screen dimensions (used for scaling)
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;
    //Game Textures
    public static Bitmap platform_end_left, platform_end_right, platform_middle;
    public static Bitmap platform_under;
    public static Bitmap platform_under_left;
    public static Bitmap platform_under_right;
    public static Bitmap player_bitmap_right, player_bitmap_left;
    public static Bitmap tree_type_1, tree_type_2;
    public static Bitmap cloud_render;
    public static Bitmap mountains;
    public static Bitmap portal_1, portal_2;
    public static Bitmap RocketBit1, RocketBit2;
    public static Bitmap BombBit;
    public static Bitmap GrenadeBit;

    public static player player_user;
    public static cloud cloud_render_1;
    public static cloud cloud_render_2;
    public static cloud cloud_render_3;
    public static missile Rocket_1, Rocket_2;
    public static fallingObject Bomb;
    public static sideObject Grenade_1,Grenade_2;

    public static boolean isFalling = false;
    public static float modifier;
    public static float _score_counter;
    public static boolean isJump = false;
    public static boolean hitLfallR = false;
    public static boolean hitRfallL = false;
    public static int t = 0;
    public static boolean isCountDownTimerFinished = false;


    //different paint buckets
    //Look on how to use less resources!
    public static Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint paint3 = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint paint4 = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint paint5 = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint paint6 = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint paint7 = new Paint(Paint.ANTI_ALIAS_FLAG);
    public static Paint paint8 = new Paint(Paint.ANTI_ALIAS_FLAG);

}