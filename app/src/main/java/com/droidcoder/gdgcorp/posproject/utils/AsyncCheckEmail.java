package com.droidcoder.gdgcorp.posproject.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import com.droidcoder.gdgcorp.posproject.dataentity.FirebaseUser;
import com.droidcoder.gdgcorp.posproject.dataentity.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by DanLuciano on 1/10/2017.
 */

public class AsyncCheckEmail extends AsyncTask<String, Void, Boolean> {

    Context context;
    FirebaseDatabase fdb;
    DatabaseReference ref;
    User user;

    public AsyncCheckEmail(Context context, User user){
        this.context = context;
        this.user = user;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        fdb = FirebaseDatabase.getInstance();
        ref = fdb.getReference();
    }

    @Override
    protected Boolean doInBackground(final String... params) {
        final boolean[] existing = {false};

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                for(DataSnapshot d : dataSnapshot.getChildren()){

                    User user = d.getValue(User.class);
                    if(user.getEmail().equalsIgnoreCase(params[0])){
                        existing[0] = true;
                        break;
                    }

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ref.child("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(existing[0]){
                    //existing cancel saving
                    ((OnCheckingEmail) context).onFinish(true, user.getEmail(), user.getPasswordCode());

                }else{
                    //not existing continue saving

                    FirebaseDatabase fdb = FirebaseDatabase.getInstance();
                    DatabaseReference ref = fdb.getReference();

                    //create id then use this to save on offline database and fire base
                    DatabaseReference newId = ref.child("users").push();

                    FirebaseUser firebaseUser = new FirebaseUser(user.getEmail(), user.getPasswordCode(), user.getFirstName(), user.getLastName());

                    newId.setValue(firebaseUser);

                    user.setFirebaseId(newId.getKey());

                    DBHelper.getDaoSession().getUserDao().insert(user);

                    ((OnCheckingEmail) context).onFinish(false, user.getEmail(), user.getPasswordCode());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return existing[0];
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
    }

    public interface OnCheckingEmail{
        void onFinish(boolean emailExist, String email, String passCode);
    }
}
