package com.example.reddit;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.sax.EndElementListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;

import com.example.reddit.adapters.PostAdapter;
import com.example.reddit.objexts.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    ArrayList<Post> posts = new ArrayList<>();
    PostAdapter adapter;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView recyclerView = findViewById(R.id.recyclerView);
        fetchPosts(1,"all");
         adapter = new PostAdapter(this,R.layout.post_interface_layout,posts);
        recyclerView.setAdapter(adapter);
        while (posts.size() == 0) {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        recyclerView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {

                }
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    ActionBar actionBar = getSupportActionBar();
                    actionBar.hide();
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                        page++;
                        fetchPosts(page,"all");
                        adapter.notifyDataSetChanged();
                    }

                }
                else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
                    ActionBar actionBar = getSupportActionBar();
                    actionBar.show();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem searchItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                posts.clear();
                fetchPosts(1,query);
                while (posts.size() == 0) {
                    try {
                        Thread.sleep(400);
                        adapter.notifyDataSetChanged();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void fetchPosts(int pageNo, String subreddit){
        OkHttpClient client = new OkHttpClient();
        //set User-Agent to avoid 403 error
        Request request = new Request.Builder()
                .url("https://www.reddit.com/r/"+subreddit+"/.json?&page="+pageNo)
                .header("User-Agent", "android")
                .build();
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                String json = response.body().string();
                System.out.println(json);

                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray children = data.getJSONArray("children");
                    for (int i = 0; i < children.length(); i++) {
                        JSONObject child = children.getJSONObject(i);
                        JSONObject childData = child.getJSONObject("data");
                        String title = childData.getString("title");
                        String author = childData.getString("author");
                        String subreddit = childData.getString("subreddit");
                        int score = childData.getInt("score");
                        String url = childData.getString("url");
                        int num_comments = childData.getInt("num_comments");

                        System.out.println(title);
                        System.out.println(author);
                        System.out.println(subreddit);
                        System.out.println(score);
                        System.out.println(url);
                        posts.add(new Post(title,author,subreddit,url,score,num_comments));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}