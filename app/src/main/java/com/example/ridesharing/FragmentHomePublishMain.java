package com.example.ridesharing;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FragmentHomePublishMain extends Fragment{
    private View view;

    private static final String VIEW_NAME = "FragmentHomePublishMain";

    private TextInputEditText from;
    private TextInputEditText to;
    private TextInputEditText peoples;
    private TextInputEditText date;
    private TextInputEditText time;
    private TextInputEditText price;
    private TextInputEditText comment;
    private CheckBox music;
    private CheckBox pets;
    private CheckBox talk;
    private CheckBox smoking;
    private Button minus;
    private Button plus;
    private Button finish;
    private String UiD;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("publish");

    private Bundle bundleGet = new Bundle();
    private Bundle bundleSet = new Bundle();

    public FragmentHomePublishMain() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_publish_main, container, false);
        initComponents();
        UiD = FragmentLoginAuth.userID;

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern ("dd-MM-YYYY");
        date.setText(dtf.format(currentDate));

        countPeopleBtn(minus);
        countPeopleBtn(plus);

        setListen(from);
        setListen(to);
        setListen(date);
        setListen(time);
        setListen(price);
        setListen(comment);

        bundleGet = getArguments();
        try {
            if(bundleGet != null){
                from.setText(bundleGet.getString("from"));
                to.setText(bundleGet.getString("to"));
                peoples.setText(bundleGet.getString("peoples"));
                date.setText(bundleGet.getString("date"));
                time.setText(bundleGet.getString("time"));
                price.setText(bundleGet.getString("price"));
                comment.setText(bundleGet.getString("comment"));
                music.setChecked(bundleGet.getBoolean("music"));
                pets.setChecked(bundleGet.getBoolean("pets"));
                talk.setChecked(bundleGet.getBoolean("talk"));
                smoking.setChecked(bundleGet.getBoolean("smoke"));
                String fieldName = bundleGet.getString("fieldName");

                String txtFrom = getResources().getString(R.string.field_from);
                String txtTo = getResources().getString(R.string.field_to);
                String txtDate = getResources().getString(R.string.field_date);
                String txtTime = getResources().getString(R.string.field_time);
                String txtPrice = getResources().getString(R.string.field_price);
                String txtComment = getResources().getString(R.string.field_comment);

                if (txtFrom.equals(fieldName)) {
                    from.setText(bundleGet.getString("fieldText"));
                } else if (txtTo.equals(fieldName)) {
                    to.setText(bundleGet.getString("fieldText"));
                } else if (txtDate.equals(fieldName)) {
                    date.setText(bundleGet.getString("fieldText"));
                }else if (txtTime.equals(fieldName)) {
                    time.setText(bundleGet.getString("fieldText"));
                } else if (txtPrice.equals(fieldName)) {
                    price.setText(bundleGet.getString("fieldText"));
                } else if (txtComment.equals(fieldName)) {
                    comment.setText(bundleGet.getString("fieldText"));
                }
            }

        }catch (Exception e){
            Log.d(ActivityHome.MAIN_TAG, VIEW_NAME + " " + e);
        }


        finish.setOnClickListener(v -> {
            try {
                realtimeDatabaseSetData();
            }catch (Exception e){
                Log.d(ActivityHome.MAIN_TAG, VIEW_NAME + " " + e);
            }
        });

        return view;
    }

    public void realtimeDatabaseSetData(){
        Map<String, Object> appointMap = new HashMap<>();
        appointMap.put("author_id", FragmentLoginAuth.userID);
        appointMap.put("from", Objects.requireNonNull(from.getText()).toString());
        appointMap.put("to", Objects.requireNonNull(to.getText()).toString());
        appointMap.put("peoples", Objects.requireNonNull(peoples.getText()).toString());
        appointMap.put("date", Objects.requireNonNull(date.getText()).toString());
        appointMap.put("time", Objects.requireNonNull(time.getText()).toString());
        appointMap.put("price", Objects.requireNonNull(price.getText()).toString());
        appointMap.put("comment", Objects.requireNonNull(comment.getText()).toString());
        appointMap.put("music", music.isChecked());
        appointMap.put("pets", pets.isChecked());
        appointMap.put("talk", talk.isChecked());
        appointMap.put("smoking", smoking.isChecked());
        appointMap.put("trip_finished", false);

        myRef.child(UiD + date.getText() + time.getText()).setValue(appointMap);
        loadFragmentFromDown(FragmentHomePublish.newInstance(), "publish");
    }

    public void setListen(View view){
        @SuppressLint("NonConstantResourceId") View.OnClickListener listenerField = v -> {
            bundleSet.putString("Parent", "Publish");
            switch (view.getId()){
                case R.id.fieldPublishFrom:
                    bundleSet.putString("FieldName", getResources().getString(R.string.field_from));
                    bundleSet.putString("FieldText", from.getText().toString());
                    break;
                case R.id.fieldPublishTo:
                    bundleSet.putString("FieldName", getResources().getString(R.string.field_to));
                    bundleSet.putString("FieldText", to.getText().toString());
                    break;
                case R.id.fieldPublishDate:
                    bundleSet.putString("FieldName", getResources().getString(R.string.field_date));
                    bundleSet.putString("FieldText", date.getText().toString());
                    break;
                case R.id.fieldPublishTime:
                    bundleSet.putString("FieldName", getResources().getString(R.string.field_time));
                    bundleSet.putString("FieldText", time.getText().toString());
                    break;
                case R.id.fieldPublishPrice:
                    bundleSet.putString("FieldName", getResources().getString(R.string.field_price));
                    bundleSet.putString("FieldText", price.getText().toString());
                    break;
                case R.id.fieldPublishComment:
                    bundleSet.putString("FieldName", getResources().getString(R.string.field_comment));
                    bundleSet.putString("FieldText", comment.getText().toString());
                    break;
                default: break;
            }

            fullBundle(bundleSet);
            loadFragmentFromTop(HelperFragmentFields.newInstance(bundleSet), "helper");
        };
        view.setOnClickListener(listenerField);
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

    public void fullBundle(Bundle bundle){
        bundle.putString("from", Objects.requireNonNull(from.getText()).toString());
        bundle.putString("to", Objects.requireNonNull(to.getText()).toString());
        bundle.putString("peoples", Objects.requireNonNull(peoples.getText()).toString());
        bundle.putString("date", Objects.requireNonNull(date.getText()).toString());
        bundle.putString("time", Objects.requireNonNull(time.getText()).toString());
        bundle.putString("price", Objects.requireNonNull(price.getText()).toString());
        bundle.putString("comment", Objects.requireNonNull(comment.getText()).toString());
        bundle.putBoolean("music", music.isChecked());
        bundle.putBoolean("pets", pets.isChecked());
        bundle.putBoolean("talk", talk.isChecked());
        bundle.putBoolean("smoke", smoking.isChecked());
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

    public void loadFragmentFromDown(Fragment fragment, String tag){
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction transaction = fragmentManager
                .beginTransaction()
                .setCustomAnimations(R.anim.enter_from_top, R.anim.exit_to_down);
        transaction.replace(R.id.fragmentHome,fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public void initComponents(){
        from = view.findViewById(R.id.fieldPublishFrom);
        to = view.findViewById(R.id.fieldPublishTo);
        peoples = view.findViewById(R.id.fieldPublishPeoples);
        date = view.findViewById(R.id.fieldPublishDate);
        time = view.findViewById(R.id.fieldPublishTime);
        price = view.findViewById(R.id.fieldPublishPrice);
        comment = view.findViewById(R.id.fieldPublishComment);
        music = view.findViewById(R.id.checkBoxMusic);
        pets = view.findViewById(R.id.checkBoxAnimals);
        talk = view.findViewById(R.id.checkBoxTalk);
        smoking = view.findViewById(R.id.checkBoxSmoke);
        minus = view.findViewById(R.id.buttonMinus);
        plus = view.findViewById(R.id.buttonPlus);
        finish = view.findViewById(R.id.btnToPublishMain);
    }

    public static FragmentHomePublishMain newInstance(Bundle bundle){
        FragmentHomePublishMain fragmentHomePublishMain = new FragmentHomePublishMain();
        fragmentHomePublishMain.setArguments(bundle);
        return fragmentHomePublishMain;
    }

    public static FragmentHomePublishMain newInstance(){
        return new FragmentHomePublishMain();
    }
}
