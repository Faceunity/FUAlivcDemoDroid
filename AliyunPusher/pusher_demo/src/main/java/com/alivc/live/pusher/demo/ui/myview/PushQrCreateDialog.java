package com.alivc.live.pusher.demo.ui.myview;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.alivc.live.pusher.demo.R;
import com.aliyun.pusher.core.utils.ScreenUtils;
import com.google.zxing.WriterException;
import com.google.zxing.encoding.EncodingHandler;

/**
 * 设置界面 生成二维码 dialog
 */
public class PushQrCreateDialog extends DialogFragment{
//    public PushQrCreateDialog g
    private static final String ARG_URL = "url";
    private String url;
    public PushQrCreateDialog() {
        // Required empty public constructor
    }
    public static PushQrCreateDialog newInstance(String pullUrl) {
        PushQrCreateDialog fragment = new PushQrCreateDialog();
        Bundle args = new Bundle();
        args.putString(ARG_URL, pullUrl);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            url = getArguments().getString(ARG_URL);
        }
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setBackgroundDrawableResource(R.color.darker_gray);
        getDialog().setCanceledOnTouchOutside(true);


    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.push_qr_create,container);
        ImageView ivQrCore = view.findViewById(R.id.iv_qr_core);
        if(url != null && !"".equals(url)){
            //根据输入的文本生成对应的二维码并且显示出来
            Bitmap mBitmap = null;
            try {
                int width = (int) (ScreenUtils.getWidth(getContext())*0.7);
                mBitmap = EncodingHandler.createQRCode(url, width);
            } catch (WriterException e) {
                e.printStackTrace();
            }
            if(mBitmap != null){
                Toast.makeText(getContext(),"二维码生成成功！",Toast.LENGTH_SHORT).show();
                ivQrCore.setImageBitmap(mBitmap);
            }
        }else{
            Toast.makeText(getContext(),"文本信息不能为空！",Toast.LENGTH_SHORT).show();
        }
        Button btnOk = view.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        return view;
    }

}