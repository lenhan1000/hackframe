package com.example.silc.hackathonframework.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.example.silc.hackathonframework.R;

import java.util.ArrayList;


public class SingleChoiceDiaglogFragment  extends DialogFragment{
    private String title;
    private ArrayList<String> listText;
    private ListView lv;
    private SearchView sv;
    private Button btn;
    ArrayAdapter<String> adapter;


    public interface NoticeDialogListener{
        public void onDialogTextSelect(DialogFragment dialog);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_single_choice_dialog, null);

        title = getArguments().getString("title");
        listText = getArguments().getStringArrayList("list");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        builder.setTitle(title);

        //BUTTON,LISTVIEW,SEARCHVIEW INITIALIZATIONS
        lv= view.findViewById(R.id.listView1);
        sv= view.findViewById(R.id.searchView1);
        btn= view.findViewById(R.id.dismiss);

        //CREATE AND SET ADAPTER TO LISTVIEW
        adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,listText);
        lv.setAdapter(adapter);

        //SEARCH
        sv.setQueryHint("Search..");
        sv.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String txt) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onQueryTextChange(String txt) {
                // TODO Auto-generated method stub

                adapter.getFilter().filter(txt);
                return false;
            }
        });

        //BUTTON
        btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                dismiss();
            }
        });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
