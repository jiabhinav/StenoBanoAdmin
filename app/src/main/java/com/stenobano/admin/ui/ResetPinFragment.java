package com.stenobano.admin.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.stenobano.admin.MainActivity;
import com.stenobano.admin.R;
import com.stenobano.admin.databinding.FragmentAddDictionaryBinding;
import com.stenobano.admin.databinding.FragmentResetPinBinding;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;
import com.stenobano.admin.session.SesssionManager;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

import static com.stenobano.admin.ui.home.HomeFragment.Pin;

public class ResetPinFragment extends Fragment {
    FragmentResetPinBinding binding;
    private Context context = getActivity();
    private static ProcessingDialog processingDialog;
    SesssionManager sesssionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reset_pin, container, false);
        context = getActivity();
        processingDialog = new ProcessingDialog(context);
        sesssionManager = new SesssionManager(getActivity());
        binding.btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Pin.equals(binding.oldPin.getText().toString())) {

                  if(binding.newPin.length()>=4){
                    if ((binding.newPin.getText().toString()).equals(binding.cnfrmpin.getText().toString())  ) {
                        pinChange();
                    } else {
                        Toast.makeText(getContext(), "Mismatch Password", Toast.LENGTH_SHORT).show();
                    }
                  }else{
                      Toast.makeText(getContext(), "At least 4 Digit ", Toast.LENGTH_SHORT).show();
                  }
                } else {
                    Toast.makeText(getContext(), "Wrong old Password ", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }

    public void pinChange() {
        processingDialog.show("wait...");
        RequestBody oldPin = RequestBody.create(MultipartBody.FORM, binding.oldPin.getText().toString());
        RequestBody newPin = RequestBody.create(MultipartBody.FORM, binding.newPin.getText().toString());
        RequestBody mobile = RequestBody.create(MultipartBody.FORM, sesssionManager.userMobile());

        Call<JsonObject> call = APIClient.getInstance().changePin(oldPin, newPin, mobile);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                processingDialog.dismiss();
                Log.d("upload", "msg" + new Gson().toJson(response.body()));
                try {
                    JSONObject jsonObject = new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.getInt("status") == 200) {
                        Toast.makeText(getContext(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        binding.oldPin.setText("");
                        binding.newPin.setText("");
                        binding.cnfrmpin.setText("");
                        binding.password.setText("");

                    } else {
                        Toast.makeText(getContext(), "" + jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Toast.makeText(getContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: " + t.getMessage());
                processingDialog.dismiss();

            }
        });
    }

}