package com.kantutapp.bloodhope.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.kantutapp.bloodhope.DetailCauseActivity;
import com.kantutapp.bloodhope.EditCauseActivity;
import com.kantutapp.bloodhope.R;
import com.kantutapp.bloodhope.adapter.CausesAdapter;
import com.kantutapp.bloodhope.models.Cause;
import com.kantutapp.bloodhope.models.User;
import com.kantutapp.bloodhope.utils.Constants;
import com.kantutapp.bloodhope.viewholder.CauseViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import mehdi.sakout.fancybuttons.FancyButton;

import static com.kantutapp.bloodhope.utils.Constants.MODE_CREATE;

public class ProfileFragment extends Fragment implements CausesAdapter.OnItemClickHandler {

    private static final String TAG = "ProfileFragment";



    @BindView(R.id.recycler_causes)
    RecyclerView recyclerViewCauses;
    @BindView(R.id.buttonCreate)
    FancyButton btnCreate;
    @BindView(R.id.list_causes_user)
    RelativeLayout listCuasesUses;


    Context mContext;
    View mView;

    @BindView(R.id.profile_progressbar)
    ProgressBar profileProgressbar;

    private DatabaseReference mDatabase;

    List<Cause> causeArrayList = new ArrayList<>();
    private FirebaseRecyclerAdapter<Cause, CauseViewHolder> mAdapter;
    private LinearLayoutManager mManager;
    public  Unbinder binder;

    public String currentUserName="";
    public String currentPhoneNumber="";

    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (container != null)
            container.removeAllViews();
        mView = inflater.inflate(R.layout.fragment_profile, container, false);
        mContext = mView.getContext();
        binder = ButterKnife.bind(this, mView);

        return mView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            setupProfileUI(firebaseUser);
            setupListOfCauses(firebaseUser);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void setupListOfCauses(FirebaseUser acct) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        final Query causeQuery = mDatabase.child(Constants.CAUSES)
                .orderByChild(Constants.CAUSES_USERID)
                .equalTo(acct.getUid());


        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        recyclerViewCauses.setLayoutManager(layoutManager);

        final CausesAdapter adapter = new CausesAdapter(causeArrayList, mContext, this);
        recyclerViewCauses.setAdapter(adapter);



        (mView.findViewById(R.id.profile_progressbar)).setVisibility(View.VISIBLE);
        ValueEventListener valueEventListener = new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                causeArrayList.clear();
                (mView.findViewById(R.id.profile_progressbar)).setVisibility(View.GONE);

                if (dataSnapshot.getChildrenCount() == 0){
                    (mView.findViewById(R.id.recycler_causes)).setVisibility(View.GONE);
                    (mView.findViewById(R.id.buttonCreate)).setVisibility(View.VISIBLE);
                } else {
                    (mView.findViewById(R.id.recycler_causes)).setVisibility(View.VISIBLE);
                    (mView.findViewById(R.id.buttonCreate)).setVisibility(View.GONE);
                    for (DataSnapshot causesSnapshot: dataSnapshot.getChildren()) {
                        Cause cause = causesSnapshot.getValue(Cause.class);
                        String causeKey = causesSnapshot.getKey();

                        cause.setKey(causeKey);
                        causeArrayList.add(cause);
                        adapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        causeQuery.addValueEventListener(valueEventListener);


    }


    private void setupProfileUI(FirebaseUser firebaseUser) {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Query userQuery = mDatabase.child(Constants.USERS).child(firebaseUser.getUid());
        userQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if (user != null){
                    String profileName = user.getName();
                    int profileDonations = user.getNumber_donations();
                    String profilePhoto = user.getPhoto();
                    String profilePhoneNumber = user.getPhone_number();

                    if (!profileName.isEmpty()){
                        ((TextView) mView.findViewById(R.id.profile_name)).setText(profileName);
                        currentUserName = profileName;
                    }

                    if (profileDonations >= 0)
                        ((TextView) mView.findViewById(R.id.profile_number_donations))
                                .setText("DONATIONS: "+ profileDonations);

                    if (profilePhoto != null){
                        if (!profilePhoto.isEmpty())
                            Picasso.get()
                                    .load(profilePhoto)
                                    .placeholder(R.drawable.profile)
                                    .into(((ImageView) mView.findViewById(R.id.profile_thumbnail)));
                    }


                    if (!profilePhoneNumber.isEmpty()){
                         currentPhoneNumber = profilePhoneNumber;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @Override
    public void onCardClickListener(Cause cause) {
        Intent intent = new Intent(mContext, DetailCauseActivity.class);
        intent.putExtra(Constants.CAUSE, cause);
        intent.putExtra(Constants.MODE_EDIT, true);
        startActivity(intent);
    }

    @Override
    public void onDeleteListener(Cause cause) {
        profileProgressbar.setVisibility(View.VISIBLE);
        mDatabase.child(Constants.CAUSES).child(cause.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                profileProgressbar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onEditListener(Cause cause) {
        Intent intent = new Intent(mContext, DetailCauseActivity.class);
        intent.putExtra(Constants.CAUSE, cause);
        intent.putExtra(Constants.MODE_EDIT, true);
        startActivity(intent);

    }

    @OnClick(R.id.buttonCreate)
    void createCause() {
        Intent intent = new Intent(getActivity(), EditCauseActivity.class);
        intent.putExtra(MODE_CREATE, true);
        intent.putExtra(Constants.USER_NAME, currentUserName);
        intent.putExtra(Constants.USER_PHONENUMBER, currentPhoneNumber);
        startActivity(intent);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binder.unbind();
    }
}
