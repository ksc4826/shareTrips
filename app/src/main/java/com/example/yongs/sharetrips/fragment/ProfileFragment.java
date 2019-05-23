package com.example.yongs.sharetrips.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yongs.sharetrips.R;
import com.example.yongs.sharetrips.activity.CountryActivity;
import com.example.yongs.sharetrips.activity.ModifyActivity;
import com.example.yongs.sharetrips.activity.ThemeActivity;
import com.example.yongs.sharetrips.api.ApiCallback;
import com.example.yongs.sharetrips.api.users.RetrofitUsers;
import com.example.yongs.sharetrips.model.User;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.email)
    TextView email;


    @BindView(R.id.location_plus)
    ImageView location_pius;
    @BindView(R.id.theme_plus)
    ImageView theme_pius;

    ArrayList<String> countryList = new ArrayList<String>();
    ArrayList<String> themeList = new ArrayList<String>();
    ArrayAdapter<String> countryAdapter;
    ArrayAdapter<String> themeAdapter;

    RetrofitUsers mRetrofitUsers;

    User mUser;

    private String mId;

    private static final String TAG = ProfileFragment.class.getSimpleName();


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);

        ListView countryListView = (ListView) view.findViewById(R.id.country_list);
        ListView themeListView = (ListView) view.findViewById(R.id.theme_list);
        countryAdapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, countryList);
        themeAdapter = new ArrayAdapter<String>(inflater.getContext(), android.R.layout.simple_list_item_1, themeList);
        countryListView.setAdapter(countryAdapter);
        themeListView.setAdapter(themeAdapter);


        mRetrofitUsers = RetrofitUsers.getInstance(getActivity().getBaseContext()).createBaseApi();

        themeListView.setOnItemClickListener(themeListener);
        countryListView.setOnItemClickListener(countryListener);

        setProfile();
        setClickEvent();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        setProfile();
    }

    private void setProfile() {
        mId = getActivity().getIntent().getStringExtra("id");

        mRetrofitUsers.getUser(mId, new ApiCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e(TAG, t.toString());
            }

            @Override
            public void onSuccess(int code, Object receiveData) {
                Log.i(TAG, String.valueOf(code));
                mUser = (User) receiveData;
                username.setText(mUser.getUsername());
                email.setText(mUser.getEmail());
                countryList.add(mUser.getTheme());
                countryList.add(mUser.getCounrty());

            }

            @Override
            public void onFailure(int code) {
                Log.i(TAG, String.valueOf(code));
            }
        });
    }

    private void deleteTheme() {
        mId = getActivity().getIntent().getStringExtra("id");

        mRetrofitUsers.deleteTheme(mId, new ApiCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e(TAG, t.toString());
            }

            @Override
            public void onSuccess(int code, Object receiveData) {

                themeList.clear();
                themeAdapter.notifyDataSetChanged();


            }

            @Override
            public void onFailure(int code) {
                Log.i(TAG, String.valueOf(code));
            }
        });
    }

    private void deleteCountry() {
        mId = getActivity().getIntent().getStringExtra("id");

        mRetrofitUsers.deleteCountry(mId, new ApiCallback() {
            @Override
            public void onError(Throwable t) {
                Log.e(TAG, t.toString());
            }

            @Override
            public void onSuccess(int code, Object receiveData) {

                countryList.clear();
                countryAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(int code) {
                Log.i(TAG, String.valueOf(code));
            }
        });
    }

    private void setClickEvent() {
        username.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModifyActivity.class);
                intent.putExtra("content", "이름");
                intent.putExtra("id", mId);
                startActivity(intent);
            }
        });

        email.setOnClickListener(new TextView.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ModifyActivity.class);
                intent.putExtra("content", "이메일");
                intent.putExtra("id", mId);
                startActivity(intent);
            }
        });

        location_pius.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (themeList.get(0) != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage("관심지역이 설정되어있습니다.");

                    builder.setNegativeButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();
                } else {
                    Intent intent = new Intent(getActivity(), CountryActivity.class);
                    startActivity(intent);
                }
            }

        });

        theme_pius.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (countryList.get(0) != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    builder.setMessage("관심테마가 설정되어있습니다.");

                    builder.setNegativeButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();
                } else {
                    Intent intent = new Intent(getActivity(), ThemeActivity.class);
                    startActivity(intent);
                }
            }
        });


    }

    AdapterView.OnItemClickListener themeListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(themeList.get(position));
            builder.setMessage("관심테마에서 삭제");
            builder.setPositiveButton("삭제",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteTheme();
                        }
                    });
            builder.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            builder.show();
        }
    };


    AdapterView.OnItemClickListener countryListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View v, final int position, long id) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle(countryList.get(position));
            builder.setMessage("관심지역에서 삭제");
            builder.setPositiveButton("삭제",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            deleteCountry();

                        }
                    });
            builder.setNegativeButton("취소",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
//                            Toast.makeText(getActivity(),"취소",Toast.LENGTH_LONG).show();
                        }
                    });
            builder.show();
        }
    };


}
