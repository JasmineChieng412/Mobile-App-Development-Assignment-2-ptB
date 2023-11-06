package curtin.edu.ptb;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
   // String url = "https://jsonplaceholder.typicode.com/users";
    //String url2 = "https://pixabay.com/api/?key="+"30867330-d86f2982c2b53d17a813a3e2f&q="+"animals"+"&image_type=photo";
    String newURL;
    String url2;
    boolean searched = false;
    boolean restart;
    private RecyclerView userRV;
    private RecyclerView.Adapter userAdapter;
    private List<HashMap<String, String>> usersList;

    EditText search;
    Button doubleB, singleB, searchB, upB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ExecutorService service = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        usersList = new ArrayList<HashMap<String, String>>();
        userAdapter = new ImageAdapter(getApplicationContext(), usersList);
        new backgroundTask().execute();


    }

    public class backgroundTask extends AsyncTask<Void,Void,String> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder stringBuilder = null;
            String line = "";
            try {
                URL urlbackground;
                HttpURLConnection connection = null;

                try {

               /* String url2 = Uri.parse("https://pixaby.com/api/")
                        .buildUpon()
                        .appendQueryParameter("key", "30867330-d86f2982c2b53d17a813a3e2f")
                        .appendQueryParameter("q", "animal")
                        .appendQueryParameter("image_type", "photo")
                        .build().toString();*/

                    search = findViewById(R.id.searchBar);
                    searchB = findViewById(R.id.searchB);
                    String newURL = search.getText().toString();

                    if(searched == true){
                        url2 = "https://pixabay.com/api/?key="
                                +"30867330-d86f2982c2b53d17a813a3e2f&q="
                                +newURL
                                +"&image_type=photo";
                    }else{
                        url2 = "https://pixabay.com/api/?key="
                                +"30867330-d86f2982c2b53d17a813a3e2f&q="
                                +"flowers"
                                +"&image_type=photo";
                    }
                    urlbackground = new URL(url2);
                    connection = (HttpURLConnection) urlbackground.openConnection();
                    InputStreamReader reader = new InputStreamReader(connection.getInputStream());
                    //BufferedReader buffer = new BufferedReader(reader);

                    stringBuilder = new StringBuilder();
                    int data = reader.read();
                    //while ((line = buffer.readLine()) != null) {
                      while(data != -1){
                        //stringBuilder.append(line);
                          line  += (char) data;
                          data = reader.read();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }
            //return stringBuilder.toString();
            return line;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();


            try {
                JSONObject jObject =  new JSONObject(s);
                JSONArray jArray = jObject.getJSONArray("hits");
                for(int i = 0; i<jArray.length(); i++){
                    JSONObject o =  jArray.getJSONObject(i);
                    String imageUrl = o.getString("webformatURL");
                    String likes = o.getString("likes");
                    String tags = o.getString("tags");

                    HashMap<String, String> users2 = new HashMap<>();
                    users2.put("tags", tags);
                    users2.put("likes", likes);
                    users2.put("webformatURL", imageUrl);
                    usersList.add(users2);
                }
                setUserRecycler(usersList);
                doubleB = (Button) findViewById(R.id.doubleCol);
                doubleB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(MainActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
                        // Intent i = new Intent(SelectedUserDetails.this, PostsActivity.class);
                        // i.putExtra("selectedUserId", selectedMainId);
                        // startActivity(i);
                        setDoubleUserRecycler(usersList);
                    }
                });

                singleB = (Button) findViewById(R.id.singleCol);
                singleB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Toast.makeText(MainActivity.this, "Loading...", Toast.LENGTH_SHORT).show();
                        // Intent i = new Intent(SelectedUserDetails.this, PostsActivity.class);
                        // i.putExtra("selectedUserId", selectedMainId);
                        // startActivity(i);
                        setUserRecycler(usersList);
                    }
                });


                searchB.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        searched = true;
                        restart = true;
                        if(restart){
                            refresh(1000);
                            restart = false;
                        }
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void setUserRecycler(List<HashMap<String, String>> usersList) {
        userRV = findViewById(R.id.imageRecyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        userRV.setLayoutManager(layoutManager);
        userAdapter = new ImageAdapter(this, usersList);
        userRV.setAdapter(userAdapter);
    }

    private void setDoubleUserRecycler(List<HashMap<String, String>> usersList) {
        userRV = findViewById(R.id.imageRecyclerView);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(this, 2);
        userRV.setLayoutManager(layoutManager);
        userAdapter = new ImageAdapter(this, usersList);
        userRV.setAdapter(userAdapter);
    }

    private void refresh(int milliseconds){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                new backgroundTask().execute();
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }
}