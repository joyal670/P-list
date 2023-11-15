
package com.protenium.irohub.model.profile.profileDetails;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings("unused")
public class Data {

    @SerializedName("alternative_phone")
    private String mAlternativePhone;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("email")
    private String mEmail;
    @SerializedName("email_verified_at")
    private Object mEmailVerifiedAt;
    @SerializedName("gender")
    private String mGender;
    @SerializedName("id")
    private Long mId;
    @SerializedName("image")
    private String mImage;
    @SerializedName("name")
    private String mName;
    @SerializedName("phone")
    private String mPhone;
    @SerializedName("status")
    private Long mStatus;
    @SerializedName("updated_at")
    private String mUpdatedAt;

    public String getAlternativePhone() {
        return mAlternativePhone;
    }

    public void setAlternativePhone(String alternativePhone) {
        mAlternativePhone = alternativePhone;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public Object getEmailVerifiedAt() {
        return mEmailVerifiedAt;
    }

    public void setEmailVerifiedAt(Object emailVerifiedAt) {
        mEmailVerifiedAt = emailVerifiedAt;
    }

    public String getGender() {
        return mGender;
    }

    public void setGender(String gender) {
        mGender = gender;
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

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPhone() {
        return mPhone;
    }

    public void setPhone(String phone) {
        mPhone = phone;
    }

    public Long getStatus() {
        return mStatus;
    }

    public void setStatus(Long status) {
        mStatus = status;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

}
