package genius.zach.seize.littleisaac;

import android.content.Context;
import android.content.res.ObbInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Seize on 4/27/2016.
 */
class MainMenuView extends SurfaceView implements SurfaceHolder.Callback {
    class MainMenuThread extends Thread{
        /*
         * State-tracking constants
         */
        public static final int STATE_RUNNING = 1;
        public static final int STATE_PAUSE = 2;
        public static final int STATE_PLAY = 3;

        /*
         * Canvas size properties
         */
        private int mCanvasHeight = 1;
        private int mCanvasWidth = 1;

        private Bitmap mountain;
        private int color;
        private Handler mHandler;
        private int mMode;
        private boolean mRun = false;
        private final Object mRunLock = new Object();
        private SurfaceHolder mSurfaceHolder;
        private Context mContext;

        public MainMenuThread(SurfaceHolder surfaceHolder, Context context, Handler handler){
            mSurfaceHolder = surfaceHolder;
            mHandler = handler;
            mContext = context;
        }

        public void doStart(){
            synchronized (mSurfaceHolder){

            }
        }

        public void updatePhysics(){

        }

        private void doDraw(Canvas canvas){
            canvas.save();
            canvas.restore();
        }

        @Override
        public void run() {
            while (mRun) {
                Canvas c = null;
                try {
                    c = mSurfaceHolder.lockCanvas(null);
                    synchronized (mSurfaceHolder) {
                        if (mMode == STATE_RUNNING) updatePhysics();
                        // Critical section. Do not allow mRun to be set false until
                        // we are sure all canvas draw operations are complete.
                        //
                        // If mRun has been toggled false, inhibit canvas operations.
                        synchronized (mRunLock) {
                            if (mRun) doDraw(c);
                        }
                    }
                } finally {
                    // do this in a finally so that if an exception is thrown
                    // during the above, we don't leave the Surface in an
                    // inconsistent state
                    if (c != null) {
                        mSurfaceHolder.unlockCanvasAndPost(c);
                    }
                }
            }
        }

        public void pause(){
            synchronized (mSurfaceHolder){

            }
        }

        public void setState(int mode){
            synchronized (mSurfaceHolder){
                setState(mode);
            }
        }

        public void setState(int mode, CharSequence message){

        }
    }
    public MainMenuView(Context context) {
        super(context);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
