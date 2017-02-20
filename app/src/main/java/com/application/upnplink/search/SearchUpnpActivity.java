package com.application.upnplink.search;

import android.os.Bundle;
import android.content.Intent;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;


import com.application.upnplink.R;
import com.application.upnplink.upnp.UpnpRegistryListener;
import org.fourthline.cling.model.meta.Device;

public class SearchUpnpActivity extends AppCompatActivity implements OnItemClickListener {

    private SearchUpnpArrayAdapter adapter;
    public static final int REQUEST_DEVICE_UPNP = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_upnp);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        adapter = new SearchUpnpArrayAdapter(this, R.layout.search_upnp_item, UpnpRegistryListener.ITEMS);
        ListView listView = (ListView) findViewById(R.id.list_search_upnp);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    //protected void onItemClick(ListView l, View v, int position, long id) {
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //super.onItemClick(l, v, position, id);
        Device device = (Device ) parent.getAdapter().getItem(position);
        Intent intent = new Intent();
        UpnpRegistryListener.SelectedDevice = device;
        intent.putExtra("DeviceUUID", device.getIdentity().getUdn().getIdentifierString());
        intent.putExtra("DeviceName", device.getDisplayString());
        setResult(RESULT_OK, intent);
        finish();
    }

}
