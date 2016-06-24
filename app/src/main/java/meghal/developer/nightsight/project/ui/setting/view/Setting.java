package meghal.developer.nightsight.project.ui.setting.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import meghal.developer.nightsight.project.R;
import meghal.developer.nightsight.project.helper.SharedPrefs;

public class Setting extends AppCompatActivity {

    private Toolbar toolbar;
    private SharedPrefs sharedPrefs;
    private CheckBox showNotification;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_arrow_back_black_36dp));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sharedPrefs = new SharedPrefs(this);
        showNotification = (CheckBox) findViewById(R.id.showNotification);

        if(sharedPrefs.showNotification()){
         showNotification.setChecked(true);
        }else{
            showNotification.setChecked(false);
        }
        showNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {
                    sharedPrefs.setShowNotification(true);
                } else {
                    sharedPrefs.setShowNotification(false);
                }
            }
        });

    }
}
