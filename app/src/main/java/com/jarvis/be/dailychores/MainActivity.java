package com.jarvis.be.dailychores;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jarvis.be.dailychores.com.jarvis.be.dailychores.model.ChoresModel;
import com.jarvis.be.dailychores.com.jarvis.be.dailychores.model.EventDbHandler;
import com.jarvis.be.dailychores.recyclerview.RecyclerViewBaseActivity;
import com.jarvis.be.dailychores.recyclerview.RowController;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends RecyclerViewBaseActivity {
    static ArrayList<ChoresModel> choresList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




        findViewById(R.id.new_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NewChoresActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAllBooksFromList();

    }

    void getAllBooksFromList(){
        EventDbHandler myDb = new EventDbHandler(getApplicationContext());
        choresList = myDb.getAllChores();

        setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        setAdapter(new ChoresAdapter(choresList));

    }





    class ChoresAdapter extends RecyclerView.Adapter<RowController> {

        private List<ChoresModel> feedItemList;
        public ChoresAdapter(ArrayList<ChoresModel> booksList) {
            this.feedItemList = booksList;
        }

        @Override
        public RowController onCreateViewHolder(ViewGroup parent, int viewType) {
            return(new RowController(MainActivity.this, getLayoutInflater()
                    .inflate(R.layout.row_list, parent, false)));
        }

        @Override
        public void onBindViewHolder(RowController holder, int position) {
            ChoresModel tempChoresModel = feedItemList.get(position);
            holder.bindModel(tempChoresModel);
        }

        @Override
        public int getItemCount() {
            return(feedItemList.size());
        }
    }
}
