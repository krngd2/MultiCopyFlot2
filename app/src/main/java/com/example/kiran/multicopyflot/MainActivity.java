package com.example.kiran.multicopyflot;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import com.mukesh.tinydb.TinyDB;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends Activity implements View.OnClickListener {
    ListView lv;
    EditText EditTxt;
    Button  update, delete, clear, copy;
    String clipData;
    Context mContext;
    CheckBox ActivateCheckBox;
    public ArrayList<String> items = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    private static final String SHARED_PREFS_NAME = "MY_SHARED_PREF";
    public String clipText;
    ArrayList newList;
    TinyDB tydb;
  /* ArrayHelper mArrayHelper = new ArrayHelper(this);
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, MainActivity.class));

        lv = (ListView) findViewById(R.id.list_view);
        EditTxt = (EditText) findViewById(R.id.editText);
        copy = (Button) findViewById(R.id.copy);
        update = (Button) findViewById(R.id.update);
        delete = (Button) findViewById(R.id.delete);
        clear = (Button) findViewById(R.id.clear);
        ActivateCheckBox = (CheckBox) findViewById(R.id.ActivateCheckBox);
        tydb = new TinyDB(this);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_multiple_choice, items );
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View v, int pos, long id) {
                EditTxt.setText(items .get(pos));
            }
        });

        copy.setOnClickListener(this);
        update.setOnClickListener(this);
        delete.setOnClickListener(this);
        clear.setOnClickListener(this);
        ActivateCheckBox.setOnClickListener(this);

        if (isMyServiceRunning(copyService.class )){
            getText();

            ActivateCheckBox.setChecked(true);
        }else { ActivateCheckBox.setChecked(false);}
        getSaveToSharedPre();
       /* if(adapter.isEmpty()){




        Toast.makeText(getApplicationContext(), "Started " + clipData,
                Toast.LENGTH_SHORT).show();
        }*/
    }
    public void saveToSharedPre(){
        tydb.putListString("ListItem",items);
        Toast.makeText(getApplicationContext(), "Saved Succefully ",
                Toast.LENGTH_SHORT).show();
    }
    public void getSaveToSharedPre(){
        newList = tydb.getListString("ListItem");
        adapter.addAll(newList);
    }
    @Override
    protected void onPause() {
        Toast.makeText(getApplicationContext(), "Destroyed ",
                Toast.LENGTH_SHORT).show();
        saveToSharedPre();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveToSharedPre();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void getText(){
        Intent in = getIntent();

        clipData = in.getStringExtra("text");
        if (clipData != null){
            items.add(clipData);
            Toast.makeText(getApplicationContext(), "Copied " + clipData,
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "Nothing to Add " ,
                    Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }
    public void CheckBox(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        if (checked){
            startService(new Intent(this,copyService.class));
        }else{
            stopService(new Intent(this,copyService.class));

        }
    }
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.update:
                update();
                break;
            case R.id.delete:
                delete();
                break;
            case R.id.clear:
                clear();
                break;
            case R.id.copy:
                copy();
                saveToSharedPre();
                break;
            case R.id.ActivateCheckBox:
                CheckBox(view);
                break;
        }
    }
    private void copy() {

        String name = EditTxt.getText().toString();
        if (!name.isEmpty() && name.length() > 0) {
            adapter.add(name);
            EditTxt.getText().clear();

            Toast.makeText(getApplicationContext(), "Copied " + name,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "!!Nothing to add ", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }
    private void update() {
        String name = EditTxt.getText().toString();
        //Get position of selected item
        int pos = lv.getCheckedItemPosition();

        if (!name.isEmpty() && name.length() > 0) {
            //Remove Current item
            adapter.remove(items .get(pos));
            //insert
            adapter.insert(name, pos);
            //refresh
            adapter.notifyDataSetChanged();

            Toast.makeText(getApplicationContext(), "Updated " + name,
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "!!Nothing to Update ", Toast.LENGTH_SHORT).show();
        }
    }
    private void delete() {
        int pos = lv.getCheckedItemPosition();

        if (pos > -1) {
            //remove
            adapter.remove(items .get(pos));


            Toast.makeText(getApplicationContext(), "Deleted ",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(),
                    "!!Nothing to Delete ", Toast.LENGTH_SHORT).show();
        }
        //refresh
        adapter.notifyDataSetChanged();
    }
    private void clear() {
        adapter.clear();
    }
    public void copiedText() {
        ClipboardManager clipboardManager = (ClipboardManager)
                getSystemService(Context.CLIPBOARD_SERVICE);
        ClipboardManager.OnPrimaryClipChangedListener
                mPrimaryChangeListener =
                new ClipboardManager.OnPrimaryClipChangedListener() {
                    public void onPrimaryClipChanged() {
                        ClipboardManager clipBoard = (ClipboardManager)
                                getSystemService(CLIPBOARD_SERVICE);

                        clipText = clipBoard.getPrimaryClip().getItemAt(0).getText().toString();

                        adapter.add(clipText);
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(),
                                "Text is copied to MultiCopy flot", Toast.LENGTH_SHORT).show();
                    }


                };
        clipboardManager.addPrimaryClipChangedListener(mPrimaryChangeListener);
    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}
