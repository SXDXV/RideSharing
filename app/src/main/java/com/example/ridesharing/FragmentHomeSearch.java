package com.example.ridesharing;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.ridesharing.dadata.Address;
import com.example.ridesharing.dadata.DaData;
import com.google.android.material.textfield.TextInputEditText;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class FragmentHomeSearch extends Fragment{
    View view;

    private static final String TAG = "CityList";
    private static final String token = "0bf33d540f944deeddf92aaee8f1a60de7fcea29";
    private static final String secretToken = "cf86dcb6b9e1cc4279ab848a561c8625d84f8d0d";

    CardView searchCard;
    boolean hidden = true;
    ImageView dropDown;
    TextInputEditText from;
    String txtFrom;

    TextInputEditText to;
    String txtTo;

    TextInputEditText peoples;
    int txtPeoples;

    TextInputEditText date;
    String txtDate;

    CheckBox pickUp;

    Button plusPeople;
    Button minusPeople;

    Bundle bundleGet = new Bundle();
    Bundle bundleSet = new Bundle();


    public FragmentHomeSearch() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home_search, container, false);
        initComponents();

        searchCard.getBackground().setAlpha(255);
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern ("dd/MM/YYYY");
        date.setText(dtf.format(currentDate));

        bundleGet = getArguments();
        if(bundleGet != null){
            switch (bundleGet.getString("fieldName")){
                case "From":
                    from.setText(bundleGet.getString("fieldText"));
                    to.setText(bundleGet.getString("to"));
                    peoples.setText(bundleGet.getString("peoples"));
                    date.setText(bundleGet.getString("date"));
                    pickUp.setChecked(bundleGet.getBoolean("pickup"));
                    break;
                case "To":
                    to.setText(bundleGet.getString("fieldText"));
                    from.setText(bundleGet.getString("from"));
                    peoples.setText(bundleGet.getString("peoples"));
                    date.setText(bundleGet.getString("date"));
                    pickUp.setChecked(bundleGet.getBoolean("pickup"));
                    break;
                case "Date":
                    to.setText(bundleGet.getString("to"));
                    from.setText(bundleGet.getString("from"));
                    peoples.setText(bundleGet.getString("peoples"));
                    date.setText(bundleGet.getString("fieldText"));
                    pickUp.setChecked(bundleGet.getBoolean("pickup"));
                    break;

            }

        }

        // Animation cardview
        resizeHeight(searchCard, 950, 530);
        // Listener to animation cardview on touch
        View.OnClickListener listenerDropDown = v -> {
            if (hidden){
                resizeHeight(searchCard, 950, 530);
                dropDown.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24);
                hidden = false;
            } else {
                resizeHeight(searchCard, 950, 1140);
                dropDown.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24);
                hidden = true;
            }
        };
        dropDown.setOnClickListener(listenerDropDown);

        setListen(from);
        setListen(to);
        //setListen(peoples);
        setListen(date);
        countPeopleBtn(minusPeople);
        countPeopleBtn(plusPeople);

        return view;
    }

    public void setListen(View view){
        @SuppressLint("NonConstantResourceId") View.OnClickListener listenerField = v -> {
            switch (view.getId()){
                case R.id.fieldFrom:
                    bundleSet.putString("FieldName", "From");
                    bundleSet.putString("FieldText", from.getText().toString());
                    break;
                case R.id.fieldTo:
                    bundleSet.putString("FieldName", "To");
                    bundleSet.putString("FieldText", to.getText().toString());
                    break;
                case R.id.fieldDate:
                    bundleSet.putString("FieldName", "Date");
                    bundleSet.putString("FieldText", date.getText().toString());
                    break;
                default: break;
            }

            fullBundle(bundleSet);
            loadFragmentFromTop(HelperFragmentFields.newInstance(bundleSet), "helper");
//            Intent helperField = new Intent(getActivity(), HelperActivityFields.class);
//            helperField.putExtra("bundle", bundleSet);
//            startActivity(helperField);

        };
        view.setOnClickListener(listenerField);
    }

    public void fullBundle(Bundle bundle){
        bundle.putString("from", Objects.requireNonNull(from.getText()).toString());
        bundle.putString("to", Objects.requireNonNull(to.getText()).toString());
        bundle.putString("peoples", Objects.requireNonNull(peoples.getText()).toString());
        bundle.putString("date", Objects.requireNonNull(date.getText()).toString());
        bundle.putBoolean("pickup", pickUp.isChecked());
    }

    public String addressValidation(String notValidAddress){
        final String[] validAddress = {null};
        DaData daData = new DaData(token, secretToken);

        CompletableFuture.supplyAsync(() -> {
            Address address = daData.cleanAddress(notValidAddress);
            return address.getResult();
        }).thenAccept(
                result -> {
                    validAddress[0] = result;
                });
        return validAddress[0];
    }

    public void countPeopleBtn(View btn){
        btn.setOnClickListener(v -> {
            int countOfPeopleNow = Integer.parseInt(peoples.getText().toString());
            switch (btn.getId()){
                case R.id.buttonPlus:
                    if (countOfPeopleNow < 8){
                        countOfPeopleNow ++;
                    }
                    break;
                case R.id.buttonMinus:
                    if (countOfPeopleNow > 1){
                        countOfPeopleNow--;
                    }
                    break;
                default: break;
            }
            peoples.setText(String.valueOf(countOfPeopleNow));
        });
    }

    public void loadFragmentFromTop(Fragment fragment, String tag){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_down, R.anim.exit_to_top);
        transaction.replace(R.id.fragmentHome,fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public void initComponents(){
        from = view.findViewById(R.id.fieldFrom);
        to = view.findViewById(R.id.fieldTo);
        peoples = view.findViewById(R.id.fieldPeoples);
        date = view.findViewById(R.id.fieldDate);
        searchCard = view.findViewById(R.id.searchCardMain);
        dropDown = view.findViewById(R.id.imageView3);
        minusPeople = view.findViewById(R.id.buttonMinus);
        plusPeople = view.findViewById(R.id.buttonPlus);
        pickUp = view.findViewById(R.id.checkBoxPickUp);
    }

    public void resizeHeight(View view, int width, int height){
        ClassResizeAnimation resizeAnimation = new ClassResizeAnimation(view, width, height);
        resizeAnimation.setDuration(400);
        view.startAnimation(resizeAnimation);
    }

    public static FragmentHomeSearch newInstance(Bundle bundle){
        FragmentHomeSearch fragmentHomeSearch = new FragmentHomeSearch();
        fragmentHomeSearch.setArguments(bundle);
        return fragmentHomeSearch;
    }

    public static FragmentHomeSearch newInstance(){
        return new FragmentHomeSearch();
    }
}
