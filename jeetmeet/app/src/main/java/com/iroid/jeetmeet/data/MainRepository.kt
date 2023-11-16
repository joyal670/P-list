package com.iroid.jeetmeet.data

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
import okhttp3.RequestBody
import retrofit2.Response


class MainRepository(private val apiHelper: ApiService) {

    /* Student login */
    suspend fun studentLogin(
        email: String,
        password: String
    ): Response<StudentLoginResponse> =
        apiHelper.studentLogin(email, password)

    /* Student logout */
    suspend fun studentLogout(): Response<StudentLogoutResponse> = apiHelper.studentLogout()

    /* Forgot password */
    suspend fun forgotPassword(email: String, user_type: String): Response<ForgotPasswordResponse> =
        apiHelper.forgotPassword(email, user_type)

    /* Student profile */
    suspend fun studentProfile(): Response<StudentProfileResponse> = apiHelper.studentProfile()

    /* Student diaries*/
    suspend fun studentDiaries(sort: Int): Response<StudentDiariesResponse> =
        apiHelper.studentDiaries(sort)

    /* Student Assignments */
    suspend fun studentAssignments(subject: Int): Response<StudentAssignmentsResponse> =
        apiHelper.studentAssignments(subject)

    /* Student name edit */
    suspend fun studentNameEdit(
        first_name: String,
        middle_name: String,
        last_name: String
    ): Response<StudentEditNameResponse> =
        apiHelper.studentEditName(first_name, middle_name, last_name)

    /* Student profile photo edit */
    suspend fun studentProfilePhotoEdit(images: List<MultipartBody.Part>): Response<StudentEditProfileImageResponse> =
        apiHelper.studentEditProfileImage(images)

    /* Student assigned leave */
    suspend fun studentAssignedLeave(sort: Int): Response<StudentAssignedLeaveResponse> =
        apiHelper.studentAssignedLeave(sort)

    /* Student leave apply */
    suspend fun studentLeaveApply(sort: Int): Response<StudentLeaveApplyResponse> =
        apiHelper.studentLeaveApply(sort)

    /* Student leave apply category */
    suspend fun studentLeaveApplyCategory(): Response<StudentLeaveApplyCatagoryResonse> =
        apiHelper.studentLeaveApplyCategory()

    /* Student submit leave application */
    suspend fun studentSubmitLeaveApplication(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        details: List<MultipartBody.Part>
    ): Response<StudentSubmitLeaveApplicationResponse> =
        apiHelper.studentSubmitLeaveApplication(params, details)

    /* Student edit leave
    * get selected leave application details*/
    suspend fun studentEditLeave(leave_id: Int): Response<StudentEditLeaveResponse> =
        apiHelper.studentEditLeave(leave_id)

    /* Student leave apply delete */
    suspend fun studentDeleteLeave(leave_id: Int): Response<StudentLeaveApplyDeleteResponse> =
        apiHelper.studentDeleteLeave(leave_id)

    /* Student update leave */
    suspend fun studentLeaveUpdate(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        details: List<MultipartBody.Part>
    ): Response<StudentLeaveUpdateResponse> = apiHelper.studentLeaveUpdate(params, details)

    /* Student online class */
    suspend fun studentOnlineClass(): Response<StudentOnlineClassResponse> =
        apiHelper.studentOnlineClass()

    /* Student exam schedule */
    suspend fun studentExamSchedule(
        edate: String,
        sort: Int
    ): Response<StudentExamScheduleResponse> = apiHelper.studentExamSchedule(edate, sort)

    /* Student attend exam */
    suspend fun studentAttendExam(sort: Int): Response<StudentAttendExamResponse> =
        apiHelper.studentAttendExam(sort)

    /* Student request book */
    suspend fun studentRequestBook(sort: Int): Response<StudentRequestBookResponse> =
        apiHelper.studentRequestBook(sort)

    /* Student issued book */
    suspend fun studentIssuedBook(sort: Int): Response<StudentIssuedBookResponse> =
        apiHelper.studentIssuedBook(sort)

    /* Student apply for book */
    suspend fun studentRequestBookApply(book_id: Int): Response<StudentRequestBookApplyResponse> =
        apiHelper.studentRequestBookApply(book_id)

    /* Student cancel for book */
    suspend fun studentRequestBookCancel(req_id: Int): Response<StudentRequestBookCancelResponse> =
        apiHelper.studentRequestBookCancel(req_id)

