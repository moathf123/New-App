package com.example.newsapp;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    static ArrayList<NewsDetails> newsDetails;

    public NewsAdapter(ArrayList<NewsDetails> newsDetails) {
        this.newsDetails = newsDetails;
    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View recycleItem = layoutInflater.inflate(R.layout.item_news, parent, false);
        return new ViewHolder(recycleItem);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        holder.titleTextView.setText(newsDetails.get(position).getmTitle());
        holder.sectionTextView.setText(newsDetails.get(position).getmSection());
        holder.dateTextView.setText(newsDetails.get(position).getmDate().substring(0, 10));
        holder.authorTextView.setText(newsDetails.get(position).getmAuthor());
    }

    @Override
    public int getItemCount() {
        if (newsDetails != null)
            return newsDetails.size();
        else
            return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.textView_title)
        TextView titleTextView;
        @BindView(R.id.textView_section)
        TextView sectionTextView;
        @BindView(R.id.textView_date)
        TextView dateTextView;
        @BindView(R.id.textView_author)
        TextView authorTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                String url = newsDetails.get(pos).getmUrl();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(url));
                itemView.getContext().startActivity(intent);
            });
        }
    }
}
