package genius.zach.seize.littleisaac;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.seize.littleisaac.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by ZachCheu on 4/17/16.
 */

public class MainActivity extends Activity{
        private ImageButton play_btn1;
    private SurfaceHolder _surfaceHolder;
    private TextView t, a;
    private Paint titlePaint = new Paint();
    private Typeface loading_text_font;
    private AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
        t = (TextView)  findViewById(R.id.titleText);
        /*titlePaint.setTextSize(150);
        titlePaint.setColor(Color.parseColor("#f4bcc4"));*/
        loading_text_font = Typeface.createFromAsset(getAssets(), "fonts/loader_font.TTF");
        t.setTypeface(loading_text_font);
        t.setText("Tilt");
        System.out.println("Successful Switch");
        //final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //this.wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Lock");
        //this.wakeLock.acquire();

        play_btn1 = (ImageButton) findViewById(R.id.playButton1);
        play_btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("TEST");
                    Intent k = new Intent(MainActivity.this, loader_activity.class);
                    startActivity(k);

                    //overridePendingTransition(R.anim.animation2, R.anim.animation);
                } catch (Exception e) {
                    //Error occured with starting single player game variant!
                    e.printStackTrace();
                }
            }
        });
    }
}