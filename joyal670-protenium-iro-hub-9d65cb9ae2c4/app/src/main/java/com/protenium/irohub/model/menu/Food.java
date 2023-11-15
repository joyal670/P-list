
package com.protenium.irohub.model.menu;




import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;


public class Food implements Parcelable {

    @SerializedName("average_rating")
    private Double mAverageRating;
    @SerializedName("calories")
    private Long mCalories;
    @SerializedName("carbs")
    private Long mCarbs;
    @SerializedName("description")
    private String mDescription;
    @SerializedName("fat")
    private Long mFat;
    @SerializedName("id")
    private Long mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("my_rating")
    private Long mMyRating;
    @SerializedName("name")
    private String mName;
    @SerializedName("ordered_status")
    private Boolean mOrderedStatus;
    @SerializedName("proteins")
    private Long mProteins;
    @SerializedName("tagline")
    private String mTagline;
    @SerializedName("tags")
    private List<Tag> mTags;

    protected Food(Parcel in) {
        if (in.readByte() == 0) {
            mAverageRating = null;
        } else {
            mAverageRating = in.readDouble();
        }
        if (in.readByte() == 0) {
            mCalories = null;
        } else {
            mCalories = in.readLong();
        }
        if (in.readByte() == 0) {
            mCarbs = null;
        } else {
            mCarbs = in.readLong();
        }
        mDescription = in.readString();
        if (in.readByte() == 0) {
            mFat = null;
        } else {
            mFat = in.readLong();
        }
        if (in.readByte() == 0) {
            mId = null;
        } else {
            mId = in.readLong();
        }
        mImage = in.readString();
        if (in.readByte() == 0) {
            mMyRating = null;
        } else {
            mMyRating = in.readLong();
        }
        mName = in.readString();
        byte tmpMOrderedStatus = in.readByte();
        mOrderedStatus = tmpMOrderedStatus == 0 ? null : tmpMOrderedStatus == 1;
        if (in.readByte() == 0) {
            mProteins = null;
        } else {
            mProteins = in.readLong();
        }
        mTagline = in.readString();
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };

    public Double getAverageRating() {
        return mAverageRating;
    }

    public void setAverageRating(Double averageRating) {
        mAverageRating = averageRating;
    }

    public Long getCalories() {
        return mCalories;
    }

    public void setCalories(Long calories) {
        mCalories = calories;
    }

    public Long getCarbs() {
        return mCarbs;
    }

    public void setCarbs(Long carbs) {
        mCarbs = carbs;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Long getFat() {
        return mFat;
    }

    public void setFat(Long fat) {
        mFat = fat;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getImage() {
        return mImage;
    }

    public void setImage(String image) {
        mImage = image;
    }

    public Long getMyRating() {
        return mMyRating;
    }

    public void setMyRating(Long myRating) {
        mMyRating = myRating;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public Boolean getOrderedStatus() {
        return mOrderedStatus;
    }

    public void setOrderedStatus(Boolean orderedStatus) {
        mOrderedStatus = orderedStatus;
    }

    public Long getProteins() {
        return mProteins;
    }

    public void setProteins(Long proteins) {
        mProteins = proteins;
    }

    public String getTagline() {
        return mTagline;
    }

    public void setTagline(String tagline) {
        mTagline = tagline;
    }

    public List<Tag> getTags() {
        return mTags;
    }

    public void setTags(List<Tag> tags) {
        mTags = tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (mAverageRating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeDouble(mAverageRating);
        }
        if (mCalories == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mCalories);
        }
        if (mCarbs == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mCarbs);
        }
        dest.writeString(mDescription);
        if (mFat == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mFat);
        }
        if (mId == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mId);
        }
        dest.writeString(mImage);
        if (mMyRating == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mMyRating);
        }
        dest.writeString(mName);
        dest.writeByte((byte) (mOrderedStatus == null ? 0 : mOrderedStatus ? 1 : 2));
        if (mProteins == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(mProteins);
        }
        dest.writeString(mTagline);
    }
}
