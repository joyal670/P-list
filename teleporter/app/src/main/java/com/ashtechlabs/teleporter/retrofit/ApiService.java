package com.ashtechlabs.teleporter.retrofit;

import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorNotifications;
import com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.VendorProfile;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.CourierNotifications;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverOrder;
import com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo.DriverProfile;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WarehouseNotification;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.PendingJob;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouse;
import com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo.WareHouseProfile;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingNotifications;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingOrder;
import com.ashtechlabs.teleporter.ui.dashboard.trucking_dashdoard.pojo.TruckingProfile;
import com.ashtechlabs.teleporter.ui.pricing.pojo.Price;
import com.ashtechlabs.teleporter.ui.reviews.ReviewList;
import com.google.gson.JsonObject;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by IROID_ANDROID1 on 18-Jan-17.
 */

public interface ApiService {
    /*
   Retrofit get annotation with our URL
   And our method that will return us the List of ContactList
   */
    @GET("maps/api/geocode/json")
    Call<JsonObject> getLatLongFromAddress(@Query("key") String apiKey, @Query("address") String address, @Query("sensor") boolean sensor);

//    @GET("getJobsWithDriverToken")
//    Call<DriverOrder> getJobsWithDriverToken(@Query("token") String token);

    @GET("getJobsWithCourierToken")
    Call<DriverOrder> getJobsWithDriverToken(@Query("token") String token);

    @GET("getJobsWithTruckingToken")
    Call<JsonObject> getJobsWithTruckingToken(@Query("token") String token);

//    @FormUrlEncoded
//    @POST("updateDriverLocation")
//    Call<JsonObject> updateDriverLocation(@Field("token") String token, @Field("lat") String latitude, @Field("lng") String longitude);

    @FormUrlEncoded
    @POST("updateCourierLocation")
    Call<JsonObject> updateDriverLocation(@Field("token") String token, @Field("lat") String latitude, @Field("lng") String longitude);

    @FormUrlEncoded
    @POST("updateTruckingLocation")
    Call<JsonObject> updateTruckingLocation(@Field("token") String token, @Field("lat") String latitude, @Field("lng") String longitude);

    @FormUrlEncoded
    @POST("updateVendorLocation")
    Call<JsonObject> updateVendorLocation(@Field("token") String token, @Field("lat") String latitude, @Field("lng") String longitude);


    @FormUrlEncoded
    @POST("setJobState")
    Call<JsonObject> setJobState(@Field("jobID") String jobid, @Field("state") String state, @Field("token") String token);

    @GET("getCustomerDetail")
    Call<JsonObject> getCustomerDetail(@Query("token") String token, @Query("orderID") String orderid, @Query("orderType") String ordertype);

//    @FormUrlEncoded
//    @POST("submitDeliveryServiceRateCard")
//    Call<JsonObject> submitDeliveryServiceRateCard(@Field("token") String token,
//                                                   @Field("deliveryType") String deliveryType,
//                                                   @Field("fromLocation") String fromLocation,
//                                                   @Field("toLocation") String toLocation,
//                                                   @Field("minAmount") String minAmount,
//                                                   @Field("perKGAmount") String perKGAmount,
//                                                   @Field("rate_validity") String rateValidity,
//                                                   @Field("duration") String duration,
//                                                   @Field("currency") int currency,
//                                                   @Field("OrderID") String orderId);
    @FormUrlEncoded
    @POST("submitCourierRateCard")
    Call<JsonObject> submitDeliveryServiceRateCard(@Field("token") String token,
                                                   @Field("deliveryType") int deliveryType,
                                                   @Field("fromLocation") String fromLocation,
                                                   @Field("toLocation") String toLocation,
                                                   @Field("minAmount") String minAmount,
                                                   @Field("perKGAmount") String perKGAmount,
                                                   @Field("rate_validity") String rateValidity,
                                                   @Field("duration") String duration,
                                                   @Field("currency") int currency,
                                                   @Field("OrderID") String orderId);


