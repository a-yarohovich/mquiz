package space.argames.mquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

public class Levels extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.levels);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setUpControls();
    }

    private void setUpControls() {
        findViewById(R.id.buttonBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    goBack();
                }
                catch (Exception e) {
                    //TODO
                }
            }
        });
        setupDifficultLevels(R.id.textView1, 1);
        setupDifficultLevels(R.id.textView2, 2);
        setupDifficultLevels(R.id.textView3, 3);
        setupDifficultLevels(R.id.textView4, 4);
        setupDifficultLevels(R.id.textView5, 5);
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
        Intent intent = new Intent(Levels.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void setupDifficultLevels(final int id, final int diffLevel) {
        findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Levels.this, GameLevel.class);
                    Bundle b = new Bundle();
                    b.putInt("level", diffLevel); //Your id
                    intent.putExtras(b); //Put your id to your next Intent
                    startActivity(intent);
                    finish();
                }
                catch (Exception e) {
                    //TODO
                }
            }
        });
    }
}
