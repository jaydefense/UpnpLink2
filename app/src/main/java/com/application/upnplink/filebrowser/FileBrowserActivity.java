package com.application.upnplink.filebrowser;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.application.upnplink.R;

import java.io.File;
import java.sql.Date;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FileBrowserActivity extends AppCompatActivity  implements ItemClickListener {

    public static final int REQUEST_FILEBROWSER = 2;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FileBrowserRecyclerViewAdapter mFileBrowserRecyclerViewAdapter;
    private File currentDir;
    private List<FileItem> fileItems = new ArrayList<FileItem>();
    private final String ROOT =  Environment.getExternalStorageDirectory().getPath();

    private final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_file_browser);
        //RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerFileBrowser);

        mRecyclerView = new RecyclerView(this);
        setContentView(mRecyclerView);

        mFileBrowserRecyclerViewAdapter = new FileBrowserRecyclerViewAdapter(fileItems, this);
        mRecyclerView.setAdapter(mFileBrowserRecyclerViewAdapter);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentDir = new File(ROOT);

        // Granted access to storage
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            fill(currentDir);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    fill(currentDir);
                } else {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void fill(File f)
    {
        File[]dirs = f.listFiles();
        this.setTitle("Current Dir: "+f.getName());
        List<FileItem>dir = new ArrayList<FileItem>();
        List<FileItem>fls = new ArrayList<FileItem>();
        try{
            for(File ff: dirs)
            {
                Date lastModDate = new Date(ff.lastModified());
                DateFormat formater = DateFormat.getDateTimeInstance();
                String date_modify = formater.format(lastModDate);
                if(ff.isDirectory()){


                    File[] fbuf = ff.listFiles();
                    int buf = 0;
                    if(fbuf != null){
                        buf = fbuf.length;
                    }
                    else buf = 0;
                    String num_item = String.valueOf(buf);
                    if(buf == 0) num_item = num_item + " item";
                    else num_item = num_item + " items";

                    //String formated = lastModDate.toString();
                    dir.add(new FileItem(ff.getName(),num_item,date_modify,ff.getAbsolutePath(),"directory_icon"));
                }
                else
                {
                    fls.add(new FileItem(ff.getName(),ff.length() + " Byte", date_modify, ff.getAbsolutePath(),"file_icon"));
                }
            }
        }catch(Exception e)
        {

        }
        Collections.sort(dir);
        Collections.sort(fls);
        dir.addAll(fls);
        if(!f.getName().equalsIgnoreCase(ROOT))
            dir.add(0,new FileItem("..","Parent Directory","",f.getParent(),"directory_up"));
        //adapter = new FileExplorerArrayAdapter(FileExplorerActivity.this, R.layout.activity_file_explorer,dir);
        mFileBrowserRecyclerViewAdapter.refill(dir);
        //this.setListAdapter(adapter);
    }

    @Override
    public void onClick( View v, int position) {
        final FileItem item = fileItems.get(position);
        if(item.getImage().equalsIgnoreCase("directory_icon")||item.getImage().equalsIgnoreCase("directory_up")){
            currentDir = new File(item.getPath());
            fill(currentDir);
        }
        else
        {
            onFileClick(item);
        }
    }

    private void onFileClick(FileItem fileItem)
    {
        //Toast.makeText(this, "Folder Clicked: "+ currentDir, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();
        intent.putExtra("Path",currentDir.toString());
        intent.putExtra("FileName",fileItem.getName());
        setResult(RESULT_OK, intent);
        finish();
    }

//*******************************************************************************************************************************

    public class FileBrowserRecyclerViewAdapter extends RecyclerView.Adapter<FileBrowserRecyclerViewAdapter.ViewHolder> {

        private final List<FileItem> mValues;
        private ItemClickListener itemClickListener;

        public FileBrowserRecyclerViewAdapter(List<FileItem> values, ItemClickListener itemClickListener) {
            mValues = values;
            this.itemClickListener = itemClickListener;
            //mValues.add(new FileItem("a","b","c","d","directory_icon"));
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.activity_file_browser_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mTextView1.setText( mValues.get(position).getName());
            holder.mTextView2.setText( mValues.get(position).getData());
            holder.mTextViewDate1.setText( mValues.get(position).getDate());
            //String uri = "drawable/" +  mValues.get(position).getImage();
            //int imageResource = getResources().getIdentifier(uri, null, getPackageName());
            //Drawable image = getResources().getDrawable(imageResource);
            //holder.mImageView.setImageDrawable(image);
            if (mValues.get(position).getImage().equals("directory_icon")) {
                holder.mImageView.setImageResource(R.mipmap.ic_directory_icon);
            } else if (mValues.get(position).getImage().equals("file_icon")) {
                holder.mImageView.setImageResource(R.mipmap.ic_file_icon);
            }
           // holder.mView.setOnClickListener(new View.OnClickListener() {            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public void add(FileItem item, int position) {
            mValues.add(position, item); // on insère le nouvel objet dans notre       liste d'article lié à l'adapter
            notifyItemInserted(position); // on notifie à l'adapter ce changement
        }

        public void refill(List<FileItem> items) {
            mValues.clear();
            mValues.addAll(items); //
            notifyDataSetChanged();
        }

        //****************************************************************************************************************
        public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            public final View mView;
            public final TextView mTextView1;
            public final TextView mTextView2;
            public final TextView mTextViewDate1;
            public final ImageView mImageView;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mTextView1 = (TextView) view.findViewById(R.id.TextView1);
                mTextView2 = (TextView) view.findViewById(R.id.TextView2);
                mTextViewDate1 = (TextView) view.findViewById(R.id.TextViewDate1);
                mImageView = (ImageView) view.findViewById(R.id.fd_Icon10);
                mView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                if (itemClickListener != null) itemClickListener.onClick(view, getAdapterPosition());
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView1.getText() + "'";
            }
        }
    }
}

interface ItemClickListener {
    public void onClick(View v, int position);
}