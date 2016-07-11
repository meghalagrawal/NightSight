package meghal.developer.nightsight.project.ui.brightness.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.assent.AssentActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

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

public class ChangeBrightness extends AssentActivity {

    private static final String TAG = "ChangeBrightness";
    private SeekBar seekBar;
    private Button startStop;
    private SharedPrefs sharedPrefs;
    private RecyclerView recyclerView;
    private ColorAdapter colorAdapter;
    private AdView adView;
    TextView seekValue;
    Toolbar toolbar;
    private RelativeLayout parent;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_brightness);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialize();
//        createPermissionListeners();

        //     Dexter.continuePendingRequestsIfPossible(allPermissionsListener);
        startStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent in = new Intent(ChangeBrightness.this, Darkness.class);

                if (sharedPrefs.isService()) {
                    stopService(in);
                    sharedPrefs.setService(false);
                    startStop.setText("Start");
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (Settings.canDrawOverlays(ChangeBrightness.this)) {
                            getApplicationContext().startService(in);
                            sharedPrefs.setService(true);
                            startStop.setText("Stop");

                        } else {
                            Intent myIntent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                            startActivity(myIntent);

                        }
                        Log.i(TAG, "Marshmallow");
                        // Take Permission
                    /*    if (!Assent.isPermissionGranted(Assent.SYSTEM_ALERT_WINDOW)) {
                            // The if statement checks if the permission has already been granted before
                            Assent.requestPermissions(new AssentCallback() {
                                @Override
                                public void onPermissionResult(PermissionResultSet result) {
                                    Log.i(TAG, "Marshmallow");

                                    if (result.isGranted(Assent.SYSTEM_ALERT_WINDOW)) {

                                    }
                                    // Permission granted or denied
                                }
                            }, 691, Assent.SYSTEM_ALERT_WINDOW);
                        }
*/
                    } else {
                        Log.i(TAG, "Not Marshmallow");

                        getApplicationContext().startService(in);
                        sharedPrefs.setService(true);
                        startStop.setText("Stop");

                    }
                    /*
                    getApplicationContext().startService(in);
                        sharedPrefs.setService(true);
                        startStop.setText("Stop");*/
                    //             }


                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progresValue,
                                          boolean fromUser) {

                seekValue.setText("( Brightness " + progresValue / 2 + "% )");
                sharedPrefs.setBrightness(progresValue);

                if (sharedPrefs.isService()) {
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
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    void initialize() {

        parent = (RelativeLayout) findViewById(R.id.parent);
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
        seekValue.setText("( Brightness " + sharedPrefs.getBrightness() / 2 + "% )");

        seekBar.setMax(200);
        seekBar.setProgress(sharedPrefs.getBrightness());

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
