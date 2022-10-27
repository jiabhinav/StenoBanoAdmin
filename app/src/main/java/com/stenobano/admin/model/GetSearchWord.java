
package com.stenobano.admin.model;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import java.io.Serializable;
import java.util.List;

import com.bumptech.glide.Glide;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.stenobano.admin.R;

public class GetSearchWord implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    private final static long serialVersionUID = -8459733215904642760L;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }
    public static class Result implements Serializable
    {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("title")
        @Expose
        private String title;
        @SerializedName("image")
        @Expose
        private String image;
        private final static long serialVersionUID = -136270772837046917L;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @BindingAdapter("image")
        public static void loadImage(ImageView view, String imageUrl) {
            Glide.with(view.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.image_placeholder)
                    .into(view);
        }

    }
}
