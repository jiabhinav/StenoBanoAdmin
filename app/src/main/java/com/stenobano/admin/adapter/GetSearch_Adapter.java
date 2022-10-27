package com.stenobano.admin.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.Picasso;
import com.stenobano.admin.R;
import com.stenobano.admin.config.BaseUrl;
import com.stenobano.admin.databinding.ListBinding;
import com.stenobano.admin.fragment.GetSearch_ResultFragment;
import com.stenobano.admin.model.DeleteWord;
import com.stenobano.admin.model.GetSearchWord;
import com.stenobano.admin.model.UpdateNews;
import com.stenobano.admin.other_class.ZoomLinearLayout;
import com.stenobano.admin.retrofit.APIClient;
import com.stenobano.admin.ui.AddDictionaryFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static com.stenobano.admin.config.BaseUrl.BASE_URL;


public class GetSearch_Adapter extends RecyclerView.Adapter<GetSearch_Adapter.HolderItem> {
     private List<GetSearchWord.Result> getSearchWordList;
    ProgressDialog progressDialog;
    Context context;
    String pid;
    ImageView imageView;
    TextView textView;
    String word_id;

    public GetSearch_Adapter(List<GetSearchWord.Result> mlistItem, Context context) {
        this.getSearchWordList = mlistItem;
        this.context = context;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ListBinding itemBinding = ListBinding.inflate(layoutInflater, parent, false);
        return new HolderItem(itemBinding);

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        GetSearchWord.Result model = getSearchWordList.get(position);
        pid = model.getId();
//        Glide.with(context).load(getSearchWordList.get(position).getImage())
//                .into(holder.binding.image);
        try
        {
            Picasso.with(context)
                    .load(BASE_URL+getSearchWordList.get(position).getImage())
                    .placeholder(R.drawable.preview)
                    .into(holder.binding.image);
        }
        catch (Exception e)
        {

        }
        holder.binding.title.setText(model.getTitle());
        holder.bind(model);

        holder.binding.update.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                for (int i = 0; i < getSearchWordList.size(); i++) {
                    if (getSearchWordList.get(i).getId().equals(pid)) {

                        AppCompatActivity activity = (AppCompatActivity) v.getContext();
                        AddDictionaryFragment myFragment = new AddDictionaryFragment();
                        Bundle bundle = new Bundle();
                        bundle.putString("id", getSearchWordList.get(position).getId());
                        bundle.putString("title", getSearchWordList.get(position).getTitle());
                        bundle.putString("image", getSearchWordList.get(position).getImage());
                        bundle.putString("update", "update");
                        myFragment.setArguments(bundle);
                        activity.getSupportFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment, myFragment)
                                .addToBackStack(null).commit();
                    }
                }
            }
        });
        holder.binding.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < getSearchWordList.size(); i++) {
                    if (getSearchWordList.get(i).getId().equals(pid)) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setMessage("Are you want sure delete this "+" '"+getSearchWordList.get(position).getTitle()+"' "+"word??");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                word_id = getSearchWordList.get(position).getId();
                                progressDialog = ProgressDialog.show(context, "Please wait...", "saving...", true);
                                progressDialog.setCancelable(true);
                                Map<String,String> map=new HashMap();
                                map.put("word_id", word_id);
                                Call<DeleteWord> call = APIClient.getInstance().deleteWord(map);
                                call.enqueue(new Callback<DeleteWord>() {
                                    @Override
                                    public void onResponse(Call<DeleteWord> call, retrofit2.Response<DeleteWord> response) {
                                        //processingDialog.dismiss();
                                        DeleteWord model=response.body();
                                        if(model.getStatus()==200){
                                            Log.d("updatenews", response.body()+"");
                                            Toast.makeText(context, model.getMessage(), Toast.LENGTH_SHORT).show();
                                            notifyDataSetChanged();
                                            progressDialog.dismiss();
                                            getSearchWordList.remove(position);
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<DeleteWord> call, Throwable t) {
                                        Log.d("erere",t.toString());
                                        progressDialog.dismiss();
                                    }
                                });
                            }

                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                builder.setCancelable(true);
                            }
                        });
                        builder.create();
                        builder.show();
                    }
                }
            }
        });

        holder.binding.rl.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {
                for (int i = 0; i < getSearchWordList.size(); i++) {
                    if (getSearchWordList.get(i).getId().equals(pid)) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                        builder.setCancelable(false);
                        final View view = inflater.inflate(R.layout.alert_view_details, null);
                        builder.setView(view);
                        textView=view.findViewById(R.id.title);
                        imageView=view.findViewById(R.id.alertimage);
                        textView.setText(getSearchWordList.get(position).getTitle());
                        Glide.with(context).load(getSearchWordList.get(position).getImage())
                                .into(imageView);

                        final AlertDialog alertDialog = builder.create();
                        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        final ZoomLinearLayout zoomLinearLayout2 = view.findViewById(R.id.zoom);
                        zoomLinearLayout2.setOnTouchListener(new View.OnTouchListener()
                        {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                zoomLinearLayout2.init(context);
                                return false;
                            }
                        });
                        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               alertDialog.dismiss();
                            }
                        });
                        builder.show();
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return getSearchWordList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class HolderItem extends RecyclerView.ViewHolder {
        private ListBinding binding;
        public void bind(GetSearchWord.Result getSearchWordModel) {
            binding.setWord(getSearchWordModel);
            binding.executePendingBindings();

        }
        public HolderItem(ListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}