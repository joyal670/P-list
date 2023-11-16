package com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.profile;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface IUpdateTruckingProfileController {
    void onUpdateProfileController(String token, String city, String name, String address, String phone, String email, String zipcode, String img_profile_path, String img_license_path, String licenseexpired);
}

