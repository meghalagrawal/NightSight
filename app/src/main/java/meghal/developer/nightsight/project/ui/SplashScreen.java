package meghal.developer.nightsight.project.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import meghal.developer.nightsight.project.R;
import meghal.developer.nightsight.project.ui.brightness.view.ChangeBrightness;

public class SplashScreen extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent in = new Intent(SplashScreen.this, ChangeBrightness.class);
                startActivity(in);
                finish();
            }
        }, 1000);
    }
}
