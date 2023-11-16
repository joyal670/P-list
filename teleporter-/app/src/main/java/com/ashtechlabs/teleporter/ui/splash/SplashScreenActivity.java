package com.ashtechlabs.teleporter.ui.splash;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.Dashboard_Main;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.CargoDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.DriverDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.WareHouseDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.TruckingDashBoardActivity;
import com.ashtechlabs.teleporter.ui.login.LoginActivity;
import com.ashtechlabs.teleporter.util.GlobalUtils;
import com.yqritc.scalablevideoview.ScalableType;
import com.yqritc.scalablevideoview.ScalableVideoView;

import java.io.IOException;

public class SplashScreenActivity extends BaseActivity {

    private static final int SPLASH_TIME_OUT = 4000;
    private ScalableVideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);


        videoView = (ScalableVideoView) findViewById(R.id.splashVideo);
        try {
            videoView.setRawData(R.raw.splash_video);
            videoView.setVolume(0, 0);
            videoView.setLooping(true);
            videoView.prepare(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    videoView.start();
                    videoView.setScalableType(ScalableType.CENTER_CROP);
                    videoView.invalidate();
                }
            });
        } catch (IOException ioe) {
            //ignore
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                String type = GlobalPreferManager.getString(GlobalPreferManager.Keys.USER_MODE,"");


                //if (type.equals("")) {
                    Intent intent = new Intent(SplashScreenActivity.this, Dashboard_Main.class);
                    startActivity(intent);
//                } else {
//
//                    int mode = Integer.parseInt(GlobalPreferManager.getString(GlobalPreferManager.Keys.USER_MODE,""));
//
//                    switch (mode) {
//                        case GlobalUtils.MODE_COURIER:
//                            if (!GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, "").equals("")) {
//                                Intent intent = new Intent(SplashScreenActivity.this, DriverDashBoardActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
//                                intent.putExtra("mode", mode);
//                                startActivity(intent);
//                            }
//                            break;
//                        case GlobalUtils.MODE_STORAGE:
//                            if (GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, "").equals("")) {
//                                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
//                                intent.putExtra("mode", mode);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(SplashScreenActivity.this, WareHouseDashBoardActivity.class);
//                                startActivity(intent);
//                            }
//                            break;
//                        case GlobalUtils.MODE_TRUCKING:
//                            if (GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, "").equals("")) {
//                                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
//                                intent.putExtra("mode", mode);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(SplashScreenActivity.this, TruckingDashBoardActivity.class);
//                                startActivity(intent);
//                            }
//                            break;
//                        case GlobalUtils.MODE_CARGO:
//                            if (GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, "").equals("")) {
//                                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
//                                intent.putExtra("mode", mode);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent( SplashScreenActivity.this, CargoDashBoardActivity.class);
//                                startActivity(intent);
//                            }
//                            break;
//                    }
//                }

                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);


    }

    @Override
    public void onBackPressed() {

    }
}
