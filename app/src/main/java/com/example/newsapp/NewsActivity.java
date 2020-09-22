package com.example.newsapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<NewsDetails>> {

    @BindView(R.id.recyclerview_NewsActivity)
    RecyclerView recyclerView;
    @BindView(R.id.textView_NewsActivity_empty)
    TextView textView;
    @BindView(R.id.button_newsActivity_wifi)
    Button wifiBtn;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    int EMPTY = 0;
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_recycle);
        ButterKnife.bind(this);

        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(0, null, this);
        } else {
            progressBar.setVisibility(View.GONE);
            textView.setText(R.string.no_internet_connection);
            wifiBtn.setVisibility(View.VISIBLE);
            wifiBtn.setOnClickListener(v -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)));
        }
    }

    private void updateUi(List<NewsDetails> newsDetails) {
        NewsAdapter newsAdapter = new NewsAdapter((ArrayList<NewsDetails>) newsDetails);
        newsAdapter.notifyDataSetChanged();
        if (newsAdapter.getItemCount() == EMPTY)
            textView.setText(R.string.NewsActivity_emptyData);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(newsAdapter);
    }

    @NonNull
    @Override
    public Loader<List<NewsDetails>> onCreateLoader(int id, @Nullable Bundle args) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String orderBy = sharedPreferences.getString(
                getString(R.string.settings_order_by_key), getString(R.string.settings_order_by_default));
        String lang = sharedPreferences.getString(
                getString(R.string.settings_language_key), getString(R.string.settings_language_default));
        String section = sharedPreferences.getString(
                getString(R.string.settings_section_key), getString(R.string.settings_section_default));

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter("section", section);
        uriBuilder.appendQueryParameter("lang", lang);
        uriBuilder.appendQueryParameter("order-by", orderBy);
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("api-key", "test");
        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<NewsDetails>> loader, List<NewsDetails> data) {
        progressBar.setVisibility(View.GONE);
        if (data == null)
        {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if(networkInfo == null)
            {
                textView.setText(R.string.no_internet_connection);

                recyclerView.setAdapter(null);
            }

            else {
                textView.setText(R.string.NewsActivity_emptyData);
            }
        }else
        {
            updateUi(data);
        }



    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<NewsDetails>> loader) {
        recyclerView.setAdapter(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}