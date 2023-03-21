package com.example.ridesharing;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;

public class HelperFragmentFields extends Fragment{
    View view;
    private static final String VIEW_NAME = "HelperFragmentFields";

    TextView fieldName;
    String txtFieldName;
    TextInputEditText field;
    String txtField;
    Button btnContinue;
    ImageView backSearch;

    Bundle bundleGet;
    Bundle bundleSet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.helper_fragment_fields, container, false);
        initComponents();

        bundleGet = getArguments();
        if (bundleGet != null) {
            txtFieldName = bundleGet.getString("FieldName");
            fieldName.setText(txtFieldName);
            field.setText(bundleGet.getString("FieldText"));
        }

        backToSearch(btnContinue);
        backToSearch(backSearch);
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    public void backToSearch(View btn){
        btn.setOnClickListener(v -> {
            bundleSet = new Bundle();
            bundleSet.putAll(bundleGet);
            switch (btn.getId()){
                case R.id.btnContinueFragmentSearch:
                    bundleSet.putString("fieldName", fieldName.getText().toString());
                    bundleSet.putString("fieldText", field.getText().toString());

                    backToParent();
                    break;
                case R.id.backToSearch:
                    bundleSet.putString("fieldName", "null");
                    bundleSet.putString("fieldText", "null");

                    backToParent();
                    break;
                default:
                    break;
            }
        });
    }

    public void backToParent(){
        if (bundleGet.getString("Parent").equals("Search")){
            loadFragmentFromDown(FragmentHomeSearch.newInstance(bundleSet), "search");
        }
        else if (bundleGet.getString("Parent").equals("Publish")){
            loadFragmentFromDown(FragmentHomePublishMain.newInstance(bundleSet), "publish");
        }
    }

    public void initComponents(){
        fieldName = view.findViewById(R.id.fieldNameFragmentSearch);
        field = view.findViewById(R.id.fieldFragmentSearch);
        btnContinue = view.findViewById(R.id.btnContinueFragmentSearch);
        backSearch = view.findViewById(R.id.backToSearch);
    }

    public void loadFragmentFromDown(Fragment fragment, String tag){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_down);
        transaction.replace(R.id.fragmentHome,fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public static HelperFragmentFields newInstance(Bundle bundle){
        HelperFragmentFields helperFragmentFields = new HelperFragmentFields();
        helperFragmentFields.setArguments(bundle);
        return helperFragmentFields;
    }
    public static HelperFragmentFields newInstance(){
        return new HelperFragmentFields();
    }
}