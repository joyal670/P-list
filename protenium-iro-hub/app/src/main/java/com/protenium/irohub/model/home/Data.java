
package com.protenium.irohub.model.home;

import java.util.List;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Data {

    @SerializedName("banners")
    private List<Banner> mBanners;
    @SerializedName("meal_categories")
    private List<MealCategory> mMealCategories;
    @SerializedName("notification_count")
    private Long mNotificationCount;

    public List<Banner> getBanners() {
        return mBanners;
    }

    public void setBanners(List<Banner> banners) {
        mBanners = banners;
    }

    public List<MealCategory> getMealCategories() {
        return mMealCategories;
    }

    public void setMealCategories(List<MealCategory> mealCategories) {
        mMealCategories = mealCategories;
    }

    public Long getNotificationCount() {
        return mNotificationCount;
    }

    public void setNotificationCount(Long notificationCount) {
        mNotificationCount = notificationCount;
    }

}
