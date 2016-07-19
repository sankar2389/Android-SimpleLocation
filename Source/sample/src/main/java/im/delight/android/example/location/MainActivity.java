package im.delight.android.example.location;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Toast;

import im.delight.android.location.SimpleLocation;

public class MainActivity extends Activity {

    private SimpleLocation mLocation;
    String address = "";
    double latitude, longitude;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        // construct a new instance
        mLocation = new SimpleLocation(this);

        // reduce the precision to 5,000m for privacy reasons
        mLocation.setBlurRadius(5000);

        // if we can't access the location yet
        if (!mLocation.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        findViewById(R.id.textView).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                latitude = mLocation.getLatitude();
                longitude = mLocation.getLongitude();
		address = getLocationAddress();

                Toast.makeText(MainActivity.this, "Latitude: "+latitude, Toast.LENGTH_SHORT).show();
                Toast.makeText(MainActivity.this, "Longitude: "+longitude, Toast.LENGTH_SHORT).show();
				Toast.makeText(MainActivity.this, "Address: "+address, Toast.LENGTH_LONG).show();
            }

        });
	}

public String getLocationAddress() {

			Geocoder geocoder = new Geocoder(this, Locale.getDefault());
			// Get the current location from the input parameter list
			// Create a list to contain the result address
			List<Address> addresses = null;
			try {
				/*
				 * Return 1 address.
				 */
				addresses = geocoder.getFromLocation(latitude, longitude, 1);
			} catch (IOException e1) {
				e1.printStackTrace();
				return ("IO Exception trying to get address:" + e1);
			} catch (IllegalArgumentException e2) {
				// Error message to post in the log
				String errorString = "Illegal arguments "
						+ Double.toString(latitude) + " , "
						+ Double.toString(longitude)
						+ " passed to address service";
				e2.printStackTrace();
				return errorString;
			}
			// If the reverse geocode returned an address
			if (addresses != null && addresses.size() > 0) {
				// Get the first address
				Address address = addresses.get(0);
				/*
				 * Format the first line of address (if available), city, and
				 * country name.
				 */
				String addressText = String.format(
						"%s, %s, %s",
						// If there's a street address, add it
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "",
						// Locality is usually a city
						address.getLocality(),
						// The country of the address
						address.getCountryName());
				// Return the text
				return addressText;
			} else {
				return "No address found by the service: Note to the developers, If no address is found by google itself, there is nothing you can do about it.";
			}
	}
	
    @Override
    protected void onResume() {
        super.onResume();

        // make the device update its location
        mLocation.beginUpdates();
    }

    @Override
    protected void onPause() {
        // stop location updates (saves battery)
        mLocation.endUpdates();

        super.onPause();
    }

}
