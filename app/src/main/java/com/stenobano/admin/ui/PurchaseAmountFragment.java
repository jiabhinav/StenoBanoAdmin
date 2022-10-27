package com.stenobano.admin.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.stenobano.admin.R;
import com.stenobano.admin.adapter.User_List_Recycler;
import com.stenobano.admin.databinding.AddplanviewBinding;
import com.stenobano.admin.databinding.AlertPlanBinding;
import com.stenobano.admin.databinding.FragmentPurchaseAmountBinding;
import com.stenobano.admin.model.PurchasePlanModel;
import com.stenobano.admin.model.SearchModel;
import com.stenobano.admin.model.UserDetailsModel;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;
import com.stenobano.admin.session.SesssionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.ContentValues.TAG;
import static com.stenobano.admin.ui.home.HomeFragment.Pin;

public class PurchaseAmountFragment extends Fragment implements View.OnClickListener{
    FragmentPurchaseAmountBinding binding;
    private ListView listView;
    private LinearLayout ll;
    TextView  text_plan,text_valid,text_amount;
    private ProcessingDialog processingDialog;
    ArrayList<PurchasePlanModel> purchasePlanModel = new ArrayList<PurchasePlanModel>();
    private Context context= getActivity();
    SesssionManager sesssionManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_purchase_amount, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Update Purchase Amount");
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context= getActivity();
        processingDialog=new ProcessingDialog(context);
        GetPlan();

        binding.plan.setOnClickListener(this);
        binding.save.setOnClickListener(this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
        final AlertDialog dialog=loginDialog.create();
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

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.plan)
        {
            alertPlan();
        }
        else   if (v.getId()==R.id.save)
        {
            UpdatePlan();
        }
    }

    private  void alertPlan()
    {
        final androidx.appcompat.app.AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
        dialogBuilder.create();
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_plan, null);
        listView =dialogView.findViewById(R.id.listView);
        final TextView select=dialogView.findViewById(R.id.select);
        select.setText("Select Plan");
        final androidx.appcompat.app.AlertDialog dialog=dialogBuilder.create();
        dialog.setView(dialogView);
        String[] some_array = getResources().getStringArray(R.array.plan);
        final ArrayAdapter adapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,some_array);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               binding.plan.setText(String.valueOf(adapter.getItem(position)));
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private  void UpdatePlan() {
        if (binding.plan.getText().toString().equals("Select Plan")) {
            Toast.makeText(getContext(), "Choose plan first", Toast.LENGTH_SHORT).show();

        } else if (binding.valid.getText().toString().equals("")) {
            Toast.makeText(getContext(), "Enter days", Toast.LENGTH_SHORT).show();

        } else if (binding.amount.getText().toString().equals("")){
            Toast.makeText(getContext(), "Enter Amount", Toast.LENGTH_SHORT).show();
        }
        else {
                processingDialog.show("wait...");
                Map<String,String> map=new HashMap();
            map.put("plan",binding.plan.getText().toString());
            map.put("valid",binding.valid.getText().toString());
            map.put("amount",binding.amount.getText().toString());
                Call<String> call = APIClient.getInstance().UpdatePurchase(map);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                        processingDialog.dismiss();
                        Log.d("type122sdddd", "msg" + new Gson().toJson(response.body()));
                        Toast.makeText(getContext(), ""+response.body(), Toast.LENGTH_SHORT).show();
                        GetPlan();
                    }
                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        Log.d("erere",t.toString());
                        processingDialog.dismiss();

                    }
                });
            }

    }

    private void GetPlan() {
        processingDialog.show("wait...");
            // RequestBody.create(MultipartBody.FORM, "stenobano");
            //Map<String, RequestBody> partMap;
            Map<String,String> map=new HashMap();
            map.put("key","wodwhouwoifwnhfwoff");
            Call<List<PurchasePlanModel>> call = APIClient.getInstance().getPurchasePlan(map);
            call.enqueue(new Callback<List<PurchasePlanModel>>() {
                @Override
                public void onResponse(Call<List<PurchasePlanModel>> call, retrofit2.Response<List<PurchasePlanModel>> response) {
                    processingDialog.dismiss();
                    Log.d("plan", "msg" + new Gson().toJson(response.body()));
                    try {
                        purchasePlanModel.addAll(response.body()) ;
                        LayoutInflater layoutInfralte=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        binding.linear.removeAllViews();
                        Log.v(TAG, "LOGS" + purchasePlanModel.size());
                        for (int i = 0; i < purchasePlanModel.size(); i++) {
                            View view=layoutInfralte.inflate(R.layout.addplanview, null);
                            ll=view.findViewById(R.id.ll);
                            text_plan=view.findViewById(R.id.text_plan);
                            text_valid=view.findViewById(R.id.text_days);
                            text_amount=view.findViewById(R.id.text_amount);
                            text_plan.setText(purchasePlanModel.get(i).getPlan());
                            text_valid.setText(purchasePlanModel.get(i).getValid()+" "+"Days");
                            text_amount.setText("Rs "+purchasePlanModel.get(i).getAmount());
                            binding.linear.addView(view);
                        }

                    } catch (Exception e) {
                        Log.d("onResponse", "There is an error");
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(Call<List<PurchasePlanModel>> call, Throwable t) {
                    Log.d("erere",t.toString());
                    processingDialog.dismiss();

                }
            });
        }

    }
