package com.example.reddit;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.os.Bundle;

import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.AbsListView;

import android.widget.ListView;
import android.widget.Toast;

import com.example.reddit.adapters.PostAdapter;
import com.example.reddit.adapters.PostAdpaterRecycler;
import com.example.reddit.objexts.Post;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class MainActivity extends AppCompatActivity {
    ArrayList<Post> posts = new ArrayList<>();
//    PostAdapter adapter;
    PostAdpaterRecycler adapter;
    int page = 1;
    String subreddit="all";
    int PostCount = 0;
   String afterPost=" ";
   int ScreenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;
        ScreenWidth = width;

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        fetchPosts(subreddit);
         adapter = new PostAdpaterRecycler(this,posts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        while (posts.size() == 0) {
            try {
                Thread.sleep(400);
                adapter.notifyDataSetChanged();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                super.onScrollStateChanged(recyclerView, newState);
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // user is scrolling
                    // pause auto scrolling
                    //adapter.setPauseOnScroll(false);
                    //load next page
                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.show();
                    }
                } else {
                    // user is not scrolling
                    // resume auto scrolling
                    //adapter.setPauseOnScroll(true);
                    //load next page
                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.hide();
                    }
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItem == (totalItemCount - 1)) {
                    page++;
                    fetchPosts(afterPost,subreddit);
                    adapter.notifyDataSetChanged();
                    System.out.println("page: " + page);
                }
            }
        });


//        recyclerView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
//
//                }
//                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
//                    ActionBar actionBar = getSupportActionBar();
//                    actionBar.hide();
//                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
//                        page++;
//                        fetchPosts(page,subreddit);
//                        adapter.notifyDataSetChanged();
//                    }
//
//                }
//                else if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE){
//                    ActionBar actionBar = getSupportActionBar();
//                    actionBar.show();
//                }
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//
//            }
//        });
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
                subreddit = query;
                fetchPosts(subreddit);
                ActionBar actionBar = getSupportActionBar();
                actionBar.setTitle("r/"+subreddit);
                PostCount = 0;
                for (int i=0;i<6;i++){
                    if (posts.size() == 0) {
                        try {
                            Thread.sleep(400);
                            adapter.notifyDataSetChanged();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (i==5 && posts.size() == 0){
                        Toast.makeText(MainActivity.this, "Subreddit vul disos motherchod", Toast.LENGTH_SHORT).show();
                    }
                }
                searchView.clearFocus();


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    public void fetchPosts(String after, String subreddit){
        OkHttpClient client = new OkHttpClient();
        //set User-Agent to avoid 403 error
        Request request = new Request.Builder()
                .url("https://www.reddit.com/r/"+subreddit+"/.json?&after="+after)
                .header("User-Agent", "android")
                .build();
        System.out.println("https://www.reddit.com/r/"+subreddit+".json?&after="+after);
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
                        PostCount++;
                        JSONObject child = children.getJSONObject(i);
                        JSONObject childData = child.getJSONObject("data");
                        String title = childData.getString("title");
                        String author = childData.getString("author");

                         afterPost = childData.getString("name");

                        String subreddit = childData.getString("subreddit");
                        int score = childData.getInt("score");
                        String url = childData.getString("url");
                        int num_comments = childData.getInt("num_comments");


                        if (url.startsWith("https://i")) {
                            JSONObject preview = childData.getJSONObject("preview");
                            JSONArray images = preview.getJSONArray("images");
                            JSONObject image = images.getJSONObject(0);
                            JSONObject source = image.getJSONObject("source");

                            int width = source.getInt("width");
                            int height = source.getInt("height");
                           String thumbnail = childData.getString("thumbnail");
                            posts.add(new Post(title,author,subreddit,url,score,num_comments,width,height,thumbnail,ScreenWidth));
                        }


                        System.out.println(title);
                        System.out.println(author);
                        System.out.println(subreddit);
                        System.out.println(score);
                        System.out.println(url);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    public void fetchPosts(String subreddit){
        OkHttpClient client = new OkHttpClient();
        //set User-Agent to avoid 403 error
        Request request = new Request.Builder()
                .url("https://www.reddit.com/r/"+subreddit+"/.json")
                .header("User-Agent", "android")
                .build();
        System.out.println("https://www.reddit.com/r/"+subreddit+".json?");
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
                        PostCount++;
                        JSONObject child = children.getJSONObject(i);
                        JSONObject childData = child.getJSONObject("data");
                        String title = childData.getString("title");
                        String author = childData.getString("author");

                        afterPost = childData.getString("name");

                        String subreddit = childData.getString("subreddit");
                        int score = childData.getInt("score");
                        String url = childData.getString("url");
                        int num_comments = childData.getInt("num_comments");


                        if (url.startsWith("https://i")) {
                            JSONObject preview = childData.getJSONObject("preview");
                            JSONArray images = preview.getJSONArray("images");
                            JSONObject image = images.getJSONObject(0);
                            JSONObject source = image.getJSONObject("source");
                            String thumbnail = childData.getString("thumbnail");

                            int width = source.getInt("width");
                            int height = source.getInt("height");
                            posts.add(new Post(title,author,subreddit,url,score,num_comments,width,height,thumbnail,ScreenWidth));
                        }


                        System.out.println(title);
                        System.out.println(author);
                        System.out.println(subreddit);
                        System.out.println(score);
                        System.out.println(url);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}