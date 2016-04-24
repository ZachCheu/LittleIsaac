package genius.zach.seize.littleisaac;

import android.app.Activity;
import android.content.Intent;
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

public class end_screen extends Activity{
    private ImageButton play_btn;
    private Typeface loading_text_font;
    private SurfaceHolder _surfaceHolder;
    private TextView t, a;
    private AdView mAdView;
    //private final Paint score = new Paint();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end_screen);
        try {
            mAdView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);
        }catch (Exception e){
            e.printStackTrace();
        }
        loading_text_font = Typeface.createFromAsset(getAssets(), "fonts/loader_font.TTF");
        t = (TextView)  findViewById(R.id.titleText);
        a = (TextView) findViewById(R.id.titleText2);
        t.setTypeface(loading_text_font);
        a.setTypeface(loading_text_font);
        t.setText("Score: " + RAM.GlobalScore);
        a.setText("Highscore: " + RAM.GlobalHighScore);
        System.out.println("Successful Switch");
        //final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //this.wakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK, "Lock");
        //this.wakeLock.acquire();

        play_btn = (ImageButton) findViewById(R.id.playButton);

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    System.out.println("TEST");
                    Intent i = new Intent(end_screen.this, MainActivity.class);
                    startActivity(i);
                    //overridePendingTransition(R.anim.animation2, R.anim.animation);
                } catch (Exception e) {
                    //Error occured with starting single player game variant!
                    e.printStackTrace(System.out);
                }
            }
        });
    }
}