    /* Student exam result */
    suspend fun studentExamResult(sort: Int): Response<StudentExamResultResponse> =
        apiHelper.studentExamResult(sort)

    /* Student events */
    suspend fun studentEvents(sort: Int): Response<StudentEventsReponse> =
        apiHelper.studentEvents(sort)

    /* Student subjects */
    suspend fun studentSubjects(subject: Int): Response<StudentSubjectsReponse> =
        apiHelper.studentSubjects(subject)

    /* Student exam schedule preview */
    suspend fun studentExamSchedulePreview(exam_id: Int): Response<StudentExamSchedulePreviewResponse> =
        apiHelper.studentExamSchedulePreview(exam_id)

    /* Student exam start */
    suspend fun studentExamStart(exam_id: Int): Response<StudentExamStartResponse> =
        apiHelper.studentExamStart(exam_id)

    /* Student exam save */
    suspend fun studentExamSave(
        exam_id: Int,
        question: ArrayList<Int>,
        answer: ArrayList<String>
    ): Response<StudentExamSaveResponse> = apiHelper.studentExamSave(exam_id, question, answer)

    /* Student online class start */
    suspend fun studentOnlineClassStart(id: Int): Response<StudentOnlineClassStartResponse> =
        apiHelper.studentOnlineClassStart(id)

    /* Student exam result details */
    suspend fun studentExamResultDetails(exam_id: Int): Response<StudentExamResultDetailsResponse> =
        apiHelper.studentExamResultDetails(exam_id)

    /* Student time table */
    suspend fun studentTimeTable(): Response<StudentTimeTableResponse> =
        apiHelper.studentTimeTable()

    /* Student staff directory */
    suspend fun studentStaffDirectory(): Response<StudentStaffDirectoryResponse> =
        apiHelper.studentStaffDirectory()

    /* Student attendance */
    suspend fun studentAttendance(): Response<StudentAttendanceResponse> =
        apiHelper.studentAttendance()

    /* Student diaries delete */
    suspend fun studentDiariesDelete(diary_id: Int): Response<StudentDiariesDeleteResponse> =
        apiHelper.studentDiariesDelete(diary_id)

    /* Student diaries edit */
    suspend fun studentEditDiaries(diary_id: Int): Response<StudentDiariesEditResponse> =
        apiHelper.studentEditDiaries(diary_id)

    /* Student diaries update */
    suspend fun studentUpdateDiaries(
        diary_id: Int,
        note: String,
        date: String
    ): Response<StudentDiariesUpdateResponse> = apiHelper.studentUpdateDiaries(diary_id, note, date)

    /* Student diaries save */
    suspend fun studentSaveDiaries(
        note: String,
        date: String
    ): Response<StudentDiariesSaveResponse> = apiHelper.studentSaveDiaries(note, date)

    /* Student dashboard */
    suspend fun studentDashboard(): Response<StudentDashboardResponse> =
        apiHelper.studentDashboard()

    /* Student view notice */
    suspend fun studentNotice(notice_id: Int): Response<StudentNoticeResponse> =
        apiHelper.studentNotice(notice_id)

    /* Student admin chat */
    suspend fun studentAdminChat(): Response<StudentChatAdminResponse> =
        apiHelper.studentAdminChat()

    /* Student teacher chat */
    suspend fun studentTeacherChat(): Response<StudentChatTeacherResponse> =
        apiHelper.studentTeacherChat()

    /* Student mock test list */
    suspend fun studentMockTestList(
        tdate: String,
        sort: Int
    ): Response<StudentMockTestListResponse> =
        apiHelper.studentMockTestList(tdate, sort)

    /* Student mock test preview */
    suspend fun studentMockTestPreview(test_id: Int): Response<StudentMockTestPreviewResponse> =
        apiHelper.studentMockTestPreview(test_id)

    /* Student mock test start */
    suspend fun studentMockTestStart(test_id: Int): Response<StudentMockTestStartResponse> =
        apiHelper.studentMockTestStart(test_id)

    /* Student mock test save */
    suspend fun studentMockTestSave(
        test_id: Int,
        question: ArrayList<Int>,
        answer: ArrayList<Int>
    ): Response<StudentMockTestSaveResponse> =
        apiHelper.studentMockTestSave(test_id, question, answer)

