package com.xonetsapps.fabtechnologies.activity;

import android.app.Activity;
import android.media.tv.TvInputService;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.xonetsapps.fabtechnologies.R;
import com.xonetsapps.fabtechnologies.SessionHandler;
import com.xonetsapps.fabtechnologies.User;

public class Profile extends Fragment {
    private ImageView update;
    private TextView name,mail,phone,address,company;
    public static SessionHandler session;
    public static User user;
    public Profile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_profile, container, false);
        session = new SessionHandler(getContext());
         user = session.getUserDetails();

        update = (ImageView) rootView.findViewById(R.id.updt);
        name = (TextView) rootView.findViewById(R.id.tv_name);
        mail = (TextView) rootView.findViewById(R.id.email);
        phone = (TextView) rootView.findViewById(R.id.phone);
        address = (TextView) rootView.findViewById(R.id.tv_address);
        company = (TextView) rootView.findViewById(R.id.company);

        //Toast.makeText(getContext(),"Email:"+user.getId(), Toast.LENGTH_SHORT).show();

        name.setText(user.getName());
        mail.setText(user.getEmail());
        phone.setText(user.getMobile());
        address.setText(user.getAddress());
        company.setText(user.getCompany());
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = new UpdateActivity();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("      Settings");
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
