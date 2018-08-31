package com.example.silc.hackathonframework.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

import com.example.silc.hackathonframework.R;

import java.util.ArrayList;


public class SingleChoiceDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener{
    private String TAG = "SingleChoiceDialogFragment";
    private String title;
    private ArrayList<String> listText;
    private ListView lv;
    private SearchView sv;
    public NoticeDialogListener notice;
    ArrayAdapter<String> adapter;


    public interface NoticeDialogListener{
        void onDialogTextSelect(int id, String dialog);
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try{
            this.notice = (NoticeDialogListener) activity;
        }catch (final ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement NoticeDialogListener");

        }
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

        //CREATE AND SET ADAPTER TO LISTVIEW
        adapter=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,listText);
        lv.setAdapter(adapter);

        //SEARCH
        sv.setQueryHint("Search..");
        sv.setOnQueryTextListener(new OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String txt) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String txt) {
                // TODO Auto-generated method stub

                adapter.getFilter().filter(txt);
                return false;
            }
        });

        lv.setOnItemClickListener(this);

        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id){
        String item = parent.getItemAtPosition(position).toString();
        this.notice.onDialogTextSelect(listText.indexOf(item), item);
        dismiss();
    }
}
