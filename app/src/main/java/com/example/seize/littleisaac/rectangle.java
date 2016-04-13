package com.example.seize.littleisaac;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import static android.graphics.Color.BLACK;

/**
 * Created by Reefer on 2/13/16.
 */
public class rectangle {

    private float x;
    private float y;
    private float width;
    private float length;
    private int color;
    private Paint f = new Paint(Paint.ANTI_ALIAS_FLAG);

    public rectangle(){

    }

    public rectangle(float x, float y, float width, float height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.length = height;
        this.color = BLACK;
        f.setColor(Color.BLACK);
        f.setStyle(Paint.Style.FILL);
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

    public float getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public float getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public void draw(Canvas canvas){

        canvas.drawRect(this.x, this.y, this.x + this.width, this.y + this.length, f);

    }
}
