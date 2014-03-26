package com.example.image_search_rbruggman;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class ImageSearchSettings extends Activity {
	private EditText etSiteFilter;
	private Button saveBtn;
	private Spinner spinImageSize;
	private Spinner spinImageColor;
	private Spinner spinImageType;
	
	ImageSettings settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_search_settings);
		
		etSiteFilter = (EditText) findViewById(R.id.etSiteFilter);
		spinImageSize = (Spinner) findViewById(R.id.spinImageSize);
		spinImageColor = (Spinner) findViewById(R.id.spinImageColor);
		spinImageType = (Spinner) findViewById(R.id.spinImageTy);
		saveBtn = (Button) findViewById(R.id.saveBtn);
		
		settings = (ImageSettings)
				getIntent().getSerializableExtra(MainActivityImage.SETTINGS_EXTRA);
				getIntent().getStringExtra(MainActivityImage.SIZE_EXTRA);
				getIntent().getStringExtra(MainActivityImage.COLOR_EXTRA);
				getIntent().getStringExtra(MainActivityImage.TYPE_EXTRA);
				getIntent().getStringExtra(MainActivityImage.SITE_EXTRA);
		
		etSiteFilter.setText(settings.website); 

		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
		    public void onClick(View v) {
				// get text returns an editable instead of a string which is why we need to toString
				settings.size = spinImageSize.getSelectedItem().toString();
				settings.color = spinImageColor.getSelectedItem().toString();
				settings.type = spinImageType.getSelectedItem().toString();
				settings.website = etSiteFilter.getText().toString();
				//setup the result result of this intent
				// intent can also just represent a shell for data
				Intent dataSettings = new Intent();
				dataSettings.putExtra(MainActivityImage.SIZE_EXTRA, spinImageSize.getSelectedItem().toString());
				dataSettings.putExtra(MainActivityImage.COLOR_EXTRA, spinImageColor.getSelectedItem().toString());
				dataSettings.putExtra(MainActivityImage.TYPE_EXTRA, spinImageType.getSelectedItem().toString());
				dataSettings.putExtra(MainActivityImage.SITE_EXTRA, etSiteFilter.getText().toString());
				// returns back whatever was in the result at that time
				setResult(RESULT_OK, dataSettings);
		
				// Close this screen and go back to first screen
				finish();
			}
		});
	}
	
//	public void addListenerOnSpinnerItemSelection() {
//	     spinImageSize.setOnItemSelectedListener(new OnItemSelectedListener());
//	   }
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.image_search_settings, menu);
		return true;
	}

}