    @FormUrlEncoded
    @POST("submitMoverPackerServiceRateCard")
    Call<JsonObject> submitMoverPackerServiceRateCard(@Field("token") String token,
                                                      @Field("vehicleType") int vechicleType,
                                                      @Field("subVehicleType") int subVehicleType,
                                                      @Field("fromLocation") String fromLocation,
                                                      @Field("toLocation") String toLocation,
                                                      @Field("minAmount") String amount,
                                                      @Field("rate_validity") String rateValidity,
                                                      @Field("insurance_percent") String insPercent,
                                                      @Field("insurance_min_amt") String insuranceMinAmt,
                                                      @Field("duration") String duration,
                                                      @Field("currency") int currency,
                                                      @Field("OrderID") String orderId);

    @GET("getJobsWithWarehousingToken")
    Call<WareHouse> getJobsWithWarehousingToken(@Query("token") String token);

    @FormUrlEncoded
    @POST("submitWarehouseServiceRateCard")
    Call<JsonObject> submitWarehouseServiceRateCard(@Field("token") String token,
                                                    @Field("warehouse_det_id") int warehouseId,
                                                    @Field("pcbm") String perCBMAmount,
                                                    @Field("dateFrom") String dateFrom,
                                                    @Field("dateTo") String dateTo,
                                                    @Field("additional_info") int additionalInfo,
                                                    @Field("perishable_det") int perishableDet,
                                                    @Field("min_insurance_amt") String minInsuranceAmt,
                                                    @Field("ins_percent") String insPercent,
                                                    @Field("rate_validity") String rateValidity,
                                                    @Field("totalCBMAvailable") String totalCBMAvailable,
                                                    @Field("currency") int currency,
                                                    @Field("OrderID") String orderId);



    @FormUrlEncoded
    @POST("submitFreightForwardRateCard")
    Call<JsonObject> submitCargoRateCard(
            @Field("token") String token,
            @Field("transport") String type,//Service type(0,1,2,3)
            @Field("fromLocation") String fromLocation,
            @Field("toLocation") String toLocation,
            @Field("additional_info") int additionalInfo,//(0,1,2)
            @Field("perishable_det") int perishableDet,//(1,2,3) Only consider if additional info is 2
            @Field("minAmount") String minAmount,// min amount need to be paid
            @Field("perKGAmount") String perKGAmount, // per kg amount
            @Field("rate_validity") String rateValidity,// Date
            @Field("addInsurance") String insurance,//Insurance percentage
            @Field("insurance_min_amt") String insuranceMinAmt,// min amount insurance will provide
            @Field("duration") String duration, // days + hours (total hours)
            @Field("currency") int currency, // Currency type(0,1,2.etc)
            @Field("OrderID") String orderId); // Only consider if there is any value

    @GET("getJobsWithVendorToken")
    Call<JsonObject> getJobsWithVendorToken(@Query("token") String token);

//    @GET("loginDriver")
//    Call<JsonObject> loginCourier(@Query("mobileNum") String mobile, @Query("password") String pass, @Query("deviceToken") String regid, @Query("socialid") String socialid, @Query("type") String type);

    @GET("loginCourier")
    Call<JsonObject> loginCourier(@Query("mobileNum") String mobile,@Query("username") String userName,@Query("email") String email, @Query("password") String pass, @Query("deviceToken") String regid, @Query("socialid") String socialid, @Query("type") String type);

    @GET("loginTrucking")
    Call<JsonObject> loginTrucking(@Query("mobileNum") String mobile,@Query("username") String userName,@Query("email") String email, @Query("password") String pass, @Query("deviceToken") String regid, @Query("socialid") String socialid, @Query("type") String type);

    @GET("loginWarehouse")
    Call<JsonObject> loginWarehouse(@Query("mobileNum") String mobile,@Query("username") String userName,@Query("email") String email, @Query("password") String pass, @Query("deviceToken") String regid, @Query("socialid") String socialid, @Query("type") String type);

    @GET("loginVendor")
    Call<JsonObject> loginVendor(@Query("mobileNum") String mobile,@Query("username") String userName,@Query("email") String email, @Query("password") String pass, @Query("deviceToken") String regid, @Query("socialid") String socialid, @Query("type") String type);

//    @Multipart
//    @POST("registerDriver")
//    Call<JsonObject> registerDriver(@PartMap Map<String, RequestBody> params,
//                                    @Part MultipartBody.Part file1,
//                                    @Part MultipartBody.Part file2,
//                                    @Part MultipartBody.Part file3);

