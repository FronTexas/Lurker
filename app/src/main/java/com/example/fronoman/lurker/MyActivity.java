package com.example.fronoman.lurker;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nhaarman.listviewanimations.appearance.simple.SwingBottomInAnimationAdapter;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.SwipeDismissAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MyActivity extends Activity implements OnDismissCallback {

    ArrayList<String> titles;

    ArrayList<String> imageURLS;
    public ArrayList<Bitmap> bs;

    ListView lvPostCard;
    GoogleCardsAdapter mGoogleCardsAdapter;

    SweetAlertDialog pDialog;
    MyActivity m;

    int window_height;
    int window_width;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m = this;

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        window_width = size.x;
        window_height = size.y;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_googlecards);

        titles = new ArrayList<String>();
        imageURLS = new ArrayList<String>();

        bs = new ArrayList<Bitmap>();

        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#3598db"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        lvPostCard = (ListView) findViewById(R.id.activity_googlecards_listview);

        mGoogleCardsAdapter = new GoogleCardsAdapter(m);
        SwingBottomInAnimationAdapter swingBottomInAnimationAdapter = new SwingBottomInAnimationAdapter(new SwipeDismissAdapter(mGoogleCardsAdapter, m));
        swingBottomInAnimationAdapter.setAbsListView(lvPostCard);

        swingBottomInAnimationAdapter.getViewAnimator().setInitialDelayMillis(300);

        lvPostCard.setAdapter(swingBottomInAnimationAdapter);

        Connection c = (Connection) new Connection().execute();


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDismiss(@NonNull ViewGroup viewGroup, @NonNull int[] ints) {

    }

    public class Connection extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            try {

                URL url = new URL("http://www.reddit.com/r/funny/.json");
                URLConnection urlCon = url.openConnection();
                BufferedReader bfr = new BufferedReader(new InputStreamReader(urlCon.getInputStream()));
                String line = "";
                StringBuffer json_string = new StringBuffer();

                while ((line = bfr.readLine()) != null) {
                    json_string.append(line);
                }

                JSONObject obj = new JSONObject(json_string.toString());
                JSONObject data = obj.getJSONObject("data");

                /*Array that contains the posts*/
                JSONArray children = data.getJSONArray("children");
                String title = "";
                String imageURLString = "";
                for (int i = 1; i < children.length(); i++) {
                    title = children.getJSONObject(i).getJSONObject("data").getString("title");
                    imageURLString = children.getJSONObject(i).getJSONObject("data").getString("url");
                    if (!imageURLString.contains(".gif")) {
                        titles.add(title);
                        imageURLS.add(imageURLString);
                        ImageView iv = new ImageView(getApplicationContext());

                        // Getting the bitmap from the image url
                        try {
                            URL image_url = new URL(imageURLString);
                            HttpURLConnection connection = (HttpURLConnection) image_url.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream input = connection.getInputStream();
                            Bitmap myBitmap = BitmapFactory.decodeStream(input);
                            bs.add(myBitmap);
                        } catch (IOException e) {
                            Log.d("Exception", "e = " + e.toString());
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return titles;
        }

        @Override
        protected void onPostExecute(Object o) {
            pDialog.dismissWithAnimation();
            lvPostCard.setVisibility(View.VISIBLE);
            mGoogleCardsAdapter.notifyDataSetChanged();
        }
    }

    class GoogleCardsAdapter extends BaseAdapter {

        private final Context mContext;

        GoogleCardsAdapter(final Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return bs.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public View getView(final int position, final View convertView, final ViewGroup parent) {
            View view = null;
            if (bs.size() > 0) {
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.google_card, null);

                TextView tv = (TextView) view.findViewById(R.id.activity_googlecards_card_textview);
                ImageView iv = (ImageView) view.findViewById(R.id.activity_googlecards_card_imageview);

                // set image and title and image using our bs
                tv.setText("" + titles.get(position));
                Bitmap b = bs.get(position);
                int width = 0;
                int height = 0;

                float scaleDP = getResources().getDisplayMetrics().density;

                int height_adjuster = window_height;
                int width_adjuster = (int) (scaleDP * 300 + 0.5f);

                tv.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

                if (b != null) {
                    width = b.getWidth();
                    height = b.getHeight();

                    int old_width = width;
                    width = width_adjuster;
                    height = height * width_adjuster / old_width;

                    if (height > height_adjuster) {
                        int old_height = height;
                        Log.d("size", "tv width = " + tv.getMeasuredWidth());
                        Log.d("size", "tv heigth = " + tv.getMeasuredHeight());
                        height = height_adjuster - tv.getMeasuredHeight() - (int) (32 * scaleDP + 0.5f);
                        width = width * height_adjuster / old_height;

                    }
                    iv.setImageBitmap(Bitmap.createScaledBitmap(b, width, height, false));
                }
            }
            return view;
        }


    }

}

