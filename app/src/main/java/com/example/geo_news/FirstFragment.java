package com.example.geo_news;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textview.MaterialTextView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class FirstFragment extends Fragment implements OnMapReadyCallback {
    private GoogleMap mMap;
    private Marker marker;
    private Context mContext;
    private List<Address> addressList,slist;
    String locality,countryName,featureName,countrycode;
    View bottomsheet;
    MaterialButton newsButton;
    FloatingActionButton button;
    MaterialCardView cardView;
    SearchView searchView;
    MaterialTextView textView;
    double latitude,longitude;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mContext=null;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button = view.findViewById(R.id.search_fab);
        cardView = view.findViewById(R.id.searchCard);
        searchView = view.findViewById(R.id.searchV);
        cardView.setVisibility(View.GONE);
        textView = view.findViewById(R.id.placeText);
        bottomsheet = view.findViewById(R.id.standardBottomSheet);
        newsButton = view.findViewById(R.id.newsLaunch);
        // Initialize the SDK
        //Places.initialize(mContext.getApplicationContext(),apiKey);

        // Create a new PlacesClient instance
        //PlacesClient placesClient = Places.createClient(mContext);

        Log.d("onViewCreated","inside onViewCreated");
        if(getActivity()!=null) {
            Log.d("get Activity","not null");
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map);
            if (mapFragment != null) {
                Log.d("map Fragment","map async");
                mapFragment.getMapAsync(this);
            }
        }


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        final BottomSheetBehavior standardBottomSheetBehavior = BottomSheetBehavior.from(bottomsheet);
        Log.d("height",String.valueOf(textView.getLineHeight()));
        Log.d("onMapReadey","map ready started");
        standardBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        final Geocoder geocoder = new Geocoder(mContext);
       /* LatLng def = new LatLng(1.0,1.0);
        if(Geocoder.isPresent())
        {
            try {
           addressList = geocoder.getFromLocation(1.0,1.0,1);
        } catch (IOException e) {
            e.printStackTrace();
        }
            if(addressList.size()>0)
            {
                locality = addressList.get(0).getLocality();
                featureName = addressList.get(0).getFeatureName();
                countrycode = addressList.get(0).getCountryCode();
               // Log.d("Locality",locality);
                countryName = addressList.get(0).getCountryName();
            }
        }
        else
        {
            Log.d("Geocoder","geocodernotpresent");
        }
        if(locality!=null && countryName!=null) {
            marker = mMap.addMarker(new MarkerOptions().position(def).title(locality+","+countryName));
            marker.showInfoWindow();
        }
        else if(featureName!=null && countryName!=null)
        {
            marker = mMap.addMarker(new MarkerOptions().position(def).title(featureName+","+countryName));
            marker.showInfoWindow();
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLng(def));
*/

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if(Geocoder.isPresent())
                {
                    try {
                        addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if(addressList.size()>0)
                    {
                        Log.d("address",addressList.toString());
                        locality = addressList.get(0).getLocality();
                        featureName = addressList.get(0).getFeatureName();
                        countrycode = addressList.get(0).getCountryCode();
                        //Log.d("Locality",locality);
                        countryName = addressList.get(0).getCountryName();
                    }
                }
                if(locality!=null && countryName!=null) {
                    if(marker!=null)
                    {
                        marker.remove();
                    }
                    marker = mMap.addMarker(new MarkerOptions().position(latLng));
                    textView.setText(locality+","+countryName+"("+countrycode+")");
                    standardBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                }
                else if(featureName!=null && countryName!=null)
                {
                    if(marker!=null)
                    {
                        marker.remove();
                    }
                    marker = mMap.addMarker(new MarkerOptions().position(latLng));
                    textView.setText(featureName+","+countryName+"("+countrycode+")");
                    standardBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

                }

            }
        });





        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cardView.getVisibility()==View.GONE)
                {
                    cardView.setVisibility(View.VISIBLE);
                    button.setImageResource(R.drawable.ic_baseline_cancel_24);
                }
                else
                {
                    cardView.setVisibility(View.GONE);
                    button.setImageResource(R.drawable.ic_baseline_search_24);
                }
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    slist = geocoder.getFromLocationName(query,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(slist.size()>0)
                {
                    Log.d("address",slist.toString());
                    locality = slist.get(0).getLocality();
                    featureName = slist.get(0).getFeatureName();
                    countrycode = slist.get(0).getCountryCode();
                    latitude = slist.get(0).getLatitude();
                    longitude = slist.get(0).getLongitude();
                    //Log.d("Locality",locality);
                    countryName = slist.get(0).getCountryName();
                }
                LatLng latLng = new LatLng(latitude,longitude);
                if(featureName!=null && countryName!=null)
                {
                    if(marker!=null)
                    {
                        marker.remove();
                    }
                    marker = mMap.addMarker(new MarkerOptions().position(latLng));
                    textView.setText(featureName+","+countryName+"("+countrycode+")");
                    standardBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }
                else if(locality!=null && countryName!=null) {
                    if(marker!=null)
                    {
                        marker.remove();
                    }
                    marker = mMap.addMarker(new MarkerOptions().position(latLng));
                    textView.setText(locality+","+countryName+"("+countrycode+")");
                    standardBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                }

                else
                {

                    Toast.makeText(mContext.getApplicationContext(), "No such place found", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //launch your news activity here
        //use countrycode variable to get country code
        newsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }




}