package com.hiit.timer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ExercicesFragment extends Fragment {

    private static ProgressDialog mProgressDialog;
    private RecyclerViewAdapter recyclerViewAdapter;
    private RecyclerView recyclerView;
    ArrayList<DataModel> dataModelArrayList;
    CardView cardView;
    Context context;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View exercicesView = inflater.inflate(R.layout.fragment_exercices, container, false);

        context = exercicesView.getContext();

        recyclerView = exercicesView.findViewById(R.id.recyclerExo);
        cardView = exercicesView.findViewById(R.id.card_view);

        readData();

        return exercicesView;

    }


    private void readData() {

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        dataModelArrayList = new ArrayList<>();

        showSimpleProgressDialog(ExercicesFragment.this, "Connexion au serveur", "Récupération des données", false);

        db.collection("exercices")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                removeSimpleProgressDialog();

                                DataModel playerModel = new DataModel();

                                playerModel.setName(document.getString("name"));
                                playerModel.setDescription(document.getString("description"));
                                playerModel.setImgURL(document.getString("imgURL"));

                                dataModelArrayList.add(playerModel);
                            }
                            setupList();
                        } else {
                            Log.w(android.content.ContentValues.TAG, "Error getting documents.", task.getException());
                        }
                    }

                });
    }

    private void setupList() {

        Collections.sort(dataModelArrayList, new Comparator<DataModel>() {
            @Override
            public int compare(DataModel dataModel, DataModel dataMode2) {
                return dataModel.getName().compareTo(dataMode2.getName());
            }
        });

        recyclerViewAdapter = new RecyclerViewAdapter(context, dataModelArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

    }


    public static void removeSimpleProgressDialog() {
        try {
            if (mProgressDialog != null) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.dismiss();
                    mProgressDialog = null;
                }
            }
        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();

        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void showSimpleProgressDialog(ExercicesFragment context, String title,
                                                String msg, boolean isCancelable) {
        try {
            if (mProgressDialog == null) {
                mProgressDialog = ProgressDialog.show(context.getContext(), title, msg);
                mProgressDialog.setCancelable(isCancelable);
            }

            if (!mProgressDialog.isShowing()) {
                mProgressDialog.show();
            }

        } catch (IllegalArgumentException ie) {
            ie.printStackTrace();
        } catch (RuntimeException re) {
            re.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
