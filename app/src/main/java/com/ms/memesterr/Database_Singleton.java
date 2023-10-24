package com.ms.memesterr;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database_Singleton{

    private static Database_Singleton singleton_Database = null;

    private DatabaseReference imageDatabase;

    public Database_Singleton(){
        imageDatabase = FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
    }

    public static synchronized Database_Singleton getInstance(){
        {
            if(singleton_Database== null){
                singleton_Database = new Database_Singleton();
            }
            return singleton_Database;
        }
    }

    public DatabaseReference getImageDatabase(){
        return imageDatabase;
    }

}
