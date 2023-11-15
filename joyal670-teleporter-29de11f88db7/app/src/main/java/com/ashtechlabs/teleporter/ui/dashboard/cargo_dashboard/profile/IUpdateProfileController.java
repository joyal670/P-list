package com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.profile;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface IUpdateProfileController {
    void onUpdateProfileController(String token, String partner, String companyname, String companyAddress, String contactNum, String email,
                                   String insuranceExpDate, String contactDetail, String poc, String designation,
                                   String bankDetail, String website, String img_insurance_path, String img_license_path, String img_profile_path);
}