    /* Student mock test result */
    suspend fun studentMockTestResultList(sort: Int): Response<StudentMockTestResultListResponse> =
        apiHelper.studentMockTestResultList(sort)

    /* Student mock test result view */
    suspend fun studentMockTestResultView(test_id: Int): Response<StudentMockTestResultViewResponse> =
        apiHelper.studentMockTestResultView(test_id)

    /* Student mock test result view details */
    suspend fun studentMockTestResultViewDetails(
        test_id: Int,
        question_id: Int
    ): Response<StudentMockTestResultViewDetailsResponse> =
        apiHelper.studentMockTestResultViewDetails(test_id, question_id)

    /* Student mock test timeout */
    suspend fun studentMockTestTimeout(
        test_id: Int,
        question: ArrayList<Int>,
        answer: ArrayList<Int>
    ): Response<StudentMockTestTimeOutRespose> =
        apiHelper.studentMockTestTimeOut(test_id, question, answer)

    /* Student Admin-Student chat read */
    suspend fun studentChatAdminStudentRead(chat_id: String): Response<StudentChatAdminStudentReadResponse> =
        apiHelper.studentChatAdminStudentRead(chat_id)

    /* Student Admin-Student chat update */
    suspend fun studentChatAdminStudentUpdate(
        chat_id: String,
        message: String
    ): Response<StudentChatAdminStudentUpdateResponse> =
        apiHelper.studentChatAdminStudentUpdate(chat_id, message)

    /* Student chat teachers list */
    suspend fun studentChatTeachersList(search: String): Response<StudentChatTeachersListResponse> =
        apiHelper.studentChatTeachersList(search)

    /* Student teacher new chat*/
    suspend fun studentChatNewTeacher(teacher_id: Int): Response<StudentChatNewTeacherResponse> =
        apiHelper.studentChatNewTeacher(teacher_id)

    /* Student-Teacher chat message update */
    suspend fun studentTeacherChatUpdate(
        chat_id: String,
        message: String
    ): Response<StudentTeacherChatUpdateResponse> =
        apiHelper.studentTeacherChatUpdate(chat_id, message)

    /* Student-Teacher chat read */
    suspend fun studentTeacherChatRead(chat_id: String): Response<StudentTeacherChatReadResponse> =
        apiHelper.studentTeacherChatRead(chat_id)


    /* Parent section*/

    /* Parent login */
    suspend fun parentLogin(email: String, password: String): Response<ParentLoginResponse> =
        apiHelper.parentLogin(email, password)

    /* Parent logout */
    suspend fun parentLogout(): Response<ParentLogoutResponse> = apiHelper.parentLogout()

    /* Parent dashboard */
    suspend fun parentDashboard(): Response<ParentDashboardResponse> = apiHelper.parentDashboard()

    /* Parent profile */
    suspend fun parentProfile(): Response<ParentProfileResponse> = apiHelper.parentProfile()

    /* Parent side menu */
    suspend fun parentSideMenu(): Response<ParentSideMenuResponse> = apiHelper.parentSideMenu()

    /* Parent students list */
    suspend fun parentStudentsList(): Response<ParentStudentsListResponse> =
        apiHelper.parentStudentsList()

    /* Parent student view */
    suspend fun parentStudentView(student_id: Int): Response<ParentStudentViewResponse> =
        apiHelper.parentStudentView(student_id)

    /* Parent calender */
    suspend fun parentCalender(student_id: Int): Response<ParentCalenderResponse> =
        apiHelper.parentCalender(student_id)

    /* Parent leave assigned */
    suspend fun parentLeaveAssigned(sort: Int): Response<ParentLeaveAssignedResponse> =
        apiHelper.parentLeaveAssigned(sort)

    /* Parent time table */
    suspend fun parentTimeTable(student_id: Int): Response<ParentTimeTableResponse> =
        apiHelper.parentTimeTable(student_id)

    /* Parent feedback */
    suspend fun parentFeedback(sort: Int): Response<ParentFeedbacksResponse> =
        apiHelper.parentFeedBack(sort)

    /* Parent edit feedback */
    suspend fun parentFeedbackEdit(feedback_id: Int): Response<ParentFeedbackEditResponse> =
        apiHelper.parentFeedbackEdit(feedback_id)

    /* Parent feedback update */
    suspend fun parentFeedbackUpdate(
        feedback_id: Int,
        date: String,
        note: String
    ): Response<ParentFeedbackUpdateResponse> =
        apiHelper.parentFeedbackUpdate(feedback_id, date, note)

