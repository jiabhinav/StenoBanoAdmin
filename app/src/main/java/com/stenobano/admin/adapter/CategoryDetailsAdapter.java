package com.stenobano.admin.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.stenobano.admin.Interface.AlertDialogInterFace;
import com.stenobano.admin.Interface.Download;
import com.stenobano.admin.R;
import com.stenobano.admin.activity.GetDataNew;
import com.stenobano.admin.config.BaseUrl;
import com.stenobano.admin.model.ModelCategoryDetail;
import com.stenobano.admin.other_class.GetData;
import com.stenobano.admin.other_class.ProcessingDialog;
import com.stenobano.admin.retrofit.APIClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class CategoryDetailsAdapter extends RecyclerView.Adapter<CategoryDetailsAdapter.HolderItem> implements Filterable {

    private List<ModelCategoryDetail.Result> userModelArrayList;
    Context context;
    String pid;
    public  static String type="";
    AlertDialogInterFace alertDialogInterFace;
    private ProcessingDialog processingDialog;

    private static final String TABLE_CONTACTS = "steno_file";

    private ArrayList<ModelCategoryDetail.Result> arraylist;
    private int pStatus = 0;
    Download download;
    public CategoryDetailsAdapter(List<ModelCategoryDetail.Result> mlistItem, Context context) {
        processingDialog=new ProcessingDialog(context);
        this.userModelArrayList = mlistItem;
        this.context = context;
        this.alertDialogInterFace=alertDialogInterFace;
        this.download=download;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.get_data_list, parent, false);
        HolderItem holder = new HolderItem(layout);
        return holder;

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        final ModelCategoryDetail.Result model = userModelArrayList.get(position);

        try
        {
            Picasso.with(context)
                    .load(BaseUrl.IMAGE_URL+model.getImage().get(0).getName())
                    .placeholder(R.drawable.preview)
                    .resize(60, 60)
                    .centerCrop()
                    .into(holder.imageView);
        }
        catch (IndexOutOfBoundsException e)
        {

        }
        holder.title.setText(model.getTitle());
        holder.date.setText(model.getCreatedAt());
        pid = model.getId();
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog(position,model.getId());

            }

        });
        holder.download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetData.modelDetail = model;

            }
        });
        }



    @Override
    public int getItemCount() {
        return userModelArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    arraylist = (ArrayList<ModelCategoryDetail.Result>) userModelArrayList;
                } else {

                    ArrayList<ModelCategoryDetail.Result> filteredList = new ArrayList<>();

                    for (ModelCategoryDetail.Result androidVersion : userModelArrayList) {

                        if (androidVersion.getTitle().toLowerCase().contains(charString) || androidVersion.getTitle().toLowerCase().contains(charString) || androidVersion.getTitle().toLowerCase().contains(charString)) {

                            filteredList.add(androidVersion);
                        }
                    }

                    arraylist = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = arraylist;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                arraylist = (ArrayList<ModelCategoryDetail.Result>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public void updateList(List<ModelCategoryDetail.Result> list){
        userModelArrayList = list;
        notifyDataSetChanged();
    }




    public class HolderItem extends RecyclerView.ViewHolder {

        // ImageView thumbnal;
        private ImageView imageView,download,delete;
        private TextView title, description, code, price,date,audio,text_download,status;
        RelativeLayout relativeLayout,rl_download;
        ProgressBar progressBar2;


        public HolderItem(View convertView) {
            super(convertView);
            imageView = convertView.findViewById(R.id.image);
            title = convertView.findViewById(R.id.title);
            date = convertView.findViewById(R.id.datetext);
            audio = convertView.findViewById(R.id.audio);
            relativeLayout=convertView.findViewById(R.id.rl2);
            delete=convertView.findViewById(R.id.delete);
            download=convertView.findViewById(R.id.download);
            progressBar2=convertView.findViewById(R.id.progressBar2);
            text_download=convertView.findViewById(R.id.text_download);
            rl_download=convertView.findViewById(R.id.rl_download);
            status=convertView.findViewById(R.id.status);

        }

    }

    private void alertDialog(final int poss, final String id)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Confirm!!");
        alertDialogBuilder.setMessage("Do you want to delete?");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Delete Now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                delete( poss,id);
            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    private void delete(final int poss, final String id)
    {
        processingDialog.show("wait...");
        Log.d("fileidis","id="+id);
        RequestBody key = RequestBody.create(MultipartBody.FORM, id);
        Call<JsonObject> call = APIClient.getInstance().delete_audio_image(key);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {

                Log.d("resspooss","dwd"+new Gson().toJson(response.body()));
                try {
                    JSONObject jsonObject=new JSONObject(new Gson().toJson(response.body()));
                    if (jsonObject.getInt("status")==200)
                    {
                        Toast.makeText(context, ""+jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                        processingDialog.dismiss();
                        userModelArrayList.remove(poss);
                        notifyDataSetChanged();
                    }
                    else
                    {
                        processingDialog.dismiss();
                        Toast.makeText(context, ""+jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("ereredf",t.toString());
                processingDialog.dismiss();
            }
        });
    }

}



