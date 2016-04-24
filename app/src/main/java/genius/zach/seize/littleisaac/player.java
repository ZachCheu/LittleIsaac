package genius.zach.seize.littleisaac;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;

//import java.awt.*;

/**
 * Created by Reefer on 2/13/16.
 */
public class player {
    private float x, y;
    private float width, height;
    private Bitmap player_bitmap_right;
    private Bitmap player_bitmap_left;
    public Paint p1;
    private float px, py;
    private Matrix matrix;
    private int direction = 0;
    //0 = right 1 = left


    public player(){

    }

    public player(float x, float y, int direction){
        this.x = x;
        this.y = y;
        this.direction = direction;
        p1 = new Paint(Paint.ANTI_ALIAS_FLAG);
        p1.setColor(Color.parseColor("#000000"));
        p1.setStyle(Paint.Style.FILL);
        matrix = new Matrix();
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }

    public void setPlayer_bitmap_right(Bitmap player_bitmap_right){
        this.player_bitmap_right = player_bitmap_right;
    }

    public void setPlayer_bitmap_left(Bitmap player_bitmap_left){
        this.player_bitmap_left = player_bitmap_left;
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
        return player_bitmap_left.getWidth();
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return player_bitmap_left.getHeight();
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public void setDirection(int direction){
        this.direction = direction;
    }

    public void draw(Canvas canvas){
        matrix.reset();
        if(RAM.hitLfallR) {
            RAM.rotationAngle += 5;
            px = this.player_bitmap_left.getWidth() / 2;
            py = this.player_bitmap_left.getHeight() / 2;
            matrix.postTranslate(-player_bitmap_left.getWidth() / 2, -player_bitmap_left.getHeight() / 2);
            matrix.postRotate(RAM.rotationAngle);
            matrix.postTranslate(px + this.x, py + this.y);
            canvas.drawBitmap(this.player_bitmap_left, matrix, null);
        }else if(RAM.hitRfallL){
            RAM.rotationAngle -=5;
            px = this.player_bitmap_left.getWidth()/2;
            py = this.player_bitmap_left.getHeight()/2;
            matrix.postTranslate(-player_bitmap_left.getWidth()/2, -player_bitmap_left.getHeight()/2);
            matrix.postRotate(RAM.rotationAngle);
            matrix.postTranslate(px + this.x, py + this.y);
            canvas.drawBitmap(this.player_bitmap_right, matrix, null);
        }else if(RAM.fallL){
            RAM.rotationAngle -=10;
            px = this.player_bitmap_left.getWidth()/2;
            py = this.player_bitmap_left.getHeight()/2;
            matrix.postTranslate(-player_bitmap_left.getWidth()/2, -player_bitmap_left.getHeight()/2);
            matrix.postRotate(RAM.rotationAngle);
            matrix.postTranslate(px + this.x, py + this.y);
            canvas.drawBitmap(this.player_bitmap_right, matrix, null);
        }else if(RAM.fallR){
            RAM.rotationAngle += 10;
            px = this.player_bitmap_left.getWidth() / 2;
            py = this.player_bitmap_left.getHeight() / 2;
            matrix.postTranslate(-player_bitmap_left.getWidth() / 2, -player_bitmap_left.getHeight() / 2);
            matrix.postRotate(RAM.rotationAngle);
            matrix.postTranslate(px + this.x, py + this.y);
            canvas.drawBitmap(this.player_bitmap_left, matrix, null);
        }
        else if(this.direction == 0){
            canvas.drawBitmap(this.player_bitmap_left, this.x, this.y, null);
        }else{
            canvas.drawBitmap(this.player_bitmap_right, this.x, this.y, null);
        }
    }
}