    /* Parent feedback delete */
    suspend fun parentFeedbackDelete(feedback_id: Int): Response<ParentFeedbackDeleteResponse> =
        apiHelper.parentFeedbackDelete(feedback_id)

    /* Parent feedback save */
    suspend fun parentFeedbackSave(
        date: String,
        note: String
    ): Response<ParentFeedbackSaveResponse> = apiHelper.parentFeedbackSave(date, note)

    /* Parent events */
    suspend fun parentEvents(sort: Int): Response<ParentEventsNewResponse> =
        apiHelper.parentEvents(sort)

    /* Parent attendance */
    suspend fun parentAttendance(student_id: Int): Response<ParentAttendanceResponse> =
        apiHelper.parentAttendance(student_id)

    /* Parent meetings */
    suspend fun parentMeetings(): Response<ParentMeetingsResponse> = apiHelper.parentMeetings()

    /* Parent meeting start */
    suspend fun parentMeetingStart(meeting_id: Int): Response<ParentMeetingStartResponse> =
        apiHelper.parentMeetingStart(meeting_id)

    /* Parent profile edit */
    suspend fun parentProfileEdit(): Response<ParentProfileEditResponse> =
        apiHelper.parentProfileEdit()

    /* Parent profile state */
    suspend fun parentProfileState(country_id: Int): Response<ParentProfileStateResponse> =
        apiHelper.parentProfileState(country_id)

    /* Parent profile update */
    suspend fun parentProfileUpdate(
        email: String,
        std_code: Int,
        phone: Int,
        address: String,
        place: String,
        zip: Int,
        state: Int,
        country: Int,
        nationality: Int
    ): Response<ParentProfileUpdateResponse> = apiHelper.parentProfileUpdate(
        email,
        std_code,
        phone,
        address,
        place,
        zip,
        state,
        country,
        nationality
    )

    /* Parent notice view */
    suspend fun parentNoticeView(notice_id: Int): Response<ParentNoticeViewResponse> =
        apiHelper.parentNoticeView(notice_id)

    /* Parent teacher chat */
    suspend fun parentTeacherChat(): Response<ParentTeacherChatResponse> =
        apiHelper.parentTeacherChat()

    /* Parent chat teachers list */
    suspend fun parentChatTeachersList(search: String): Response<ParentChatTeachersListResponse> =
        apiHelper.parentChatTeachersListResponse(search)

    /* Parent teacher selected */
    suspend fun parentChatTeacherSelected(teacher_id: Int): Response<ParentChatTeacherSelectedResponse> =
        apiHelper.parentChatTeacherSelected(teacher_id)

    /* Parent teacher chat update */
    suspend fun parentChatTeacherMessageUpdate(
        chat_id: String,
        message: String
    ): Response<ParentChatTeacherMessageUpdateResponse> =
        apiHelper.parentChatTeacherMessageUpdate(chat_id, message)

    /* Parent teacher chat message read */
    suspend fun parentChatTeacherMessageRead(chat_id: String): Response<ParentChatTeacherMessageReadResponse> =
        apiHelper.parentChatTeacherMessageRead(chat_id)

    /* My account  */
    suspend fun parentMyAccount(student_id: String): Response<ParentMyAccount> =
        apiHelper.parentMyAccount(student_id)

    /* My Account Details */
    suspend fun parentMyAccountDetails(student_code: String): Response<ParentMyAccountDetailsResponse> =
        apiHelper.parentMyAccountDetails(student_code)

    /* My Account Pay Partially */
    suspend fun parentMyAccountPayPartially(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        posting_id: List<Int>
    ): Response<ParentMyAccountPartialPayResponse> =
        apiHelper.parentMyAccountPayPartially(params, posting_id)

    /* My Account Debit from Advance */
    suspend fun parentMyAccountDebitFromAdvance(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        posting_id: List<Int>
    ): Response<ParentMyaccountDebitFromAdvanceResponse> =
        apiHelper.parentMyAccountDebitFromAdvance(params, posting_id)

    /* My Account payment */
    suspend fun parentMyAccountPayment(
        params: Map<String, @JvmSuppressWildcards RequestBody>,
        posting_id: List<Int>
    ): Response<ParentMyAccountPaymentResponse> =
        apiHelper.parentMyAccountPayment(params, posting_id)
}


