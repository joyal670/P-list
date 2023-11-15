package com.ashtechlabs.teleporter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ashtechlabs.teleporter.BaseActivity;
import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.app_prefs.GlobalPreferManager;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.CargoDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.DriverDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.WareHouseDashBoardActivity;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.TruckingDashBoardActivity;
import com.ashtechlabs.teleporter.ui.login.LoginActivity;
import com.ashtechlabs.teleporter.util.GlobalUtils;

/**
 * Created by IROID_ANDROID1 on 22-Feb-17.
 */

public class Dashboard_Main extends BaseActivity {
    ImageView driver, vendor, storage, trucking;
    private Animation zoomOut;
    private LinearLayout linContainer;
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        driver = (ImageView) findViewById(R.id.btdrivers);
        vendor = (ImageView) findViewById(R.id.btvendors);
        storage = (ImageView) findViewById(R.id.btstorage);
        trucking = (ImageView) findViewById(R.id.bttrucking);

        linContainer = (LinearLayout) findViewById(R.id.linContainer);
        zoomOut = AnimationUtils.loadAnimation(this, R.anim.zoom_out);
        type = GlobalPreferManager.getString(GlobalPreferManager.Keys.USER_MODE,"");


        driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (type.equals("")) {
//                    Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                    intent.putExtra("mode", "0");
//                    startActivity(intent);
//                } else {
//                    int mode = Integer.parseInt(GlobalPreferManager.getString(GlobalPreferManager.Keys.USER_MODE,""));
//                    switch (mode) {
//                        case GlobalUtils.MODE_COURIER:
                            if (!GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, "").equals("")) {
                                Intent intent = new Intent(Dashboard_Main.this, DriverDashBoardActivity.class);
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
                                intent.putExtra("mode", "0");
                                startActivity(intent);
                            }
//                            break;
//                        case GlobalUtils.MODE_TRUCKING:
//                            if (!GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, "").equals("")) {
//                                Intent intent = new Intent(Dashboard_Main.this, TruckingDashBoardActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                                intent.putExtra("mode", String.valueOf(mode));
//                                startActivity(intent);
//                            }
//                            break;
//                        case GlobalUtils.MODE_STORAGE:
//                            if (GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, "").equals("")) {
//                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                                intent.putExtra("mode", String.valueOf(mode));
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(Dashboard_Main.this, WareHouseDashBoardActivity.class);
//                                startActivity(intent);
//                            }
//                            break;
//                        case GlobalUtils.MODE_CARGO:
//                            if (GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, "").equals("")) {
//                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                                intent.putExtra("mode", String.valueOf(mode));
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(Dashboard_Main.this, CargoDashBoardActivity.class);
//                                startActivity(intent);
//                            }
//                            break;
//                    }
//                }
            }
        });


        trucking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (type.equals("")) {
//                    Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                    intent.putExtra("mode", "3");
//                    startActivity(intent);
//                } else {
//                    int mode = Integer.parseInt(GlobalPreferManager.getString(GlobalPreferManager.Keys.USER_MODE,""));
//                    switch (mode) {
//                        case GlobalUtils.MODE_COURIER:
//                            if (!GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, "").equals("")) {
//                                Intent intent = new Intent(Dashboard_Main.this, DriverDashBoardActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                                intent.putExtra("mode", String.valueOf(mode));
//                                startActivity(intent);
//                            }
//                            break;
//                        case GlobalUtils.MODE_TRUCKING:
                if (!GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, "").equals("")) {
                    Intent intent = new Intent(Dashboard_Main.this, TruckingDashBoardActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
                    intent.putExtra("mode", "1");
                    startActivity(intent);
                }
//                            break;
//                        case GlobalUtils.MODE_STORAGE:
//                            if (GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, "").equals("")) {
//                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                                intent.putExtra("mode", String.valueOf(mode));
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(Dashboard_Main.this, WareHouseDashBoardActivity.class);
//                                startActivity(intent);
//                            }
//                            break;
//                        case GlobalUtils.MODE_CARGO:
//                            if (GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, "").equals("")) {
//                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                                intent.putExtra("mode", String.valueOf(mode));
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(Dashboard_Main.this, CargoDashBoardActivity.class);
//                                startActivity(intent);
//                            }
//                            break;
//                    }
//               }
            }
        });

        vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (type.equals("")) {
//                    Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                    intent.putExtra("mode", "2");
//                    startActivity(intent);
//                } else {
//                    int mode = Integer.parseInt(GlobalPreferManager.getString(GlobalPreferManager.Keys.USER_MODE,""));
////                    switch (mode) {
//                        case GlobalUtils.MODE_COURIER:
//                            if (!GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, "").equals("")) {
//                                Intent intent = new Intent(Dashboard_Main.this, DriverDashBoardActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                                intent.putExtra("mode", String.valueOf(mode));
//                                startActivity(intent);
//                            }
//                            break;
//                        case GlobalUtils.MODE_TRUCKING:
//                            if (!GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, "").equals("")) {
//                                Intent intent = new Intent(Dashboard_Main.this, TruckingDashBoardActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                                intent.putExtra("mode", String.valueOf(mode));
//                                startActivity(intent);
//                            }
//                            break;
//                        case GlobalUtils.MODE_STORAGE:
//                            if (GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, "").equals("")) {
//                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                                intent.putExtra("mode", String.valueOf(mode));
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(Dashboard_Main.this, WareHouseDashBoardActivity.class);
//                                startActivity(intent);
//                            }
//                            break;
//                        case GlobalUtils.MODE_CARGO:
                            if (GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, "").equals("")) {
                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
                                intent.putExtra("mode", "2");
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(Dashboard_Main.this, CargoDashBoardActivity.class);
                                startActivity(intent);
                            }
