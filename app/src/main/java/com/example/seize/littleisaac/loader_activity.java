package com.example.seize.littleisaac;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class loader_activity extends Activity implements SurfaceHolder.Callback {

    //Gameloop requirements
    private Handler handlerApplication;
    private SurfaceHolder _surfaceHolder;
    private SurfaceView _surfaceView;

    //Variables associated with GameLoopThread
    private GameLoopThread gameLoopThread;
    static boolean textures_isFinished_loading;
    static boolean isGameLoopThreadRunning = false;

    //UI Components
    private ProgressBar loader_spinner;
    private Typeface loading_text_font;
    private TextView loading_text;
    private View front_cover;
    private Typeface score_font;
    private TextView score;

    //projectile components
    int randobj, rvalue, fallnear,sUpDown = 20,sChange;
    Random r = new Random();
    boolean mleft = true, sleft = false, everyOther = true,enablehit =true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //make view full screen and remove actionbar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(0xFFFFFFFF, WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //Get Display Width and Height
        Display device_display = getWindowManager().getDefaultDisplay();
        RAM.SCREEN_HEIGHT = device_display.getHeight();
        RAM.SCREEN_WIDTH = device_display.getWidth();
        //load activity_loader_activity
        setContentView(R.layout.activity_loader_activity);
        //textures have not loaded yet, so set boolean to false
        textures_isFinished_loading = false;
        //Connect and modify UI Components
        loader_spinner = (ProgressBar) findViewById(R.id.loader);
        loader_spinner.setVisibility(View.VISIBLE);
        loading_text_font = Typeface.createFromAsset(getAssets(), "fonts/loader_font.TTF");
        loading_text = (TextView) findViewById(R.id.Loader_text);
        loading_text.setTypeface(loading_text_font);
        score_font = Typeface.createFromAsset(getAssets(),"fonts/loader_font.TTF");
        score = (TextView) findViewById(R.id.Loader_text);
        front_cover = (View) findViewById(R.id.front_cover);

        _surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        _surfaceHolder = _surfaceView.getHolder();
        _surfaceHolder.addCallback(this);


        //OnTouchListener setup
        _surfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        RAM.isJump = true;
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        //
        //Device Tilt Setup
        ((SensorManager) getSystemService(Context.SENSOR_SERVICE)).registerListener(
                new SensorEventListener() {
                    @Override
                    public void onSensorChanged(SensorEvent sensorEvent) {
                        if (isGameLoopThreadRunning) {
                            if (-sensorEvent.values[0] > 1) {
                                RAM.player_user.setDirection(1);
                                RAM.modifier = 15f;
                            } else if (-sensorEvent.values[0] < -1) {
                                RAM.player_user.setDirection(0);
                                RAM.modifier = -15f;
                            } else {
                                RAM.modifier = 0.0f;
                            }
                            //modifier = 2.5f * -sensorEvent.values[0];
                            //figure.setX((figure.getX() + (10*-sensorEvent.values[0])));
                        }
                    }

                    //ignore for now
                    @Override
                    public void onAccuracyChanged(Sensor sensor, int i) {

                    }
                }, ((SensorManager) getSystemService(Context.SENSOR_SERVICE))
                        .getSensorList(Sensor.TYPE_ACCELEROMETER).get(0),
                SensorManager.SENSOR_DELAY_NORMAL);

        new CountDownTimer(2100, 1000) {
            //Every tick perform what...
            public void onTick(long millisUntilFinished) {

            }

            //Give 2.1 seconds for the textures to load
            public void onFinish() {
                loader_spinner.setVisibility(View.INVISIBLE);
                loading_text.setVisibility(View.INVISIBLE);
                for(int i = 255000; i >= 0; i -= 100){
                    front_cover.getBackground().setAlpha(i/100);
                }


            }
        }.start();
        MediaPlayer mp = MediaPlayer.create(loader_activity.this, R.raw.tetris_gameboy_soundtrack);
        mp.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        gameLoopThread = new GameLoopThread(_surfaceHolder, new Handler() {
            @Override
            public void close() {

            }

            @Override
            public void flush() {

            }

            @Override
            public void publish(LogRecord logRecord) {

            }
        });
        gameLoopThread.setRunning(true);
        gameLoopThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
        gameLoopThread.setSurfaceSize(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        boolean retry = true;
        gameLoopThread.setRunning(false);
        while (retry) {
            try {
                gameLoopThread.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //Nested class File
    //Clase Name: GameLoopThread
    //Objective: Canvas Setup with SurfaceView

    class GameLoopThread extends Thread {
        private GamePhysicsThread gamePhysicsThread;

        public GameLoopThread(SurfaceHolder surfaceHolder, Handler handler) {
            _surfaceHolder = surfaceHolder;
            handlerApplication = handler;
            gamePhysicsThread = new GamePhysicsThread();
        }

        public boolean getRunning() {
            return isGameLoopThreadRunning;
        }

        public void doStart() {
            synchronized (_surfaceHolder) {
                //load textures here!
                RAM.player_bitmap_right = BitmapFactory.decodeResource(getResources(), R.drawable.character_right);
                RAM.player_bitmap_left = BitmapFactory.decodeResource(getResources(), R.drawable.character_left);

                //load grenade texture
                RAM.GrenadeBit = BitmapFactory.decodeResource(getResources(),R.drawable.grenade);
                RAM.Grenade_1 = new sideObject(-50,(2*RAM.SCREEN_HEIGHT/3)-400,RAM.GrenadeBit);
                RAM.Grenade_2 = new sideObject(RAM.SCREEN_WIDTH,(2*RAM.SCREEN_HEIGHT/3)-400,RAM.GrenadeBit);

                //load bomb texture
                RAM.BombBit = BitmapFactory.decodeResource(getResources(),R.drawable.bomb);
                RAM.Bomb = new fallingObject(0,-50,RAM.BombBit);

                //load missile texture
                RAM.RocketBit1 = BitmapFactory.decodeResource(getResources(), R.drawable.rocketleft);
                RAM.RocketBit2 = BitmapFactory.decodeResource(getResources(),R.drawable.rocketright);
                RAM.Rocket_1 = new missile(-10,(2*RAM.SCREEN_HEIGHT/3)-75,RAM.RocketBit1);
                RAM.Rocket_2 = new missile(RAM.SCREEN_WIDTH,(2*RAM.SCREEN_HEIGHT/3)-75,RAM.RocketBit2);

                //resize
                RAM.player_bitmap_right = getResizedBitmap(RAM.player_bitmap_right, 108, 44);
                RAM.player_bitmap_left = getResizedBitmap(RAM.player_bitmap_left, 108, 44);

                //load tree data!
                RAM.tree_type_1 = BitmapFactory.decodeResource(getResources(), R.drawable.tree_type_1);
                RAM.tree_type_2 = BitmapFactory.decodeResource(getResources(), R.drawable.tree_type_2);

                //load clouds!
                RAM.cloud_render = BitmapFactory.decodeResource(getResources(), R.drawable.cloud_render);
                RAM.cloud_render = getResizedBitmap(RAM.cloud_render, 67, 204);
                RAM.cloud_render_1 = new cloud(RAM.SCREEN_WIDTH/300, 210, RAM.cloud_render);
                RAM.cloud_render_2 = new cloud(RAM.SCREEN_WIDTH - 2 * RAM.cloud_render.getWidth(), (RAM.SCREEN_HEIGHT / 3 + 100), RAM.cloud_render);
                RAM.cloud_render_3 = new cloud(RAM.SCREEN_WIDTH - RAM.cloud_render.getWidth(), (RAM.SCREEN_HEIGHT / 3 - 150), RAM.cloud_render);

                //load platform tiles!
                RAM.platform_end_left = BitmapFactory.decodeResource(getResources(), R.drawable.left_end);
                RAM.platform_end_right = BitmapFactory.decodeResource(getResources(), R.drawable.right_end);
                RAM.platform_middle = BitmapFactory.decodeResource(getResources(), R.drawable.mid_section);

                //resize
                RAM.platform_end_left = getResizedBitmap(RAM.platform_end_left, 25, 25);
                RAM.platform_middle = getResizedBitmap(RAM.platform_middle, 25, 25);
                RAM.platform_end_right = getResizedBitmap(RAM.platform_end_right, 25, 25);

                RAM.paint.setColor(Color.parseColor("#e8f1ef"));
                RAM.paint1.setColor(Color.parseColor("#e0eeec"));
                RAM.paint2.setColor(Color.parseColor("#d8ece8"));
                RAM.paint3.setColor(Color.parseColor("#cde3e1"));
                RAM.paint4.setColor(Color.parseColor("#c8d7d5"));
                RAM.paint5.setColor(Color.parseColor("#bed0cd"));
                RAM.paint6.setColor(Color.parseColor("#000000"));
                RAM.paint7.setColor(Color.parseColor("#608342"));
                RAM.paint8.setColor(Color.parseColor("#847543"));

                RAM.player_user = new player(500, 2 * (RAM.SCREEN_HEIGHT / 3) - RAM.player_bitmap_left.getHeight(), 0);
                RAM.player_user.setPlayer_bitmap_right(RAM.player_bitmap_right);
                RAM.player_user.setPlayer_bitmap_left(RAM.player_bitmap_left);

                RAM.platform_under = BitmapFactory.decodeResource(getResources(), R.drawable.middle_piece);
                RAM.platform_under = getResizedBitmap(RAM.platform_under, 25, 25);

                RAM.platform_under_left = BitmapFactory.decodeResource(getResources(), R.drawable.pleb_1);
                RAM.platform_under_right = BitmapFactory.decodeResource(getResources(), R.drawable.pleb_2);
                RAM.platform_under_right = getResizedBitmap(RAM.platform_under_right, 25, 25);
                RAM.platform_under_left = getResizedBitmap(RAM.platform_under_left, 25, 25);

                RAM.mountains = BitmapFactory.decodeResource(getResources(), R.drawable.mountain);
                RAM.mountains = getResizedBitmap(RAM.mountains, RAM.SCREEN_WIDTH, RAM.SCREEN_WIDTH);

                RAM.portal_1 = BitmapFactory.decodeResource(getResources(), R.drawable.stand_1);
                RAM.portal_2 = BitmapFactory.decodeResource(getResources(), R.drawable.stand_2);

                RAM.portal_1 = getResizedBitmap(RAM.portal_1, 50, 50);
                RAM.portal_2 = getResizedBitmap(RAM.portal_2, 50, 50);

                //END OF LOADING TEXTURES//
                textures_isFinished_loading = true;

            }
        }

        public void run() {
            long tickFPS = 1000 / 60;
            long startTime;
            long sleepTime;
            gamePhysicsThread.setRunning(true);
            while (isGameLoopThreadRunning) {
                Canvas c = null;
                startTime = System.currentTimeMillis();
                try {
                    c = _surfaceHolder.lockCanvas(null);
                    synchronized (_surfaceHolder) {
                        //update, draw
                        gamePhysicsThread.update();
                        doDraw(c);
                    }
                } finally {
                    if (c != null) {
                        _surfaceHolder.unlockCanvasAndPost(c);
                    }
                }
                sleepTime = tickFPS - (System.currentTimeMillis() - startTime);
                try {
                    if (sleepTime > 0) {
                        sleep(sleepTime);
                    } else {
                        sleep(10);
                    }
                } catch (Exception e) {

                }
            }
        }

        public void setRunning(boolean b) {
            isGameLoopThreadRunning = b;
        }

        public void setSurfaceSize(int width, int height) {
            synchronized (_surfaceHolder) {
                doStart();
            }
        }


        public void doDraw(Canvas canvas) {
            if (isGameLoopThreadRunning) {
                canvas.save();
                canvas.drawColor(Color.parseColor("#FFFFFF"));

                //draw background tiles!
                canvas.drawRect(0, 0, RAM.SCREEN_WIDTH, RAM.SCREEN_HEIGHT / 3, RAM.paint);
                canvas.drawRect(0, RAM.SCREEN_HEIGHT / 3, RAM.SCREEN_WIDTH, RAM.SCREEN_HEIGHT / 3 + RAM.SCREEN_HEIGHT / 9, RAM.paint1);
                canvas.drawRect(0, RAM.SCREEN_HEIGHT / 3 + RAM.SCREEN_HEIGHT / 9, RAM.SCREEN_WIDTH, RAM.SCREEN_HEIGHT / 3 + 2 * (RAM.SCREEN_HEIGHT / 9), RAM.paint2);
                canvas.drawRect(0, RAM.SCREEN_HEIGHT / 3 + 2 * (RAM.SCREEN_HEIGHT / 9), RAM.SCREEN_WIDTH, RAM.SCREEN_HEIGHT / 3 + 3 * (RAM.SCREEN_HEIGHT / 9), RAM.paint3);
                canvas.drawRect(0, RAM.SCREEN_HEIGHT / 3 + 3 * (RAM.SCREEN_HEIGHT / 9), RAM.SCREEN_WIDTH, RAM.SCREEN_HEIGHT / 3 + 4 * (RAM.SCREEN_HEIGHT / 9), RAM.paint4);
                canvas.drawRect(0, RAM.SCREEN_HEIGHT / 3 + 4 * (RAM.SCREEN_HEIGHT / 9), RAM.SCREEN_WIDTH, RAM.SCREEN_HEIGHT / 3 + 5 * (RAM.SCREEN_HEIGHT / 9), RAM.paint5);
                canvas.drawBitmap(RAM.mountains, 0, RAM.SCREEN_HEIGHT - RAM.mountains.getHeight(), null);

                //render trees here!
                for (int i = 0; i < RAM.SCREEN_HEIGHT / RAM.tree_type_1.getWidth(); i++) {
                    if (i % 2 == 0) {
                        canvas.drawBitmap(RAM.tree_type_1, i * RAM.tree_type_1.getWidth(), RAM.SCREEN_HEIGHT - 100 - RAM.tree_type_1.getHeight(), null);
                    } else {
                        canvas.drawBitmap(RAM.tree_type_2, i * RAM.tree_type_2.getWidth(), RAM.SCREEN_HEIGHT - 100 - RAM.tree_type_2.getHeight(), null);
                    }
                }

                RAM.player_user.draw(canvas);
                canvas.drawBitmap(RAM.platform_end_left, 150 - RAM.platform_end_left.getWidth(), 2 * (RAM.SCREEN_HEIGHT / 3), null);
                for(int i = 150 ; i<=RAM.SCREEN_WIDTH-125;i+=25){
                    canvas.drawBitmap(RAM.platform_middle, i-25 + RAM.platform_middle.getWidth(), 2 * (RAM.SCREEN_HEIGHT / 3), null);
                }
                canvas.drawBitmap(RAM.platform_end_right, RAM.SCREEN_WIDTH - 125, 2 * (RAM.SCREEN_HEIGHT / 3), null);
                for (int i = 200; i <= RAM.SCREEN_WIDTH-175; i+=25) {
                    canvas.drawBitmap(RAM.platform_under, i - 25+ RAM.platform_under.getWidth(), 2 * (RAM.SCREEN_HEIGHT / 3) + RAM.platform_under.getHeight(), null);
                }

                RAM.cloud_render_1.draw(canvas);
                RAM.cloud_render_2.draw(canvas);
                RAM.cloud_render_3.draw(canvas);
                RAM.Rocket_1.draw(canvas);
                RAM.Rocket_2.draw(canvas);
                RAM.Bomb.draw(canvas);
                RAM.Grenade_1.draw(canvas);
                RAM.Grenade_2.draw(canvas);
                if(RAM.hitLfallR){
                    //RAM.player_bitmap_right = RotateBitmap(RAM.player_bitmap_right, 5f);
                }
                if(RAM.hitRfallL){
                    //RAM.player_bitmap_left = RotateBitmap(RAM.player_bitmap_left, -5f);
                }

                for (int i = 1; i <= (RAM.SCREEN_HEIGHT - 2 * (RAM.SCREEN_HEIGHT / 3) + RAM.platform_under_left.getHeight()) / RAM.portal_1.getHeight(); i++) {
                    if (i % 2 == 0) {
                        canvas.drawBitmap(RAM.portal_1, 225 + RAM.platform_under_left.getWidth(), 2 * (RAM.SCREEN_HEIGHT / 3) + i * RAM.portal_1.getHeight(), null);
                    } else {
                        canvas.drawBitmap(RAM.portal_2, 225 + RAM.platform_under_left.getWidth(), 2 * (RAM.SCREEN_HEIGHT / 3) + i * RAM.portal_1.getHeight(), null);
                    }
                }

                for (int i = 1; i <= (RAM.SCREEN_HEIGHT - 2 * (RAM.SCREEN_HEIGHT / 3) + RAM.platform_under_left.getHeight()) / RAM.portal_1.getHeight(); i++) {
                    if (i % 2 == 0) {
                        canvas.drawBitmap(RAM.portal_2, RAM.SCREEN_WIDTH - (225 + 2*RAM.platform_under_left.getWidth()), 2 * (RAM.SCREEN_HEIGHT / 3) + i * RAM.portal_1.getHeight(), null);
                    } else {
                        canvas.drawBitmap(RAM.portal_1, RAM.SCREEN_WIDTH - (225 + 2*RAM.platform_under_left.getWidth()), 2 * (RAM.SCREEN_HEIGHT / 3) + i * RAM.portal_1.getHeight(), null);
                    }
                }

                canvas.drawRect(0, RAM.SCREEN_HEIGHT - 100, RAM.SCREEN_WIDTH, RAM.SCREEN_HEIGHT - 100 + 20, RAM.paint6);
                canvas.drawRect(0, RAM.SCREEN_HEIGHT - 100 + 20, RAM.SCREEN_WIDTH, RAM.SCREEN_HEIGHT - 100 + 60, RAM.paint7);
                canvas.drawRect(0, RAM.SCREEN_HEIGHT - 100 + 60, RAM.SCREEN_WIDTH, RAM.SCREEN_HEIGHT, RAM.paint8);

            }
            canvas.restore();
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
    }

    class GamePhysicsThread {
        private boolean isRunning = false;

        public GamePhysicsThread() {

        }

        public GamePhysicsThread(boolean setRun) {
            this.isRunning = setRun;
        }

        public void setRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }
        public void update() {
            randobj++;
            if(randobj>150){
                rvalue = r.nextInt(3);
                randobj = 0;
                if(rvalue == 1){
                    fallnear = r.nextInt(500);
                    RAM.Bomb.setX(RAM.player_user.getX()-250+fallnear);
                }
                if(rvalue == 2){
                    sChange = r.nextInt(10);
                }
            }
            else if(rvalue == 0){
                if(mleft)
                    if(RAM.Rocket_1.getX() >= RAM.SCREEN_WIDTH){
                        RAM.Rocket_1.setX(-100);
                        randobj = 180;
                        mleft = false;
                    }else if((enablehit&&(RAM.Rocket_1.getX()+90 > RAM.player_user.getX()) && RAM.Rocket_1.getX()<RAM.player_user.getX()+RAM.player_user.getWidth())&& (RAM.player_user.getY()+RAM.player_user.getHeight()>RAM.Rocket_1.getY())){
                        RAM.Rocket_1.setX(-100);
                        randobj = 180;
                        mleft = false;
                        RAM.isFalling = true;
                        RAM.hitLfallR = true;
                        enablehit = false;
                    }
                    else{
                        RAM.Rocket_1.setX(RAM.Rocket_1.getX()+20f);
                    }
                else{
                    if(RAM.Rocket_2.getX() < -50){
                        RAM.Rocket_2.setX(RAM.SCREEN_WIDTH);
                        randobj = 180;
                        mleft = true;
                    }else if((enablehit&&((RAM.Rocket_2.getX() < RAM.player_user.getX()+RAM.player_user.getWidth()) && RAM.Rocket_2.getX()+90>RAM.player_user.getX()+RAM.player_user.getWidth())&& (RAM.player_user.getY()+RAM.player_user.getHeight()>RAM.Rocket_2.getY()))){
                        RAM.Rocket_2.setX(RAM.SCREEN_WIDTH);
                        randobj = 180;
                        mleft = true;
                        RAM.isFalling = true;
                        RAM.hitRfallL = true;
                        enablehit = false;
                    }
                    else{
                        RAM.Rocket_2.setX(RAM.Rocket_2.getX()-20f);
                    }
                }
            }
            else if(rvalue == 1){
                    if(RAM.Bomb.getY()>(2*RAM.SCREEN_HEIGHT/3)-50){
                        RAM.Bomb.setY(-50);
                        randobj = 180;
                    }else if(enablehit&&((RAM.Bomb.getY()+RAM.Bomb.getHeight()>RAM.player_user.getY()) && (Math.abs(RAM.Bomb.getX()-RAM.player_user.getX()))<RAM.Bomb.getWidth())){
                        RAM.Bomb.setY(-50);
                        randobj = 180;
                        RAM.isFalling = true;
                        enablehit = false;
                    }
                    else {
                        RAM.Bomb.setY(RAM.Bomb.getY() + 25f);
                    }
            }
            else if(rvalue == 2){
                if(sleft){
                    if(RAM.Grenade_1.getY()>(2*RAM.SCREEN_HEIGHT/3)-50){
                        RAM.Grenade_1.setY((2*RAM.SCREEN_HEIGHT/3)-400);
                        randobj = 180;
                        sleft = false;
                        sUpDown = 20;
                        RAM.Grenade_1.setX(-50);
                    }else{
                        RAM.Grenade_1.setY(RAM.Grenade_1.getY()-sUpDown--);
                        RAM.Grenade_1.setX(RAM.Grenade_1.getX() + 15f+sChange);
                    }
                }else{
                    if(RAM.Grenade_2.getY()>(2*RAM.SCREEN_HEIGHT/3)-50){
                        RAM.Grenade_2.setY((2*RAM.SCREEN_HEIGHT/3)-400);
                        randobj = 180;
                        sleft = true;
                        sUpDown = 20;
                        RAM.Grenade_2.setX(RAM.SCREEN_WIDTH);
                    }else{
                        RAM.Grenade_2.setY(RAM.Grenade_2.getY()-sUpDown--);
                        RAM.Grenade_2.setX(RAM.Grenade_2.getX()-15f+sChange);
                    }
                }
            }
            if (RAM.cloud_render_1.getX() >= RAM.SCREEN_WIDTH) {
                RAM.cloud_render_1.setX(-RAM.cloud_render_1.getWidth());
            } else {
                RAM.cloud_render_1.setX(RAM.cloud_render_1.getX() + 0.5f);
            }

            if(RAM.cloud_render_3.getX() >= RAM.SCREEN_WIDTH){
                RAM.cloud_render_3.setX(-RAM.cloud_render_3.getWidth());
            }else {
                RAM.cloud_render_3.setX(RAM.cloud_render_3.getX() + 0.5f);
            }

            if (RAM.cloud_render_2.getX() - RAM.cloud_render_2.getWidth() >= 0) {
                RAM.cloud_render_2.setX(RAM.SCREEN_WIDTH - RAM.cloud_render_2.getWidth());
            } else {
                RAM.cloud_render_2.setX(RAM.cloud_render_2.getX() - 1.0f);
            }

            if (RAM.isJump) {
                if (RAM.t == 75) {
                    RAM.isJump = false;
                    RAM.t = 0;
                    //player_user.setY(2*(screen_height/3) - player_bitmap_left.getHeight());
                } else {

                    RAM.t += 3;
                }
                RAM.player_user.setY((float) (RAM.player_user.getY() - 37.5 + RAM.t));
                //figure.setY((float) (figure.getY() - 37.5 + t));
            }
            RAM.player_user.setX(RAM.player_user.getX() + RAM.modifier);

            if (RAM.player_user.getY() >= RAM.SCREEN_HEIGHT) {
                RAM.player_user.setY(2 * (RAM.SCREEN_HEIGHT / 3) - RAM.player_bitmap_left.getHeight());
                RAM.player_user.setX(RAM.SCREEN_WIDTH / 2);
                RAM.isFalling = false;
            }

            if (RAM.player_user.getX() > RAM.SCREEN_WIDTH-125) {
                //player_user.setY(player_user.getY() - 50);
                RAM.isFalling = true;
            } else if (RAM.player_user.getX() < 125 - RAM.platform_end_left.getWidth()) {
                RAM.isFalling = true;
                //player_user.setY(player_user.getY() - 50);
            } else {
                //to do
            }

            if (RAM.isFalling) {
                RAM.modifier = 0;
                if(everyOther){
                    RAM.deadMovement--;
                    everyOther = false;
                }else{
                    everyOther = true;
                }
                RAM.player_user.setY(RAM.player_user.getY() + RAM._score_counter);
                if(RAM.hitRfallL) {
                    RAM.player_user.setX(RAM.player_user.getX() - RAM.deadMovement);
                }else if(RAM.hitLfallR)
                    RAM.player_user.setX(RAM.player_user.getX() + RAM.deadMovement);
                RAM._score_counter++;
            } else {

            }
            if (RAM.player_user.getY() >= RAM.SCREEN_HEIGHT) {
                RAM.player_user.setY(2 * (RAM.SCREEN_HEIGHT / 3) - RAM.player_bitmap_left.getHeight());
                RAM.player_user.setX(RAM.SCREEN_WIDTH / 2);
                RAM.isFalling = false;
                RAM._score_counter = 0;
                RAM.rotationAngle = 0;
                RAM.hitRfallL = false;
                RAM.hitLfallR = false;
                RAM.deadMovement = 30;
                enablehit = true;
            }
        }
    }
}
