package com.wavent.src.activity;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wavent.R;
import com.wavent.databinding.FragmentEventInfoBinding;
import com.wavent.src.manager.ApiManager;
import com.wavent.src.model.Event;
import com.wavent.src.model.Session;
import com.wavent.src.model.User;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by arnau on 07/12/2016.
 */
public class EventInfoFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentEventInfoBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_event_info,container,false);
        final Event detailEvent = getArguments().getParcelable("event");
        binding.setEvent(detailEvent);

        //Permet de participer à un évenement
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = Session.getInstance(null).getUserConnected();
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                gson.toJson(user,User.class);
                String jsonString = gson.toJson(user,User.class);
                JSONObject params = null;
                try {
                    params = new JSONObject(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ApiManager.getInstance().joinEvent(v.getContext(),detailEvent.getId(),params);
                detailEvent.addParticipant(user);

            }
        });

        //Permet de lancer googleMap sur le lieu de l'évenement
        binding.textAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q="+detailEvent.getAddress());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });
        return binding.getRoot();
    }
}