//                            break;
//                    }
//                }

            }
        });

        storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                if (type.equals("")) {
//                    Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                    intent.putExtra("mode", "1");
//                    startActivity(intent);
//                } else {
//                    int mode = Integer.parseInt(GlobalPreferManager.getString(GlobalPreferManager.Keys.USER_MODE,""));
//                    switch (mode) {
//                        case GlobalUtils.MODE_COURIER:
//                            if (!GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_COURIER, "").equals("")) {
//                                Intent intent = new Intent(Dashboard_Main.this, DriverDashBoardActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                                intent.putExtra("mode", String.valueOf(mode));
//                                startActivity(intent);
//                            }
//                            break;
//                        case GlobalUtils.MODE_TRUCKING:
//                            if (!GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_TRUCKING, "").equals("")) {
//                                Intent intent = new Intent(Dashboard_Main.this, TruckingDashBoardActivity.class);
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                                intent.putExtra("mode", String.valueOf(mode));
//                                startActivity(intent);
//                            }
//                            break;
//                        case GlobalUtils.MODE_STORAGE:
                            if (GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_STORAGE, "").equals("")) {
                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
                                intent.putExtra("mode", "3");
                                startActivity(intent);
                            } else {
                                Intent intent = new Intent(Dashboard_Main.this, WareHouseDashBoardActivity.class);
                                startActivity(intent);
                            }
//                            break;
//                        case GlobalUtils.MODE_CARGO:
//                            if (GlobalPreferManager.getString(GlobalPreferManager.Keys.TOKEN_CARGO, "").equals("")) {
//                                Intent intent = new Intent(Dashboard_Main.this, LoginActivity.class);
//                                intent.putExtra("mode", String.valueOf(mode));
//                                startActivity(intent);
//                            } else {
//                                Intent intent = new Intent(Dashboard_Main.this, CargoDashBoardActivity.class);
//                                startActivity(intent);
//                            }
//                            break;
//                    }
//                }
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        linContainer.setAnimation(zoomOut);

    }
}
