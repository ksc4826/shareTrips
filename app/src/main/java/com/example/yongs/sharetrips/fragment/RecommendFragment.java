package com.example.yongs.sharetrips.fragment;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.yongs.sharetrips.R;
import com.example.yongs.sharetrips.adapter.ReportAdapter;
import com.example.yongs.sharetrips.api.ApiCallback;
import com.example.yongs.sharetrips.api.reports.ReportApiService;
import com.example.yongs.sharetrips.api.reports.RetrofitReports;
import com.example.yongs.sharetrips.model.Report;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecommendFragment extends Fragment {

    @BindView(R.id.list)
    ListView list;

    ReportAdapter mReportAdapter;
    ArrayList<Report> mReportArrayList;
    RetrofitReports mRetrofitReports;
    List<Report> mReportList;

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
        View view  = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);

        mRetrofitReports = RetrofitReports.getInstance(getActivity().getBaseContext()).createBaseApi();

        setList();

        return view;
    }

    private void setList(){
        mReportArrayList = new ArrayList<Report>();
        mReportAdapter = new ReportAdapter(this.getActivity(), mReportArrayList);
        list.setAdapter(mReportAdapter);
        mReportAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume(){
        super.onResume();
        listRenewal();
    }

    private void listRenewal(){
        mReportAdapter.reportArrayList.clear();
        mRetrofitReports.getRecommend(getActivity().getIntent().getStringExtra("username"),new ApiCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e(TAG,t.toString());
            }

            @Override
            public void onSuccess(int code, Object receiveData) {
                Log.i(TAG, String.valueOf(code));

                mReportList = (List<Report>) receiveData;
                for (int i = 0; i < mReportList.size(); i++) {
                    Report report = mReportList.get(i);
                    Log.d(TAG,String.valueOf(report.getId()));

                    ReportApiService reportApiService = mRetrofitReports.create(ReportApiService.class);
                    Call<ResponseBody> call = reportApiService.getImage(report.getId());
                    new RecommendFragment.ImageCall(report).execute(call);
                }
            }
            @Override
            public void onFailure(int code) {
                Log.i(TAG,String.valueOf(code));
            }
        });
    }

    private class ImageCall extends AsyncTask<Call,Void,ResponseBody> {
        Report report;

        public ImageCall(){
            this.report = null;
        }

        public ImageCall(Report report){
            this.report = report;
        }

        @Override
        protected ResponseBody doInBackground(Call... calls) {
            try {
                Call<ResponseBody> call = calls[0];
                retrofit2.Response<ResponseBody> response = call.execute();
                return response.body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(ResponseBody responseBody) {
            Bitmap bitmap = null;
            try {

                InputStream inputStream = null;

                inputStream = responseBody.byteStream();

                bitmap = BitmapFactory.decodeStream(inputStream);
                if (inputStream != null) {
                    inputStream.close();
                }

                Report r = new Report(report.getId(), report.getUsername(), report.getTitle(), report.getLocation(), report.getContent(), report.getDate(), report.getView(), bitmap);
                mReportAdapter.reportArrayList.add(r);
                list.setAdapter(mReportAdapter);
                mReportAdapter.notifyDataSetChanged();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
