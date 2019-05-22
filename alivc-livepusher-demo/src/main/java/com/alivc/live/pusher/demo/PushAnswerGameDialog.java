package com.alivc.live.pusher.demo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.alivc.live.pusher.AlivcLivePusher;

public class PushAnswerGameDialog extends DialogFragment {

    private AlivcLivePusher mAlivcLivePusher = null;
    private RecyclerView mAnswerView;
    private AnswerAdapter mAnswerAdapter;

    public static PushAnswerGameDialog newInstance() {
        PushAnswerGameDialog pushAnswerGameDialog = new PushAnswerGameDialog();
        return pushAnswerGameDialog;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        getDialog().setCanceledOnTouchOutside(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.DialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.push_answer, container);
        mAnswerView = (RecyclerView) view.findViewById(R.id.answer_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        if(mAnswerAdapter == null) {
            mAnswerAdapter = new AnswerAdapter(getActivity());
            mAnswerAdapter.setAlivcLivePusher(mAlivcLivePusher);
        }
        mAnswerView.setLayoutManager(layoutManager);
        mAnswerView.setAdapter(mAnswerAdapter);
        mAnswerView.addItemDecoration(new DividerItemDecoration(
                getActivity(), LinearLayoutManager.HORIZONTAL));
        return view;
    }

    @Override
    public void onResume() {
        getDialog().getWindow().setGravity(Gravity.CENTER);
        super.onResume();

        DisplayMetrics dpMetrics = new DisplayMetrics();
        getActivity().getWindow().getWindowManager().getDefaultDisplay().getMetrics(dpMetrics);
        WindowManager.LayoutParams p = getDialog().getWindow().getAttributes();

        p.width = dpMetrics.widthPixels;
        p.height = dpMetrics.heightPixels/2;
        getDialog().getWindow().setAttributes(p);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }

    public void setAlivcLivePusher(AlivcLivePusher alivcLivePusher) {
        this.mAlivcLivePusher = alivcLivePusher;
    }
}
