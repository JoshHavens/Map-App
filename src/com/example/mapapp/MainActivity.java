package com.example.mapapp;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;









import android.location.*;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;
public class MainActivity extends Activity 
{
	GoogleMap map;
	Switch hybrid;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		hybrid = (Switch) findViewById(R.id.switch1);
		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		map.setMyLocationEnabled(true); 
		map.setTrafficEnabled(true);
		//set the switch to OFF 
		hybrid.setChecked(false);
		
		hybrid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
		    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		    	map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		        if (isChecked) {
		           map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
		        } else {
		           map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
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
	
	
	public void onClick(View view) throws IOException
	{
		map.clear();
		EditText text = (EditText) findViewById(R.id.info);
		String address = text.getText().toString();
		if(!address.isEmpty()){		// Prevent a null call to GeoCoder
			Geocoder earth = new Geocoder(MainActivity.this, Locale.ENGLISH);
			List<Address> addresses = earth.getFromLocationName(address, 1); //ONLY GETS 1 ADDRESS!!!!
			map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
			MarkerOptions markOp = new MarkerOptions();
			Address target = addresses.get(0);
			LatLng loc = new LatLng(target.getLatitude(), target.getLongitude());
			markOp.position(loc);
			String addressText = String.format(
					"%s, %s, %s",
					// If there's a street address, add it
					target.getMaxAddressLineIndex() > 0 ?
							target.getAddressLine(0) : "",
							// Locality is usually a city
							target.getLocality(),
							// The country of the address
							target.getPostalCode());
			markOp.title(addressText);
			map.addMarker(markOp).showInfoWindow();
			CameraPosition cam = new CameraPosition.Builder().target(loc).zoom(15.0f).build();
			CameraUpdate camUp = CameraUpdateFactory.newCameraPosition(cam);
			map.moveCamera(camUp);
			//Street View
			//Random Location Button
		}
	}
	
	
}
