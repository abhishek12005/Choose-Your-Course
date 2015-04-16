package com.example.chooseyourcourse;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


public class SelectCourse extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Button b;
    private int choose;
    private GoogleApiClient mGoogleApiClient;
    private String courses[] = new String[3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_course);
        MyAdapter adapter = new MyAdapter(this, generateData());
        ListView listView = (ListView) findViewById(R.id.listView2);
        listView.setAdapter(adapter);
        b = (Button)findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CourseAdd().execute();
            }
        });
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    @Override
    protected void onStart(){
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    private class CourseAdd extends AsyncTask<String, Void, String >
    {

        String content = "";
        @Override
        protected String doInBackground(String... urls) {

            HttpClient httpClient = new DefaultHttpClient();

            String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
            HttpPost httpPost = new HttpPost("http://192.168.49.8:8080/addcourse/");

            List<NameValuePair> params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("email", email));
            params.add(new BasicNameValuePair("course", courses[choose]));
            //params.add(new BasicNameValuePair("operation", "1"));

            try {
                httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                // writing error to Log
                e.printStackTrace();
            }

            try {
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity respEntity = response.getEntity();

                if (respEntity != null) {
                    // EntityUtils to get the response content
                    content =  EntityUtils.toString(respEntity);
                }


            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            return content;
        }

        @Override
        protected void onPostExecute(String s) {
            Intent i = new Intent(SelectCourse.this,MainActivity.class);
            startActivity(i);

        }
    }

    public void onListItemClick(ListView parent, View v,int position,long id){
        courses[0] = "Practice of Programming (CSE 301)";
        courses[1] = "PCSMA (CSE 635)";
        courses[2] = "ICW (HSS XXX)";
        choose = position;
        new CourseAdd().execute();
    }

    private ArrayList<Items> generateData(){
        ArrayList<Items> items = new ArrayList<Items>();
        items.add(new Items("Practice of Programming (CSE 301)"));
        items.add(new Items("PCSMA (CSE 635)"));
        items.add(new Items("ICW (HSS XXX)"));

        return items;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_course, menu);
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
}
