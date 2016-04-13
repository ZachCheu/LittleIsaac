package com.example.seize.littleisaac;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

public class missile{
    private float x;
    private float y;
    private String direction;
    private Paint f = new Paint();
    private Bitmap Rocket;


    public missile(int x, int y, Bitmap left){
        this.x = x;
        this.y = y;
        Rocket = left;
    }
    public missile(int x, int y, String direction, Bitmap right){
        this.direction = direction;
        this.x = x;
        this.y = y;
        Rocket = right;
    }
    public float getX() {
        return x;
    }
    public void setX(float x) {
        this.x = x;
    }
    public float getY() {
        return y;
    }
    public void setY(float y) {
        this.y = y;
    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(Rocket, this.x, this.y, null);
    }
}
