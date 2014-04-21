package com.ageelg.android.examples.HelloTextViewImageGetterThingie;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;
import android.util.Log;
import android.widget.TextView;

public class TestImageGetter extends Activity implements ImageGetter {
    private final static String TAG = "TestImageGetter";
    private TextView mTv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       
       
                
            
        String source =  "<p> Ã—»… textview »«·’Ê— <p>"
        		
        		+"<p><img alt=\"»ÿÊ·… «·« Õ«œ «·”⁄ÊœÌ ··„’‰›Ì‰ ·· ‰” «·√—÷Ì\" id=\"imgID_14564\""
				+"src=\"http://cdn29.elitedaily.com/wp-content/uploads/2014/04/zp6mpfoikd6l7mgevdjb.jpg\" style=\"width:" 
				+"375px; height: 249px; margin: 5px;\" /><img alt=\"»ÿÊ·… «·« Õ«œ" 
				+"«·”⁄ÊœÌ ··„’‰›Ì‰ ·· ‰” «·√—÷Ì\" id=\"imgID_14565\" src=\"http://cdn29.elitedaily.com/wp-content/uploads/2014/04/nn6pauri634w0upffij5.jpg\" style=\"width: 375px;" 
				+"height: 262px; margin: 5px;\" /></p>"
        		+"<h1> ﬂ·«„ «÷«›Ì Ê  ⁄«·Ìﬁ ⁄·Ï «·’Ê— </h1>" ;
        
      
        
        Spanned spanned = Html.fromHtml(source, this, null);
        mTv = (TextView) findViewById(R.id.tvnotwebview);
        Typeface myfont = Typeface.createFromAsset(getAssets(), "f2.ttf");
        mTv.setTypeface(myfont, Typeface.BOLD);
        mTv.setText(spanned);
    }

    @Override
    public Drawable getDrawable(String source) {
        LevelListDrawable d = new LevelListDrawable();
        Drawable empty = getResources().getDrawable(R.drawable.ic_launcher);
        d.addLevel(0, 0, empty);
        d.setBounds(0, 0, empty.getIntrinsicWidth(), empty.getIntrinsicHeight());

        new LoadImage().execute(source, d);

        return d;
    }
    
   

    class LoadImage extends AsyncTask<Object, Void, Bitmap> {

        private LevelListDrawable mDrawable;

        @Override
        protected Bitmap doInBackground(Object... params) {
            String source = (String) params[0];
            mDrawable = (LevelListDrawable) params[1];
            Log.d(TAG, "doInBackground " + source);
            try {
                InputStream is = new URL(source).openStream();
                return BitmapFactory.decodeStream(is);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d(TAG, "onPostExecute drawable " + mDrawable);
            Log.d(TAG, "onPostExecute bitmap " + bitmap);
            if (bitmap != null) {
                BitmapDrawable d = new BitmapDrawable(bitmap);
                mDrawable.addLevel(1, 1, d);
                mDrawable.setBounds(0, 0, bitmap.getWidth(), bitmap.getHeight());
                mDrawable.setLevel(1);
                // i don't know yet a better way to refresh TextView
                // mTv.invalidate() doesn't work as expected
                CharSequence t = mTv.getText();
                mTv.setText(t);
            }
        }
    }
}