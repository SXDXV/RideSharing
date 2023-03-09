package com.example.ridesharing;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ridesharing.dadata.Address;
import com.example.ridesharing.dadata.DaData;
import com.google.android.material.textfield.TextInputEditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class HelperFragmentFields extends Fragment{
    private static final String TAG = "CityList";
    private static final String token = "ba5f3c53669fec2fe07843b5b8d667b690d87013";
    private static final String secretToken = "ad01b963d06b340db4721d1819980b78e52347e7";

    TextView fieldName;
    String txtFieldName;
    TextInputEditText field;
    String txtField;
    Button btnContinue;

    View view;

    ArrayList<String> cityList = new ArrayList<>();;
    ListView citiesLV;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.helper_fragment_fields, container, false);
        initComponents();

        Bundle bundle = getArguments();
        if (bundle != null) {
            fieldName.setText(bundle.getString("FieldName"));
        }

        DaData daData = new DaData(token, secretToken);



//        new Thread(new Runnable() {
//            public void run() {
//                Address address = daData.cleanAddress("мск сухонска 11/-89");
//                field.setText(address.getResult());
//            }
//        }).start();


        field.addTextChangedListener(new TextWatcher() {
            String nowValue;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // This method is called before the text is changed.
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nowValue = Objects.requireNonNull(field.getText()).toString();

                CompletableFuture.supplyAsync(() -> {
                    Address address = daData.cleanAddress("Санкт петербург марата 8");
                    return address.getResult();
                }).thenAccept(
                        result -> {
                            cityList.add(result);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                                    android.R.layout.simple_list_item_1, cityList);
                            citiesLV.setAdapter(adapter);
                        }
                );
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        return view;
    }

    public void initComponents(){
        fieldName = view.findViewById(R.id.fieldNameFragmentSearch);
        field = view.findViewById(R.id.fieldFragmentSearch);
        btnContinue = view.findViewById(R.id.btnContinueFragmentSearch);
        citiesLV = view.findViewById(R.id.citiesLV);
    }

    public static HelperFragmentFields newInstance(Bundle bundle){
        HelperFragmentFields helperFragmentFields = new HelperFragmentFields();
        helperFragmentFields.setArguments(bundle);
        return helperFragmentFields;
    }
}