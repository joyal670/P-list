package com.iroid.healthdomain.data.network

import com.iroid.healthdomain.data.dummyModel.HashedModel
import com.iroid.healthdomain.data.model_class.AccountModel
import com.iroid.healthdomain.data.model_class.GenerateOtp
import com.iroid.healthdomain.data.model_class.activity_api.UpdateActivityResponse
import com.iroid.healthdomain.data.model_class.add_remove_fav.FavResponse
import com.iroid.healthdomain.data.model_class.cms.CmsResponse
import com.iroid.healthdomain.data.model_class.contacts_api.ContactResponse
import com.iroid.healthdomain.data.model_class.create_challenge.CreateChallengeResponse
import com.iroid.healthdomain.data.model_class.index.IndexApiResponse
import com.iroid.healthdomain.data.model_class.invite.UserInviteResponse
import com.iroid.healthdomain.data.model_class.match_contact.GetContactMatchResponse
import com.iroid.healthdomain.data.model_class.notification_receive.NotificationReceiveResponse
import com.iroid.healthdomain.data.model_class.notification_sent.NotificationSentResponse
import com.iroid.healthdomain.data.model_class.otpValidation.ValidateOtpResponse
import com.iroid.healthdomain.data.model_class.profile_image.ProfileImageResponse
import com.iroid.healthdomain.data.model_class.update_challenge.UpdateChallengeResponse
import com.iroid.healthdomain.data.model_class.updated_steps_data.SendStepsUpdates
import com.iroid.healthdomain.data.model_class.user_challenge.UserChallengeResponse
import com.iroid.healthdomain.data.model_class.user_profile.UserModelResponse
import com.iroid.healthdomain.data.model_class.user_update.UpdateUserResponse
import com.iroid.healthdomain.ui.home.notification.fragments.NotificationSentFragment
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.http.*
import retrofit2.http.Body

import retrofit2.http.POST

import retrofit2.http.FormUrlEncoded
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.HostnameVerifier
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager


interface ApiServices {

    @FormUrlEncoded
    @POST("api/login/otp/generate")
    suspend fun generateOtp(
        @Field("mobile") number: String,
        @Field("country_code") code: String
    ): GenerateOtp

    @FormUrlEncoded
    @POST("api/login/otp/validate")
    suspend fun validateOtp(
        @Field("otp") otpValue: String,
        @Field("mobile") phone: String,
        @Field("country_code") code: String
    ): ValidateOtpResponse

    @FormUrlEncoded
    @POST("api/users/profile/update")
    suspend fun updateUser(
        @Field("name") name: String,
        @Field("age") age: String,
        @Field("gender") gender: String,
        @Field("blood_group") bloodGroup: String,
        @Field("weight") weight: String,
        @Field("height") height: String
    ): UpdateUserResponse

    @GET("api/users/home/index")
    suspend fun getIndexApiFromServer(): IndexApiResponse

    @GET("api/users/profile/details")
    suspend fun getUserDetails(): UserModelResponse

    @FormUrlEncoded
    @POST("api/login/otp/resend")
    suspend fun resendOtp(
        @Field("mobile") number: String,
        @Field("country_code") code: String
    ): GenerateOtp

    @GET("api/users/contact/list")
    suspend fun getContacts(): ContactResponse


    @POST("api/users/profile/upload-profile-image")
    @Multipart
    suspend fun uploadImage(@Part profile_image: MultipartBody.Part): ProfileImageResponse

    @POST("api/users/profile/update")
    suspend fun updateUserProfile(@Body accountModel: AccountModel): UpdateUserResponse

    @FormUrlEncoded
    @POST("api/users/activity/update")
    suspend fun updateStepCount(
        @Field("steps") steps: String,
        @Field("activity_id") activity: String,
        @Field("calories") calories: String,
        @Field("duration") duration: String
    ): UpdateActivityResponse

//    @FormUrlEncoded
//    @POST("api/users/activity/update")
//    suspend fun updateStepCounts( @Field("steps") steps: String,
//                                  @Field("activity_id") activity: String,
//                                  @Field("date") date:String,
//                                  @Field("calories") calories: String,
//                                  @Field("duration") duration: String): UpdateActivityResponse


