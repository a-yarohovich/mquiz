package space.argames.mquiz;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class GameLevel extends AppCompatActivity {
    //Fields
    private Random mRandom = new Random();
    private Timer mTimer;
    private ImageView mImgLeft;
    private ImageView mImgRight;
    long mTimeStart;
    private int mNumLeft;
    private int mNumRight;
    private int mCountRightAnswer;
    private int mDifficultLevel;
    private final  int[] mProgressPoints = {
            R.id.point1,
            R.id.point2,
            R.id.point3,
            R.id.point4,
            R.id.point5,
    };
    // Inner classes
    class DifficultTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    negativNext(mProgressPoints);
                    generateNumbers(mImgLeft, mImgRight);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal);
        initParams();
        showPreviewDialog();
        setUpControls();
        generateNumbers(mImgLeft, mImgRight);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setUpControls() {
        findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goBack();
                }
                catch (Exception e) {
                    // TODO
                }
            }
        });
        mImgLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mImgRight.setEnabled(false);
                    if (mNumLeft > mNumRight) {
                        mImgLeft.setImageResource(R.drawable.img_true);
                    }
                    else {
                        mImgLeft.setImageResource(R.drawable.img_false);
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mNumLeft > mNumRight) {
                        positivNext(mProgressPoints);
                    }
                    else {
                        negativNext(mProgressPoints);
                    }
                    mImgRight.setEnabled(true);
                    if (mCountRightAnswer >= mProgressPoints.length) {
                        showEndDialog();
                    }
                    else {
                        generateNumbers(mImgLeft, mImgRight);
                    }
                }
                return true;
            }
        });
        mImgRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    mImgLeft.setEnabled(false);
                    if (mNumRight > mNumLeft) {
                        mImgRight.setImageResource(R.drawable.img_true);
                    }
                    else {
                        mImgRight.setImageResource(R.drawable.img_false);
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mNumRight > mNumLeft) {
                        positivNext(mProgressPoints);
                    }
                    else {
                        negativNext(mProgressPoints);
                    }
                    mImgLeft.setEnabled(true);
                    if (mCountRightAnswer >= mProgressPoints.length) {
                        showEndDialog();
                    }
                    else {
                        generateNumbers(mImgLeft, mImgRight);
                    }
                }
                return true;
            }
        });
    }

    private void negativNext(int[] progress) {
        if (mCountRightAnswer > 0) {
            if (mCountRightAnswer == 1) {
                mCountRightAnswer = 0;
            } else {
                mCountRightAnswer -= 2;
            }
        }
        for (int value : progress) {
            findViewById(value).setBackgroundResource(R.drawable.style_points);
        }
        for (int i = 0; i < mCountRightAnswer; i++) {
            findViewById(progress[i]).setBackgroundResource(R.drawable.style_points_green);
        }
    }

    private void positivNext(int[] progress) {
        if (mCountRightAnswer < progress.length) {
            mCountRightAnswer += 1;
        }
        for (int value : progress) {
            findViewById(value).setBackgroundResource(R.drawable.style_points);
        }
        for (int i = 0; i < mCountRightAnswer; i++) {
            findViewById(progress[i]).setBackgroundResource(R.drawable.style_points_green);
        }
    }

    private void generateNumbers(ImageView imgLeft, ImageView imgRight) {
        mNumLeft = mRandom.nextInt(10);
        imgLeft.setImageResource(Array.imgs[mNumLeft]);

        final TextView textViewLeft = findViewById(R.id.txtLeft);
        textViewLeft.setText(Array.texts[mNumLeft]);

        mNumRight = mRandom.nextInt(10);
        while (mNumLeft == mNumRight) {
            mNumRight = mRandom.nextInt(10);
        }
        imgRight.setImageResource(Array.imgs[mNumRight]);
        final TextView textViewRight = findViewById(R.id.txtRight);
        textViewRight.setText(Array.texts[mNumRight]);
        startGenTimer(mDifficultLevel * 1000);
    }

    @Override
    public void onBackPressed() {
        try {
            goBack();
        }
        catch (Exception e) {
            //TODO
        }
    }

    private void goBack() {
        Intent intent = new Intent(GameLevel.this, Levels.class);
        startActivity(intent);
        finish();
    }

    private void showPreviewDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.previewdialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.findViewById(R.id.btnPreviewClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goBack();
                    //dialog.dismiss();
                }
                catch (Exception e) {
                    dialog.dismiss();
                }
            }
        });
        dialog.findViewById(R.id.btncontinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mTimeStart = System.currentTimeMillis();
                    startGenTimer(mDifficultLevel * 1000);
                    dialog.dismiss();
                }
                catch (Exception e) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    @SuppressLint("DefaultLocale")
    private String millisToHumanTime(long millis) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));

    }

    private void showEndDialog() {
        stopGenTimer();
        long timeEnd = System.currentTimeMillis();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.enddialog);
        TextView tv = dialog.findViewById(R.id.textdescEnd);
        tv.setText(getString(R.string.levelend, millisToHumanTime(timeEnd - mTimeStart)));
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
        dialog.findViewById(R.id.btncontinue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goBack();
                }
                catch (Exception e) {
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }

    private void startGenTimer(int delay) {
        stopGenTimer();
        mTimer = new Timer();
        DifficultTimerTask mTimerTask = new DifficultTimerTask();
        mTimer.schedule(mTimerTask, delay);
    }

    private void stopGenTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }

    private void initParams() {
        Bundle b = getIntent().getExtras();
        if(b != null)
            mDifficultLevel = b.getInt("level", 1);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mImgLeft = findViewById(R.id.imgLeft);
        mImgRight = findViewById(R.id.imgRight);
        TextView text_levels = findViewById(R.id.textlevels);
        text_levels.setText(String.format("%s %s", getString(R.string.level), Integer.toString(mDifficultLevel)));
    }
}
