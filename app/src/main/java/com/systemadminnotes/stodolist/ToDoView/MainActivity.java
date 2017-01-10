package com.systemadminnotes.stodolist.ToDoView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.design.widget.FloatingActionButton;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.systemadminnotes.stodolist.DetailView.DetailItem;
import com.systemadminnotes.stodolist.DividerItemDecoration;
import com.systemadminnotes.stodolist.IntroActivity;
import com.systemadminnotes.stodolist.R;
import com.systemadminnotes.stodolist.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    SQLiteHelper db = new SQLiteHelper(this, null, null, 1);
    List<ToDoItem> toDoDBList;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    MainAdapter mainAdapter;
    private List toDoItems = new ArrayList();

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private String mActivityTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);


            mDrawerList = (ListView)findViewById(R.id.navList);
            mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
            mActivityTitle = getTitle().toString();

            addDrawerItems();

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            toDoDBList = db.getAllToDos(); //retrieves everything in the database

            for (int i = 0; i < toDoDBList.size(); i++)
            {
                toDoItems.add(i, new ToDoItem(toDoDBList.get(i).getToDoItemName()));
            }

            recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(false);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL_LIST));
            mainAdapter = new MainAdapter(toDoItems, R.layout.todoitem, this);
            recyclerView.setAdapter(mainAdapter);

            ItemTouchHelper.Callback callback =
                    new SimpleItemTouchHelperCallback(MainActivity.this, mainAdapter);
            ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
            touchHelper.attachToRecyclerView(recyclerView);

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LayoutInflater li = LayoutInflater.from(MainActivity.this);
                    View promptsView = li.inflate(R.layout.prompts, null);

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.Theme_AppCompat_Light_Dialog_Alert);

                    alertDialogBuilder.setView(promptsView);
                    final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
                    alertDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Add",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            //package a new ArrayList for all the detailItems into a gson string
                                            ArrayList<String> tempArray = new ArrayList<String>();
                                            Gson gson = new Gson();
                                            String inputString= gson.toJson(tempArray);
                                            //add created ToDoItem to the database
                                            db.addProduct(new ToDoItem(userInput.getText().toString()));
                                            toDoDBList = db.getAllToDos();
                                            if (toDoDBList.size() > 0) {
                                                //add created ToDoItem to the RecyclerView
                                                toDoItems.add(toDoDBList.size() - 1, new ToDoItem(toDoDBList.get(toDoDBList.size() - 1).getToDoItemName()));
                                                //create empty ArrayList for DetailItems associated with this ToDoItem to avoid null call when clicked and create column in database
                                                db.addDetailItemName(new DetailItem(inputString, inputString));
                                                mainAdapter.notifyItemInserted(toDoDBList.size() - 1);
                                            }else{
                                                toDoItems.add(0, new ToDoItem(toDoDBList.get(0).getToDoItemName()));
                                                db.addDetailItemName(new DetailItem(inputString, inputString));
                                                mainAdapter.notifyItemInserted(toDoDBList.size());
                                            }
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });
                    alertDialogBuilder.show();
                }
            });
        }
        catch (Exception e){
            Log.i(TAG, e.toString());
        }
    }

    private void addDrawerItems() {
        String[] osArray = { "Author Details" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Mohanbabu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main , menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.details:
                startActivity(new Intent(this, ProfileActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
