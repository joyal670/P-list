package com.ashtechlabs.teleporter.ui.reviews;

import com.ashtechlabs.teleporter.ui.ICommonProgressCallback;

/**
 * Created by IROID_ANDROID1 on 20-Jan-17.
 */

public interface ReviewControllerCallback extends ICommonProgressCallback{
    void onGetReviewDetails(ReviewList reviews);

}
