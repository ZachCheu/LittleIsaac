package genius.zach.seize.littleisaac;



import android.graphics.Bitmap;
import android.graphics.Canvas;

public class sideObject extends rectangle{
    private float x;
    private float y;
    private Bitmap grenade;

    public sideObject(int x, int y, Bitmap grenade){
        this.x = x;
        this.y = y;
        this.grenade = grenade;
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
        return grenade.getHeight();
    }
    public float getWidth(){
        return grenade.getWidth();
    }
    public void draw(Canvas canvas){
        canvas.drawBitmap(grenade, this.x, this.y, null);
    }
}
