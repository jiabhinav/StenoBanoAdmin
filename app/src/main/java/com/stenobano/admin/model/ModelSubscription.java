
package com.stenobano.admin.model;

import java.util.List;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ModelSubscription implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;
    public final static Creator<ModelSubscription> CREATOR = new Creator<ModelSubscription>() {


        @SuppressWarnings({
            "unchecked"
        })
        public ModelSubscription createFromParcel(android.os.Parcel in) {
            return new ModelSubscription(in);
        }

        public ModelSubscription[] newArray(int size) {
            return (new ModelSubscription[size]);
        }

    }
    ;

    protected ModelSubscription(android.os.Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.message = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.result, (Result.class.getClassLoader()));
    }

    public ModelSubscription() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(message);
        dest.writeList(result);
    }

    public int describeContents() {
        return  0;
    }


    public static class Result implements Parcelable
    {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("valid")
        @Expose
        private String valid;
        @SerializedName("plan")
        @Expose
        private String plan;
        @SerializedName("des")
        @Expose
        private String des;
        @SerializedName("dis_price")
        @Expose
        private String disPrice;
        @SerializedName("offer_valid_mesg")
        @Expose
        private String offerValidMesg;
        @SerializedName("status")
        @Expose
        private String status;
        public final static Creator<Result> CREATOR = new Creator<Result>() {


            @SuppressWarnings({
                    "unchecked"
            })
            public Result createFromParcel(android.os.Parcel in) {
                return new Result(in);
            }

            public Result[] newArray(int size) {
                return (new Result[size]);
            }

        }
                ;

        protected Result(android.os.Parcel in) {
            this.id = ((String) in.readValue((String.class.getClassLoader())));
            this.amount = ((String) in.readValue((String.class.getClassLoader())));
            this.valid = ((String) in.readValue((String.class.getClassLoader())));
            this.plan = ((String) in.readValue((String.class.getClassLoader())));
            this.des = ((String) in.readValue((String.class.getClassLoader())));
            this.disPrice = ((String) in.readValue((String.class.getClassLoader())));
            this.offerValidMesg = ((String) in.readValue((String.class.getClassLoader())));
            this.status = ((String) in.readValue((String.class.getClassLoader())));
        }

        public Result() {
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getValid() {
            return valid;
        }

        public void setValid(String valid) {
            this.valid = valid;
        }

        public String getPlan() {
            return plan;
        }

        public void setPlan(String plan) {
            this.plan = plan;
        }

        public String getDes() {
            return des;
        }

        public void setDes(String des) {
            this.des = des;
        }

        public String getDisPrice() {
            return disPrice;
        }

        public void setDisPrice(String disPrice) {
            this.disPrice = disPrice;
        }

        public String getOfferValidMesg() {
            return offerValidMesg;
        }

        public void setOfferValidMesg(String offerValidMesg) {
            this.offerValidMesg = offerValidMesg;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public void writeToParcel(android.os.Parcel dest, int flags) {
            dest.writeValue(id);
            dest.writeValue(amount);
            dest.writeValue(valid);
            dest.writeValue(plan);
            dest.writeValue(des);
            dest.writeValue(disPrice);
            dest.writeValue(offerValidMesg);
            dest.writeValue(status);
        }

        public int describeContents() {
            return  0;
        }

    }



}
