package com.example.fronoman.lurker;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.ArrayList;


public class MyActivity extends Activity {

    ArrayList<String> titles;
    ArrayList<String> imageURLS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_my);


        titles = new ArrayList<String>();
        imageURLS = new ArrayList<String>();

        new Connection().execute();


        ListView lvPostCard = (ListView) findViewById(R.id.lvPostCard);
        lvPostCard.setAdapter(new PostCardAdapter());


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

                /*Array that contains the topics*/
                JSONArray children = data.getJSONArray("children");
                String title = "";
                String imageURL = "";
                for (int i = 0; i < children.length(); i++) {
                    title = children.getJSONObject(i).getJSONObject("data").getString("title");
                    imageURL = children.getJSONObject(i).getJSONObject("data").getString("url");
                    titles.add(title);
                    imageURLS.add(imageURL);
                }


            } catch (Exception e) {
                Log.d("Exception", "exception = " + e.toString());
                e.printStackTrace();
            }
            return titles;
        }

        @Override
        protected void onPostExecute(Object o) {

        }
    }

    public class PostCardAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View post_card = getLayoutInflater().inflate(R.layout.post_card, null);
            TextView tvPostTitle = (TextView) post_card.findViewById(R.id.tvPostTitle);
            ImageView ivPost = (ImageView) post_card.findViewById(R.id.ivPost);

            tvPostTitle.setText(titles.get(position));
            Picasso.with(getApplicationContext()).load(imageURLS.get(position)).into(ivPost);


            return post_card;
        }
    }
}
