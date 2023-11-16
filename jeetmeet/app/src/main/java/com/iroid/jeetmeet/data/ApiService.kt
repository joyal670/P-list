package com.iroid.jeetmeet.data

import com.androidnetworking.interceptors.HttpLoggingInterceptor
import com.iroid.jeetmeet.modal.forgot_password.ForgotPasswordResponse
import com.iroid.jeetmeet.modal.parent.attaendance.ParentAttendanceResponse
import com.iroid.jeetmeet.modal.parent.calender.ParentCalenderResponse
import com.iroid.jeetmeet.modal.parent.chat_teacher.ParentTeacherChatResponse
import com.iroid.jeetmeet.modal.parent.chat_teacher_message_read.ParentChatTeacherMessageReadResponse
import com.iroid.jeetmeet.modal.parent.chat_teacher_message_update.ParentChatTeacherMessageUpdateResponse
import com.iroid.jeetmeet.modal.parent.chat_teacher_selected.ParentChatTeacherSelectedResponse
import com.iroid.jeetmeet.modal.parent.chat_teachers_list.ParentChatTeachersListResponse
import com.iroid.jeetmeet.modal.parent.dashboard.ParentDashboardResponse
import com.iroid.jeetmeet.modal.parent.events.new.ParentEventsNewResponse
import com.iroid.jeetmeet.modal.parent.feedback_delete.ParentFeedbackDeleteResponse
import com.iroid.jeetmeet.modal.parent.feedback_edit.ParentFeedbackEditResponse
import com.iroid.jeetmeet.modal.parent.feedback_save.ParentFeedbackSaveResponse
import com.iroid.jeetmeet.modal.parent.feedback_update.ParentFeedbackUpdateResponse
import com.iroid.jeetmeet.modal.parent.feedbacks.ParentFeedbacksResponse
import com.iroid.jeetmeet.modal.parent.leave_assigned.ParentLeaveAssignedResponse
import com.iroid.jeetmeet.modal.parent.login.ParentLoginResponse
import com.iroid.jeetmeet.modal.parent.logout.ParentLogoutResponse
import com.iroid.jeetmeet.modal.parent.meeting_start.ParentMeetingStartResponse
import com.iroid.jeetmeet.modal.parent.meetings.ParentMeetingsResponse
import com.iroid.jeetmeet.modal.parent.my_account.ParentMyAccount
import com.iroid.jeetmeet.modal.parent.my_account_debit_from_advance.ParentMyaccountDebitFromAdvanceResponse
import com.iroid.jeetmeet.modal.parent.my_account_details.ParentMyAccountDetailsResponse
import com.iroid.jeetmeet.modal.parent.my_account_partial_pay.ParentMyAccountPartialPayResponse
import com.iroid.jeetmeet.modal.parent.my_account_payment.ParentMyAccountPaymentResponse
import com.iroid.jeetmeet.modal.parent.notice_view.ParentNoticeViewResponse
import com.iroid.jeetmeet.modal.parent.profile.ParentProfileResponse
import com.iroid.jeetmeet.modal.parent.profile_edit.ParentProfileEditResponse
import com.iroid.jeetmeet.modal.parent.profile_state.ParentProfileStateResponse
import com.iroid.jeetmeet.modal.parent.profile_update.ParentProfileUpdateResponse
import com.iroid.jeetmeet.modal.parent.side_menu.ParentSideMenuResponse
import com.iroid.jeetmeet.modal.parent.student_view.ParentStudentViewResponse
import com.iroid.jeetmeet.modal.parent.students_list.ParentStudentsListResponse
import com.iroid.jeetmeet.modal.parent.time_table.ParentTimeTableResponse
import com.iroid.jeetmeet.modal.student.assigned_leave.StudentAssignedLeaveResponse
import com.iroid.jeetmeet.modal.student.assignments.StudentAssignmentsResponse
import com.iroid.jeetmeet.modal.student.attend_exam.StudentAttendExamResponse
import com.iroid.jeetmeet.modal.student.attendance.StudentAttendanceResponse
import com.iroid.jeetmeet.modal.student.chat_admin.StudentChatAdminResponse
import com.iroid.jeetmeet.modal.student.chat_student_admin_read.StudentChatAdminStudentReadResponse
import com.iroid.jeetmeet.modal.student.chat_student_admin_update.StudentChatAdminStudentUpdateResponse
import com.iroid.jeetmeet.modal.student.chat_student_teacher.StudentChatTeacherResponse
import com.iroid.jeetmeet.modal.student.chat_student_teacher_read.StudentTeacherChatReadResponse
import com.iroid.jeetmeet.modal.student.chat_student_teacher_selected.StudentChatNewTeacherResponse
import com.iroid.jeetmeet.modal.student.chat_student_teacher_update.StudentTeacherChatUpdateResponse
import com.iroid.jeetmeet.modal.student.chat_student_teachers_list.StudentChatTeachersListResponse
import com.iroid.jeetmeet.modal.student.dashboard.StudentDashboardResponse
import com.iroid.jeetmeet.modal.student.diaries.StudentDiariesResponse
import com.iroid.jeetmeet.modal.student.diaries_delete.StudentDiariesDeleteResponse
import com.iroid.jeetmeet.modal.student.diaries_edit.StudentDiariesEditResponse
import com.iroid.jeetmeet.modal.student.diaries_save.StudentDiariesSaveResponse
import com.iroid.jeetmeet.modal.student.diaries_update.StudentDiariesUpdateResponse
import com.iroid.jeetmeet.modal.student.edit_leave.StudentEditLeaveResponse
import com.iroid.jeetmeet.modal.student.edit_name.StudentEditNameResponse
import com.iroid.jeetmeet.modal.student.edit_profile_image.StudentEditProfileImageResponse
import com.iroid.jeetmeet.modal.student.events.StudentEventsReponse
import com.iroid.jeetmeet.modal.student.exam_result.StudentExamResultResponse
import com.iroid.jeetmeet.modal.student.exam_result_details.StudentExamResultDetailsResponse
import com.iroid.jeetmeet.modal.student.exam_save.StudentExamSaveResponse
import com.iroid.jeetmeet.modal.student.exam_schedule.StudentExamScheduleResponse
import com.iroid.jeetmeet.modal.student.exam_schedule_preview.StudentExamSchedulePreviewResponse
import com.iroid.jeetmeet.modal.student.exam_start.StudentExamStartResponse
import com.iroid.jeetmeet.modal.student.issued_books.StudentIssuedBookResponse
import com.iroid.jeetmeet.modal.student.leave_apply.StudentLeaveApplyResponse
import com.iroid.jeetmeet.modal.student.leave_apply_catagory.StudentLeaveApplyCatagoryResonse
import com.iroid.jeetmeet.modal.student.leave_apply_delete.StudentLeaveApplyDeleteResponse
import com.iroid.jeetmeet.modal.student.leave_update.StudentLeaveUpdateResponse
import com.iroid.jeetmeet.modal.student.login.StudentLoginResponse
import com.iroid.jeetmeet.modal.student.logout.StudentLogoutResponse
import com.iroid.jeetmeet.modal.student.mock_test_list.StudentMockTestListResponse
import com.iroid.jeetmeet.modal.student.mock_test_preview.StudentMockTestPreviewResponse
import com.iroid.jeetmeet.modal.student.mock_test_result_list.StudentMockTestResultListResponse
import com.iroid.jeetmeet.modal.student.mock_test_result_view.StudentMockTestResultViewResponse
import com.iroid.jeetmeet.modal.student.mock_test_result_view_details.StudentMockTestResultViewDetailsResponse
import com.iroid.jeetmeet.modal.student.mock_test_save.StudentMockTestSaveResponse
import com.iroid.jeetmeet.modal.student.mock_test_start.StudentMockTestStartResponse
import com.iroid.jeetmeet.modal.student.mock_test_timeout.StudentMockTestTimeOutRespose
import com.iroid.jeetmeet.modal.student.notice.StudentNoticeResponse
import com.iroid.jeetmeet.modal.student.online_class.StudentOnlineClassResponse
import com.iroid.jeetmeet.modal.student.online_class_start.StudentOnlineClassStartResponse
import com.iroid.jeetmeet.modal.student.profile.StudentProfileResponse
import com.iroid.jeetmeet.modal.student.request_book.StudentRequestBookResponse
import com.iroid.jeetmeet.modal.student.request_book_apply.StudentRequestBookApplyResponse
import com.iroid.jeetmeet.modal.student.request_book_cancel.StudentRequestBookCancelResponse
import com.iroid.jeetmeet.modal.student.staff_directory.StudentStaffDirectoryResponse
import com.iroid.jeetmeet.modal.student.subjects.StudentSubjectsReponse
import com.iroid.jeetmeet.modal.student.submit_leave_application.StudentSubmitLeaveApplicationResponse
import com.iroid.jeetmeet.modal.student.timetable.StudentTimeTableResponse
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*
import java.security.KeyStore
import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.net.ssl.*

