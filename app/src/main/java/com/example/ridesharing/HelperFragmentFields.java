package com.example.ridesharing;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HelperFragmentFields extends Fragment {
    TextView fieldName;
    String txtFieldName;
    TextInputEditText field;
    String txtField;
    Button btnContinue;

    View view;

    public HelperFragmentFields() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.helper_fragment_fields, container, false);
        initComponents();

        Bundle bundle = getArguments();
        if (bundle != null){
            fieldName.setText(bundle.getString("FieldName"));
        }

        fillList();

        return view;
    }

    public List<String> getCitiesListAPI(String searchQuery){
        List<String> cityNames = null;
        // Set up the Yandex Maps API URL with your API key and the search query
        String url = "https://search-maps.yandex.ru/v1/suggest?" +
                "apikey=<YOUR_API_KEY>&" +
                "type=geo&" +
                "part=" + searchQuery + "&" +
                "lang=ru_RU";

        // Create an HTTP client and request object
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();

        // Make the request and parse the response
        try {
            Response response = client.newCall(request).execute();
            String responseBody = Objects.requireNonNull(response.body()).string();
            JSONObject responseJson = new JSONObject(responseBody);

            // Get the array of suggestions from the response
            JSONArray suggestions = responseJson.getJSONArray("features");

            // Extract the city names from the suggestions and add them to a list
            cityNames = new ArrayList<>();
            for (int i = 0; i < suggestions.length(); i++) {
                JSONObject suggestion = suggestions.getJSONObject(i);
                String cityName = suggestion.getJSONObject("properties").getString("name");
                cityNames.add(cityName);
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        return cityNames;
    }

    public void fillList(){
        String searchQuery = "";

        // Assuming you have a reference to the ListView and the List of cities
        ListView citiesListView = view.findViewById(R.id.citiesLV);
        List<String> citiesList = getCitiesListAPI(searchQuery);

        // Create an ArrayAdapter to populate the ListView
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, citiesList);

        // Set the adapter to the ListView
        citiesListView.setAdapter(adapter);

        // Add a TextWatcher to the EditText to filter the ListView as the user types
        TextInputEditText searchEditText = view.findViewById(R.id.fieldFragmentSearch);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Filter the adapter based on the user's input
                adapter.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void initComponents(){
        fieldName = view.findViewById(R.id.fieldNameFragmentSearch);
        field = view.findViewById(R.id.fieldFragmentSearch);
        btnContinue = view.findViewById(R.id.btnContinueFragmentSearch);
    }

    public static HelperFragmentFields newInstance(Bundle bundle){
        HelperFragmentFields helperFragmentFields = new HelperFragmentFields();
        helperFragmentFields.setArguments(bundle);
        return helperFragmentFields;
    }
}
