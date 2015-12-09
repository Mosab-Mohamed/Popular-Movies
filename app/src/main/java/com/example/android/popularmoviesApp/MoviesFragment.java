package com.example.android.popularmoviesApp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mosab Mohamed Khames on 04/12/2015.
 */
public class MoviesFragment extends Fragment {

    private final String LOG_TAG = FetchMoviesData.class.getSimpleName();
    private MovieAdapter ad ;
    private View rootView ;
    private ArrayList<Movie> movies ;
    private final String BASE_POSTER = "http://image.tmdb.org/t/p/w185" ;

    public MoviesFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();
    }

    private void updateView(){
        FetchMoviesData fetchMoviesData = new FetchMoviesData();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.sort_key),
                getString(R.string.sort_by_popularity));
        fetchMoviesData.execute(location);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridview = (GridView) rootView.findViewById(R.id.movies_gridview);
        ad = new MovieAdapter(getActivity() , new ArrayList <String>() );
        gridview.setAdapter(ad);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Movie movie = movies.get(position);
                Intent intent = new Intent( getActivity(), Detail.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });


        return rootView;
    }

    class FetchMoviesData extends AsyncTask<String,Void,ArrayList<Movie>> {

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            if(params.length==0){
                return null;
            }
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            String sort = "popularity" ;
            String app_id = "your_api_key";

            try {
                final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?" ;
                final String SORT_PARAM = "sort_by" ;
                final String APP_ID = "api_key";

                Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0]+".desc")
                        .appendQueryParameter(APP_ID, app_id)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.e(LOG_TAG, "the url ==> " + url.toString());
                // Create the request to OpenWeatherMap, and open the connection

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.connect();
                Log.e(LOG_TAG,"connected");


                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();

                Log.e(LOG_TAG,"the json string ==> "+moviesJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try{
                movies = getMovieDataFromJson(moviesJsonStr);
                return movies ;
            }
            catch (JSONException e){
                Log.e(LOG_TAG,e.getMessage(),e);
                e.printStackTrace();
            }

            //this will only happened if there will an error during parsing or getting the movies information
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            if(movies != null ){
                Log.e("not emptyMovies", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1");
                ad.clear();
                for(int i=0 ; i<movies.size() ; i++){
                    Log.e("ELLINK AHO", BASE_POSTER+movies.get(i).getMoviePoster());
                    ad.add(movies.get(i).getMoviePoster());
                }

            }
            else
                Log.e("emptyMovies", "????????????????????????");

        }

        private ArrayList<Movie> getMovieDataFromJson(String moviesJsonStr)
            throws JSONException{

            // These are the names of the JSON objects that need to be extracted.
            final String RESULTS = "results";
            final String ORIGINAL_TITLE = "original_title";
            final String POSTER = "backdrop_path";
            final String PLOT_SYNOPSIS = "overview";
            final String USER_RATING = "vote_average";
            final String RELEASE_DATE = "release_date";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray resultsArray = moviesJson.getJSONArray(RESULTS);

            ArrayList<Movie> movies = new ArrayList<Movie>();

            // Data is fetched in Celsius by default.
            // If user prefers to see in Fahrenheit, convert the values here.
            // We do this rather than fetching in Fahrenheit so that the user can
            // change this option without us having to re-fetch the data once
            // we start storing the values in a database.
            /*SharedPreferences sharedPrefs =
                    PreferenceManager.getDefaultSharedPreferences(getActivity());
            String unitType = sharedPrefs.getString(
                    getString(R.string.pref_units_key),
                    getString(R.string.pref_units_metric));*/

            Log.e("getMovieDataFromJson" , Integer.toString(resultsArray.length()));
            for(int i = 0; i < resultsArray.length(); i++) {
                String original_title;
                String movie_poster;
                String overview ;
                String user_rating;
                String release_date;

                // Get the JSON object representing the Movie
                JSONObject movieDetails = resultsArray.getJSONObject(i);

                original_title = movieDetails.getString(ORIGINAL_TITLE) ;
                movie_poster = movieDetails.getString(POSTER);
                overview = movieDetails.getString(PLOT_SYNOPSIS);
                user_rating = movieDetails.getString(USER_RATING);
                release_date = movieDetails.getString(RELEASE_DATE);

                movies.add(i,  new Movie(original_title, movie_poster,
                         overview , user_rating, release_date) );

                Log.e("getMovieDataFromJson", movies.get(i).getMoviePoster());
            }

            return movies ;

         }
    }
}
