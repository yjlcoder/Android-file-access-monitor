package website.jace.fileaccessmonitor;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import website.jace.fileaccessmonitor.dataItem.DataItem;

import java.util.*;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private class Target {
        final static int KERNEL = 1;
        final static int JNI = 2;
    }

    private class Type {
        final static int APPLICATION = 1;
        final static int TYPE = 2;
    }

    private ExpandableListView expandableListView;
    private PackageManager pm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        expandableListView = findViewById(R.id.expandableListView);
        pm = getApplicationContext().getPackageManager();
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                Snackbar.make(view, "Building file access data", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Data.getInstance().startBuild();
                setApplicationNames(Data.getInstance().kernDataItems);
                setApplicationNames(Data.getInstance().jniDataItems);
                refreshData(Target.KERNEL, Type.APPLICATION);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_application_kernel) {
            refreshData(Target.KERNEL, Type.APPLICATION);
        } else if (id == R.id.nav_application_jni) {
            refreshData(Target.JNI, Type.APPLICATION);
        } else if (id == R.id.nav_type_kernel) {
            refreshData(Target.KERNEL, Type.TYPE);
        } else if (id == R.id.nav_type_jni) {
            refreshData(Target.JNI, Type.TYPE);
        } else if (id == R.id.edit_rules) {
            Intent intent = new Intent(MainActivity.this, RulesActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void refreshData(int target, int type) {
        List<DataItem> list = null;
        if (target == Target.KERNEL) list = Data.getInstance().kernDataItems;
        else list = Data.getInstance().jniDataItems;

        if (type == Type.APPLICATION) {
            Set<String> hashSet = new HashSet<>();

            for (DataItem item : list)
                hashSet.add(item.getApplicationName());

            List<String> titles = new ArrayList<>(hashSet);
            Map<String, List<String>> map = new HashMap<>();
            for (String title : titles)
                map.put(title, new ArrayList<>());
            for (DataItem item : list)
                map.get(item.getApplicationName()).add("[" + item.getTimeString() + "] <" + item.getFileType() + "> " + item.getAccessPath());

            this.expandableListView.setAdapter(new MyExpandableListAdapter(MainActivity.this, titles, map));
        } else if (type == Type.TYPE) {
            Set<String> hashSet = new HashSet<>();

            for (DataItem item : list) hashSet.add(item.getFileType());

            List<String> titles = new ArrayList<>(hashSet);
            Map<String, List<String>> map = new HashMap<>();
            for (String title : titles) map.put(title, new ArrayList<>());
            for (DataItem item : list)
                map.get(item.getFileType()).add("[" + item.getTimeString() + "] <" + item.getApplicationName() + "> " + item.getAccessPath());

            this.expandableListView.setAdapter(new MyExpandableListAdapter(MainActivity.this, titles, map));
        }
    }

    private void setApplicationNames(List<DataItem> dataItems) {
        for (DataItem dataItem : dataItems) {
            String packageName = dataItem.getPackageName();
            String applicationName = Data.getInstance().packageApplicationNameMap.get(packageName);
            if (applicationName == null) {
                ApplicationInfo ai = null;
                try {
                    ai = pm.getApplicationInfo(packageName, 0);
                } catch (PackageManager.NameNotFoundException e) {
                }
                applicationName = ai != null ? (String) pm.getApplicationLabel(ai) : "Unknown";
                Data.getInstance().packageApplicationNameMap.put(packageName, applicationName);
            }
            dataItem.setApplicationName(applicationName);
        }
    }
}
