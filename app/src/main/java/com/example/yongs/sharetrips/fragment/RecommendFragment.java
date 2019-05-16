package com.example.yongs.sharetrips.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.example.yongs.sharetrips.R;
import com.example.yongs.sharetrips.adapter.RecommendAdapter;
import com.example.yongs.sharetrips.api.ApiCallback;
import com.example.yongs.sharetrips.api.reports.RetrofitReports;
import com.example.yongs.sharetrips.model.Report;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends Fragment {

    @BindView(R.id.recommend_list)
    ExpandableListView expandableListView;

    RecommendAdapter mRecommendAdapter;
    ArrayList<String> parentList;
    HashMap<String, ArrayList<Report>> childHashMap;
    RetrofitReports mRetrofitReports;

    private static final String TAG = RecommendFragment.class.getSimpleName();

    public RecommendFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_recommend, container, false);
        ButterKnife.bind(this,view);

        parentList = new ArrayList<>();
        childHashMap = new HashMap<>();

        JSONObject jsonObject = sampleJSON();
        setRecommendList(jsonObject);

        /*mRetrofitReports.getRecommend(getActivity().getIntent().getStringExtra("username"), new ApiCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e(TAG,t.toString());
            }

            @Override
            public void onSuccess(int code, Object receiveData) {
                Log.i(TAG, String.valueOf(code));
                setRecommendList((JSONObject)receiveData);
            }

            @Override
            public void onFailure(int code) {
                Log.i(TAG,String.valueOf(code));
            }
        });*/

        mRecommendAdapter = new RecommendAdapter(getActivity(), parentList, childHashMap);
        expandableListView.setAdapter(mRecommendAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {

            }
        });
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                return false;
            }
        });
        expandableListView.expandGroup(0);

        return view;
    }

    JSONObject sampleJSON(){
        JSONObject jsonObject = new JSONObject();
        JSONArray array1 = new JSONArray();
        JSONArray array2 = new JSONArray();
        JSONObject data;

        for(int i=1;i<=4; i++) {
            try {
                data = new JSONObject();
                data.put("id", i);
                data.put("username", "test" + i);
                data.put("title", "test" + i);
                data.put("location", "location" + i);
                data.put("content", "content" + i);
                data.put("date", "test" + i);
                data.put("view", i);
                if(i<=2)
                    array1.put(data);
                else
                    array2.put(data);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        try {
            jsonObject.put("aaa", array1);
            jsonObject.put("bbb", array2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String str = jsonObject.toString();
        System.out.println(str);
        return jsonObject;
    }

    void setRecommendList(JSONObject jsonObject){
        Iterator<String> keys = jsonObject.keys();
        JSONArray jsonArray;
        JSONObject reportObject;
        ArrayList<Report> childList;
        Report report;

        while(keys.hasNext()){
            String key = keys.next();
            parentList.add(key);
            childList = new ArrayList<>();
            try {
                jsonArray = (JSONArray)jsonObject.get(key);
                for(int i = 0; i<jsonArray.length(); i++){
                    reportObject = (JSONObject)jsonArray.get(i);
                    report = new Report();

                    report.setId(Integer.parseInt(reportObject.get("id").toString()));
                    report.setUsername(reportObject.get("username").toString());
                    report.setTitle(reportObject.get("title").toString());
                    report.setLocation(reportObject.get("location").toString());
                    report.setContent(reportObject.get("content").toString());
                    report.setDate(reportObject.get("date").toString());
                    report.setView(Integer.parseInt(reportObject.get("view").toString()));

                    /*Log.d(TAG,String.valueOf(report.getId()));

                    ReportApiService reportApiService = mRetrofitReports.create(ReportApiService.class);
                    Call<ResponseBody> call = reportApiService.getImage(report.getId());
                    new RecommendFragment().ImageCall(report).execute(call);*/

                    childList.add(report);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            childHashMap.put(key, childList);
        }
    }
}
