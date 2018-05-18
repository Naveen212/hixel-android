package com.hixel.hixel.dashboard;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.support.v4.widget.SimpleCursorAdapter;


import android.widget.TextView;
import android.widget.Toast;

import com.hixel.hixel.R;
import com.hixel.hixel.databinding.ActivityDashboardBinding;

import java.util.ArrayList;

public class DashboardActivity extends AppCompatActivity implements DashboardContract.View,
        OnItemSelectedListener {

    private DashboardContract.Presenter presenter;
    RecyclerView.Adapter adapter;
    ActivityDashboardBinding binding;
     //SimpleCursorAdapter mAdapter;
     private SimpleCursorAdapter mAdapter;
     ArrayList<String> dummyList=new ArrayList<String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard);
        setSupportActionBar(binding.toolbar.toolbar);
        binding.toolbar.toolbarTitle.setText(R.string.dashboard);

        // Init presenter
        presenter = new DashboardPresenter(this);
        presenter.start();

        Spinner spinner = binding.spinner;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.company_dropdown, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        RecyclerView recyclerView = binding.recyclerView;
        this.adapter = new DashboardAdapter(this, presenter);
        recyclerView.setAdapter(this.adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        String[] from=new String[]{""};
        int[] to=new int[]{};
        dummyList.add("aaaa");
        dummyList.add("apple lo");
        mAdapter=new SimpleCursorAdapter(this,R.layout.row_for_search_suggestion,null,from,to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem search = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) search.getActionView();
        searchView.setQueryHint("enter company...");


        SearchView.SearchAutoComplete searchAutoComplete = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHintTextColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.WHITE);
        String dataArr[] = {"Apple" , "Amazon" , "Amd", "Microsoft", "Microwave", "MicroNews", "Intel", "Intelligence"};
        ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,dataArr);
        searchAutoComplete.setAdapter(newsAdapter);
       // searchView.setOnQueryTextListener(new on);

        searchAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long id) {
                String queryString=(String)adapterView.getItemAtPosition(itemIndex);
                searchAutoComplete.setText("" + queryString);

               // Toast.makeText(ActionBarSearchActivity.this, "you clicked " + queryString, Toast.LENGTH_LONG).show();
                newsAdapter.notifyDataSetChanged();

            }




        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                presenter.loadSearchSuggestion(searchAutoComplete.getText().toString());
                //newsAdapter.notifyDataSetChanged();

                ArrayList<String> names1 = presenter.getnames();

                ArrayAdapter<String> newsAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line,presenter.getnames());
                searchAutoComplete.setAdapter(newsAdapter);

                newsAdapter.notifyDataSetChanged();
                //searchAutoComplete.setText("" + names1.get(0));



                return false;
            }
        });

        ImageView searchClose = searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchClose.setImageResource(R.drawable.ic_clear);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void setPresenter(@NonNull DashboardContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        presenter.sortCompaniesBy(item);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void portfolioChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /*@Override
    public void showMainGraph(ArrayList<Company> companies) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < companies.size(); i++) {
            entries.add(new BarEntry((float) i, (float) companies.get(i).getHealth()));
        }

        BarChart chart = binding.chart;

        MainGraphDataSet set = new MainGraphDataSet(entries, "Health");
        set.setColors(ContextCompat.getColor(this, R.color.good),
                ContextCompat.getColor(this, R.color.average),
                ContextCompat.getColor(this, R.color.bad));
        set.setValueTextColor(R.color.textColorDefault);
        BarData barData = new BarData(set);

        chart.getAxisLeft().setDrawLabels(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getXAxis().setDrawLabels(false);

        chart.getLegend().setEnabled(false);   // Hide the legend

        chart.setData(barData);
        chart.animateY(1000);
        chart.animateX(1000);
        chart.invalidate(); // refresh
    }*/
}
