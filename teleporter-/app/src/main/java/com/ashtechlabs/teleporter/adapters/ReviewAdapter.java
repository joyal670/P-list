package com.ashtechlabs.teleporter.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ashtechlabs.teleporter.R;
import com.ashtechlabs.teleporter.ui.reviews.Review;

import java.util.ArrayList;

/**
 * Created by IROID_ANDROID1 on 31-Jan-17.
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.CustomViewHolder> {
    Activity mContext;
    ArrayList<Review> mReviews;
    int star_height;

    public ReviewAdapter(Activity context, ArrayList<Review> revs) {
        this.mContext = context;
        this.mReviews = revs;
    }

    @Override
    public ReviewAdapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review, viewGroup, false);
        return new ReviewAdapter.CustomViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ReviewAdapter.CustomViewHolder holder, final int position) {

        holder.txtComment.setText(mReviews.get(position).getDriverReview());
        holder.txtTime.setText(mReviews.get(position).getReviewDate());
        holder.txtPoster.setText("By: "+mReviews.get(position).getCustomerName());

        Bitmap icon_star = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.star_checked);
        star_height = icon_star.getHeight();

        holder.rate_review.setRating(Float.parseFloat(mReviews.get(position).getDriverRating()));
        holder.rate_review.setMinimumHeight(star_height);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holder.rate_review.getLayoutParams();
        params.height = star_height;
        holder.rate_review.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return (null != mReviews ? mReviews.size() : 0);
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView txtComment;
        TextView txtTime;
        TextView txtPoster;

        RatingBar rate_review;


        public CustomViewHolder(View itemView) {
            super(itemView);
            txtComment = (TextView) itemView.findViewById(R.id.txtComment);
            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            txtPoster = (TextView) itemView.findViewById(R.id.txtPoster);

            rate_review = (RatingBar) itemView.findViewById(R.id.rating_review);

        }
    }
}