    @POST("api/users/activity/update")
    suspend fun updateStepCounts(@Body  body: ArrayList<SendStepsUpdates>): UpdateActivityResponse

    @POST("api/users/contact/match-device-contacts")
    suspend fun GetContactMatch(@Body hashedModel: HashedModel): GetContactMatchResponse

    @FormUrlEncoded
    @POST("api/users/invite")
    suspend fun inviteUser(
        @Field("mobile") number: String,
        @Field("country_code") code: String
    ): UserInviteResponse

    @FormUrlEncoded
    @POST("api/users/challenges/create")
    suspend fun createChallenge(
        @Field("challenged_user") challenged_user: String,
        @Field("notes") notes: String
    ): CreateChallengeResponse

    @FormUrlEncoded
    @POST("api/users/favroite/add-favroite")
    suspend fun addRemoveFavorite(
        @Field("favroited_user_id") favroited_user_id: String,
        @Field("favroite") favroite: String
    ): FavResponse


    @GET("api/users/challenges/user/{user_id}")
    suspend fun getPastChallenges(
        @Path("user_id") user_id: String,
        @Query("page") page: String
    ): UserChallengeResponse

    @GET("api/users/notificaiton/received")
    suspend fun getReceivedNotification(): NotificationReceiveResponse

    @GET("api/users/notificaiton/sent")
    suspend fun getSentNotification(): NotificationSentResponse

    @FormUrlEncoded
    @POST("api/users/challenges/update/{challenge_id}")
    suspend fun updateChallenge(
        @Path("challenge_id") challenge_id: String,
        @Field("current_state") current_state: String,
        @Field("notes") notes: String,

    ): UpdateChallengeResponse

    @GET("api/terms-conditions")
    suspend fun getTerms():CmsResponse

    @GET("api/licence-agreement")
    suspend fun getPrivacy():CmsResponse

 /*   companion object {
        fun create(): ApiServices {
            val retrofit = retrofit2.Retrofit.Builder()
                .client(initializeClient())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .baseUrl("http://ec2-13-235-103-217.ap-south-1.compute.amazonaws.com/healthdomain/healthdomain/")
                .build()
            return retrofit.create(ApiServices::class.java)
        }

        private fun initializeClient(): OkHttpClient {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.HEADERS
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            System.setProperty("http.keepAlive", "false")

            val trustAllCerts = arrayOf<TrustManager>(
                object : X509TrustManager {
                    @Throws(CertificateException::class)
                    override fun checkClientTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    @Throws(CertificateException::class)
                    override fun checkServerTrusted(
                        chain: Array<X509Certificate?>?,
                        authType: String?
                    ) {
                    }

                    override fun getAcceptedIssuers(): Array<X509Certificate?>? {
                        return arrayOf()
                    }
                }
            )

            // Install the all-trusting trust manager
            val sslContext = SSLContext.getInstance("SSL")
            sslContext.init(null, trustAllCerts, SecureRandom())
            // Create an ssl socket factory with our all-trusting manager
            val sslSocketFactory = sslContext.socketFactory
            val trustManagerFactory: TrustManagerFactory =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(null as KeyStore?)
            val trustManagers: Array<TrustManager> =
                trustManagerFactory.trustManagers
            check(!(trustManagers.size != 1 || trustManagers[0] !is X509TrustManager)) {
                "Unexpected default trust managers:" + trustManagers.contentToString()
            }

            val trustManager =
                trustManagers[0] as X509TrustManager

            val builder = OkHttpClient.Builder()
            builder.addInterceptor(interceptor)
                .addInterceptor(SupportInterceptor())
                .sslSocketFactory(sslSocketFactory, trustManager)
                .hostnameVerifier(HostnameVerifier { _, _ -> true })
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
            return builder.build()
        }
    }*/
}
