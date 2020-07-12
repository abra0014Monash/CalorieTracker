package com.example.caloriestracker.Fragment;

import android.app.Fragment;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caloriestracker.DailySteps;
import com.example.caloriestracker.DailyStepsDatabase;
import com.example.caloriestracker.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class StepsFragment  extends Fragment {

    DailyStepsDatabase db = null;
    EditText editText = null;
    TextView textView_insert = null;
    ListView listView_read = null;
    EditText et_editSteps = null;
    View view;
    Date currentTime;
    TextView textView_update = null;
    DailySteps record=null;
    int SelectedItem;
    List<String> recordList = new ArrayList<>();
    ArrayAdapter<String> arrayAdapter ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Set Variables and listener
        view = inflater.inflate(R.layout.fragment_steps, container, false);

        db = Room.databaseBuilder(getActivity().getApplicationContext(),
                DailyStepsDatabase.class, "DailyStepsDatabase")
                .fallbackToDestructiveMigration()
                .build();
        Button addButton = (Button)view.findViewById(R.id.addButton);
        editText = (EditText) view.findViewById(R.id.editText);
        textView_insert = (TextView) view.findViewById(R.id.textView);
        Button readButton = (Button) view.findViewById(R.id.readButton);
        listView_read = (ListView) view.findViewById(R.id.ListView_read);
        et_editSteps = (EditText)view.findViewById(R.id.et_editSteps) ;
        textView_update = (TextView) view.findViewById(R.id.textView_update);
        Button editButton = (Button)view.findViewById(R.id.editButton);

        currentTime = Calendar.getInstance().getTime();

        addButton.setOnClickListener(new View.OnClickListener() {
            //including onClick() method
            public void onClick(View v) {
                InsertDatabase addDatabase = new InsertDatabase();
                addDatabase.execute();
            }
        });

        readButton.setOnClickListener(new View.OnClickListener() {
            //including onClick() method
            public void onClick(View v) {
                ReadDatabase readDatabase = new ReadDatabase();
                readDatabase.execute();
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            //including onClick() method
            public void onClick(View v) {
                int StepsCount = Integer.valueOf(et_editSteps.getText().toString());
                System.out.println("+++++++++++++++++steps count+++++++++++ "+StepsCount + "id: " + SelectedItem);
                UpdateDatabase updateDatabase = new UpdateDatabase();
                updateDatabase.execute(SelectedItem, StepsCount);
            }
        });
        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, recordList);
        listView_read.setAdapter(arrayAdapter);
        listView_read.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> arg0, View arg1,int arg2, long arg3)
            {
                String str = ((TextView) arg1).getText().toString();
                String[] details= str.split(" ");
                Toast.makeText(getContext(),str, Toast.LENGTH_LONG).show();
                //et_editSteps.setText(details[0]);

                SelectedItem = Integer.valueOf(details[0]);


            }
        });

        return view;
    }

    private class InsertDatabase extends AsyncTask<Void, Void, String> {
    @Override
    protected String doInBackground(Void... params) {
        if (!(editText.getText().toString().isEmpty())) {
            DailySteps dailySteps = new DailySteps(Integer.parseInt(editText.getText().toString()), currentTime.toString());
            long id = db.dailyStepsDao().insert(dailySteps);

            return (id + "  steps: " + editText.getText().toString() + "    timestamp: " + currentTime.toString());
        }
        else
            return "nothing to add !!! please insert";
        }
        @Override
        protected void onPostExecute(String details) {
            textView_insert.setText("Added Record: " + details);
        }
     }
private class ReadDatabase extends AsyncTask<Void, Void, List<DailySteps>> {
    @Override
    protected List<DailySteps> doInBackground(Void... params) {
        List<DailySteps> stepsList = db.dailyStepsDao().getAll();
      //  if (!(stepsList.isEmpty() || stepsList == null))

            return stepsList;

    }
    @Override
    protected void onPostExecute(List<DailySteps> stepsList)
    {
        ShowListView(stepsList);
    }
}

    public void ShowListView(List<DailySteps> stepsList){

        recordList.clear();
        if (stepsList != null) {
            for(DailySteps item : stepsList) {

             String record = (item.getId() + "  steps:" +
                     item.getStepsTaken() + "    date:" + item.getDate() + "\n");


             recordList.add(record);
            }
        }
        arrayAdapter.notifyDataSetChanged();
    }

    private class UpdateDatabase extends AsyncTask<Object, Void, String> {
        @Override protected String doInBackground(Object... params) {

                record = db.dailyStepsDao().findByID((int)params[0]);
                record.setStepsTaken((int)params[1]);

            if (record!=null) {
                db.dailyStepsDao().updateSteps(record);

            }
            return "";
        }
        @Override
        protected void onPostExecute(String details) {
            textView_update.setText("Updated details: "+ details);

            ReadDatabase readDatabase = new ReadDatabase();
            readDatabase.execute();
        }
    }


}
