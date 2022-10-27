
package com.stenobano.admin.model.dashboard;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DashBoardModel implements Serializable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("mesg")
    @Expose
    private String mesg;
    @SerializedName("todaystudent")
    @Expose
    private List<Todaystudent> todaystudent = null;
    @SerializedName("totalstudent")
    @Expose
    private List<Totalstudent> totalstudent = null;
    private final static long serialVersionUID = -8073318973725748518L;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMesg() {
        return mesg;
    }

    public void setMesg(String mesg) {
        this.mesg = mesg;
    }

    public List<Todaystudent> getTodaystudent() {
        return todaystudent;
    }

    public void setTodaystudent(List<Todaystudent> todaystudent) {
        this.todaystudent = todaystudent;
    }

    public List<Totalstudent> getTotalstudent() {
        return totalstudent;
    }

    public void setTotalstudent(List<Totalstudent> totalstudent) {
        this.totalstudent = totalstudent;
    }
    public class Todaystudent
     {

         @SerializedName("pin")
         @Expose
         private String pin;

         @SerializedName("todoy_register")
        @Expose
        private String todoyRegister;
        @SerializedName("free_trial")
        @Expose
        private String freeTrial;
        @SerializedName("paid_student")
        @Expose
        private String paidStudent;
        @SerializedName("free_expired")
        @Expose
        private String freeExpired;
        @SerializedName("paid_expired")
        @Expose
        private String paidExpired;
        @SerializedName("tomorrow_free_expired")
        @Expose
        private String tomorrowFreeExpired;
        @SerializedName("tomorrow_paid_expired")
        @Expose
        private String tomorrowPaidExpired;
        private final static long serialVersionUID = -260590243121300190L;
         public String getPin() {
             return pin;
         }

         public void setPin(String pin) {
             this.pin = pin;
         }
        public String getTodoyRegister() {
            return todoyRegister;
        }

        public void setTodoyRegister(String todoyRegister) {
            this.todoyRegister = todoyRegister;
        }

        public String getFreeTrial() {
            return freeTrial;
        }

        public void setFreeTrial(String freeTrial) {
            this.freeTrial = freeTrial;
        }

        public String getPaidStudent() {
            return paidStudent;
        }

        public void setPaidStudent(String paidStudent) {
            this.paidStudent = paidStudent;
        }

        public String getFreeExpired() {
            return freeExpired;
        }

        public void setFreeExpired(String freeExpired) {
            this.freeExpired = freeExpired;
        }

        public String getPaidExpired() {
            return paidExpired;
        }

        public void setPaidExpired(String paidExpired) {
            this.paidExpired = paidExpired;
        }

        public String getTomorrowFreeExpired() {
            return tomorrowFreeExpired;
        }

        public void setTomorrowFreeExpired(String tomorrowFreeExpired) {
            this.tomorrowFreeExpired = tomorrowFreeExpired;
        }

        public String getTomorrowPaidExpired() {
            return tomorrowPaidExpired;
        }

        public void setTomorrowPaidExpired(String tomorrowPaidExpired) {
            this.tomorrowPaidExpired = tomorrowPaidExpired;
        }

    }
    public class Totalstudent
    {

        @SerializedName("todoy_register")
        @Expose
        private String todoyRegister;
        @SerializedName("free_trial")
        @Expose
        private String freeTrial;
        @SerializedName("paid_student")
        @Expose
        private String paidStudent;
        @SerializedName("free_expired")
        @Expose
        private String freeExpired;
        @SerializedName("paid_expired")
        @Expose
        private String paidExpired;
        private final static long serialVersionUID = 7607450866308170710L;

        public String getTodoyRegister() {
            return todoyRegister;
        }

        public void setTodoyRegister(String todoyRegister) {
            this.todoyRegister = todoyRegister;
        }

        public String getFreeTrial() {
            return freeTrial;
        }

        public void setFreeTrial(String freeTrial) {
            this.freeTrial = freeTrial;
        }

        public String getPaidStudent() {
            return paidStudent;
        }

        public void setPaidStudent(String paidStudent) {
            this.paidStudent = paidStudent;
        }

        public String getFreeExpired() {
            return freeExpired;
        }

        public void setFreeExpired(String freeExpired) {
            this.freeExpired = freeExpired;
        }

        public String getPaidExpired() {
            return paidExpired;
        }

        public void setPaidExpired(String paidExpired) {
            this.paidExpired = paidExpired;
        }

    }
}
