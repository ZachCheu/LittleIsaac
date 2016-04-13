package com.example.seize.littleisaac;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class fallingObject extends rectangle{
    private float x;
    private float y;
    private Bitmap Bomb;
    private float height, width;

    public fallingObject(int x, int y, Bitmap bomb){
        this.Bomb = bomb;
        this.x = x;
        this.y = y;
        height = bomb.getHeight();
        width = bomb.getWidth();
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
    public float getHeight(){
        return height;
    }
    public float getWidth(){
        return width;
    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(Bomb, this.x, this.y, null);
    }
}

