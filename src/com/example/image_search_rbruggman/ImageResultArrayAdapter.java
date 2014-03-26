package com.example.image_search_rbruggman;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.loopj.android.image.SmartImageView;

public class ImageResultArrayAdapter extends ArrayAdapter<ImageResult> {

	public ImageResultArrayAdapter(Context context, List<ImageResult> images) {
		// text view is able to just display a string
		super(context, R.layout.item_image_result, images);
	}

	@Override
	// position of the item in array which allows you to get access to the underlying data
	// convertView manages memory and resuses subviews
	// parent gives you access to gridview itself
	// take a data source Java object and translating it into a view
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageResult imageInfo = this.getItem(position);
		SmartImageView ivImage;
		// check to see if convertView already exists
		if (convertView == null) {
			LayoutInflater inflator = LayoutInflater.from(getContext());
			ivImage = (SmartImageView) inflator.inflate(R.layout.item_image_result, parent, false);
		} else {
			// convert the image to a smart image view
			ivImage = (SmartImageView) convertView;
			//clear out an data that was already there
			ivImage.setImageResource(android.R.color.transparent);
		}
		ivImage.setImageUrl(imageInfo.getThumbUrl());
		return ivImage;
	}
}