    @Multipart
    @POST("registerCourier")
    Call<JsonObject> registerDriver(@PartMap Map<String, RequestBody> params,
                                    @Part MultipartBody.Part file1,
                                    @Part MultipartBody.Part file2,
                                    @Part MultipartBody.Part file3);

    @Multipart
    @POST("registerTrucking")
    Call<JsonObject> registerTrucking(@PartMap Map<String, RequestBody> params,
                                    @Part MultipartBody.Part file1,
                                    @Part MultipartBody.Part file2);

    @Multipart
    @POST("registerWarehouse")
    Call<JsonObject> registerWarehouse(@PartMap Map<String, RequestBody> params,
                                       @Part MultipartBody.Part file1);


    @Multipart
    @POST("registerVendor")
    Call<JsonObject> registerVendor(@PartMap Map<String, RequestBody> params,
                                    @Part MultipartBody.Part file1,
                                    @Part MultipartBody.Part file2,
                                    @Part MultipartBody.Part file3);

//    @FormUrlEncoded
//    @POST("createDriverAccount")
//    Call<JsonObject> createDriverAccount(@Field("mobileNum") String mobileNum, @Field("password") String password, @Field("registerId") String registerId, @Field("email") String email);

    @FormUrlEncoded
    @POST("createCourierAccount")
    Call<JsonObject> createDriverAccount(@Field("mobileNum") String mobileNum, @Field("password") String password, @Field("registerId") String registerId, @Field("email") String email);

    @FormUrlEncoded
    @POST("createTruckingAccount")
    Call<JsonObject> createTruckingAccount(@Field("mobileNum") String mobileNum, @Field("password") String password, @Field("registerId") String registerId, @Field("email") String email);

    @FormUrlEncoded
    @POST("createWarehouseAccount")
    Call<JsonObject> createWarehouseAccount(@Field("mobileNum") String mobileNum, @Field("password") String password, @Field("registerId") String registerId, @Field("email") String email);

    @FormUrlEncoded
    @POST("createVendorAccount")
    Call<JsonObject> createVendorAccount(@Field("mobileNum") String mobileNum, @Field("password") String password, @Field("registerId") String registerId, @Field("email") String email);

    @FormUrlEncoded
    @POST("getReview")
    Call<ReviewList> getReview(@Field("token") String token, @Field("type") int mode);

    @FormUrlEncoded
    @POST("getUploadedRates")
    Call<Price> getUploadedRates(@Field("token") String token);

    @FormUrlEncoded
    @POST("deleteRateCard")
    Call<JsonObject> deleteRateCard(@Field("token") String token, @Field("rate_card_id") String rateCardId);

    @FormUrlEncoded
    @POST("getUploadedRates")
    Call<JsonObject> getPricingVendor(@Field("token") String token);

    @FormUrlEncoded
    @POST("getUploadedRates")
    Call<JsonObject> getPricingWareHouse(@Field("token") String token);

    @FormUrlEncoded
    @POST("getUploadedRates")
    Call<JsonObject> getPricingTrucking(@Field("token") String token);

    @FormUrlEncoded
    @POST("forgotPassword")
    Call<JsonObject> forgotPassword(@Field("mobilenumber") String mobile, @Field("mode") int mode);

//    @GET("getDriverProfile")
//    Call<DriverProfile> getDriverProfile(@Query("token") String token);

    @GET("getCourierProfile")
    Call<DriverProfile> getDriverProfile(@Query("token") String token);

    @GET("getTruckingProfile")
    Call<TruckingProfile> getTruckingProfile(@Query("token") String token);


    //    @Multipart
//    @POST("updateDriverProfile")
//    Call<JsonObject> updateDriverProfile(@PartMap Map<String, RequestBody> params,
//                                         @Part MultipartBody.Part file1,
//                                         @Part MultipartBody.Part file2,
//                                         @Part MultipartBody.Part file3);
    @Multipart
    @POST("updateCourierProfile")
    Call<JsonObject> updateDriverProfile(@PartMap Map<String, RequestBody> params,
                                         @Part MultipartBody.Part file1,
                                         @Part MultipartBody.Part file2,
                                         @Part MultipartBody.Part file3);

