package com.stenobano.admin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.stenobano.admin.R;
import com.stenobano.admin.activity.GetDataNew;
import com.stenobano.admin.config.BaseUrl;
import com.stenobano.admin.model.HomeModelCategory;
import com.stenobano.admin.model.ModelCategory;

import java.util.ArrayList;
import java.util.List;


public class CategoryAapter extends RecyclerView.Adapter<CategoryAapter.HolderItem> {
   private List<HomeModelCategory.Result> userModelArrayList;
    Context context;
    String pid;
    ImageView imageView,cancel;
    TextView textView;

   private ArrayList<ModelCategory.Result> arraylist;

    public CategoryAapter(List<HomeModelCategory.Result> mlistItem, Context context) {
        this.userModelArrayList = mlistItem;
        this.context = context;
    }

    @Override
    public HolderItem onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category, parent, false);
        HolderItem holder = new HolderItem(layout);

        return holder;

    }

    @Override
    public void onBindViewHolder(HolderItem holder, final int position) {
        final HomeModelCategory.Result model = userModelArrayList.get(position);

        try
        {
            Picasso.with(context)
                    .load(BaseUrl.CATEGORY_URL +model.getImage())
                    .placeholder(R.drawable.preview)
                    .into(holder.image);
        }
        catch (Exception e)
        {

        }

        holder.name.setText(model.getName());
        pid = model.getId();
       holder.card.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {

                    if (model.getType().equals("Audio/Image"))
                    {
                        Intent intent=new Intent(context, GetDataNew.class);
                        intent.putExtra("type_id",model.getId());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                    else if (model.getType().equals("Link"))
                    {
                        String url =model.getUrl();
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        context.startActivity(i);
                    }
                    else
                    {
                     /*   Intent i = new Intent(context, Type_Searching.class);
                        i.putExtra("type_id", "101");
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);*/
                    }

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

    public class HolderItem extends RecyclerView.ViewHolder {

        // ImageView thumbnal;
        private ImageView image;
        private TextView name;
     CardView card;


        public HolderItem(View convertView) {
            super(convertView);
            image = convertView.findViewById(R.id.image);
            name = convertView.findViewById(R.id.name);
            card=convertView.findViewById(R.id.card);

        }
    }
}