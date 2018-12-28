package com.example.team32gb.jobit.Model.ListJobSearch;

import androidx.annotation.NonNull;

import android.util.Log;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.RequestOptions;
import com.example.team32gb.jobit.Lib.GreenRobotEventBus;
import com.example.team32gb.jobit.Model.PostJob.DataPostJob;
import com.example.team32gb.jobit.Model.PostJob.ItemPostJob;
import com.example.team32gb.jobit.Model.PostJob.ModelPostJob;
import com.example.team32gb.jobit.Utility.Util;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ModelListJobSearch {
    private GreenRobotEventBus eventBus;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    public ModelListJobSearch() {
        eventBus = GreenRobotEventBus.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public void getListJob(final String timKiem, String diaDiem) {
        Client client = new Client("OYGFHT3AFC", "5d4c2ba1711e8b20d3754bc717222edc");
        final Index index = client.getIndex("tinTuyenDungs");
        final List<ItemPostJob> itemPostJobs = new ArrayList<>();
        CompletionHandler completionHandler = new CompletionHandler() {
                @Override
                public void requestCompleted(JSONObject jsonObject, AlgoliaException e) {
                    if (jsonObject == null && e != null) {
                        Log.e("kiemtraAlgoliaError0", e.getMessage());
                    } else {
                        Log.e("kiemtraAlgolia", jsonObject.toString());
                        try {
                            JSONArray array = jsonObject.getJSONArray("hits");
                            Log.e("kiemtraAlgolia", array.length() + "");

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = (JSONObject) array.get(i);
                                Log.e("kiemtraAlgolia", object.getString("nameJob") + "");
                                itemPostJobs.add(Util.parserJSONToItemPost(object));
                            }
                           // Log.e("kiemtraAlgolia", itemPostJobs.get(0).getDataPostJob().getIdCompany());
                            eventBus.post(itemPostJobs);
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }

                    }
                }
            };
            //JSONObject settings = new JSONObject().put("customRanking", "desc(maxSalary)");
            //index.setSettingsAsync(settings, completionHandler);
            Query query = new Query(timKiem + " " + diaDiem)
                    .setAttributesToHighlight("nameJob")
                    .setHitsPerPage(20);
            index.searchAsync(query, completionHandler);

    }

}
