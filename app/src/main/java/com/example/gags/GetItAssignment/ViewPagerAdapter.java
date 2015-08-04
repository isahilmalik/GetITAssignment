package com.example.gags.GetItAssignment;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by gags on 01/08/15.
 */
public class ViewPagerAdapter extends PagerAdapter {
    ArrayList<ParsedItemsImage> mInnerItems;
    Context context;
    String template;
    private final WeakReference<View> ref;
    public ViewPagerAdapter(ArrayList<ParsedItemsImage> items,Context context, View view,String template)
    {
     mInnerItems = new ArrayList<ParsedItemsImage>(items);
        this.context = context;
        ref = new WeakReference<View>(view);
        this.template = template;
    }
    @Override
    public int getCount() {
        return mInnerItems.size();
        //return 6;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View collection;
        int width;
        RelativeLayout.LayoutParams params;
        ParsedItemsImage mParsedItemsImage = mInnerItems.get(position);
        String url=mParsedItemsImage.getImage();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        collection = inflater.inflate(R.layout.template3_imageview,container,false);
        ImageView imageView =(ImageView)collection.findViewById(R.id.imageView_template3);
       // ImageView imageView = new ImageView(container.getContext());
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        if(template.equals("product-template-3")){
            width = dm.widthPixels;
            params = new RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams.MATCH_PARENT);
        }
        else
        {
            width = (dm.widthPixels*9)/10;
            params = new RelativeLayout.LayoutParams(width, RelativeLayout.LayoutParams.MATCH_PARENT);
            imageView.setPadding(30,0,30,0);
        }
        imageView.setLayoutParams(params);
        if(mParsedItemsImage.getImage_bitmap() != null){
            imageView.setImageBitmap(mParsedItemsImage.getImage_bitmap());}
        else{
        new loadImage(imageView,container,position,width,params.height).execute(url);}

       // collection.addView(imageView, new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.MATCH_PARENT));
      //  TextView textView = new TextView(container.getContext());
       // ViewPager vp = (ViewPager)container;
        // TextView textView = new TextView(container.getContext());
       // textView.setText("mar ja");
                ((ViewPager) container).addView(collection);
        return collection;

    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    private class loadImage extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> mImgView;
        private final WeakReference<View> mView;
        private int position,width,height;
        //   private final WeakReference<LinearLayout> mLinearLayout;

        public loadImage(ImageView image, View view,int position,int width,int height) {
            mImgView = new WeakReference<ImageView>(image);
            mView = new WeakReference<View>(view);
            this.position=position;
            this.width = width;
            this.height = height;
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
                    if(imageView!=null) {
                      /*  int bwidth = image.getWidth();
                        int bheight = image.getHeight();
                        float scaleWidth = ((float) width) / bwidth;
                        float scaleHeight = ((float) height) / bheight;
                        // CREATE A MATRIX FOR THE MANIPULATION
                        Matrix matrix = new Matrix();
                        // RESIZE THE BIT MAP
                        matrix.postScale(scaleWidth, scaleHeight);
                        Bitmap resizedBitmap = Bitmap.createBitmap(
                                image, 0, 0, bwidth, bheight, matrix, false);*/
                        imageView.setImageBitmap(image);
                        ParsedItemsImage mPII = mInnerItems.get(position);
                        mPII.setImage_bitmap(image);
                    }
                }

            }
        }
    }
}