    @Multipart
    @POST("updateTruckingProfile")
    Call<JsonObject> updateTruckingProfile(@PartMap Map<String, RequestBody> params,
                                           @Part MultipartBody.Part file1,
                                           @Part MultipartBody.Part file2);


    @GET("getVendorProfile")
    Call<VendorProfile> getVendorProfile(@Query("token") String token);

    @Multipart
    @POST("updateVendorProfile")
    Call<JsonObject> updateVendorProfile(@PartMap Map<String, RequestBody> params,
                                         @Part MultipartBody.Part file1,
                                         @Part MultipartBody.Part file2,
                                         @Part MultipartBody.Part file3);

    @GET("getWarehouseProfile")
    Call<WareHouseProfile> getWarehouseProfile(@Query("token") String token);


    @Multipart
    @POST("updateWarehouseProfiles")
    Call<JsonObject> updateWarehouseProfile(@PartMap Map<String, RequestBody> params,
                                            @Part MultipartBody.Part file1);


    @GET("getVendorAnnouncement")
    Call<VendorNotifications> getVendorAnnouncement(@Query("token") String token);

    @GET("getCourierAnnouncement")
    Call<CourierNotifications> getDriverAnnouncement(@Query("token") String token);

    @GET("getTruckingAnnouncement")
    Call<TruckingNotifications> getTruckingAnnouncement(@Query("token") String token);

    @GET("getWarehouseAnnoucement")
    Call<WarehouseNotification> getWarehouseAnnoucement(@Query("token") String token);

    @GET("getPendingJobsWithCourierToken")
    Call<DriverOrder> getPendingJobsWithDriverToken(@Query("token") String token);

    @GET("getPendingJobsWithTruckingToken")
    Call<TruckingOrder> getPendingJobsWithTruckingToken(@Query("token") String token);


    @GET("getPendingJobsWithVendorToken")
    Call<com.ashtechlabs.teleporter.ui.dashboard.cargo_dashboard.pojo.PendingJob> getPendingJobsWithVendorToken(@Query("token") String token);

    @GET("getPendingJobsWithWarehousingToken")
    Call<PendingJob> getPendingJobsWithWarehousingToken(@Query("token") String token);

    @FormUrlEncoded
    @POST("updateDeliveryServiceRateCard")
    Call<JsonObject> updateDeliveryServiceRateCard(@Field("token") String token,
                                                   @Field("ID") String ID,
                                                   @Field("deliveryType") int deliveryType,
                                                   @Field("fromLocation") String fromLocation,
                                                   @Field("toLocation") String toLocation,
                                                   @Field("minAmount") String minAmount,
                                                   @Field("perKGAmount") String perKGAmount,
                                                   @Field("rate_validity") String rateValidity,
                                                   @Field("duration") String duration,
                                                   @Field("currency") int currency);


    @FormUrlEncoded
    @POST("updateFreightForwardRateCard")
    Call<JsonObject> updateCargoRateCard(@Field("token") String token,
                                         @Field("ID") String ID,
                                         @Field("transport") String type,//Service type(0,1,2,3)
                                         @Field("fromLocation") String fromLocation,
                                         @Field("toLocation") String toLocation,
                                         @Field("additional_info") int additionalInfo,//(0,1,2)
                                         @Field("perishable_det") int perishableDet,//(1,2,3) Only consider if additional info is 2
                                         @Field("minAmount") String minAmount,// min amount need to be paid
                                         @Field("perKGAmount") String perKGAmount, // per kg amount
                                         @Field("rate_validity") String rateValidity,// Date of ratecard validity
                                         @Field("addInsurance") String insurance,//Insurance percentage
                                         @Field("insurance_min_amt") String insuranceMinAmt,// min amount insurance will get
                                         @Field("duration") String duration, // days + hours (total hours)
                                         @Field("currency") int currency); // Currency type(0,1,2.etc)

