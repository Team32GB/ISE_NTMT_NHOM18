package com.example.team32gb.jobit.Model.PostJob;

        import android.content.ClipData;

        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

        import java.util.List;

        import androidx.annotation.NonNull;

public class ModelPostJob {
    DatabaseReference databaseReference;
    public ModelPostJob() {
        databaseReference = FirebaseDatabase.getInstance().getReference().child("tinTuyenDungs");
    }
    public void SavePost(final String Uid, final DataPostJob dataPostJob){
        final String idJob = databaseReference.child(Uid).push().getKey();
        DatabaseReference dfCompany = FirebaseDatabase.getInstance().getReference().child("companys").child(Uid);
        dfCompany.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataPostJob.setNameCompany(dataSnapshot.child("name").getValue(String.class));
                dataPostJob.setProvince(dataSnapshot.child("province").getValue(String.class));
                dataPostJob.setIdCompany(Uid);
                dataPostJob.setIdJob(idJob);
                databaseReference.child(dataPostJob.getIdCompany()).child(idJob).setValue(dataPostJob);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public void savePostEdit(final String idJob, final String uid, final DataPostJob dataPostJob) {
        DatabaseReference dfCompany = FirebaseDatabase.getInstance().getReference().child("companys").child(uid);
        dfCompany.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataPostJob.setNameCompany(dataSnapshot.child("name").getValue(String.class));
                dataPostJob.setProvince(dataSnapshot.child("province").getValue(String.class));
                dataPostJob.setIdCompany(uid);
                dataPostJob.setIdJob(idJob);
                databaseReference.child(uid).child(idJob).setValue(dataPostJob);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
