package comin.chaten.statens.caugfitten.straighttoend.its.fragments.contacts;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import comin.chaten.statens.caugfitten.R;
import comin.chaten.statens.caugfitten.straighttoend.verify.LoginActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class SwitchUser extends Fragment  {

    private View fragmentViewers;

    SQLiteDatabase sqld;

    List<String> list;

    public SwitchUser() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentViewers = inflater.inflate(R.layout.fragment_switch_user, container, false);

        list = new ArrayList<String>();

        list.add("Select User");

        sqld = getActivity().openOrCreateDatabase("Chaters", MODE_PRIVATE, null);

        final Spinner spinns = (Spinner) fragmentViewers.findViewById(R.id.spinner);

        Cursor c = sqld.rawQuery("select * from signupers", null);

        if (c.getCount() > 0) {
            c.moveToNext();

            do {
                String str1 = c.getString(0);

                String str2 = c.getString(1);

                list.add(str1);

            }
            while (c.moveToNext());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //ArrayAdapter<?> adapter = ArrayAdapter.createFromResource(getActivity().getBaseContext(), R.array.spinnerItems, android.R.layout.simple_spinner_item);
        spinns.setAdapter(dataAdapter);

        spinns.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String str = adapterView.getItemAtPosition(i).toString();

                if(i == 0){

                }
                else {
                    Intent ijk = new Intent(getActivity(), LoginActivity.class);
                    ijk.putExtra("nameofuser" , str);
                    startActivity(ijk);
                    Toast.makeText(getActivity(), spinns.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return fragmentViewers;
    }
}