    //
    @FormUrlEncoded
    @POST("updateWarehouseServiceRateCard")
    Call<JsonObject> updateWarehouseServiceRateCard(@Field("token") String token,
                                                    @Field("ID") String ID,
                                                    @Field("warehouse_det_id") int wareHouseId,
                                                    @Field("pcbm") String perCBMAmount,
                                                    @Field("dateFrom") String dateFrom,
                                                    @Field("dateTo") String dateTo,
                                                    @Field("additional_info") int additionalInfo,
                                                    @Field("perishable_det") int perishableDet,
                                                    @Field("min_insurance_amt") String minInsuranceAmt,
                                                    @Field("ins_percent") String insPercent,
                                                    @Field("rate_validity") String rateValidity,
                                                    @Field("totalCBMAvailable") String totalCBMAvailable,
                                                    @Field("currency") int currency);

    @FormUrlEncoded
    @POST("updateMoverPackerServiceRateCard")
    Call<JsonObject> updateMoverPackerServiceRateCard(@Field("token") String token,
                                                      @Field("ID") String ID,
                                                      @Field("vehicleType") int vechicleType,
                                                      @Field("subVehicleType") int subVehicleType,
                                                      @Field("fromLocation") String fromLocation,
                                                      @Field("toLocation") String toLocation,
                                                      @Field("minAmount") String amount,
                                                      @Field("rate_validity") String rateValidity,
                                                      @Field("insurance_percent") String insPercent,
                                                      @Field("insurance_min_amt") String insuranceMinAmt,
                                                      @Field("duration") String duration,
                                                      @Field("currency") int currency);

    @FormUrlEncoded
    @POST("getDeliveryandPickupAddress")
    Call<JsonObject> getDeliveryandPickupAddress(@Field("jobid") String jobId);

    @FormUrlEncoded
    @POST("orderUpdate")
    Call<JsonObject> getorderUpdate(
            @Field("orderID") String oderID,
            @Field("orderStatus") String orderStatus,
            @Field("message") String message);

    @FormUrlEncoded
    @POST("getWareHouseDeclaredOrders/")
    Call<JsonObject> getWareHouseDeclaredOrders(@Field("token") String token, @Field("mobileNumber") String mobileNumber);

    @FormUrlEncoded
    @POST("getDeliveryServiceDeclaredOrders/")
    Call<JsonObject> getDriverDeclaredOrders(@Field("token") String token, @Field("mobileNumber") String mobileNumber);

    @FormUrlEncoded
    @POST("getTruckingDeclaredOrders/")
    Call<JsonObject> getTruckingDeclaredOrders(@Field("token") String token, @Field("mobileNumber") String mobileNumber);

    @FormUrlEncoded
    @POST("getFreightForwardDeclaredOrders/")
    Call<JsonObject> getFreightForwardDeclaredOrders(@Field("token") String token, @Field("mobileNumber") String mobileNumber);

    @GET("on_off_duty/")
    Call<JsonObject> setDutyStatus(@Query("token") String token, @Query("service_type") int serviceType, @Query("duty_status") int dutyStatus);

    @GET("get_on_off_duty_status/")
    Call<JsonObject> getDutyStatus(@Query("token") String token, @Query("service_type") int serviceType);

    @Multipart
    @POST("submitWarehouse/")
    Call<JsonObject> submitWarehouse(@PartMap Map<String, RequestBody> params,
                                     @Part MultipartBody.Part file1,
                                     @Part MultipartBody.Part file2,
                                     @Part MultipartBody.Part file3);

    @GET("getWarehouseDetails/")
    Call<JsonObject> getWarehouseDetails(@Query("token") String token);

    @GET("gettruckingDetails/")
    Call<JsonObject> gettruckingDetails(@Query("token") String token);

    @Multipart
    @POST("submitTrucking/")
    Call<JsonObject> submitVehicle(@PartMap Map<String, RequestBody> params,
                                   @Part MultipartBody.Part file3);

    @FormUrlEncoded
    @POST("getOrderStatus/")
    Call<JsonObject> getTrackingStatus(@Field("orderID") String orderID);

    @FormUrlEncoded
    @POST("updateTeleporterMobileNumb/")
    Call<JsonObject> updateCustomerMobileNumber(@Field("email") String email,@Field("type") int type, @Field("mobile_number") String mobileNumber);

}

