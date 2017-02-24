package com.application.upnplink.home;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.application.upnplink.R;
import com.application.upnplink.filebrowser.FileBrowserActivity;
import com.application.upnplink.mediaPlayer.VideoPlayerActivity;
import com.application.upnplink.search.SearchUpnpActivity;
import com.application.upnplink.upnp.UpnpRegistryListener;
import com.application.upnplink.upnp.renderer.RendererMachine;
import com.application.upnplink.utils.PreferencesUtils;

import org.fourthline.cling.android.AndroidUpnpService;
import org.fourthline.cling.android.AndroidUpnpServiceImpl;
import org.fourthline.cling.android.FixedAndroidLogHandler;
import org.fourthline.cling.model.message.header.STAllHeader;
import org.fourthline.cling.model.meta.Device;
import org.fourthline.cling.model.types.UDN;

import java.util.UUID;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String defaultDeviceName;
    private String pathFileSelected;
    private String fileNameSelected;
    private SharedPreferences sharedPreferences;

    public static AndroidUpnpService upnpService;
    public static Device deviceSelected;


    private UpnpRegistryListener registryListener = new UpnpRegistryListener();
    private RendererMachine rendererMachine = null;


    private ServiceConnection serviceConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder service) {
            System.out.println("Connecté au service UPNP de cling");
            upnpService = (AndroidUpnpService) service;

            // Add a listener for device registration events
            upnpService.getRegistry().addListener(registryListener);
            // Now add all devices to the list we already know about
            for (Device device : upnpService.getRegistry().getDevices()) {
                System.out.println(device.getDisplayString());
                registryListener.deviceAdded(device);
            }
            // Broadcast a search message for all devices
            upnpService.getControlPoint().search(new STAllHeader());

            // TODO : try to connect to the prefered Upnp Server
        }

        public void onServiceDisconnected(ComponentName className) {
            upnpService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Fix the logging integration between java.util.logging and Android internal logging
        org.seamless.util.logging.LoggingUtil.resetRootHandler(
                new FixedAndroidLogHandler()
        );

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // init preferences
        sharedPreferences = getPreferences(Context.MODE_PRIVATE);

        managePreference();


        // This will start the UPnP service if it wasn't already started
        getApplicationContext().bindService(
                new Intent(this, AndroidUpnpServiceImpl.class),
                serviceConnection,
                Context.BIND_AUTO_CREATE
        );

        // Call from external activity for the Video Player
        Intent intent = getIntent();
        Uri data = intent.getData();
        if (data != null) {
            setFileBrowserPreference(data.getLastPathSegment(), data.toString());
            VideoPlayerActivity.showRemoteVideo(this, data.toString());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (upnpService != null) {
            upnpService.getRegistry().removeListener(registryListener);
        }
        // This will stop the UPnP service if nobody else is bound to it
        getApplicationContext().unbindService(serviceConnection);
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
            System.out.println("settings : affichage des DISPLAY");
            for (Device device : registryListener.ITEMS) {
                System.out.println(device.getDisplayString());
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_browser) {
            Intent intent = new Intent(this, FileBrowserActivity.class);
            startActivity(intent);
            startActivityForResult(intent,FileBrowserActivity.REQUEST_FILEBROWSER);
        } else if (id == R.id.nav_player) {
            if (pathFileSelected != null) {
                String url = pathFileSelected;
                VideoPlayerActivity.showRemoteVideo(this, url);
            } else {
                Toast toast = Toast.makeText(this,  getString(R.string.no_media_file_selected), Toast.LENGTH_SHORT);
                toast.show();
            }
        } else if (id == R.id.nav_search_upnp) {
            Intent intent = new Intent(this, SearchUpnpActivity.class);
            startActivity(intent);
            startActivityForResult(intent, SearchUpnpActivity.REQUEST_DEVICE_UPNP);

        } else if (id == R.id.nav_server_upnp) {
            if (rendererMachine == null) {
                rendererMachine = new RendererMachine(upnpService);
                TextView tv = (TextView) findViewById(R.id.serverStarted);
                tv.setText(getString(R.string.server_connected));
            }
        } else if (id == R.id.nav_settings) {

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
        if (requestCode == SearchUpnpActivity.REQUEST_DEVICE_UPNP){
            TextView connectedDeviceName = (TextView) findViewById(R.id.connectedDeviceName);
            if (resultCode == RESULT_OK) {
                System.out.println("retour : "+ data.getStringExtra("DeviceName"));

                if (data.getStringExtra("DeviceName") != null) {
                    connectedDeviceName.setText(getString(R.string.device_connected) + UpnpRegistryListener.SelectedDevice.getDisplayString());
                    PreferencesUtils.putPreference(sharedPreferences,"DEFAULT_DEVICE_NAME",data.getStringExtra(UpnpRegistryListener.SelectedDevice.getIdentity().getUdn().getIdentifierString()));
                } else {
                    connectedDeviceName.setText(getString(R.string.no_device_connected));
                }
            } else {
                connectedDeviceName.setText(getString(R.string.no_device_connected));
            }
        }
        else if (requestCode == FileBrowserActivity.REQUEST_FILEBROWSER){
            if (resultCode == RESULT_OK) {
                setFileBrowserPreference(data.getStringExtra("FileName"), data.getStringExtra("PathFile"));
            }
        }
    }

    /**
     * Preference management
      */
    private void managePreference() {
        TextView tv;

        // Last Device UPNP
        defaultDeviceName = PreferencesUtils.getPreference(sharedPreferences,"DEFAULT_DEVICE_NAME");
        if (defaultDeviceName != null) {
            tv = (TextView) findViewById(R.id.connectedDeviceName);
            tv.setText(getString(R.string.device_connected) + defaultDeviceName);
        }

        // Last Movie File
        pathFileSelected  = PreferencesUtils.getPreference(sharedPreferences,"DEFAULT_PATH_FILE");
        fileNameSelected  = PreferencesUtils.getPreference(sharedPreferences,"DEFAULT_FILE_NAME");
        if (fileNameSelected != null) {
            tv = (TextView) findViewById(R.id.fileSelected);
            tv.setText(fileNameSelected);
        }
    }

    private void setFileBrowserPreference(String fileName, String pathFile) {
        fileNameSelected = fileName;
        pathFileSelected = pathFile;
        PreferencesUtils.putPreference(sharedPreferences,"DEFAULT_PATH_FILE",pathFileSelected );
        PreferencesUtils.putPreference(sharedPreferences,"DEFAULT_FILE_NAME",fileNameSelected  );
        TextView tv = (TextView) findViewById(R.id.fileSelected);
        tv.setText(fileNameSelected );

    }
}
