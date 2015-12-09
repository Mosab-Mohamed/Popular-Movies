package com.example.android.popularmoviesApp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mosab Mohamed Khames on 06/12/2015.
 */
public class Movie implements Parcelable {

    private String original_title;
    private String movie_poster;
    private String overview ;
    private String user_rating;
    private String release_date;

    public Movie(String original_title,String movie_poster,
            String overview ,String user_rating,String release_date){

        this.movie_poster = movie_poster ;
        this.original_title = original_title ;
        this.overview = overview ;
        this.release_date = release_date ;
        this.user_rating = user_rating ;
    }

    String getOriginalTitle(){
        return  original_title ;
    }
    String getMoviePoster(){
        return movie_poster;
    }

    String getOverview(){
        return overview;
    }

    String getUserRating(){
        return user_rating ;
    }

    String getReleaseDate(){
        return release_date;
    }

    void setOriginalTitle(String s){
        original_title = s ;
    }

    void setMoviePoster(String s){
        movie_poster = s;
    }

    void setOverview(String s){
         overview = s;
    }

    void setUserRating(String s){
        user_rating =s ;
    }

    void setReleaseDate(String s){
        release_date = s;
    }


    @Override
    public int describeContents() {
// ignore for now
        return 0;
    }

    @Override
    public void writeToParcel(Parcel pc, int flags) {
        pc.writeString(original_title);
        pc.writeString(movie_poster);
        pc.writeString(overview);
        pc.writeString(user_rating);
        pc.writeString(release_date);
    }

    /** Static field used to regenerate object, individually or as arrays */
    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        public Movie createFromParcel(Parcel pc) {
            return new Movie(pc);
        }
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    /**Ctor from Parcel, reads back fields IN THE ORDER they were written */
    public Movie(Parcel pc){
        original_title         = pc.readString();
        movie_poster        =  pc.readString();
        overview      = pc.readString();
        user_rating = pc.readString();
        release_date = pc.readString();
    }


    /*public Movie(Parcel in){
        String data[]  = new String[5];

        in.readStringArray(data);
        this.original_title =data[0];
        this.movie_poster = data[1] ;
        this.overview = data[2];
        this.user_rating = data[3];
        this.release_date = data[5];
    }

    @Override
    public int describeContents(){
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.original_title,
                this.movie_poster,
                this.overview,
                this.user_rating,
                this.release_date});
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };*/
}
