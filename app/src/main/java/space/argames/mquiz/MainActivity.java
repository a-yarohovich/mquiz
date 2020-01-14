package space.argames.mquiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private long backPressedTime;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setUpControls();
    }

    private void setUpControls() {
        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MainActivity.this, Levels.class);
                    startActivity(intent);
                    finish();
                }
                catch (Exception e) {
                    finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()) {
            backToast.cancel();
            super.onBackPressed();
        }
        else {
            backToast = Toast.makeText(getBaseContext(), "Нажмите еще раз, чтобы выйти", Toast.LENGTH_SHORT);
            backToast.setGravity(Gravity.CENTER, 0, 0);
            backToast.show();
        }
        backPressedTime = System.currentTimeMillis();
    }
}
