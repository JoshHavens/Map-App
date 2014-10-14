package com.example.mapapp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.Fragment;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends Activity {
	GoogleMap map;
	Switch hybrid;
	Switch Street;
	StreetViewPanorama StreetView;
	LatLng Location;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		hybrid = (Switch) findViewById(R.id.switch1);
		Street = (Switch) findViewById(R.id.switch2);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true);
		Location currLoc = map.getMyLocation();
		if(currLoc != null){
		Location = new LatLng(currLoc.getLatitude(), currLoc.getLongitude());
		}
		map.setBuildingsEnabled(true);
		map.setTrafficEnabled(true);
		StreetView = ((StreetViewPanoramaFragment) getFragmentManager()
				.findFragmentById(R.id.streetView)).getStreetViewPanorama();

		Fragment streetFrag = getFragmentManager().findFragmentById(
				R.id.streetView);
		getFragmentManager().beginTransaction().hide(streetFrag).commit();

		// set the switch to OFF
		hybrid.setChecked(false);

		hybrid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				map = ((MapFragment) getFragmentManager().findFragmentById(
						R.id.map)).getMap();
				if (isChecked) {
					map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
				} else {
					map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
				}
			}
		});

		Street.setChecked(false);

		Street.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (StreetView != null) {
					if (isChecked) {
						Fragment mapFrag = getFragmentManager()
								.findFragmentById(R.id.map);
						getFragmentManager().beginTransaction().hide(mapFrag)
								.commit();
						Fragment streetFrag = getFragmentManager()
								.findFragmentById(R.id.streetView);
						getFragmentManager().beginTransaction()
								.show(streetFrag).commit();
						StreetView.setPosition(Location, 1000);
					} else {
						Fragment streetFrag = getFragmentManager()
								.findFragmentById(R.id.streetView);
						getFragmentManager().beginTransaction()
								.hide(streetFrag).commit();
						Fragment mapFrag = getFragmentManager()
								.findFragmentById(R.id.map);
						getFragmentManager().beginTransaction().show(mapFrag)
								.commit();
					}
				}
			}
		});

		map.setOnMyLocationButtonClickListener(new OnMyLocationButtonClickListener() {
			@Override
			public boolean onMyLocationButtonClick() {
				Location currLoc = map.getMyLocation();
				Location = new LatLng(currLoc.getLatitude(), currLoc.getLongitude());
				return false;
			}
		});

		map.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public void onMapClick(LatLng latLng) {
				if (StreetView != null) {
					/** Passed the tapped location through to the Street View **/
					map.clear();
					MarkerOptions markOp = new MarkerOptions();
					markOp.position(latLng);
					markOp.title("Custom Marker");
					map.addMarker(markOp).showInfoWindow();
					Location = latLng;
				}
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onClick(View view) throws IOException {
		EditText text = (EditText) findViewById(R.id.info);
		String address = text.getText().toString();
		if (!address.isEmpty()) { // Prevent a null call to GeoCoder
			map.clear();
			Geocoder earth = new Geocoder(MainActivity.this, Locale.ENGLISH);
			List<Address> addresses = earth.getFromLocationName(address, 1); // ONLY
																				// GETS
																				// 1
																				// ADDRESS!!!!
			map = ((MapFragment) getFragmentManager()
					.findFragmentById(R.id.map)).getMap();
			MarkerOptions markOp = new MarkerOptions();
			Address target = addresses.get(0);
			LatLng loc = new LatLng(target.getLatitude(), target.getLongitude());
			Location = loc;
			markOp.position(loc);
			String addressText = String.format(
					"%s, %s, %s",
					// If there's a street address, add it
					target.getMaxAddressLineIndex() > 0 ? target
							.getAddressLine(0) : "",
					// Locality is usually a city
					target.getLocality(),
					// The country of the address
					target.getPostalCode());
			markOp.title(addressText);
			map.addMarker(markOp).showInfoWindow();
			CameraPosition cam = new CameraPosition.Builder().target(loc)
					.zoom(15.0f).build();
			CameraUpdate camUp = CameraUpdateFactory.newCameraPosition(cam);
			map.moveCamera(camUp);
		}
	}

	public void randLocation(View view) throws IOException {
		map.clear();
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		MarkerOptions markOp = new MarkerOptions();
		double minLat = -90.00;
		double maxLat = 90.00;
		double latitude = minLat
				+ (double) (Math.random() * ((maxLat - minLat) + 1));
		double minLon = 0.00;
		double maxLon = 180.00;
		double longitude = minLon
				+ (double) (Math.random() * ((maxLon - minLon) + 1));
		LatLng randLoc = new LatLng(latitude, longitude);
		markOp.position(randLoc);
		markOp.title("Random Location");
		map.addMarker(markOp).showInfoWindow();
		CameraPosition cam = new CameraPosition.Builder().target(randLoc).zoom(5.0f).build();
		CameraUpdate camUp = CameraUpdateFactory.newCameraPosition(cam);
		map.moveCamera(camUp);
	}
}
