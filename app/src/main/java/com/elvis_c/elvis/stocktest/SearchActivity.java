package com.elvis_c.elvis.stocktest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.elvis_c.elvis.stocktest.Common.SearchAdapter;
import com.elvis_c.elvis.stocktest.Model.AllCompanyCode;
import com.elvis_c.elvis.stocktest.controller.DataManagement;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchActivity extends AppCompatActivity {

    private String TAG = getClass().getSimpleName();
    private SearchView searchView;
    private ListView mListView;
    private ArrayAdapter mArrayAdapter;

    private ArrayList<AllCompanyCode> allCompanyCodes = new ArrayList<>();
    private EditText mEditText;
    private RecyclerView mRecyclerView;
    private SearchAdapter searchAdapter;
    private ArrayList<AllCompanyCode> searchResultCodes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        allCompanyCodes = DataManagement.getInstance(getApplicationContext()).getAllCompanyCodes();
        Log.d(TAG, "onCreate, allCompanyCodes.size() = " + allCompanyCodes.size());
        initView();
    }

    private void initView(){
        mEditText = findViewById(R.id.et_search);
        mRecyclerView = findViewById(R.id.rv_searchResult);
        searchAdapter = new SearchAdapter(getApplicationContext());
        searchAdapter.upDateList(searchResultCodes);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        searchAdapter.registerCallback(adapterCallback);
        mRecyclerView.setAdapter(searchAdapter);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.d(TAG, "onEditorAction, actionId = " + actionId);
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (!mEditText.getText().equals("")) {
                        searchAllCompany(mEditText.getText().toString());
                        searchAdapter.upDateList(searchResultCodes);
                        searchAdapter.notifyDataSetChanged();
                    }
                }

                return false;
            }
        });

//        searchView = findViewById(R.id.sv_stock);
//        mListView = findViewById(R.id.lv_search);
//        mArrayAdapter = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_list_item_1, allCompanyCodes);
//        mListView.setAdapter(mArrayAdapter);
//        mListView.setTextFilterEnabled(true);
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                Log.d(TAG, "onQueryTextChange, s = " + s);
//                mArrayAdapter.getFilter().filter(s);
//                return false;
//            }
//        });
    }

    private void searchAllCompany(String input){
        Log.d(TAG, "searchAllCompany, input = " + input);
        searchResultCodes.clear();
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(input);
        if (m.matches()) {
            for (int i = 0; i < allCompanyCodes.size(); i++) {
                if (allCompanyCodes.get(i).getCompanyCode().contains(input)) {
                    searchResultCodes.add(allCompanyCodes.get(i));
                }
            }
        } else {
            String [] strings = new String[] {input};
            Log.d(TAG, "searchAllCompany, input.length = " + input.length());
            for (int i = 0; i < allCompanyCodes.size(); i++) {
//                if (allCompanyCodes.get(i).getCompanyName().contains(input)) {
//                    searchResultCodes.add(allCompanyCodes.get(i));
//                }
                if (searchStrings(allCompanyCodes.get(i).getCompanyName(), strings)) {
                    searchResultCodes.add(allCompanyCodes.get(i));
                }
            }
        }
        Log.d(TAG, "searchAllCompany, searchResultCodes.size = " + searchResultCodes.size());
    }

    private boolean searchStrings(String target, String[] strings){
        Log.d(TAG, "searchStrings, target = " + target);
        Log.d(TAG, "searchStrings, strings.length = " + strings.length);
        boolean result = false;
        for (String s : strings) {
            if (target.contains(s)) {
                result = true;
            } else {
                result = false;
            }
        }
        Log.d(TAG, "searchStrings, result = " + result);
        return result;
    }

    private SearchAdapter.adapterCallback adapterCallback = new SearchAdapter.adapterCallback() {
        @Override
        public void onClick(String stockID) {
            Log.d(TAG, "adapterCallback, onClick, stockID = " + stockID);
        }
    };


}
