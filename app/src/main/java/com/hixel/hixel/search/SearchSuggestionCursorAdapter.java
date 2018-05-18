package com.hixel.hixel.search;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import com.hixel.hixel.R;

public class SearchSuggestionCursorAdapter extends CursorAdapter {
    private LayoutInflater mLayoutInflater;
    private Context mContext;
    private SearchView searchView;
    public SearchSuggestionCursorAdapter(Context context, Cursor c, SearchView sv) {
        super(context, c, false);
        mContext=context;
        this.searchView=sv;
        mLayoutInflater=LayoutInflater.from(context);


    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View v  = mLayoutInflater.inflate(R.layout.row_for_search_suggestion,parent,false);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView suggestion=(TextView) view.findViewById(R.id.singleRowForSearch);
                searchView.setIconified(true);

            }
        });

    }
}
