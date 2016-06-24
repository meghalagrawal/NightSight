package meghal.developer.nightsight.project.ui.brightness.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import meghal.developer.nightsight.project.Events.BrightnessEvent;
import meghal.developer.nightsight.project.Events.ColorEvent;
import meghal.developer.nightsight.project.R;
import meghal.developer.nightsight.project.helper.SharedPrefs;
import meghal.developer.nightsight.project.services.Darkness;
import meghal.developer.nightsight.project.ui.brightness.model.ColorData;
import meghal.developer.nightsight.project.ui.setting.view.Setting;

public class ChangeBrightness extends AppCompatActivity {

    private static final String TAG = "ChangeBrightness";
    private SeekBar seekBar;
    private Button startStop;
    private SharedPrefs sharedPrefs;
    private RecyclerView recyclerView;
    private ColorAdapter colorAdapter;
    private AdView adView;
    TextView seekValue;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_brightness);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initialize();

        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ChangeBrightness.this, Darkness.class);

                if (sharedPrefs.isService()) {
                    stopService(in);
                    sharedPrefs.setService(false);
                    startStop.setText("Start");
                } else {
                    getApplicationContext().startService(in);
                    sharedPrefs.setService(true);
                    startStop.setText("Stop");

                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue,
                                          boolean fromUser) {

                seekValue.setText("( Brightness " + progresValue / 2 + "% )");

                if (sharedPrefs.isService()) {
                    sharedPrefs.setBrightness(progresValue);
                    BrightnessEvent brightnessEvent = new BrightnessEvent(sharedPrefs.getBrightness());
                    EventBus.getDefault().postSticky(brightnessEvent);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        if (sharedPrefs.getColor() == 0) {
            sharedPrefs.setColorId(R.color.black);
        }
    }

    void initialize() {

        adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .build();
        adView.loadAd(adRequest);

        EventBus.getDefault().register(new Darkness());
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        colorAdapter = new ColorAdapter(this);
        colorAdapter.setColorData(getMockData());
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(4, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        //    GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
        //   recyclerView.setLayoutManager(gridLayoutManager);
        //   recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(colorAdapter);

        sharedPrefs = new SharedPrefs(this);
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        startStop = (Button) findViewById(R.id.startStop);
        seekValue = (TextView) findViewById(R.id.seekPercentage);

        seekBar.setMax(200);
        seekBar.setProgress(0);

        if (sharedPrefs.isService()) {
            startStop.setText("Stop");

        } else {
            startStop.setText("Start");
        }


    }

    List<ColorData> getMockData() {
        List<ColorData> colorDataList = new ArrayList<>();

        colorDataList.add(new ColorData(R.color.black));
        colorDataList.add(new ColorData(R.color.grey));
        colorDataList.add(new ColorData(R.color.pink));
        colorDataList.add(new ColorData(R.color.green));
        colorDataList.add(new ColorData(R.color.orange));
        colorDataList.add(new ColorData(R.color.yellow));
        colorDataList.add(new ColorData(R.color.purple));
        colorDataList.add(new ColorData(R.color.blue));
        colorDataList.add(new ColorData(R.color.brown));
        colorDataList.add(new ColorData(R.color.whitish));
        colorDataList.add(new ColorData(R.color.light_blue));
        colorDataList.add(new ColorData(R.color.light_green));


        return colorDataList;

    }

    protected void changeColor(int color) {

        EventBus.getDefault().post(new ColorEvent(color));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(new Darkness());
        if (adView != null) {
            adView.pause();
        }

    }

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.pause();
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_setting) {
            Intent setting = new Intent(ChangeBrightness.this, Setting.class);
            startActivity(setting);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}


 /*   WindowManager.LayoutParams layout = getWindow().getAttributes();
layout.alpha = (Darkness.PROGRESS_VALUE + 55) / (float) 255;
        getWindow().setAttributes(layout);
           */
