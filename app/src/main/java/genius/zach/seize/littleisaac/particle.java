package genius.zach.seize.littleisaac;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by ZachCheu on 4/13/16.
 */
public class particle {
    private float x, y;
    private Bitmap particle1;
    private Bitmap particle2;
    //private Bitmap particle3;

    public particle(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void setParticle1(Bitmap particle1){
        this.particle1 = particle1;
    }
    public void setParticle2(Bitmap particle2){
        this.particle2 = particle2;
    }
    /*public void setParticle3(Bitmap particle3){
        this.particle3 = particle3;
    }*/
    public void draw(Canvas canvas){

    }
}
