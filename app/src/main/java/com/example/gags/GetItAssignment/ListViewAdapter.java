package com.example.gags.GetItAssignment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by gags on 28/07/15.
 */
public class ListViewAdapter extends ArrayAdapter<ParsedItems> {

    LinearLayout mainLinnerLayout;
    Context context;
    ArrayList<ParsedItems> mObject;
    ViewPagerAdapter adapter;
    private ProgressDialog progressDialog;
    public ListViewAdapter(Context context,int resource, ArrayList<ParsedItems> objects) {
        super(context,resource,objects);
        this.context = context;
        this.mObject = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder=null;
        ParsedItems items = mObject.get(position);
        String template = items.getTemplate();
        ParsedItemsImage mInnerItem;
        ArrayList<ParsedItemsImage> mInnerItems = items.getItems();
        LinearLayout innerLinearLayout=null;
        if(template.equals("product-template-1"))
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout, parent,false);
            mainLinnerLayout=(LinearLayout)convertView.findViewById(R.id.linear);
            for(int i=0;i<mInnerItems.size();i++)
            {
                mInnerItem =mInnerItems.get(i);
                View hsv_view;
                ImageView hsv_image;
                hsv_image = new ImageView(convertView.getContext());
                hsv_image.setPadding(10, 0, 10, 0);
                String url = mInnerItem.getImage();
                if(mInnerItem.getImage_bitmap()!=null)
                {
                    hsv_image.setImageBitmap(mInnerItem.getImage_bitmap());
                }
                else
                {
                    new loadImage(position,i,hsv_image,convertView).execute(url);
                }
                    // hsv_image.setAdjustViewBounds(true);
                    hsv_image.setScaleType(ImageView.ScaleType.FIT_XY);
                    DisplayMetrics dm = new DisplayMetrics();
                    dm = context.getResources().getDisplayMetrics();
                    int width = dm.widthPixels;
                    mainLinnerLayout.addView(hsv_image, new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT));
                    // mainLinnerLayout.addView(hsv_image);
                    break;
            }
        }
        else if(template.equals("product-template-3"))
        {
           if((Holder)convertView.getTag() == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout_template3, parent,false);
            ViewPager viewPager = (ViewPager)convertView.findViewById(R.id.pager);
                holder = new Holder();
            holder.viewPager = viewPager;
                holder.viewPager.setId(NotificationID.getID());
               holder.viewPagerAdapter = new ViewPagerAdapter(mInnerItems,convertView.getContext(),convertView,template);
               holder.textView = (TextView)convertView.findViewById(R.id.template3_textView);
            convertView.setTag(holder);}
            else
            {
                holder = (Holder)convertView.getTag();
            }
            holder.textView.setText(items.getLabel());
          //  pager.setPageMargin(getResources().getDisplayMetrics().widthPixels /-7);
           // holder.viewPager.setPageMargin(context.getResources().getDisplayMetrics().widthPixels/-7);
            holder.viewPager.setAdapter(holder.viewPagerAdapter);
            CirclePageIndicator titleIndicator = (CirclePageIndicator)convertView.findViewById(R.id.titles);
            titleIndicator.setViewPager(holder.viewPager);
         /*   LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_layout_template3, parent,false);
            ViewPager viewPager = (ViewPager)convertView.findViewById(R.id.pager);
            TextView textView = (TextView)convertView.findViewById(R.id.template3_textView);
            textView.setText("oyi!");
            ViewPagerAdapter adapter = new ViewPagerAdapter(mInnerItems,context,convertView);
            viewPager.setAdapter(adapter);
            //Bind the title indicator to the adapter
            TitlePageIndicator titleIndicator = (TitlePageIndicator)convertView.findViewById(R.id.titles);
            titleIndicator.setViewPager(viewPager);*/

        }
        else
        {
            if((Holder)convertView.getTag() == null){
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_layout_template2, parent,false);
                ViewPager viewPager = (ViewPager)convertView.findViewById(R.id.pager);
                holder = new Holder();
                holder.viewPager = viewPager;
                holder.viewPager.setId(NotificationID.getID());
                holder.viewPagerAdapter = new ViewPagerAdapter(mInnerItems,convertView.getContext(),convertView,template);
                holder.textView = (TextView)convertView.findViewById(R.id.template3_textView);
                convertView.setTag(holder);}
            else
            {
                holder = (Holder)convertView.getTag();
            }
            holder.textView.setText(items.getLabel());
            //  pager.setPageMargin(getResources().getDisplayMetrics().widthPixels /-7);
            holder.viewPager.setPageMargin((context.getResources().getDisplayMetrics().widthPixels*1)/-7);
            holder.viewPager.setOffscreenPageLimit(2);
            holder.viewPager.setAdapter(holder.viewPagerAdapter);
        }
        return convertView;
    }

    class Holder
    {
        TextView textView;
        ViewPager viewPager;
        ViewPagerAdapter viewPagerAdapter;
    }


    private class loadImage extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> mImgView;
        private final WeakReference<View> mView;
        int indexI,indexJ;
     //   private final WeakReference<LinearLayout> mLinearLayout;

        public loadImage(int position,int i,ImageView image, View view) {
            mImgView = new WeakReference<ImageView>(image);
            mView = new WeakReference<View>(view);
         //   mLinearLayout= new WeakReference<LinearLayout>(ll);
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
            if(image!= null){
                if(mImgView!=null)
                {
                    final ImageView imageView = (ImageView) mImgView.get();
                    if(imageView!=null)
                    imageView.setImageBitmap(image);
                    ParsedItems mPI = mObject.get(indexI);
                    ArrayList<ParsedItemsImage> mArrList = mPI.getItems();
                    ParsedItemsImage mPII = mArrList.get(indexJ);
                    mPII.setImage_bitmap(image);
                }

            }
        }
    }

   /* private class loadImage extends AsyncTask<String, Void, Bitmap> {
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
                ParsedItems mPI = mObject.get(indexI);
                ArrayList<ParsedItemsImage> mArrList = mPI.getItems();
                ParsedItemsImage mPII = mArrList.get(indexJ);
                mPII.setImage_bitmap(image);
            }
        }
    }*/

}