interface ApiService {

    /* retrofit builder */
    companion object {
        fun create(): ApiService {
            val retrofit = retrofit2.Retrofit.Builder()
                .client(initializeClient())
                .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
                .baseUrl("https://www.jeetmeet.com/api/")
                .build()
            return retrofit.create(ApiService::class.java)
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
    }

    /* API' s */

    /* Student Login */
    @FormUrlEncoded
    @POST("student/login")
    suspend fun studentLogin(
        @Field("username") email: String,
        @Field("password") password: String
    ): Response<StudentLoginResponse>

    /* Student logout */
    @GET("student/logout")
    suspend fun studentLogout(): Response<StudentLogoutResponse>

    /* Forgot password */
    @FormUrlEncoded
    @POST("forgot-password")
    suspend fun forgotPassword(
        @Field("email") email: String,
        @Field("user_type") user_type: String
    ): Response<ForgotPasswordResponse>

    /* Student profile */
    @GET("student/details")
    suspend fun studentProfile(): Response<StudentProfileResponse>

    /* Student diaries */
    @FormUrlEncoded
    @POST("student/diaries")
    suspend fun studentDiaries(@Field("sort") sort: Int): Response<StudentDiariesResponse>

    /* Student assignments */
    @FormUrlEncoded
    @POST("student/assignments")
    suspend fun studentAssignments(@Field("subject") subject: Int): Response<StudentAssignmentsResponse>

    /* Student edit name */
    @FormUrlEncoded
    @POST("student/profile/edit_name")
    suspend fun studentEditName(
        @Field("first_name") first_name: String,
        @Field("middle_name") middle_name: String,
        @Field("last_name") last_name: String
    ): Response<StudentEditNameResponse>

    /* Student edit profile image */
    @Multipart
    @POST("student/profile/edit_picture")
    suspend fun studentEditProfileImage(@Part images: List<MultipartBody.Part>): Response<StudentEditProfileImageResponse>

    /* Student assigned leave */
    @FormUrlEncoded
    @POST("student/leave/assigned")
    suspend fun studentAssignedLeave(@Field("sort") sort: Int): Response<StudentAssignedLeaveResponse>

    /* Student leave apply */
    @FormUrlEncoded
    @POST("student/leave/apply")
    suspend fun studentLeaveApply(@Field("sort") sort: Int): Response<StudentLeaveApplyResponse>

    /* Student leave apply category */
    @GET("student/leave/apply/add")
    suspend fun studentLeaveApplyCategory(): Response<StudentLeaveApplyCatagoryResonse>

    /* Student submit leave application */
    @Multipart
    @POST("student/leave/apply/save")
    suspend fun studentSubmitLeaveApplication(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part details: List<MultipartBody.Part>
    ): Response<StudentSubmitLeaveApplicationResponse>

    /* Student edit leave
    * get selected leave application details*/
    @FormUrlEncoded
    @POST("student/leave/apply/edit")
    suspend fun studentEditLeave(@Field("leave_id") leave_id: Int): Response<StudentEditLeaveResponse>

    /* Student leave apply delete */
    @FormUrlEncoded
    @POST("student/leave/apply/delete")
    suspend fun studentDeleteLeave(@Field("leave_id") leave_id: Int): Response<StudentLeaveApplyDeleteResponse>

    /* Student update leave */
    @Multipart
    @POST("student/leave/apply/update")
    suspend fun studentLeaveUpdate(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part details: List<MultipartBody.Part>
    ): Response<StudentLeaveUpdateResponse>

    /* Student online class */
    @GET("student/online-class")
    suspend fun studentOnlineClass(): Response<StudentOnlineClassResponse>

    /* Student exam schedule */
    @FormUrlEncoded
    @POST("student/exam/schedule")
    suspend fun studentExamSchedule(
        @Field("edate") edate: String,
        @Field("sort") sort: Int
    ): Response<StudentExamScheduleResponse>

    /* Student attend exam */
    @FormUrlEncoded
    @POST("student/exam/attended")
    suspend fun studentAttendExam(@Field("sort") sort: Int): Response<StudentAttendExamResponse>

    /* Student request book */
    @FormUrlEncoded
    @POST("student/request-books")
    suspend fun studentRequestBook(@Field("sort") sort: Int): Response<StudentRequestBookResponse>

    /* Student issued book */
    @FormUrlEncoded
    @POST("student/issued-books")
    suspend fun studentIssuedBook(@Field("sort") sort: Int): Response<StudentIssuedBookResponse>

    /* Student apply for book */
    @FormUrlEncoded
    @POST("student/request-book/apply")
    suspend fun studentRequestBookApply(@Field("book_id") book_id: Int): Response<StudentRequestBookApplyResponse>

    /* Student cancel for book */
    @FormUrlEncoded
    @POST("student/request-book/cancel")
    suspend fun studentRequestBookCancel(@Field("req_id") req_id: Int): Response<StudentRequestBookCancelResponse>

    /* Student exam result */
    @FormUrlEncoded
    @POST("student/exam/result")
    suspend fun studentExamResult(@Field("sort") sort: Int): Response<StudentExamResultResponse>

    /* Student events */
    @FormUrlEncoded
    @POST("student/events")
    suspend fun studentEvents(@Field("sort") sort: Int): Response<StudentEventsReponse>

    /* Student subjects */
    @FormUrlEncoded
    @POST("student/subjects")
    suspend fun studentSubjects(@Field("subject") subject: Int): Response<StudentSubjectsReponse>

    /* Student exam schedule preview */
    @FormUrlEncoded
    @POST("student/exam/schedule/preview")
    suspend fun studentExamSchedulePreview(@Field("exam_id") exam_id: Int): Response<StudentExamSchedulePreviewResponse>

    /* Student exam start */
    @FormUrlEncoded
    @POST("student/exam/start")
    suspend fun studentExamStart(@Field("exam_id") exam_id: Int): Response<StudentExamStartResponse>

    /* Student exam save */
    @FormUrlEncoded
    @POST("student/exam/save")
    suspend fun studentExamSave(
        @Field("exam_id") exam_id: Int,
        @Field("question[]") question: ArrayList<Int>,
        @Field("answer[]") answer: ArrayList<String>
    ): Response<StudentExamSaveResponse>

    /* Student online class start */
    @FormUrlEncoded
    @POST("student/online-class/start")
    suspend fun studentOnlineClassStart(@Field("id") id: Int): Response<StudentOnlineClassStartResponse>

    /* Student exam result details */
    @FormUrlEncoded
    @POST("student/exam/result/view")
    suspend fun studentExamResultDetails(@Field("exam_id") exam_id: Int): Response<StudentExamResultDetailsResponse>

    /* Student time table */
    @GET("student/timetable")
    suspend fun studentTimeTable(): Response<StudentTimeTableResponse>

    /* Student staff directory */
    @GET("student/staff-directory")
    suspend fun studentStaffDirectory(): Response<StudentStaffDirectoryResponse>

    /* Student attendance */
    @GET("student/attandance")
    suspend fun studentAttendance(): Response<StudentAttendanceResponse>

    /* Student diaries delete */
    @FormUrlEncoded
    @POST("student/diaries/delete")
    suspend fun studentDiariesDelete(@Field("diary_id") diary_id: Int): Response<StudentDiariesDeleteResponse>

    /* Student diaries edit */
    @FormUrlEncoded
    @POST("student/diaries/edit")
    suspend fun studentEditDiaries(@Field("diary_id") diary_id: Int): Response<StudentDiariesEditResponse>

    /* Student diaries update */
    @FormUrlEncoded
    @POST("student/diaries/update")
    suspend fun studentUpdateDiaries(
        @Field("diary_id") diary_id: Int,
        @Field("note") note: String,
        @Field("date") date: String
    ): Response<StudentDiariesUpdateResponse>

    /* Student diaries save */
    @FormUrlEncoded
    @POST("student/diaries/save")
    suspend fun studentSaveDiaries(
        @Field("note") note: String,
        @Field("date") date: String
    ): Response<StudentDiariesSaveResponse>

    /* Student dashboard */
    @GET("student/dashboard")
    suspend fun studentDashboard(): Response<StudentDashboardResponse>

    /* Student view notice */
    @FormUrlEncoded
    @POST("student/notice-view")
    suspend fun studentNotice(@Field("notice_id") notice_id: Int): Response<StudentNoticeResponse>

    /* Student admin chat */
    @GET("student/chat/admin")
    suspend fun studentAdminChat(): Response<StudentChatAdminResponse>

    /* Student teacher chat */
    @GET("student/chat/teacher")
    suspend fun studentTeacherChat(): Response<StudentChatTeacherResponse>

    /* Student mock test list */
    @FormUrlEncoded
    @POST("student/mock/list")
    suspend fun studentMockTestList(
        @Field("tdate") tdate: String,
        @Field("sort") sort: Int
    ): Response<StudentMockTestListResponse>

    /* Student mock test preview */
    @FormUrlEncoded
    @POST("student/mock/preview")
    suspend fun studentMockTestPreview(@Field("test_id") test_id: Int): Response<StudentMockTestPreviewResponse>

    /* Student mock test start */
    @FormUrlEncoded
    @POST("student/mock/start")
    suspend fun studentMockTestStart(@Field("test_id") test_id: Int): Response<StudentMockTestStartResponse>

    /* Student mock test save */
    @FormUrlEncoded
    @POST("student/mock/save")
    suspend fun studentMockTestSave(
        @Field("test_id") test_id: Int,
        @Field("question[]") question: ArrayList<Int>,
        @Field("answer[]") answer: ArrayList<Int>
    ): Response<StudentMockTestSaveResponse>

    /* Student mock test result */
    @FormUrlEncoded
    @POST("student/mock/result/test-list")
    suspend fun studentMockTestResultList(@Field("sort") sort: Int): Response<StudentMockTestResultListResponse>

    /* Student mock test result view */
    @FormUrlEncoded
    @POST("student/mock/result/view")
    suspend fun studentMockTestResultView(@Field("test_id") test_id: Int): Response<StudentMockTestResultViewResponse>

    /* Student mock test result view details */
    @FormUrlEncoded
    @POST("student/mock/result/view/details")
    suspend fun studentMockTestResultViewDetails(
        @Field("test_id") test_id: Int,
        @Field("question_id") question_id: Int
    ): Response<StudentMockTestResultViewDetailsResponse>

    /* Student mock test timeout */
    @FormUrlEncoded
    @POST("student/mock/timeout")
    suspend fun studentMockTestTimeOut(
        @Field("test_id") test_id: Int,
        @Field("question[]") question: ArrayList<Int>,
        @Field("answer[]") answer: ArrayList<Int>
    ): Response<StudentMockTestTimeOutRespose>

    /* Student Admin-Student chat read */
    @FormUrlEncoded
    @POST("student/chat/read-studentadmin-message")
    suspend fun studentChatAdminStudentRead(@Field("chat_id") chat_id: String): Response<StudentChatAdminStudentReadResponse>

    /* Student Admin-Student chat update */
    @FormUrlEncoded
    @POST("student/chat/update-studentadmin-message")
    suspend fun studentChatAdminStudentUpdate(
        @Field("chat_id") chat_id: String,
        @Field("message") message: String
    ): Response<StudentChatAdminStudentUpdateResponse>

    /* Student chat teachers list */
    @FormUrlEncoded
    @POST("student/chat/get-teachers")
    suspend fun studentChatTeachersList(@Field("search") search: String): Response<StudentChatTeachersListResponse>

    /* Student teacher new chat*/
    @FormUrlEncoded
    @POST("student/chat/new-chatteacher")
    suspend fun studentChatNewTeacher(@Field("teacher_id") teacher_id: Int): Response<StudentChatNewTeacherResponse>

    /* Student-Teacher chat message update */
    @FormUrlEncoded
    @POST("student/chat/update-studentteacher-message")
    suspend fun studentTeacherChatUpdate(
        @Field("chat_id") chat_id: String,
        @Field("message") message: String
    ): Response<StudentTeacherChatUpdateResponse>

    /* Student-Teacher chat read */
    @FormUrlEncoded
    @POST("student/chat/read-studentteacher-message")
    suspend fun studentTeacherChatRead(@Field("chat_id") chat_id: String): Response<StudentTeacherChatReadResponse>


    /* Parent section*/

    /* Parent login */
    @FormUrlEncoded
    @POST("parent/login")
    suspend fun parentLogin(
        @Field("username") email: String,
        @Field("password") password: String
    ): Response<ParentLoginResponse>

    /* Parent logout */
    @GET("parent/logout")
    suspend fun parentLogout(): Response<ParentLogoutResponse>

    /* Parent dashboard */
    @GET("parent/dashboard")
    suspend fun parentDashboard(): Response<ParentDashboardResponse>

    /* Parent profile */
    @GET("parent/profile")
    suspend fun parentProfile(): Response<ParentProfileResponse>

    /* Parent side menu */
    @GET("parent/menu")
    suspend fun parentSideMenu(): Response<ParentSideMenuResponse>

    /* Parent students list */
    @GET("parent/student/list")
    suspend fun parentStudentsList(): Response<ParentStudentsListResponse>

    /* Parent student view */
    @FormUrlEncoded
    @POST("parent/student/view")
    suspend fun parentStudentView(@Field("student_id") student_id: Int): Response<ParentStudentViewResponse>

    /* Parent calender */
    @FormUrlEncoded
    @POST("parent/calendar")
    suspend fun parentCalender(@Field("student_id") student_id: Int): Response<ParentCalenderResponse>

    /* Parent leave assigned */
    @FormUrlEncoded
    @POST("parent/student/leave-assigned")
    suspend fun parentLeaveAssigned(@Field("sort") sort: Int): Response<ParentLeaveAssignedResponse>

    /* Parent time table */
    @FormUrlEncoded
    @POST("parent/timetable")
    suspend fun parentTimeTable(@Field("student_id") student_id: Int): Response<ParentTimeTableResponse>

    /* Parent feedback */
    @FormUrlEncoded
    @POST("parent/feedback")
    suspend fun parentFeedBack(@Field("sort") sort: Int): Response<ParentFeedbacksResponse>

    /* Parent edit feedback */
    @FormUrlEncoded
    @POST("parent/feedback/edit")
    suspend fun parentFeedbackEdit(@Field("feedback_id") feedback_id: Int): Response<ParentFeedbackEditResponse>

    /* Parent feedback update */
    @FormUrlEncoded
    @POST("parent/feedback/update")
    suspend fun parentFeedbackUpdate(
        @Field("feedback_id") feedback_id: Int,
        @Field("date") date: String,
        @Field("note") note: String
    ): Response<ParentFeedbackUpdateResponse>

    /* Parent feedback delete */
    @FormUrlEncoded
    @POST("parent/feedback/delete/")
    suspend fun parentFeedbackDelete(@Field("feedback_id") feedback_id: Int): Response<ParentFeedbackDeleteResponse>

    /* Parent feedback save */
    @FormUrlEncoded
    @POST("parent/feedback/save")
    suspend fun parentFeedbackSave(
        @Field("date") date: String,
        @Field("note") note: String
    ): Response<ParentFeedbackSaveResponse>

    /* Parent events */
    @FormUrlEncoded
    @POST("parent/events")
    suspend fun parentEvents(@Field("sort") sort: Int): Response<ParentEventsNewResponse>

    /* Parent attendance */
    @FormUrlEncoded
    @POST("parent/student/attendance")
    suspend fun parentAttendance(@Field("student_id") student_id: Int): Response<ParentAttendanceResponse>

    /* Parent meetings */
    @GET("parent/meeting")
    suspend fun parentMeetings(): Response<ParentMeetingsResponse>

    /* Parent meeting start */
    @FormUrlEncoded
    @POST("parent/meeting/start")
    suspend fun parentMeetingStart(@Field("meeting_id") meeting_id: Int): Response<ParentMeetingStartResponse>

    /* Parent profile edit */
    @GET("parent/profile/edit")
    suspend fun parentProfileEdit(): Response<ParentProfileEditResponse>

    /* Parent profile state */
    @FormUrlEncoded
    @POST("parent/profile/edit/states-list")
    suspend fun parentProfileState(@Field("country_id") country_id: Int): Response<ParentProfileStateResponse>

    /* Parent profile update */
    @FormUrlEncoded
    @POST("parent/profile/update")
    suspend fun parentProfileUpdate(
        @Field("email") email: String,
        @Field("std_code") std_code: Int,
        @Field("phone") phone: Int,
        @Field("address") address: String,
        @Field("place") place: String,
        @Field("zip") zip: Int,
        @Field("state") state: Int,
        @Field("country") country: Int,
        @Field("nationality") nationality: Int
    ): Response<ParentProfileUpdateResponse>

    /* Parent notice view */
    @FormUrlEncoded
    @POST("parent/notice-view")
    suspend fun parentNoticeView(@Field("notice_id") notice_id: Int): Response<ParentNoticeViewResponse>

    /* Parent teacher chat */
    @GET("parent/chat/teacher")
    suspend fun parentTeacherChat(): Response<ParentTeacherChatResponse>

    /* Parent chat teachers list */
    @FormUrlEncoded
    @POST("parent/chat/get-teachers")
    suspend fun parentChatTeachersListResponse(@Field("search") search: String): Response<ParentChatTeachersListResponse>

    /* Parent teacher selected */
    @FormUrlEncoded
    @POST("parent/chat/new-chatteacher")
    suspend fun parentChatTeacherSelected(@Field("teacher_id") teacher_id: Int): Response<ParentChatTeacherSelectedResponse>

    /* Parent teacher chat update */
    @FormUrlEncoded
    @POST("parent/chat/update-parentteacher-message")
    suspend fun parentChatTeacherMessageUpdate(
        @Field("chat_id") chat_id: String,
        @Field("message") message: String
    ): Response<ParentChatTeacherMessageUpdateResponse>

    /* Parent teacher chat message read */
    @FormUrlEncoded
    @POST("parent/chat/read-parentteacher-message")
    suspend fun parentChatTeacherMessageRead(@Field("chat_id") chat_id: String): Response<ParentChatTeacherMessageReadResponse>

    /* My account  */
    @FormUrlEncoded
    @POST("parent/student-billing")
    suspend fun parentMyAccount(@Field("student_id") student_id: String): Response<ParentMyAccount>

    /* My Account Details */
    @FormUrlEncoded
    @POST("parent/student-show-payment")
    suspend fun parentMyAccountDetails(@Field("student_code") student_code: String): Response<ParentMyAccountDetailsResponse>

    /* My Account Pay Partially */
    @Multipart
    @POST("parent/student-partialPayment/invoice")
    suspend fun parentMyAccountPayPartially(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part("posting_id[]") posting_id: List<Int>
    ): Response<ParentMyAccountPartialPayResponse>

    /* My Account Debit from Advance */
    @Multipart
    @POST("parent/student-debitFromAdvance/invoice")
    suspend fun parentMyAccountDebitFromAdvance(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part("posting_id[]") posting_id: List<Int>
    ): Response<ParentMyaccountDebitFromAdvanceResponse>

    /* My Account payment */
    @Multipart
    @POST("parent/student-payOnline/invoice")
    suspend fun parentMyAccountPayment(
        @PartMap params: Map<String, @JvmSuppressWildcards RequestBody>,
        @Part("posting_id[]") posting_id: List<Int>
    ): Response<ParentMyAccountPaymentResponse>
}