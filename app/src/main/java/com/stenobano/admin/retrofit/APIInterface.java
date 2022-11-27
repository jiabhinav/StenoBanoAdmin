package com.stenobano.admin.retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.stenobano.admin.chat.notification_model.RootModel;
import com.stenobano.admin.model.AddNews;
import com.stenobano.admin.model.Category_Model;
import com.stenobano.admin.model.DeleteNews;
import com.stenobano.admin.model.DeleteWord;
import com.stenobano.admin.model.GetSearchWord;
import com.stenobano.admin.model.HomeModelCategory;
import com.stenobano.admin.model.ModelAds;
import com.stenobano.admin.model.ModelCategory;
import com.stenobano.admin.model.ModelCategoryDetail;

import com.stenobano.admin.model.ModelSubscription;
import com.stenobano.admin.model.SearchWord;

import com.stenobano.admin.model.SerchUserListModel;
import com.stenobano.admin.model.UpdateNews;
import com.stenobano.admin.model.UserDetailsModel;
import com.stenobano.admin.model.UserRequestModel;
import com.stenobano.admin.model.dashboard.DashBoardModel;
import com.stenobano.admin.model.news.NewsDetailsModel;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface APIInterface {

    /*
    @Multipart
    @POST("meetings")
    Call<AddMeeting>getMeeting(@Part("CompCode") RequestBody CompCod,
                                @Part("CentCode")RequestBody CentCode,
                                @Part("TypeCode")RequestBody TypeCode,
                                @Part("CityCode")RequestBody CityCode,
                                @Part("ClientCode")RequestBody ClientCode,
                                @Part("ClientName")RequestBody ClientName,
                                @Part("Mobile")RequestBody Mobile,
                                // @Part("VisitStatus")RequestBody VisitStatus,
                                @Part("Addr1")RequestBody Addr1,
                                @Part("Addr2")RequestBody Addr2,
                                @Part MultipartBody.Part PhotoPath);
                                */


    @Multipart
    @POST("api.php?p=tokenUpdateUser")
    Call<JsonObject>tokenUpdateUser(
            @Part("user_id") RequestBody user_id,
            @Part("token") RequestBody token
    );

    @Multipart
    @POST("notification/startlive_notification.php")
    Call<JsonObject>startlive_notification(
            @Part("sender_id") RequestBody user_id,
            @Part("school_id") RequestBody school_id,
            @Part("title") RequestBody title,
            @Part("body") RequestBody body,
            @Part("topic") RequestBody topic,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id,
            @Part("subject") RequestBody subject
    );

    @Multipart
    @POST("api.php?p=joinroombyuser")
    Call<JsonObject>joinroombyuser(
            @Part("school_id") RequestBody school_id,
            @Part("teacher_id") RequestBody teacher_id,
            @Part("student_id") RequestBody student_id,
            @Part("room_id") RequestBody room_id,
            @Part("type") RequestBody type

    );


    @Multipart
    @POST("api.php?p=updateLiveStatus")
    Call<JsonObject>updateLiveStatus(
            @Part("school_id") RequestBody school_id,
            @Part("teacher_id") RequestBody teacher_id,
            @Part("student_id") RequestBody student_id,
            @Part("room_id") RequestBody room_id,
            @Part("status") RequestBody status
    );


    @Multipart
    @POST("update_token.php")
    Call<JsonObject>update_token(
            @Part("user_id") RequestBody user_id,
            @Part("token") RequestBody token,
            @Part("type") RequestBody type
    );

    @Multipart
    @POST("api.php?p=updateStudentLiveStatus")
    Call<JsonObject>updateStudentLiveStatus(
            @Part("school_id") RequestBody school_id,
            @Part("teacher_id") RequestBody teacher_id,
            @Part("student_id") RequestBody student_id,
            @Part("room_id") RequestBody room_id,
            @Part("status") RequestBody status
    );




    @Multipart
    @POST("api.php?p=removeuserfromroom")
    Call<JsonObject>removeuserfromroom(
            @Part("school_id") RequestBody school_id,
            @Part("teacher_id") RequestBody teacher_id,
            @Part("student_id") RequestBody student_id,
            @Part("room_id") RequestBody room_id,
            @Part("type") RequestBody type

    );




    @Multipart
    @POST("notification/sendNotification.php")
    Call<JsonObject>sendNotification(
            @Part("sender_id") RequestBody user_id,
            @Part("school_id") RequestBody school_id,
            @Part("title") RequestBody title,
            @Part("body") RequestBody body,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id
    );


    @Multipart
    @POST("sign_upas_provider")
    Call<JsonObject> sign_upas_provider(@Part("name") RequestBody name,
                                        @Part("lat") RequestBody CompCod,
                                        @Part("lon") RequestBody CentCode,
                                        @Part("mobile") RequestBody TypeCode,
                                        @Part("cat_id") RequestBody CityCode,
                                        @Part("amt") RequestBody ClientCode,
                                        @Part("state") RequestBody ClientName,
                                        @Part("city") RequestBody Mobile,
                                        @Part("address") RequestBody Addr1,
                                        @Part MultipartBody.Part PhotoPath);



    @Headers({"Authorization: key=" + "AAAAokNr_4I:APA91bGRMjunsGfW9UXeLrHZ8UWtPI-4dx5NFxIAC4Qbelha_h7T6ygive7xSycBIIoSlF5HDGnLENwmeYCc_E9niCIXHjolUe4yt7tsP6ZOpW6vDO4WxpzI2ZANfj32BNNIu29GkvFO", "Content-Type:application/json"})
    @POST("fcm/send")
    Call<JsonObject> sendNotification(@Body RootModel root);



    @Multipart
    @POST("api.php?p=updateProfilePic")
    Call<JsonObject> updateProfilePic(@Part("login_id") RequestBody login_id,
                                      @Part("file_name") RequestBody file_name,
                                      @Part("type") RequestBody type,
                                      @Part MultipartBody.Part profilepath);


    @Multipart
    @POST("api.php?p=addstudent")
    Call<JsonObject> addstudent(@Part("first_name") RequestBody first_name,
                                @Part("last_name") RequestBody last_name,
                                @Part("classname") RequestBody class_name,
                                @Part("class_id") RequestBody class_id,
                                @Part("section") RequestBody section,
                                @Part("section_id") RequestBody section_id,
                                @Part("school_id") RequestBody school_id,
                                @Part("created_by_id") RequestBody created_by_id,
                                @Part("created_by") RequestBody created_by,
                                @Part("pname") RequestBody pname,
                                @Part("email") RequestBody email,
                                @Part("primary_mobile") RequestBody primary_mobile,
                                @Part("secondry_mobile") RequestBody secondry_mobile,
                                @Part("gender") RequestBody gender,
                                @Part MultipartBody.Part profilepath);


    @Multipart
    @POST("api.php?p=updateStudent")
    Call<JsonObject> updateStudent(@Part("first_name") RequestBody first_name,
                                   @Part("last_name") RequestBody last_name,
                                   @Part("classname") RequestBody class_name,
                                   @Part("class_id") RequestBody class_id,
                                   @Part("section") RequestBody section,
                                   @Part("section_id") RequestBody section_id,
                                   @Part("school_id") RequestBody school_id,
                                   @Part("created_by_id") RequestBody created_by_id,
                                   @Part("created_by") RequestBody created_by,
                                   @Part("pname") RequestBody pname,
                                   @Part("email") RequestBody email,
                                   @Part("primary_mobile") RequestBody primary_mobile,
                                   @Part("secondry_mobile") RequestBody secondry_mobile,
                                   @Part("gender") RequestBody gender,
                                   @Part("old_mobile") RequestBody old_mobile,
                                   @Part("login_id") RequestBody login_id,
                                   @Part("student_login") RequestBody student_login,
                                   @Part("img_name") RequestBody img_name,
                                   @Part("address") RequestBody address,
                                   @Part MultipartBody.Part profilepath);




    @Multipart
    @POST("api.php?p=addstudent")
    Call<JsonObject> addstudent2(@Part("first_name") RequestBody first_name,
                                 @Part("last_name") RequestBody last_name,
                                 @Part("classname") RequestBody class_name,
                                 @Part("class_id") RequestBody class_id,
                                 @Part("section") RequestBody section,
                                 @Part("section_id") RequestBody section_id,
                                 @Part("school_id") RequestBody school_id,
                                 @Part("created_by_id") RequestBody created_by_id,
                                 @Part("created_by") RequestBody created_by,
                                 @Part("pname") RequestBody pname,
                                 @Part("email") RequestBody email,
                                 @Part("primary_mobile") RequestBody primary_mobile,
                                 @Part("secondry_mobile") RequestBody secondry_mobile,
                                 @Part("gender") RequestBody gender);

    @Multipart
    @POST("api.php?p=updateStudent")
    Call<JsonObject> updateStudent2(@Part("first_name") RequestBody first_name,
                                    @Part("last_name") RequestBody last_name,
                                    @Part("classname") RequestBody class_name,
                                    @Part("class_id") RequestBody class_id,
                                    @Part("section") RequestBody section,
                                    @Part("section_id") RequestBody section_id,
                                    @Part("school_id") RequestBody school_id,
                                    @Part("created_by_id") RequestBody created_by_id,
                                    @Part("created_by") RequestBody created_by,
                                    @Part("pname") RequestBody pname,
                                    @Part("email") RequestBody email,
                                    @Part("primary_mobile") RequestBody primary_mobile,
                                    @Part("secondry_mobile") RequestBody secondry_mobile,
                                    @Part("gender") RequestBody gender,
                                    @Part("old_mobile") RequestBody old_mobile,
                                    @Part("login_id") RequestBody login_id,
                                    @Part("student_login") RequestBody student_login,
                                    @Part("address") RequestBody address
    );







    @Multipart
    @POST("api.php?p=addstudent_ifexist")
    Call<JsonObject> addstudent_ifexist(@Part("first_name") RequestBody first_name,
                                        @Part("last_name") RequestBody last_name,
                                        @Part("classname") RequestBody class_name,
                                        @Part("class_id") RequestBody class_id,
                                        @Part("section_id") RequestBody section_id,
                                        @Part("section") RequestBody section,
                                        @Part("school_id") RequestBody school_id,
                                        @Part("created_by_id") RequestBody created_by_id,
                                        @Part("created_by") RequestBody created_by,
                                        @Part("pname") RequestBody pname,
                                        @Part("email") RequestBody email,
                                        @Part("primary_mobile") RequestBody primary_mobile,
                                        @Part("secondry_mobile") RequestBody secondry_mobile,
                                        @Part("gender") RequestBody gender,
                                        @Part MultipartBody.Part profilepath);

    @Multipart
    @POST("api.php?p=updateStudent_ifexist")
    Call<JsonObject> updateStudent_ifexist(@Part("first_name") RequestBody first_name,
                                           @Part("last_name") RequestBody last_name,
                                           @Part("classname") RequestBody class_name,
                                           @Part("class_id") RequestBody class_id,
                                           @Part("section_id") RequestBody section_id,
                                           @Part("section") RequestBody section,
                                           @Part("school_id") RequestBody school_id,
                                           @Part("created_by_id") RequestBody created_by_id,
                                           @Part("created_by") RequestBody created_by,
                                           @Part("pname") RequestBody pname,
                                           @Part("email") RequestBody email,
                                           @Part("primary_mobile") RequestBody primary_mobile,
                                           @Part("secondry_mobile") RequestBody secondry_mobile,
                                           @Part("address") RequestBody address,
                                           @Part("gender") RequestBody gender,
                                           @Part("img_name") RequestBody imgname,
                                           @Part("login_id") RequestBody login_id,
                                           @Part("student_id") RequestBody student_id,
                                           @Part MultipartBody.Part profilepath);

    @Multipart
    @POST("api.php?p=updateStudent_ifexist")
    Call<JsonObject> updateStudent_ifexist2(@Part("first_name") RequestBody first_name,
                                            @Part("last_name") RequestBody last_name,
                                            @Part("classname") RequestBody class_name,
                                            @Part("class_id") RequestBody class_id,
                                            @Part("section_id") RequestBody section_id,
                                            @Part("section") RequestBody section,
                                            @Part("school_id") RequestBody school_id,
                                            @Part("created_by_id") RequestBody created_by_id,
                                            @Part("created_by") RequestBody created_by,
                                            @Part("pname") RequestBody pname,
                                            @Part("email") RequestBody email,
                                            @Part("primary_mobile") RequestBody primary_mobile,
                                            @Part("secondry_mobile") RequestBody secondry_mobile,
                                            @Part("address") RequestBody address,
                                            @Part("gender") RequestBody gender,
                                            @Part("img_name") RequestBody imgname,
                                            @Part("login_id") RequestBody login_id,
                                            @Part("student_id") RequestBody student_id);



    @Multipart
    @POST("api.php?p=addstudent_ifexist")
    Call<JsonObject> addstudent_ifexist2(@Part("first_name") RequestBody first_name,
                                         @Part("last_name") RequestBody last_name,
                                         @Part("classname") RequestBody class_name,
                                         @Part("class_id") RequestBody class_id,
                                         @Part("section_id") RequestBody section_id,
                                         @Part("section") RequestBody section,
                                         @Part("school_id") RequestBody school_id,
                                         @Part("created_by_id") RequestBody created_by_id,
                                         @Part("created_by") RequestBody created_by,
                                         @Part("pname") RequestBody pname,
                                         @Part("email") RequestBody email,
                                         @Part("primary_mobile") RequestBody primary_mobile,
                                         @Part("secondry_mobile") RequestBody secondry_mobile);





    @Multipart
    @POST("getDataAudio_Image.php")
    Call<ModelCategoryDetail> getDataAudio_Image(
            @Part("id") RequestBody id,
              @Part("user_id") RequestBody user_id
    );

    @Multipart
    @POST("delete_audio_image.php")
    Call<JsonObject> delete_audio_image(
            @Part("id") RequestBody id
    );

    @Multipart
    @POST("getCategory.php")
    Call<ModelCategory> getCategory(@Part("user_id") RequestBody key);

    @Multipart
    @POST("getCategory.php")
    Call<HomeModelCategory>homegetCategory(@Part("user_id") RequestBody key);

    @FormUrlEncoded
    @POST("getUserList.php")
    Call<SerchUserListModel>searchUser_Model(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("admin_dashboard.php")
    Call<DashBoardModel>admin_dashboard(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("getUserAdminRequest.php")
    Call<List<UserRequestModel>>getUserAdminRequest(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("getPurchasePlan.php")
    Call<ModelSubscription>getPurchasePlan(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("getSearch_User.php")
    Call<SerchUserListModel>search_Model(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("delete_user.php")
    Call<List<SerchUserListModel>>delete_user(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("get_User.php")
    Call<List<UserDetailsModel>>get_User(@FieldMap Map<String, String> params);


    @GET("getNewsDetails.php")
    Call<NewsDetailsModel>getNewsDetails();

    @FormUrlEncoded
    @POST("update_news.php")
    Call<UpdateNews>updatNews(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("add_news.php")
    Call<AddNews>addNews(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("renew_user.php")
    Call<JsonObject>renew_User(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("deleteNews.php")
    Call<DeleteNews>deleteNews(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("userUpdate.php")
    Call<String>userUpdate(@FieldMap Map<String, String> params);

    @POST("upload.php")
    Call<String>dictionaryUpdate(@FieldMap Map<String, String> params);

    @Multipart
    @POST("upload.php")
    Call<JsonObject> dictionaryUpdate(@Part("image") RequestBody image,
                                        @Part("admin_id") RequestBody adminid,
                                        @Part("type") RequestBody type,
                                        @Part("title") RequestBody title,
                                        @Part("type_id") RequestBody typeid
    );

    @Multipart
    @POST("search_word_admin.php")
    Call<SearchWord> searchWord(
                               @Part("title") RequestBody title
    );
    @Multipart
    @POST("getSearch_Word_admin.php")
    Call<GetSearchWord> getSearch_Word(
            @Part("title") RequestBody title
    );


    @FormUrlEncoded
    @POST("delete_word.php")
    Call<DeleteWord>deleteWord(@FieldMap Map<String, String> params);


    @Multipart
    @POST("change_pin.php")
    Call<JsonObject> changePin(@Part("old_pin") RequestBody oldpin,
                                      @Part("new_pin") RequestBody newpin,
                                      @Part("mobile") RequestBody mobile

    );

    @Multipart
    @POST("admin/login_admin.php")
    Call<JsonObject> loginAdmin(@Part("email") RequestBody email,
                                      @Part("password") RequestBody password,
                                      @Part("key") RequestBody key
    );

    @FormUrlEncoded
    @POST("sendNotification.php")
    Call<String>sendNotification(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("updateLoginRequest.php")
    Call<JsonObject>updateLoginRequest(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("search_user.php")
    Call<SerchUserListModel>search_user(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("UpdatePurchase.php")
    Call<String>UpdatePurchase(@FieldMap Map<String, String> params);

    @Multipart
    @POST("api.php?p=deleteRooms")
    Call<JsonObject>deleteRooms(@Part("room_id") RequestBody room_id);

    @Multipart
    @POST("api.php?p=deleteschool")
    Call<JsonObject> deleteschool(@Part("school_id") RequestBody school_id);

    @Multipart
    @POST("api.php?p=updateclass_section")
    Call<JsonObject> updateclass_section(@Part("school_id") RequestBody school_id,
                                         @Part("id") RequestBody id,
                                         @Part("update_value") RequestBody update_value,
                                         @Part("old_value") RequestBody old_value,
                                         @Part("type") RequestBody type);
    @Multipart
    @POST("api.php?p=deleteclass_section")
    Call<JsonObject> deleteclass_section(@Part("school_id") RequestBody school_id,
                                         @Part("id") RequestBody id,
                                         @Part("type") RequestBody type);

    @Multipart
    @POST("api.php?p=create_class_teacher")
    Call<JsonObject> create_class_teacher(@Part("school_id") RequestBody school_id,
                                          @Part("user_id") RequestBody user_id,
                                          @Part("teacher_id") RequestBody teacher_id,
                                          @Part("class_id") RequestBody class_id,
                                          @Part("section_id") RequestBody section_id);
    @Multipart
    @POST("api.php?p=updateClassTeacher")
    Call<JsonObject> updateClassTeacher
                                          (@Part("school_id") RequestBody school_id,
                                           @Part("user_id") RequestBody user_id,
                                           @Part("teacher_id") RequestBody teacher_id,
                                           @Part("class_id") RequestBody class_id,
                                           @Part("section_id") RequestBody section_i,
                                           @Part("add") RequestBody add,
                                           @Part("exist_id") RequestBody exist_id,
                                           @Part("pre_id") RequestBody pre_id);



    @Multipart
    @POST("api.php?p=updateSession")
    Call<JsonObject> updateSession(@Part("school_id") RequestBody school_id,
                                   @Part("session_id") RequestBody session_id,
                                   @Part("session_name") RequestBody session_name,
                                   @Part("from") RequestBody from,
                                   @Part("to") RequestBody to,
                                   @Part("type") RequestBody type);





    @Multipart
    @POST("api.php?p=deleteTeacher")
    Call<JsonObject> deleteTeacher(@Part("school_id") RequestBody school_id,
                                   @Part("user_id") RequestBody user_id);

    @Multipart
    @POST("api.php?p=deleteClassTeacher")
    Call<JsonObject> deleteClassTeacher(@Part("school_id") RequestBody school_id,
                                        @Part("id") RequestBody user_id);
    @Multipart
    @POST("api.php?p=changepassword")
    Call<JsonObject> changepassword(@Part("user_id") RequestBody school_id,
                                    @Part("old_pass") RequestBody old_pass,
                                    @Part("new_pass") RequestBody new_pass
    );





    @Multipart
    @POST("uploadimage_audio.php")
    Call<JsonObject> uploadimage_audio(
            @Part List<MultipartBody.Part> gallary,
            @Part MultipartBody.Part file,
            @Part("admin_id") RequestBody std_id,
            @Part("category_id") RequestBody school_id,
            @Part("title") RequestBody session_id
    );

    @Multipart
    @POST("add_adds.php")
    Call<JsonObject>add_adds(
            @Part MultipartBody.Part file,
            @Part("admin_id") RequestBody admin_id,
            @Part("message") RequestBody message,
            @Part("clickable") RequestBody clickable,
            @Part("url") RequestBody url,
            @Part("status") RequestBody status,
             @Part("expire_date") RequestBody expire_date
    );

    @Multipart
    @POST("update_adds.php")
    Call<JsonObject>update_adds(
            @Part MultipartBody.Part file,
            @Part("admin_id") RequestBody admin_id,
            @Part("message") RequestBody message,
            @Part("clickable") RequestBody clickable,
            @Part("url") RequestBody url,
            @Part("status") RequestBody status,
            @Part("expire_date") RequestBody expire_date,
             @Part("imagename") RequestBody imagename,
            @Part("id") RequestBody id
    );

    @Multipart
    @POST("add_category.php")
    Call<JsonObject> add_category(
            @Part MultipartBody.Part file,
            @Part("admin_id") RequestBody admin_id,
            @Part("title") RequestBody title,
            @Part("type") RequestBody type
    );


    @Multipart
    @POST("api.php?p=upload_homework")
    Call<JsonObject> upload_homework2(
            @Part MultipartBody.Part file,
            @Part("student_id") RequestBody std_id,
            @Part("school_id") RequestBody school_id,
            @Part("session_id") RequestBody session_id,
            @Part("assignment_id") RequestBody assignment_id,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id,
            @Part("teacher_id") RequestBody teacherid,
            @Part("description") RequestBody description
    );






    @Multipart
    @POST("api.php?p=uploadAssignment")
    Call<JsonObject> uploadAssignment(
            @Part MultipartBody.Part file,
            @Part("subject") RequestBody subject,
            @Part("topic") RequestBody topic,
            @Part("des") RequestBody des,
            @Part("date") RequestBody date,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id,
            @Part("section_name") RequestBody section_name,
            @Part("school_id") RequestBody school_id,
            @Part("teacher_id") RequestBody teacher_id
    );




    @Multipart
    @POST("api.php?p=upload_homework")
    Call<JsonObject> upload_homework3(
            @Part List<MultipartBody.Part> gallary,
            @Part("student_id") RequestBody std_id,
            @Part("school_id") RequestBody school_id,
            @Part("session_id") RequestBody session_id,
            @Part("assignment_id") RequestBody assignment_id,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id,
            @Part("teacher_id") RequestBody teacherid,
            @Part("description") RequestBody description
    );

    @Multipart
    @POST("api.php?p=upload_homework")
    Call<JsonObject> upload_homework4(

            @Part("student_id") RequestBody std_id,
            @Part("school_id") RequestBody school_id,
            @Part("session_id") RequestBody session_id,
            @Part("assignment_id") RequestBody assignment_id,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id,
            @Part("teacher_id") RequestBody teacherid,
            @Part("description") RequestBody description
    );





    @Multipart
    @POST("api.php?p=deActiveStudent")
    Call<JsonObject>deActiveStudent(@Part("school_id") RequestBody school_id,
                                    @Part("student_id") RequestBody teacher_id,
                                    @Part("status") RequestBody type);

    @Multipart
    @POST("api.php?p=deleteStudent")
    Call<JsonObject>deleteStudent(@Part("user_id") RequestBody userid,
                                  @Part("school_id") RequestBody school_id,
                                  @Part("student_id") RequestBody student_id);


    @Multipart
    @POST("api.php?p=createRooms")
    Call<JsonObject>createRooms(@Part("school_id") RequestBody school_id,
                                @Part("teacher_id") RequestBody teacher_id,
                                @Part("class_id") RequestBody type,
                                @Part("section_id") RequestBody section_id,
                                @Part("section_name") RequestBody section_name,
                                @Part("subject") RequestBody subject,
                                @Part("title") RequestBody title,
                                @Part("desc") RequestBody desc,
                                @Part("time") RequestBody time);


    @Multipart
    @POST("api.php?p=uploadAssignment")
    Call<JsonObject>uploadAssignment(
            @Part("subject") RequestBody subject,
            @Part("topic") RequestBody topic,
            @Part("des") RequestBody des,
            @Part("class_id") RequestBody class_id,
            @Part("section_id") RequestBody section_id,
            @Part("section_name") RequestBody section_name,
            @Part("school_id") RequestBody school_id,
            @Part("teacher_id") RequestBody teacher_id);




    @Multipart
    @POST("api.php?p=updateRooms")
    Call<JsonObject>updateRooms(
            @Part("room_id") RequestBody room_id,
            @Part("school_id") RequestBody school_id,
            @Part("teacher_id") RequestBody teacher_id,
            @Part("class_id") RequestBody type,
            @Part("section_id") RequestBody section_id,
            @Part("section_name") RequestBody section_name,
            @Part("subject") RequestBody subject,
            @Part("title") RequestBody title,
            @Part("desc") RequestBody desc,
            @Part("time") RequestBody time);




    @Multipart
    @POST("getadstoAdmin.php")
    Call<ModelAds>getadstoAdmin(
            @Part("key") RequestBody key
    );


    @Multipart
    @POST("update_ads.php")
    Call<ModelAds>update_ads(
            @Part("type") RequestBody type,
            @Part("id") RequestBody id,
            @Part("image") RequestBody image
    );

}
