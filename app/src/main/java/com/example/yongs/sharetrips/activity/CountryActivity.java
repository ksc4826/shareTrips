package com.example.yongs.sharetrips.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.Toast;


import java.util.ArrayList;

import com.example.yongs.sharetrips.R;
import com.example.yongs.sharetrips.adapter.CountryListAdapter;
import com.example.yongs.sharetrips.api.ApiCallback;
import com.example.yongs.sharetrips.api.users.RetrofitUsers;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CountryActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    private static ExpandableListView countryListView;
    private static CountryListAdapter adapter;

    EditText search;

    String mId;
    RetrofitUsers mRetrofitUsers;

    String[] continent = new String[]{
            "동남아시아", "동북아시아", "중앙아시아", "남아시아", "동유럽", "북유럽", "서유럽", "남유럽",
            "중동", "아프리카", "북,중미", "남미", "태평양", "국내", "기타"
    };
    String[][] country = {
            //대륙 구분

            /*동남아*/    {"태국", "베트남", "미얀마", "말레이시아", "싱가포르", "인도네시아", "라오스", "동티모르", "브루나이","캄보디아","필리핀"},
            /*동북아*/    {"중국", "일본", "대만", "몽골", "홍콩"},
            /*중앙아*/    {"우즈베키스탄", "카자흐스탄", "키르기스스탄"},
            /*남아*/      {"네팔", "몰디브", "방글라데시", "부탄", "스리랑카", "인도", "파키스탄", "아프가니스탄"},
            /*동유럽*/    {"러시아", "루마니아", "몰도바", "벨라루스", "불가리아", "슬로바키아", "우크라이나", "체코", "폴란드", "헝가리"},
            /*북유럽*/    {"아이슬란드", "영국", "아일랜드", "덴마크", "핀란드", "노르웨이", "스웨덴"},
            /*서유럽*/    {"프랑스", "룩셈부르크", "벨기에", "네덜란드", "독일", "스위스", "모나코", "오스트리아"},
            /*남유럽*/    {"포르투갈", "스페인", "바티칸 시국", "세르비아", "슬로베니아", "그리스", "크로아티아", "이탈리아"},
            /*중동*/      {"이집트", "레바논", "바레인", "터키", "사우디아라비아", "아랍에미리트", "요르단", "이라크", "이란", "이스라엘", "예멘", "카타르", "쿠웨이트", "시리아"},
            /*아프리카*/    {"알제리",  "리비아", "튀니지", "모로코", "수단", "케냐", "에티오피아", "소말리아",
                            "우간다", "남수단", "가나", "감비아", "말리", "남아프리카 공화국", "잠비아", "짐바브웨"},
            /*북중미*/     {"미국", "캐나다", "멕시코", "과테말라", "쿠바", "도미니카공화국", "아이티","자메이카", "코스타리카", "파나마"},
            /*남미*/      {"브라질", "아르헨티나", "칠레", "콜롬비아", "볼리비아", "베네수엘라", "에콰도르", "우루과이", "파라과이", "페루"},
            /*태평양*/     {"뉴질랜드", "호주", "피지", "파푸아뉴기니", "통가", "하와이", "괌"},
            /*국내*/      {"서울", "경기", "인천", "강원", "충청", "전라", "경상", "대전", "광주", "대구", "부산", "울산", "제주"},
            /*기타*/      {"북한"}
    };

    private void setToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("관심지역");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);


        countryListView = findViewById(R.id.country_listview);
        countryListView.setGroupIndicator(null);

        search = (EditText) findViewById(R.id.inputSearch);

        ButterKnife.bind(this);
        setToolbar();
        setItems();
        setListener();
        setSearch();


    }


    void setItems() {
        ArrayList<String> header = new ArrayList<String>();
        HashMap<String, List<String>> hashMap = new HashMap<String, List<String>>();

        for (int i = 0; i < continent.length; i++) {
            header.add(continent[i]);
            List<String> child = new ArrayList<String>();
            for (int j = 0; j < country[i].length; j++) {
                child.add(country[i][j]);
                hashMap.put(header.get(i), child);
            }
        }
        adapter = new CountryListAdapter(CountryActivity.this, header, hashMap);
        countryListView.setAdapter(adapter);

    }

    void setListener() {

        countryListView.setOnChildClickListener(new OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView listview, View view,
                                        int groupPos, int childPos, long id) {
                Toast.makeText(
                        CountryActivity.this,
                        "" + adapter.getChild(groupPos, childPos),
                        Toast.LENGTH_SHORT).show();

                mId = getIntent().getStringExtra("id");



                mRetrofitUsers.PostCountry(mId,adapter.getChild(groupPos, childPos).toString(), new ApiCallback() {
                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onSuccess(int code, Object receiveData) {
                        finish();
                    }

                    @Override
                    public void onFailure(int code) {

                    }
                });
              /*  */

      //          finish();
                return false;
            }

        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    void setSearch() {
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}