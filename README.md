# Android-SimpleLocation

Utility class for easy access to the device location on Android

## Requirements

 * Android 2.2+

## Installation

 * Add this library to your project
   * Declare the Gradle repository in your root `build.gradle`

     ```gradle
     allprojects {
         repositories {
             maven { url "https://jitpack.io" }
         }
     }
     ```

   * Declare the Gradle dependency in your app module's `build.gradle`

     ```gradle
     dependencies {
         compile 'com.github.delight-im:Android-SimpleLocation:v1.0.1'
     }
     ```

## Usage

### Decide for the required granularity

 * If you want to get the device's location with fine granularity (between 2m and 100m precision), GPS will be required. This consumes more battery but is most precise.
 * If you want to get the device's location with coarse granularity only (precise to several hundred meters), the location will be retrieved from the network (Wi-Fi and cell towers). This saves battery but is less precise.

### Add the required permissions

For fine location (GPS location), add the following permission in your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
```

For coarse location (network location), add the following permission in your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
```

### Retrieve the location from the device

```java
public class MyActivity extends Activity {

    private SimpleLocation location;
    String address = "";
    double latitude, longitude;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ...

        // construct a new instance of SimpleLocation
        location = new SimpleLocation(this);

        // if we can't access the location yet
        if (!location.hasLocationEnabled()) {
            // ask the user to enable location access
            SimpleLocation.openSettings(this);
        }

        findViewById(R.id.someView).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                latitude = mLocation.getLatitude();
                longitude = mLocation.getLongitude();
		        address = getLocationAddress();

                // TODO
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
        location.beginUpdates();

        // ...
    }

    @Override
    protected void onPause() {
        // stop location updates (saves battery)
        location.endUpdates();

        // ...

        super.onPause();
    }

}
```


### Calculate the distance between two locations

```java
// alternative A
location.calculateDistance(startLatitude, startLongitude, endLatitude, endLongitude);

// alternative B
location.calculateDistance(startPoint, endPoint);
```

### Blur the location for privacy reasons

```java
// reduce the precision to 10,000m for privacy reasons
location.setBlurRadius(10000);
```

## License

```
Copyright (c) delight.im <info@delight.im>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
