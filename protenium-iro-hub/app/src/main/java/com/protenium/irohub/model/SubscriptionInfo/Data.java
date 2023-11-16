
package com.protenium.irohub.model.SubscriptionInfo;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Data {

    @SerializedName("address")
    private Address mAddress;
    @SerializedName("adress_id")
    private int mAdressId;
    @SerializedName("base_price")
    private Double mBasePrice;
    @SerializedName("carbs")
    private int mCarbs;
    @SerializedName("carbs_price")
    private int mCarbsPrice;
    @SerializedName("carbs_updated_at")
    private String mCarbsUpdatedAt;
    @SerializedName("comments")
    private Object mComments;
    @SerializedName("created_at")
    private String mCreatedAt;
    @SerializedName("duration")
    private int mDuration;
    @SerializedName("enable_modifications")
    private int mEnableModifications;
    @SerializedName("extras")
    private List<Object> mExtras;
    @SerializedName("extras_total")
    private int mExtrasTotal;
    @SerializedName("global_suspensions")
    private List<String> mGlobalSuspensions;
    @SerializedName("id")
    private int mId;
    @SerializedName("meal_category_id")
    private int mMealCategoryId;
    @SerializedName("meal_plan_id")
    private int mMealPlanId;
    @SerializedName("non_stop_delivery_price")
    private Object mNonStopDeliveryPrice;
    @SerializedName("non_stop_delivery_status")
    private int mNonStopDeliveryStatus;
    @SerializedName("off_days")
    private List<Object> mOffDays;
    @SerializedName("order_placement_buffer")
    private int mOrderPlacementBuffer;
    @SerializedName("order_statuses")
    private OrderStatuses mOrderStatuses;
    @SerializedName("payment_method")
    private String mPaymentMethod;
    @SerializedName("payment_reference")
    private String mPaymentReference;
    @SerializedName("payment_status")
    private int mPaymentStatus;
    @SerializedName("plan_end_date")
    private String mPlanEndDate;
    @SerializedName("plan_start_buffer")
    private int mPlanStartBuffer;
    @SerializedName("plan_start_date")
    private String mPlanStartDate;
    @SerializedName("promo_code_id")
    private int mPromoCodeId;
    @SerializedName("promo_discount_price")
    private int mPromoDiscountPrice;
    @SerializedName("proteins")
    private int mProteins;
    @SerializedName("proteins_price")
    private int mProteinsPrice;
    @SerializedName("proteins_updated_at")
    private String mProteinsUpdatedAt;
    @SerializedName("suspend_limit")
    private int mSuspendLimit;
    @SerializedName("total")
    private Double mTotal;
    @SerializedName("unique_key")
    private String mUniqueKey;
    @SerializedName("updated_at")
    private String mUpdatedAt;
    @SerializedName("user_id")
    private int mUserId;

    public Address getAddress() {
        return mAddress;
    }

    public void setAddress(Address address) {
        mAddress = address;
    }

    public int getAdressId() {
        return mAdressId;
    }

    public void setAdressId(int adressId) {
        mAdressId = adressId;
    }

    public Double getBasePrice() {
        return mBasePrice;
    }

    public void setBasePrice(Double basePrice) {
        mBasePrice = basePrice;
    }

    public int getCarbs() {
        return mCarbs;
    }

    public void setCarbs(int carbs) {
        mCarbs = carbs;
    }

    public int getCarbsPrice() {
        return mCarbsPrice;
    }

    public void setCarbsPrice(int carbsPrice) {
        mCarbsPrice = carbsPrice;
    }

    public String getCarbsUpdatedAt() {
        return mCarbsUpdatedAt;
    }

    public void setCarbsUpdatedAt(String carbsUpdatedAt) {
        mCarbsUpdatedAt = carbsUpdatedAt;
    }

    public Object getComments() {
        return mComments;
    }

    public void setComments(Object comments) {
        mComments = comments;
    }

    public String getCreatedAt() {
        return mCreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        mCreatedAt = createdAt;
    }

    public int getDuration() {
        return mDuration;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public int getEnableModifications() {
        return mEnableModifications;
    }

    public void setEnableModifications(int enableModifications) {
        mEnableModifications = enableModifications;
    }

    public List<Object> getExtras() {
        return mExtras;
    }

    public void setExtras(List<Object> extras) {
        mExtras = extras;
    }

    public int getExtrasTotal() {
        return mExtrasTotal;
    }

    public void setExtrasTotal(int extrasTotal) {
        mExtrasTotal = extrasTotal;
    }

    public List<String> getGlobalSuspensions() {
        return mGlobalSuspensions;
    }

    public void setGlobalSuspensions(List<String> globalSuspensions) {
        mGlobalSuspensions = globalSuspensions;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public int getMealCategoryId() {
        return mMealCategoryId;
    }

    public void setMealCategoryId(int mealCategoryId) {
        mMealCategoryId = mealCategoryId;
    }

    public int getMealPlanId() {
        return mMealPlanId;
    }

    public void setMealPlanId(int mealPlanId) {
        mMealPlanId = mealPlanId;
    }

    public Object getNonStopDeliveryPrice() {
        return mNonStopDeliveryPrice;
    }

    public void setNonStopDeliveryPrice(Object nonStopDeliveryPrice) {
        mNonStopDeliveryPrice = nonStopDeliveryPrice;
    }

    public int getNonStopDeliveryStatus() {
        return mNonStopDeliveryStatus;
    }

    public void setNonStopDeliveryStatus(int nonStopDeliveryStatus) {
        mNonStopDeliveryStatus = nonStopDeliveryStatus;
    }

    public List<Object> getOffDays() {
        return mOffDays;
    }

    public void setOffDays(List<Object> offDays) {
        mOffDays = offDays;
    }

    public int getOrderPlacementBuffer() {
        return mOrderPlacementBuffer;
    }

    public void setOrderPlacementBuffer(int orderPlacementBuffer) {
        mOrderPlacementBuffer = orderPlacementBuffer;
    }

    public OrderStatuses getOrderStatuses() {
        return mOrderStatuses;
    }

    public void setOrderStatuses(OrderStatuses orderStatuses) {
        mOrderStatuses = orderStatuses;
    }

    public String getPaymentMethod() {
        return mPaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        mPaymentMethod = paymentMethod;
    }

    public String getPaymentReference() {
        return mPaymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        mPaymentReference = paymentReference;
    }

    public int getPaymentStatus() {
        return mPaymentStatus;
    }

    public void setPaymentStatus(int paymentStatus) {
        mPaymentStatus = paymentStatus;
    }

    public String getPlanEndDate() {
        return mPlanEndDate;
    }

    public void setPlanEndDate(String planEndDate) {
        mPlanEndDate = planEndDate;
    }

    public int getPlanStartBuffer() {
        return mPlanStartBuffer;
    }

    public void setPlanStartBuffer(int planStartBuffer) {
        mPlanStartBuffer = planStartBuffer;
    }

    public String getPlanStartDate() {
        return mPlanStartDate;
    }

    public void setPlanStartDate(String planStartDate) {
        mPlanStartDate = planStartDate;
    }

    public int getPromoCodeId() {
        return mPromoCodeId;
    }

    public void setPromoCodeId(int promoCodeId) {
        mPromoCodeId = promoCodeId;
    }

    public int getPromoDiscountPrice() {
        return mPromoDiscountPrice;
    }

    public void setPromoDiscountPrice(int promoDiscountPrice) {
        mPromoDiscountPrice = promoDiscountPrice;
    }

    public int getProteins() {
        return mProteins;
    }

    public void setProteins(int proteins) {
        mProteins = proteins;
    }

    public int getProteinsPrice() {
        return mProteinsPrice;
    }

    public void setProteinsPrice(int proteinsPrice) {
        mProteinsPrice = proteinsPrice;
    }

    public String getProteinsUpdatedAt() {
        return mProteinsUpdatedAt;
    }

    public void setProteinsUpdatedAt(String proteinsUpdatedAt) {
        mProteinsUpdatedAt = proteinsUpdatedAt;
    }

    public int getSuspendLimit() {
        return mSuspendLimit;
    }

    public void setSuspendLimit(int suspendLimit) {
        mSuspendLimit = suspendLimit;
    }

    public Double getTotal() {
        return mTotal;
    }

    public void setTotal(Double total) {
        mTotal = total;
    }

    public String getUniqueKey() {
        return mUniqueKey;
    }

    public void setUniqueKey(String uniqueKey) {
        mUniqueKey = uniqueKey;
    }

    public String getUpdatedAt() {
        return mUpdatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        mUpdatedAt = updatedAt;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

}
