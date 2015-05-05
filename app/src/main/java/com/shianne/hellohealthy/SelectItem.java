package com.shianne.hellohealthy;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.sql.SQLException;


public class SelectItem extends ActionBarActivity {

    DBAdapter db = new DBAdapter(this);
    Cursor c;
    SimpleCursorAdapter SCAdapter;
    private ListView drawerList;
    private ArrayAdapter<String> navAdapter;
    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private String activityTitle;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_item);

        try{
            db.openDatabase();
        }catch(SQLException e){
            e.printStackTrace();
        }

        c = db.getAllItems();

        if(c.getCount() == 0){
            initialItems();
            c = db.getAllItems();
        }
        displayAllItems();

        db.closeDatabase();

        drawerList = (ListView) findViewById(R.id.navList);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        activityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }


    public void onClickToAddItems(View view){

    }

    private void displayAllItems(){

        ListView listView = (ListView) findViewById(R.id.selectItemListView);
        String[] from = new String[]{db.KEY_ITEM};
        int[] to = new int[]{R.id.item};
        SCAdapter = new SimpleCursorAdapter(this, R.layout.activity_item_list_single_row, c, from, to, 0);
        // Inserts the single rows into the ListView section of Item Selection
        listView.setAdapter(SCAdapter);
    }

    private void initialItems(){

        String[] itemListArr = getResources().getStringArray(R.array.selectItemsList);
        for(int i = 0; i < itemListArr.length; i++){
            long id = db.createItem(itemListArr[i]);
        }
    }

    private void addDrawerItems(){

        String[] listArr = getResources().getStringArray(R.array.navItems);
        navAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listArr);
        drawerList.setAdapter(navAdapter);

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                drawerList.setItemChecked(position, true);
                switch(position){
                    case 0:
                        intent = new Intent(SelectItem.this, AddGoal.class);
                        startActivity(intent);
                        break;
                    case 1:
                        intent = new Intent(SelectItem.this, GoalsList.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent = new Intent(SelectItem.this, CompletedGoalsList.class);
                        startActivity(intent);
                        break;
                    case 3:
                        intent = new Intent(SelectItem.this, AddWeight.class);
                        startActivity(intent);
                        break;
                    case 4:
                        intent = new Intent(SelectItem.this, WeightHistory.class);
                        startActivity(intent);
                        break;
                    case 5:
                        intent = new Intent(SelectItem.this, SelectItem.class);
                        startActivity(intent);
                        break;
                    case 6:
                        intent = new Intent(SelectItem.this, ItemsHistory.class);
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void setupDrawer(){

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawerOpen,
                R.string.drawerClose){

            public void onDrawerOpened(View drawerView){

                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(R.string.app_name);
                invalidateOptionsMenu();
            }

            public void onDrawerClosed(View view){

                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(activityTitle);
                invalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState){
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_item, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item) || drawerToggle.onOptionsItemSelected(item);
    }
}
