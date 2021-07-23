package com.restapi.datauploadtofirebasejava.Model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

public class Upload implements Parcelable {
    private String mName;
    private String mImageUrl;
    private String mPhoneNo;
    private String covidResult;



    private String mDate;


    private String mKey;

    public Upload() {
        //empty constructor needed
    }

    public Upload(String mName, String mImageUrl, String mPhoneNo, String covidResult, String mDate) {
        if (mName.trim().equals("")) {
            mName = "No Name";
        }
        this.mName = mName;
        this.mImageUrl = mImageUrl;
        this.mPhoneNo = mPhoneNo;
        this.covidResult = covidResult;
        this.mDate = mDate;
    }



    public static final Creator<Upload> CREATOR = new Creator<Upload>() {
        @Override
        public Upload createFromParcel(Parcel in) {
            return new Upload(in);
        }

        @Override
        public Upload[] newArray(int size) {
            return new Upload[size];
        }
    };

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public String getPhoneNo() {
        return mPhoneNo;
    }

    public void setPhoneNo(String mPhoneNo) {
        this.mPhoneNo = mPhoneNo;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getCovidResult() {
        return covidResult;
    }

    public void setCovidResult(String covidResult) {
        this.covidResult = covidResult;
    }

    @Exclude
    public String getKey() {
        return mKey;
    }

    @Exclude
    public void setKey(String Key) {
        this.mKey = Key;
    }





    @Override
    public int describeContents() {
        return 0;
    }

    protected Upload(Parcel in) {
        mName = in.readString();
        mImageUrl = in.readString();
        mPhoneNo = in.readString();
        covidResult = in.readString();
        mDate = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mName);
        dest.writeString(mImageUrl);
        dest.writeString(mPhoneNo);
        dest.writeString(covidResult);
        dest.writeString(mDate);


    }

    /*public Upload(String name, String imageUrl) {
        if (name.trim().equals("")) {
            name = "No Name";
        }
        mName = name;
        mImageUrl = imageUrl;
    }
    public String getName() {
        return mName;
    }
    public void setName(String name) {
        mName = name;
    }
    public String getImageUrl() {
        return mImageUrl;
    }
    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }*/
}
