package com.stenobano.admin.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stenobano.admin.R;
import com.stenobano.admin.adapter.UploadHomeWork_Adapter;
import com.stenobano.admin.databinding.FragmentAddCategoryBinding;
import com.stenobano.admin.databinding.FragmentUploadImageAudioBinding;
import com.stenobano.admin.model.ModelAssignment;
import com.stenobano.admin.model.ModelCategory;
import com.stenobano.admin.other_class.FilePath;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;


public class AddCategory extends Fragment implements View.OnClickListener {
    private static final int PICK_FILE_REQUEST = 103;
    private static final int PERMISSION_REQUEST_CODE = 108;
    FragmentAddCategoryBinding binding;
    private ProcessingDialog processingDialog;
    ArrayList<Uri> data_list;
    UploadHomeWork_Adapter adapter;
    int REQUEST_IMAGE_CODE=101;
    View view;
    Context context;

    Map<String,String> map;
    private Uri uri=null;
    String otherfile=null;
    String category_id="";
    List<ModelCategory.Result> modelList;
    private String type="Audio/Image";
    private ModelAssignment.Assignment modelAssignment;
    public AddCategory()
    {
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        processingDialog=new ProcessingDialog(getActivity());
        modelList=new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_add_category, container, false);
        context=getContext();

        binding.choosse.setOnClickListener(this);
        binding.save.setOnClickListener(this);

        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i==R.id.audio)
                {
                    type="Audio/Image";
                    Log.d("dsdsd",type);
                }
                else if (i==R.id.dictionary)
                {
                    type="Dictionary";
                    Log.d("dsdsd",type);
                }
                else if (i==R.id.link)
                {
                    type="Link";
                    Log.d("dsdsd",type);
                }
            }
        });

    }

    @Override
    public void onClick(View v) {

       if (v.getId()==R.id.choosse)
        {
            if (checkPermission())
            {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "pic"), PICK_FILE_REQUEST);
            }
            else
            {
                requestPermission();
            }

        }
        else if (v.getId()==R.id.save)
        {
            if (binding.editMessg.getText().toString().equals(""))
            {
                Toast.makeText(context, "All field should be not blank or empty!!", Toast.LENGTH_LONG).show();
            }
            else if(uri==null)
            {
                Toast.makeText(context, "Select category icon", Toast.LENGTH_SHORT).show();
            }
            else
                {
                upload_homework();
            }

        }
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            Log.d("urifileis",uri.toString());
            binding.textattach.setText("Attach category icon-Selected");
            otherfile = FilePath.getPath(getActivity(), uri);
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),uri);
                binding.choosse.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }




    private void upload_homework()
    {
        processingDialog.show("Uploading");

        File file = new File(FilePath.getPath(getActivity(), uri));
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody admin_id = RequestBody.create(MultipartBody.FORM,"1");
        RequestBody title = RequestBody.create(MultipartBody.FORM,binding.editMessg.getText().toString());
        RequestBody types = RequestBody.create(MultipartBody.FORM,type);
        Call<JsonObject> call = APIClient.getInstance().add_category(fileToUpload,admin_id,title,types);
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




    private void requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getActivity(), "Write External Storage permission allows us to save files. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE,android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
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
