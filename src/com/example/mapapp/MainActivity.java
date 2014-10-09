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
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
		EditText text = (EditText) findViewById(R.id.info);
		String address = text.getText().toString();
		Geocoder earth = new Geocoder(MainActivity.this, Locale.ENGLISH);
		List<Address> addresses = earth.getFromLocationName(address, 1); //ONLY GETS 1 ADDRESS!!!!
		GoogleMap map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		MarkerOptions markOp = new MarkerOptions();
		Address target = addresses.get(0);
		LatLng loc = new LatLng(target.getLatitude(), target.getLongitude());
		markOp.position(loc);
		markOp.title(address); //Only displays on marker click
		Marker mark = map.addMarker(markOp);
		CameraPosition cam = new CameraPosition.Builder().target(loc).zoom(14.0f).build(); //Zoom in more/less?
		CameraUpdate camUp = CameraUpdateFactory.newCameraPosition(cam);
		map.moveCamera(camUp);
		//Street View
		//Current location
		//Random Location Button
		//Terrain/Satellite
	}
}
