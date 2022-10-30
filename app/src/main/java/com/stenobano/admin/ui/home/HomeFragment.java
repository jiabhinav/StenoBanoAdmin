package com.stenobano.admin.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.stenobano.admin.MainActivity;
import com.stenobano.admin.R;
import com.stenobano.admin.adapter.DashboradAdapter;
import com.stenobano.admin.adapter.User_List_Recycler;
import com.stenobano.admin.databinding.FragmentHomeBinding;
import com.stenobano.admin.model.SearchModel;
import com.stenobano.admin.model.dashboard.DashBoardModel;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;
import com.stenobano.admin.session.SesssionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.validation.Validator;

import retrofit2.Call;
import retrofit2.Callback;

import static com.stenobano.admin.session.SesssionManager.MOBILE;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private  ProcessingDialog processingDialog;
    DashboradAdapter dashboradAdapter;
    private Context context = getActivity();
    List<DashBoardModel> modellist;
    public static String Pin = "", mobile;
    SesssionManager sesssionManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sesssionManager = new SesssionManager(getActivity());
        try {
            if (sesssionManager.userID().equals("")) {

            } else {
                displayDialogWindow();
            }
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        processingDialog = new ProcessingDialog(requireActivity());
        // mobile=new SesssionManager(getActivity()).userMobile();
        modellist = new ArrayList<>();
        getDashBoardList();
    }

    private void getDashBoardList() {
        processingDialog.show("wait...");
        //RequestBody.create(MultipartBody.FORM, "stenobano");
        //Map<String, RequestBody> partMap;
        Map<String, String> map = new HashMap();
        map.put("key", "stenobano");
        map.put("mobile", sesssionManager.userMobile());
        Call<DashBoardModel> call = APIClient.getInstance().admin_dashboard(map);
        call.enqueue(new Callback<DashBoardModel>() {
            @Override
            public void onResponse(Call<DashBoardModel> call, retrofit2.Response<DashBoardModel> response) {
                processingDialog.dismiss();
                Log.d("type122sdddd", "msg" + new Gson().toJson(response.body()));
                DashBoardModel model = response.body();
                Pin = model.getTodaystudent().get(0).getPin();
                binding.setStu(model);

            }

            @Override
            public void onFailure(Call<DashBoardModel> call, Throwable t) {
                Log.d("erere", t.toString());
                processingDialog.dismiss();

            }
        });
    }

    public void displayDialogWindow() {
        final AlertDialog.Builder loginDialog = new AlertDialog.Builder(getActivity());
        loginDialog.setCancelable(false);
        // loginDialog.setCanceledOnTouchOutside(false);
//        final AlertDialog.Builder loginDialog = new
//                AlertDialog.Builder(new ContextThemeWrapper(context, android.R.style.Theme_DeviceDefault_Light_Dialog));
        LayoutInflater factory = LayoutInflater.from(getActivity());
        final View f = factory.inflate(R.layout.custompindialog, null);
        loginDialog.setView(f);
        TextView Back = (TextView) f.findViewById(R.id.button_Back);
        final AlertDialog dialog = loginDialog.create();
        final EditText pin = (EditText) f.findViewById(R.id.pin);

        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
        pin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String st = s.toString();
                if (Pin.equalsIgnoreCase(st)) {
                    dialog.dismiss();
                }

            }
        });
        loginDialog.setCancelable(false);
        dialog.show();

    }


}