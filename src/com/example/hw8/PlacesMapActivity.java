package com.example.hw8;

import java.util.List;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class PlacesMapActivity extends MapActivity {
	
	PlacesList nearPlaces;

	
	MapView mapView;

	
	List<Overlay> mapOverlays;

	AddItemizedOverlay itemizedOverlay;

	GeoPoint geoPoint;
	
	MapController mc;
	
	double latitude;
	double longitude;
	OverlayItem overlayitem;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map_places);

		//  intent 
		Intent i = getIntent();
		
		//geo location
		String user_latitude = i.getStringExtra("user_latitude");
		String user_longitude = i.getStringExtra("user_longitude");
		
	
		nearPlaces = (PlacesList) i.getSerializableExtra("near_places");

		mapView = (MapView) findViewById(R.id.mapView);
		mapView.setBuiltInZoomControls(true);

		mapOverlays = mapView.getOverlays();
		
		// Geopoint 
		geoPoint = new GeoPoint((int) (Double.parseDouble(user_latitude) * 1E6),
				(int) (Double.parseDouble(user_longitude) * 1E6));
		
		Drawable drawable_user = this.getResources()
				.getDrawable(R.drawable.mark_red);
		
		itemizedOverlay = new AddItemizedOverlay(drawable_user, this);
		
		// Map
		overlayitem = new OverlayItem(geoPoint, "Your Location");

		itemizedOverlay.addOverlay(overlayitem);
		
		mapOverlays.add(itemizedOverlay);
		itemizedOverlay.populateNow();
		
		Drawable drawable = this.getResources()
				.getDrawable(R.drawable.mark_blue);
		
		itemizedOverlay = new AddItemizedOverlay(drawable, this);

		mc = mapView.getController();

		int minLat = Integer.MAX_VALUE;
		int minLong = Integer.MAX_VALUE;
		int maxLat = Integer.MIN_VALUE;
		int maxLong = Integer.MIN_VALUE;


		if (nearPlaces.results != null) {
			
			for (Place place : nearPlaces.results) {
				latitude = place.geometry.location.lat; // latitude
				longitude = place.geometry.location.lng; // long
				
			
				geoPoint = new GeoPoint((int) (latitude * 1E6),
						(int) (longitude * 1E6));
				
				// Map 
				overlayitem = new OverlayItem(geoPoint, place.name,
						place.vicinity);

				itemizedOverlay.addOverlay(overlayitem);
				
				minLat  = (int) Math.min( geoPoint.getLatitudeE6(), minLat );
			    minLong = (int) Math.min( geoPoint.getLongitudeE6(), minLong);
			    maxLat  = (int) Math.max( geoPoint.getLatitudeE6(), maxLat );
			    maxLong = (int) Math.max( geoPoint.getLongitudeE6(), maxLong );
			}

		mapView.getController().zoomToSpan(Math.abs( minLat - maxLat ), Math.abs( minLong - maxLong ));
		
		mc.animateTo(new GeoPoint((maxLat + minLat)/2, (maxLong + minLong)/2 ));
		mapView.postInvalidate();

	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

}
