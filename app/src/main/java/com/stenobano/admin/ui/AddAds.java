package com.stenobano.admin.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.stenobano.admin.R;
import com.stenobano.admin.config.Constant;
import com.stenobano.admin.databinding.FragmentAddAdsBinding;
import com.stenobano.admin.databinding.FragmentNewsBinding;
import com.stenobano.admin.model.AddNews;
import com.stenobano.admin.model.DeleteNews;
import com.stenobano.admin.model.UpdateNews;
import com.stenobano.admin.model.news.NewsDetailsModel;
import com.stenobano.admin.other_class.DatePickerFragment;
import com.stenobano.admin.other_class.FilePath;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;


public class AddAds extends Fragment implements View.OnClickListener {

    FragmentAddAdsBinding binding;
    private ProcessingDialog processingDialog;
    private Context context = getActivity();
    private static final int PICK_FILE_REQUEST = 103;
    private static final int PERMISSION_REQUEST_CODE = 108;
    ArrayList<NewsDetailsModel.News> newsList, data;
    public static ArrayList<NewsDetailsModel.News> mData = new ArrayList<>();
    Type listType;
    String id = "",click="0",status="1";
    private Uri uri=null;
    String otherfile=null;
    public static final int REQUEST_CODE = 11;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_ads, container, false);
        binding.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();
                // Check which checkbox was clicked
                if (checked) {
                    binding.layoutUrl.setVisibility(View.VISIBLE);
                    click="1";
                } else {
                    binding.layoutUrl.setVisibility(View.GONE);
                    click="0";
                }
            }
        });

        return binding.getRoot();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        processingDialog = new ProcessingDialog(context);
        binding.choosse.setOnClickListener(this);
        newsList = new ArrayList<>();
        data = new ArrayList<>();
        binding.selectDate.setOnClickListener(this);
        binding.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i==R.id.active)
                {
                    status="1";
                }
                else
                {
                    status="0";
                }
            }
        });

        binding.addbutton.setOnClickListener(this);
    }


    private void checkValidation() {

        if (click.equals("1")) {
            if (binding.editurl.getText().toString().equals("")) {
                Toast.makeText(context, "Enter Website URL", Toast.LENGTH_SHORT).show();
            }

            else if (!isValidFormat("yyyy-MM-dd",binding.selectDate.getText().toString()))
            {
                Toast.makeText(getActivity(), "Incorrect date format", Toast.LENGTH_SHORT).show();
            }
            else if (uri == null)
            {
                Toast.makeText(context, "Select Banner Images Fist", Toast.LENGTH_SHORT).show();
            }
            else
            {
                upload();
            }

        }

        else if (!isValidFormat("yyyy-MM-dd",binding.selectDate.getText().toString()))
        {
            Toast.makeText(getActivity(), "Incorrect date format", Toast.LENGTH_SHORT).show();
        }
        else if (uri == null)
        {
            Toast.makeText(context, "Select Banner Images Fist", Toast.LENGTH_SHORT).show();
        }
        else
        {
            upload();
        }
    }



    private void upload()
    {
        processingDialog.show("Uploading");

        File file = new File(FilePath.getPath(getActivity(), uri));
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        RequestBody admin_id = RequestBody.create(MultipartBody.FORM,"1");
        RequestBody message = RequestBody.create(MultipartBody.FORM,binding.message.getText().toString());
        RequestBody clickable = RequestBody.create(MultipartBody.FORM,click);
        RequestBody url = RequestBody.create(MultipartBody.FORM,binding.editurl.getText().toString());
        RequestBody stat = RequestBody.create(MultipartBody.FORM,status);
        RequestBody expire_date = RequestBody.create(MultipartBody.FORM,binding.selectDate.getText().toString());
        Call<JsonObject> call = APIClient.getInstance().add_adds(fileToUpload,admin_id,message,clickable,url,stat,expire_date);
        call.enqueue(new Callback<JsonObject>()
        {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                processingDialog.dismiss();

                try {
                    JSONObject jsonObject=new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.getInt("status")==200)
                    {
                        Toast.makeText(getActivity(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    }
                    else
                    {
                        Toast.makeText(getActivity(), ""+jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("responsisassignment",new Gson().toJson(response.body()));

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("erere",t.toString());
                processingDialog.dismiss();
            }
        });
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.addbutton)
        {
            checkValidation();
        }



        else if (v.getId() == R.id.choosse) {
            if (checkPermission()) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "pic"), PICK_FILE_REQUEST);
            } else {
                requestPermission();
            }
        }
    }

    public static boolean isValidFormat(String format, String value) {
        Date date = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            date = sdf.parse(value);
            if (!value.equals(sdf.format(date))) {
                date = null;
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return date != null;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            Log.d("urifileis",uri.toString());
            binding.textattach.setText("Attach banner-Selected");
            otherfile = FilePath.getPath(getActivity(), uri);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
                binding.choosse.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
       else if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // get date from string
            String date2 = data.getStringExtra("selectedDate");
            // set the value of the editText
            binding.selectDate.setText(date2);
        }

    }



    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("valuedssfdd", "Permission Granted, Now you can use local drive .");
                } else {
                    Log.d("valuesdss", "Permission Denied, You cannot use local drive .");
                }
                break;
        }
    }
}