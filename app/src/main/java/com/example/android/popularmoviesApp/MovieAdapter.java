package com.example.android.popularmoviesApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Mosab Mohamed Khames on 04/12/2015.
 */
public class MovieAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> movies ;
    private final String BASE_POSTER = "http://image.tmdb.org/t/p/w185" ;
    private LayoutInflater inflater;

    public MovieAdapter(Context c , ArrayList<String> movies ) {
        mContext = c;
        this.movies = movies ;
        inflater = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {

        return movies.size();
    }

    public Object getItem(int position) {

        return movies.get(position) ;
    }

    public long getItemId(int position) {

        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            convertView = inflater.inflate(R.layout.movie_items, null);
            imageView = (ImageView) convertView ;
        }
        else{
            imageView = (ImageView) convertView ;
        }

        //put picasso
        Picasso.with(mContext)
                .load(BASE_POSTER+movies.get(position))
                .into( imageView );
        return imageView;
    }

    public void add(String poster){
        movies.add(poster);
        this.notifyDataSetChanged();
    }

    public void clear (){
        movies.clear();
        this.notifyDataSetChanged();
    }
}
