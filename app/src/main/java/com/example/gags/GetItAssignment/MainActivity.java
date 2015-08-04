package com.example.gags.GetItAssignment;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends Activity {
    Bitmap bitmap;
    String template;
    ArrayList<ParsedItems> mItems;
    ImageView img;
    LinearLayout hsv;
    RelativeLayout rl;
    ListView mListView;
    ListViewAdapter adapter;
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("f_one.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mItems = new ArrayList<ParsedItems>();
       // hsv= (LinearLayout)findViewById(R.id.horizontalscrollview);
        rl = (RelativeLayout)findViewById(R.id.rl);
        ParsedItemsImage mSingleItemImage;
        ArrayList<ParsedItemsImage> mTempList=new ArrayList<ParsedItemsImage>();
        mListView = (ListView)findViewById(R.id.listView);
        try {
            JSONArray obj = new JSONArray(loadJSONFromAsset());
            for(int i=0;i<obj.length();i++)
            {
                ParsedItems mSingleItem;
                JSONObject view = obj.getJSONObject(i);
                String label = view.getString("label");
                JSONArray items = view.getJSONArray("items");
                template = view.getString("template");
                for(int j=0;j<items.length();j++)
                {
                    mSingleItemImage=new ParsedItemsImage();
                    JSONObject jo_inside = items.getJSONObject(j);
                    String url= jo_inside.getString("image");
                    if(url==null || url.isEmpty()){
                        url= jo_inside.getString("image_url");
                    }
                    String mLabel= jo_inside.getString("label");
                    String mWeburl= jo_inside.getString("web-url");
                    mSingleItemImage.setImage(url);
                    mSingleItemImage.setLabel(mLabel);
                    mSingleItemImage.setWeb_url(mWeburl);
                    mTempList.add(mSingleItemImage);

                }
                mSingleItem = new ParsedItems(label, template, mTempList);
                mItems.add(mSingleItem);
                mTempList.clear();
            }
            for(int i=0;i<mItems.size();i++)
            {
                ParsedItems mPI = mItems.get(i);
                ArrayList<ParsedItemsImage> mArrList = mPI.getItems();
                for(int j = 0;j<mArrList.size();j++)
                {
                    ParsedItemsImage mPII = mArrList.get(j);
                    String url = mPII.getImage();
                    new loadImage(i,j).execute(url);
                }

            }
            adapter = new ListViewAdapter(this,R.layout.row_layout,mItems);
            mListView.setAdapter(adapter);

        }
        catch(JSONException e)
        {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

        return super.onOptionsItemSelected(item);
    }

    private class loadImage extends AsyncTask<String, Void, Bitmap> {
        private int indexI, indexJ;

        public loadImage(int i,int j) {
            indexI=i;
            indexJ=j;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(params[0]).getContent());
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
            return bitmap;
        }
        protected void onPostExecute(Bitmap image) {
            if(image!=null)
            {
                ParsedItems mPI = mItems.get(indexI);
                ArrayList<ParsedItemsImage> mArrList = mPI.getItems();
                ParsedItemsImage mPII = mArrList.get(indexJ);
                mPII.setImage_bitmap(image);
            }
        }
    }

}
