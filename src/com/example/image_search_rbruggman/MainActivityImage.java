package com.example.image_search_rbruggman;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MainActivityImage extends Activity {
	EditText etQuery;
	GridView gvResults;
	Button btnSearch;
	int myLastVisiblePosition;
	ArrayList<ImageResult> imageResults = new ArrayList<ImageResult>();
	ImageResultArrayAdapter imageAdapter;
	public static final String SIZE_EXTRA = "foo";
	public static final String COLOR_EXTRA = "goo";
	public static final String TYPE_EXTRA = "moo";
	public static final String SITE_EXTRA = "loo";
	public static final String SETTINGS_EXTRA = "settings";
	public final int SETTINGS_REQUEST = 123;
	
	ImageSettings settings;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_activity_image);
		
		settings = new ImageSettings();
		settings.size = " ";
		settings.color = " ";
		settings.type = " ";
		settings.website = " ";
		
		setupViews();
		// setting the images results to the adapter to take them to the view
		imageAdapter = new ImageResultArrayAdapter(this, imageResults);
		gvResults.setAdapter(imageAdapter);
		gvResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapter, View parent, int position,
					long rowId) {
				// Intent is just a request to bring up a new activity
				// Declare that which you want to do and then fire the app!
				Intent i = new Intent(getApplicationContext(), ImageDisplayActivity.class);
				ImageResult imageResult = imageResults.get(position);
				// bundle is a parameter passed along with the request that you access on both sides
				// can get data from the underlying activity that opened it
				i.putExtra("result", imageResult);
				startActivity(i);
			}
			
		});
		
		gvResults.setOnScrollListener(new EndlessScrollListener() {
		    @Override
		    public void onLoadMore(int page, int totalItemsCount) {
	                // Triggered only when new data needs to be appended to the list
	                // Add whatever code is needed to append new items to your AdapterView
		        customLoadMoreDataFromApi(totalItemsCount); 
		    }
        });
	}
	
	public void customLoadMoreDataFromApi(int offset) {
		String query = etQuery.getText().toString();
  		AsyncHttpClient client = new AsyncHttpClient();
			client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" + "&start=" + offset + "&imgsz=" + settings.size + "&imgcolor=" + settings.color + "&imgtype=" + settings.type + "&as_sitesearch=" + settings.website + "&v=1.0&q=" + Uri.encode(query), 
				new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
					Log.d("DEBUG", imageResults.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_activity_image, menu);
		return true;
	}
	
	
	
	public void setupViews() {
		etQuery = (EditText) findViewById(R.id.etQuery);
		gvResults = (GridView) findViewById(R.id.gvResults);
		btnSearch = (Button) findViewById(R.id.btnSearch);
		myLastVisiblePosition = gvResults.getFirstVisiblePosition();
	}
	
	public void onSettingsPress(MenuItem item) {
		Toast.makeText(this, "Go Warrior!", Toast.LENGTH_SHORT).show();
		
		Intent i = new Intent(this, ImageSearchSettings.class);
		i.putExtra(SIZE_EXTRA, "small");
		i.putExtra(COLOR_EXTRA, "blue");
		i.putExtra(TYPE_EXTRA, "faces");
		i.putExtra(SITE_EXTRA, " ");
		i.putExtra(SETTINGS_EXTRA, settings);
		
		startActivityForResult(i, SETTINGS_REQUEST);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent dataSettings) {
		// differentiate between the requests that are coming back
		if (requestCode == SETTINGS_REQUEST) {
			if (resultCode == RESULT_OK) {
				settings.size = dataSettings.getExtras().getString(SIZE_EXTRA);
				settings.color = dataSettings.getExtras().getString(COLOR_EXTRA);
				settings.type = dataSettings.getExtras().getString(TYPE_EXTRA);
				settings.website = dataSettings.getExtras().getString(SITE_EXTRA);
			}
		}
	}

	public void onImageSearch(View v) {
		String query = etQuery.getText().toString();
		// clear to start our results fresh
		imageAdapter.clear();
		AsyncHttpClient client = new AsyncHttpClient();
		//rsz=8 to only return 8 items at a time
		// encode the query in case there are any illegal chars for http
		// response object will be all the result from hitting the api with our query
		client.get("https://ajax.googleapis.com/ajax/services/search/images?rsz=8&" + "imgsz=" + settings.size + "&imgcolor=" + settings.color + "&imgtype=" + settings.type + "&as_sitesearch=" + settings.website + "&start=" + 0 + "&v=1.0&q=" + Uri.encode(query), 
				new JsonHttpResponseHandler() {
			@Override
			public void onSuccess(JSONObject response) {
				JSONArray imageJsonResults = null;
				try {
					imageJsonResults = response.getJSONObject("responseData").getJSONArray("results");
					
					// common pattern to have the model parse the array for us
					// will load data into array and grid while notifying the adapter
					imageAdapter.addAll(ImageResult.fromJSONArray(imageJsonResults));
					Log.d("DEBUG", imageResults.toString());
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
