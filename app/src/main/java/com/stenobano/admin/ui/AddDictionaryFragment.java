package com.stenobano.admin.ui;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.stenobano.admin.R;
import com.stenobano.admin.config.BaseUrl;
import com.stenobano.admin.databinding.FragmentAddDictionaryBinding;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;
import static com.stenobano.admin.config.BaseUrl.BASE_URL;


public class AddDictionaryFragment extends Fragment implements View.OnClickListener {
    FragmentAddDictionaryBinding binding;
    private Context context= getActivity();
    private static ProcessingDialog processingDialog;
    ProgressDialog progressDialog;
    private Bitmap bitmap;
    Bitmap bitmap4;
    private Bitmap bitmap2;
    private int PICK_IMAGE_REQUEST = 1;
    private int PICK_IMAGE_REQUEST2 = 2;
    String adminid;
    String type_id = "101";
    Uri uri;
    String image_name,word_id;
    final int CROP_PIC = 3;
    Uri imageUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_add_dictionary, container, false);
        context= getActivity();
        askPermission(context, getActivity());
        processingDialog=new ProcessingDialog(context);
        try {
            Bundle bundle = getArguments();
            word_id=bundle.getString("id");
            //image_name=bundle.getString("image_name");
            binding.title.setText(bundle.getString("title"));

            try
            {
                Picasso.with(context)
                        .load(BASE_URL+bundle.getString("image"))
                        .placeholder(R.drawable.preview)
                        .into(binding.imageView23);
            }
            catch (Exception e)
            {

            }
//            Glide.with(context).load(bundle.getString("image"))
//                    .into(binding.imageView23);
        } catch (Exception e) {
            e.printStackTrace();
        }


        binding.choose.setOnClickListener(this);
        binding.camera.setOnClickListener(this);
        binding.save.setOnClickListener(this);

        // EnableRuntimePermission();
//        SharedPreferences sp = getActivity().getSharedPreferences("steno_info", MODE_PRIVATE);
//        adminid = sp.getString("id", null);
        return binding.getRoot();

    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.choose) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "pic"), PICK_IMAGE_REQUEST);
        }
        if (id == R.id.camera) {
 /*
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, PICK_IMAGE_REQUEST2);
*/
            //=====================

            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, "New Picture");
            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
            imageUri = getActivity().getApplicationContext().getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, PICK_IMAGE_REQUEST2);
            Log.d("imageuriisi", "onClick: "+imageUri);


            //================================

        } else if (id == R.id.save) {
            save();

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {


            try {
                Uri filePath = data.getData();
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath);
                String name = filePath.getPath();
                //choose_image.setText("Selected");
                binding.imageView23.setImageBitmap(bitmap);
                //choose_image.setBackgroundColor(Color.rgb(0,150,0));
                binding.select.setText("Seleted");
                binding.select.setTextColor(Color.rgb(0, 150, 0));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == PICK_IMAGE_REQUEST2) {


            Bitmap thumbnail = null;
            try {
                thumbnail = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), imageUri);
                //imageView.setImageBitmap(thumbnail);
                //uri = getRealPathFromURI(imageUri);
                uri=getImageUri(getContext(),thumbnail);
                performCrop();
            } catch (Exception e) {
                e.printStackTrace();
            }



            //  ==================
        }
        else if (requestCode == CROP_PIC) {

           // try {
                if(data.getData()==null)
                {
                bitmap = (Bitmap)data.getExtras().get("data");
                binding.imageView23.setImageBitmap(bitmap);

                }else{
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
                        binding.imageView23.setImageBitmap(bitmap);

                    } catch (IOException e) {

                        e.printStackTrace();

                    }
                }
            }
         /*   catch (Exception e)
            {

            }*/

        }

 /*   public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 80, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }*/

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }



    public void EnableRuntimePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {

            Toast.makeText(getContext(), "CAMERA permission allows us to Access CAMERA app", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(getActivity(), new String[]{
                    Manifest.permission.CAMERA}, 1);

        }
    }


    public String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 30, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;

    }
    private void save() {
        if (!isOnline())
        {
            AlertDialog.Builder builder =new AlertDialog.Builder(getContext());
            builder.setTitle("No internet Connection");
            builder.setMessage("Please turn on internet connection to continue");
            builder.setNegativeButton("close", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();

                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.setCancelable(false);
            alertDialog.show();
        }
        else {
            //  Toast.makeText(this, ""+getStringImage(bitmap), Toast.LENGTH_SHORT).show();
            if (binding.title.getText().toString().equals("")) {
                Toast.makeText(getContext(), "Enter Title", Toast.LENGTH_SHORT).show();

            }  else {
                processingDialog.show("wait...");

                String image = getStringImage(bitmap);
//                Map<String, String> map = new HashMap();
//                map.put("admin_id", "2");
//                map.put("type", binding.type.getText().toString());
//                map.put("title", binding.title.getText().toString());
//                map.put("image", image);
                //map.put("type_id", type_id);
//                File file = new File(getStringImage(bitmap));
//                // RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
//                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
//                MultipartBody.Part body = MultipartBody.Part.createFormData("image",file.getName(),reqFile);
                RequestBody images = RequestBody.create(MultipartBody.FORM, image);
                RequestBody admin_id = RequestBody.create(MultipartBody.FORM, "1");
                RequestBody type = RequestBody.create(MultipartBody.FORM, binding.type.getText().toString());
                RequestBody title = RequestBody.create(MultipartBody.FORM, binding.title.getText().toString());
                RequestBody typeid = RequestBody.create(MultipartBody.FORM, type_id);

                Call<JsonObject> call = APIClient.getInstance().dictionaryUpdate(images,admin_id,type,title,typeid);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                        processingDialog.dismiss();
                        Log.d("upload", "msg" + new Gson().toJson(response.body()));
                        try {
                            JSONObject jsonObject=new JSONObject(new Gson().toJson(response.body()));
                            if(jsonObject.getInt("status")==200){
                                Toast.makeText(getContext(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                binding.title.setText("");
                                binding.type.setText("");
                                binding.imageView23.setImageDrawable(null);
                            }else{
                                Toast.makeText(getContext(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("TAG", "onFailure: "+t.getMessage());
                        processingDialog.dismiss();

                    }
                });
            }

        }
    }

    private void performCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not
            // support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(uri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", .8);
            cropIntent.putExtra("aspectY", .6);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 400);
            cropIntent.putExtra("outputY", 250);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, CROP_PIC);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast.makeText(getContext(), "This device doesn't support the crop action!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    protected boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public void askPermission(Context context, Activity activity) {
        if     (
                (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        || (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                        || (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED))
        {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,  Manifest.permission.CAMERA}, 400);
        }
        else {

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 400) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED) {


            } else {


            }
        }
    }

}