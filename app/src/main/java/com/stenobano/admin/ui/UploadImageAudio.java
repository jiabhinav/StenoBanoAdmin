package com.stenobano.admin.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stenobano.admin.R;
import com.stenobano.admin.adapter.UploadHomeWork_Adapter;
import com.stenobano.admin.databinding.FragmentUploadImageAudioBinding;
import com.stenobano.admin.model.ModelAssignment;
import com.stenobano.admin.model.ModelCategory;
import com.stenobano.admin.other_class.CameraActivity;
import com.stenobano.admin.other_class.FilePath;
import com.stenobano.admin.other_class.PermissionUtil;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;
import com.stenobano.admin.session.SesssionManager;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


import static android.app.Activity.RESULT_OK;
import static com.stenobano.admin.session.SesssionManager.ID;


public class UploadImageAudio extends Fragment implements View.OnClickListener {

    private static final int PICK_FILE_REQUEST = 103;
    private static final int PERMISSION_REQUEST_CODE = 108;
    private final int PERMISSIONS_REQUEST_READ_CONTACTS = 1234;
    FragmentUploadImageAudioBinding binding;
    private ProcessingDialog processingDialog;
    ArrayList<Uri> data_list;
    UploadHomeWork_Adapter adapter;
    int REQUEST_IMAGE_CODE=101;
    View view;
    Context context;
    String otherfile=null;
    Map<String,String> map;
    private Uri uri=null;
    String category_id="";
    List<ModelCategory.Result>modelList;
    private ModelAssignment.Assignment modelAssignment;
    String[] PERMISSIONS = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    public UploadImageAudio()
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
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_upload_image_audio, container, false);
        context=getContext();

        final GridLayoutManager layoutManager = new GridLayoutManager(context,3);
        binding.recylerview.setLayoutManager(layoutManager);
        binding.recylerview.setHasFixedSize(true);
        binding.add.setOnClickListener(this);
        binding.choosse.setOnClickListener(this);
        binding.save.setOnClickListener(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            binding.recylerview.setNestedScrollingEnabled(false);
        }else {
            binding.recylerview.setNestedScrollingEnabled(true);
        }

        data_list=new ArrayList<>();

        binding.editMessg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length()>0)
                {
                    binding.countText.setText(String.valueOf(s.toString().length())+"/1000");
                }
                else
                {
                    binding.countText.setText("0/1000");
                }
            }
        });

        return binding.getRoot();

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCategory();
    }

    @Override
    public void onClick(View v) {

      if (v.getId()==R.id.add)
        {
            if (checkPermission())
            {

                if (hasPermissions(getActivity(), PERMISSIONS)) {
                    Intent intent1 = new Intent(getActivity(), CameraActivity.class);
                    startActivityForResult(intent1, 123);
                } else {
                    ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, PERMISSIONS_REQUEST_READ_CONTACTS);
                }
            }
            else
            {
                requestPermission();
            }


        }
        else if (v.getId()==R.id.choosse)
        {
            if (checkPermission())
            {
                Intent intent = new Intent();
                intent.setType("*/*");
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

            if (data_list.size()==0  && uri==null || binding.editMessg.getText().toString().equals(""))
            {
                Toast.makeText(context, "All field should be not blank or empty!!", Toast.LENGTH_LONG).show();
            }
            else {
                    upload_homework();
            }

        }
    }


    public boolean hasPermissions(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_CONTACTS: {
                if (PermissionUtil.verifyPermissions(grantResults)) {
                    Intent intent1 = new Intent(getActivity(), CameraActivity.class);
                    startActivityForResult(intent1, 123);
                } else {
                    Toast.makeText(getActivity(), "Parcel Move Application requires this permission to launch...", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 2 && requestCode == 123) {
            Log.d("imageis","wwee="+data.getStringExtra("image"));
            if (data.hasExtra("image")) {
                if (!data.getStringExtra("image").equals("")) {
                   // new SesssionManager(getActivity()).setImagePath("");
                    String imagePath = data.getStringExtra("image");
                    Uri uri= Uri.fromFile(new File(imagePath));
                   // data_list.add(uri);
                    data_list.add(Uri.parse(imagePath));
                    binding.countText.setText(String.valueOf(data_list.size()));
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
                    binding.recylerview.setLayoutManager(linearLayoutManager);
                    adapter=new UploadHomeWork_Adapter(getActivity(),data_list);
                    binding.recylerview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                }
            }
        }
        else if (resultCode == 2 && requestCode == 124)
        {
            Log.d("imageis22","wwee="+data.getStringExtra("image"));
            if (data.hasExtra("image")) {

                if (!data.getStringExtra("image").equals("")) {
                   // new SesssionManager(getActivity()).setImagePath("");

                    String imagePath = data.getStringExtra("image");
                  //  data_list.add(data.getData());
                    Uri uri= Uri.fromFile(new File(imagePath));
                    data_list.add(uri);
                    binding.countText.setText(String.valueOf(data_list.size()));
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
                    binding.recylerview.setLayoutManager(linearLayoutManager);
                    adapter=new UploadHomeWork_Adapter(getActivity(),data_list);
                    binding.recylerview.setAdapter(adapter);
                    adapter.notifyDataSetChanged();


                }
            }
        }

       else if (requestCode == PICK_FILE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();
            Log.d("urifileis",uri.toString());
            binding.textattach.setText("Attach Other File-Selected");
            otherfile = FilePath.getPath(getActivity(), uri);

        }

    }



    private void getCategory()
    {

        modelList.clear();
        processingDialog.show("Saving");
        RequestBody id = RequestBody.create(MultipartBody.FORM, new SesssionManager(requireActivity()).getUserDetails().get(ID));
      //  RequestBody schoolid = RequestBody.create(MultipartBody.FORM, id);
        Call<ModelCategory> call = APIClient.getInstance().getCategory(id);
        call.enqueue(new Callback<ModelCategory>() {
            @Override
            public void onResponse(Call<ModelCategory> call, retrofit2.Response<ModelCategory> response) {
                processingDialog.dismiss();
                Log.d("type122sdddd", "msg" + new Gson().toJson(response.body()));

                modelList=response.body().getResult();
                final ArrayAdapter<ModelCategory.Result> adapter = new ArrayAdapter<ModelCategory.Result>(getActivity(), android.R.layout.simple_spinner_dropdown_item, modelList);
                binding.spinnerteacher.setAdapter(adapter);
                binding.spinnerteacher.setSelection(1);
                binding.spinnerteacher.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                        category_id = modelList.get(position).getId();
                        Log.d("saddddw", "category_id-" + category_id);
                        if (position==0)
                        {
                            Toast.makeText(getActivity(), "You can't add dictioary word from here!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            }

            @Override
            public void onFailure(Call<ModelCategory> call, Throwable t) {
                Log.d("erere",t.toString());
                processingDialog.dismiss();
            }
        });
    }




    private void upload_homework()
    {

        processingDialog.show("Uploading");
        List<MultipartBody.Part> list = new ArrayList<>();
        for (int i = 0; i < data_list.size(); i++)
        {
            list.add(prepareFilePart("image[" + i + "]", data_list.get(i)));
        }
        File file = new File(FilePath.getPath(getActivity(), uri));
        Log.d("filepathis", file.getName()+"-"+file+"-"+list.size());
        RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        RequestBody admin_id = RequestBody.create(MultipartBody.FORM,"1");
        RequestBody category_ids = RequestBody.create(MultipartBody.FORM,category_id);
        RequestBody title = RequestBody.create(MultipartBody.FORM,binding.editMessg.getText().toString());
        Call<JsonObject> call = APIClient.getInstance().uploadimage_audio(list,fileToUpload,admin_id,category_ids,title);
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



    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        File file = new File(FilePath.getPath(getActivity(), fileUri));
        RequestBody requestBody = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
           // requestBody = RequestBody.create(MediaType.parse(Objects.requireNonNull(getActivity().getContentResolver().getType(fileUri))), file);
               requestBody=RequestBody.create(MediaType.parse("multipart/form-data"), file);
        }
        else
            {

        }

        return MultipartBody.Part.createFormData(partName, file.getName(), requestBody);

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



}
