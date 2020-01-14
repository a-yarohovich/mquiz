package space.argames.mquiz;

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

public class Level1 extends AppCompatActivity {
    Dialog dialog;
    Random random = new Random();
    private ImageView imgLeft;
    private ImageView imgRight;
    private int numLeft;
    private int numRight;
    private int count;
    private int difficultLevel;
    private final  int[] progress = {
            R.id.point1,
            R.id.point2,
            R.id.point3,
            R.id.point4,
            R.id.point5,
    };
    private Timer mTimer;
    private DifficultTimerTask mTimerTask;
    class DifficultTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    negativNext(progress);
                    generateNumbers(imgLeft, imgRight);
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.universal);
        // Fullscreen window
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        imgLeft = findViewById(R.id.imgLeft);
        imgRight = findViewById(R.id.imgRight);
        // Initialize level of difficult and init difficult image
        Bundle b = getIntent().getExtras();
        if(b != null)
            difficultLevel = b.getInt("level", 1);
        TextView text_levels = findViewById(R.id.textlevels);
        text_levels.setText(String.format("%s %s", getString(R.string.level), Integer.toString(difficultLevel)));
        // Show preview dialog
        showPreviewDialog();
        // Set Up controls on game level
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
        generateNumbers(imgLeft, imgRight);
        // Animation
        //final Animation animation = AnimationUtils.loadAnimation(Level1.this, R.anim.alpha);

        imgLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    imgRight.setEnabled(false);
                    if (numLeft > numRight) {
                        imgLeft.setImageResource(R.drawable.img_true);
                    }
                    else {
                        imgLeft.setImageResource(R.drawable.img_false);
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (numLeft > numRight) {
                        positivNext(progress);
                    }
                    else {
                        negativNext(progress);
                    }
                    imgRight.setEnabled(true);
                    if (count >= progress.length) {
                        showEndDialog();
                    }
                    else {
                        generateNumbers(imgLeft, imgRight);
                    }
                }
                return true;
            }
        });

        imgRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    imgLeft.setEnabled(false);
                    if (numRight > numLeft ) {
                        imgRight.setImageResource(R.drawable.img_true);
                    }
                    else {
                        imgRight.setImageResource(R.drawable.img_false);
                    }
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (numRight > numLeft) {
                        positivNext(progress);
                    }
                    else {
                        negativNext(progress);
                    }
                    imgLeft.setEnabled(true);
                    if (count >= progress.length) {
                        showEndDialog();
                    }
                    else {
                        generateNumbers(imgLeft, imgRight);
                    }
                }
                return true;
            }
        });
    }

    private void negativNext(int[] progress) {
        if (count > 0) {
            if (count == 1) {
                count = 0;
            } else {
                count -= 2;
            }
        }
        for (int value : progress) {
            findViewById(value).setBackgroundResource(R.drawable.style_points);
        }
        for (int i = 0; i < count; i++) {
            findViewById(progress[i]).setBackgroundResource(R.drawable.style_points_green);
        }
    }

    private void positivNext(int[] progress) {
        if (count < progress.length) {
            count += 1;
        }
        for (int value : progress) {
            findViewById(value).setBackgroundResource(R.drawable.style_points);
        }
        for (int i = 0; i < count; i++) {
            findViewById(progress[i]).setBackgroundResource(R.drawable.style_points_green);
        }
    }

    private void generateNumbers(ImageView imgLeft, ImageView imgRight) {
        numLeft = random.nextInt(10);
        imgLeft.setImageResource(Array.imgs[numLeft]);

        final TextView textViewLeft = findViewById(R.id.txtLeft);
        textViewLeft.setText(Array.texts[numLeft]);

        numRight = random.nextInt(10);
        while (numLeft == numRight) {
            numRight = random.nextInt(10);
        }
        imgRight.setImageResource(Array.imgs[numRight]);
        final TextView textViewRight = findViewById(R.id.txtRight);
        textViewRight.setText(Array.texts[numRight]);
        startGenTimer(difficultLevel * 1000);
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
        Intent intent = new Intent(Level1.this, Levels.class);
        startActivity(intent);
        finish();
    }

    private void showEndDialog() {
        stopGenTimer();
        // Show preview dialog
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.enddialog);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        dialog.setCancelable(false);
        dialog.findViewById(R.id.btncontinue).setOnClickListener(new View.OnClickListener() {
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
        dialog.show();
    }

    private void showPreviewDialog() {
        dialog = new Dialog(this);
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
                    startGenTimer(difficultLevel * 1000);
                    dialog.dismiss();
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
        mTimerTask = new DifficultTimerTask();
        mTimer.schedule(mTimerTask, delay);
    }

    private void stopGenTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
    }
}
