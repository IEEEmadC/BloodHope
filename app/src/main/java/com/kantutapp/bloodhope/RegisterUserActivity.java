package com.kantutapp.bloodhope;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kantutapp.bloodhope.adapter.BloodAdapter;
import com.kantutapp.bloodhope.fragments.DonateFragment;
import com.kantutapp.bloodhope.fragments.PricesFragment;
import com.kantutapp.bloodhope.fragments.ProfileFragment;
import com.kantutapp.bloodhope.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import mehdi.sakout.fancybuttons.FancyButton;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener, BloodAdapter.OnBloodClickHandler {

    private static final String TAG = RegisterUserActivity.class.getSimpleName();
    private static final int NUMBER_COLS = 6;
    private static final String USER = "User";

    @BindView(R.id.btn_done) FancyButton btnDone;
    @BindView(R.id.recycler_blood_types) RecyclerView recyclerBloodTypes;
    @BindView(R.id.et_user_number) EditText etUserNumber;

    private User currentUser = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        ButterKnife.bind(this);

        List<String> bloodTypes = new ArrayList<>();
        bloodTypes.add("A+");
        bloodTypes.add("A-");
        bloodTypes.add("B-");
        bloodTypes.add("B+");
        bloodTypes.add("O+");
        bloodTypes.add("O-");
        bloodTypes.add("AB-");
        bloodTypes.add("AB+");

        BloodAdapter adapter = new BloodAdapter(bloodTypes, this, this);
        recyclerBloodTypes.setAdapter(adapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, NUMBER_COLS);
        recyclerBloodTypes.setLayoutManager(gridLayoutManager);

        btnDone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_done:
                goToGeneralActivity();
                break;
        }
    }

    private void goToGeneralActivity() {
        Intent intent = new Intent(RegisterUserActivity.this, GeneralActivity.class);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser.setName(firebaseUser.getDisplayName());
        currentUser.setPhoto(firebaseUser.getPhotoUrl().toString());
        currentUser.setPhoneNumber(etUserNumber.getText().toString());
        intent.putExtra(USER, currentUser);
        startActivity(intent);
        finish();
    }


    @Override
    public void onBloodTypeClickListener(String bloodType) {
        currentUser.setTypeOfBlood(bloodType);
        Toast.makeText(this, bloodType, Toast.LENGTH_SHORT).show();
    }
}
