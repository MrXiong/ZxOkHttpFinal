package com.demo.zx.zxokhttpfinal.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zx on 2016/2/1.
 */
public class Photo implements Parcelable {
    private int id;
    private String name;
    private int age;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.age);
    }

    public Photo() {
    }

    protected Photo(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.age = in.readInt();
    }

    public static final Parcelable.Creator<Photo> CREATOR = new Parcelable.Creator<Photo>() {
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };
}
