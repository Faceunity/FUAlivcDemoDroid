package com.alivc.live.pusher.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alivc.live.pusher.AlivcLivePushStatsInfo;

public class PushTextStatsFragment extends Fragment{
    public static final String TAG = "PushTextStatsFragment";

    private LogInfoAdapter mAdapter;
    private RecyclerView mLogRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.push_text_log, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLogRecyclerView = (RecyclerView) view.findViewById(R.id.log_recycler);

        mAdapter = new LogInfoAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mLogRecyclerView.setLayoutManager(layoutManager);
        mAdapter.setHasStableIds(true);
        mLogRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    public void updateValue(AlivcLivePushStatsInfo alivcLivePushStatsInfo) {
        if(mAdapter != null) {
            mAdapter.updateValue(alivcLivePushStatsInfo);
        }
    }
}
