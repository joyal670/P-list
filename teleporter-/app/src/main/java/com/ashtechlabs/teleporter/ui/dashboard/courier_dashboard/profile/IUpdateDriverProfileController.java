package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.profile;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface IUpdateDriverProfileController {
    void onUpdateProfileController(String token, String vehicletype, String city, String address,String phone,String email, String zipcode, String licenseexpired, String vehicleno, String insuranceexpiry, String name, String img_license_path, String img_insurance_path, String img_profile_path);
}

