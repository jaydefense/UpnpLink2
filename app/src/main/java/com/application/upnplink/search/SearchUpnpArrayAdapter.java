package com.application.upnplink.search;

import android.graphics.drawable.Drawable;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.upnplink.R;

import java.util.List;
import org.fourthline.cling.model.meta.Device;

/**
 * Created by jperraudeau on 17/02/2017.
 */

public class SearchUpnpArrayAdapter extends ArrayAdapter<Device> {
    private Context c;
    private int id;
    private List<Device> items;

    public SearchUpnpArrayAdapter(Context context, int viewResourceId,
                                    List<Device> objects) {
        super(context, viewResourceId, objects);
        c = context;
        id = viewResourceId;
        items = objects;
    }

    public Device getItem(int i)
    {
        return items.get(i);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(id, null);
        }

        /* create a new view of my layout and inflate it in the row */
        //convertView = ( RelativeLayout ) inflater.inflate( resource, null );

        final Device o = items.get(position);
        if (o != null) {
            TextView t1 = (TextView) v.findViewById(R.id.upnpName);
            t1.setText(o.getDisplayString());
            /* Take the ImageView from layout and set image */
            //ImageView image = (ImageView) v.findViewById(R.id.upnpIcon);
            //String uri = "drawable/ic_movie_black_24dp";// + o.getImage();
            //int imageResource = c.getResources().getIdentifier(uri, null, c.getPackageName());
            //Drawable drawable = c.getResources().getDrawable(imageResource);
            //image.setImageDrawable(drawable );
        }
        return v;
    }

}
