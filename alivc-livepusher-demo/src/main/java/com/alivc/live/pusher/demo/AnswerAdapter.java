package com.alivc.live.pusher.demo;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alivc.live.pusher.AlivcLivePusher;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.AnswerViewHolder> {

    private static final int MAX_CHATS = 4000;

    private Context mContext;
    private AlivcLivePusher mAlivcLivePusher = null;

    private List<QuestionInfo> mQuestionList = new ArrayList<>();
    private List<AnswerInfo> mAnswerList = new ArrayList<>();
    private String mLiveId;
    private ScheduledExecutorService mExecutorService;

    public AnswerAdapter(Context context) {
        this.mContext = context;
        QuestionData questionData = getQuestion(mContext);
        if(questionData != null) {
            mQuestionList = questionData.getmContents();
        }
        AnswerData answerData = getAnswer(mContext);
        if(answerData != null) {
            mAnswerList = answerData.getmContents();
        }
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_item_info, parent, false);
        AnswerViewHolder holder = new AnswerViewHolder(itemView);
        holder.questionInfo = (TextView) itemView.findViewById(R.id.question_info);
        holder.questionId = (TextView) itemView.findViewById(R.id.question_id);
        holder.sendQuestion = (TextView) itemView.findViewById(R.id.send_question);
        holder.sendAnswer = (TextView) itemView.findViewById(R.id.send_answer);
        return holder;
    }

    @Override
    public void onBindViewHolder(final AnswerViewHolder holder, final int position) {

        final AnswerInfo answerInfo = mAnswerList.get(position);
        final QuestionInfo questionInfo = mQuestionList.get(position);
        if(answerInfo == null || questionInfo == null) {
            return;
        }
        holder.questionId.setText(questionInfo.getQuestionId());
        holder.questionInfo.setText(questionInfo.getContent());
        holder.sendQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(mAlivcLivePusher != null && mAlivcLivePusher.isPushing()) {
                    if(mAlivcLivePusher.getPushUrl() == null || "".equals(mAlivcLivePusher.getPushUrl())) {
                        return;
                    }

                    String temp = "";
                    if(!mAlivcLivePusher.getPushUrl().contains("auth_key=")) {
                        temp = mAlivcLivePusher.getPushUrl().substring(7);
                    } else {
                        temp = mAlivcLivePusher.getPushUrl().substring(7, mAlivcLivePusher.getPushUrl().indexOf("auth_key=") - 1);
                    }
                    if(temp != null && !temp.isEmpty()) {
                        mLiveId = temp.replace("push", "pull");
                    }
                    final JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("liveId", mLiveId);
                        jsonObject.put("questionId", questionInfo.getQuestionId());
                        jsonObject.put("expiredSeconds", 15);
                        jsonObject.put("seiDelay", 2000);
                        jsonObject.put("noSEI", true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if(mExecutorService == null || mExecutorService.isShutdown()) {
                        mExecutorService = new ScheduledThreadPoolExecutor(1,
                                new BasicThreadFactory.Builder().namingPattern("example-schedule-pool-%d").daemon(true).build());
                    }

                    mExecutorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            String response = "";
                            try {
                                response = HttpUrlConnectionUtil.doHttpsPost("http://101.132.137.92/mgr/pushQuestion", jsonObject.toString());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if(response != null && !"".equals(response)) {
                                JSONObject result = null;
                                try {
                                    result = new JSONObject(response);
                                    if(result != null && result.getJSONObject("result").getString("code").equals("Success")) {
                                        Gson g = new Gson();
                                        mAlivcLivePusher.sendMessage(g.toJson(questionInfo), 100, 0, false);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IllegalStateException e) {
                                    showToast("只能在推流时才能下发题目！");
                                } catch (IllegalArgumentException e) {
                                    showToast("题目最长不能超过4000字节");
                                }
                            }

                        }
                    });
                } else {
                    Toast.makeText(mContext, "只能在推流时才能下发题目！", Toast.LENGTH_LONG).show();
                }
            }
        });
        holder.sendAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Gson g = new Gson();
            try{
                mAlivcLivePusher.sendMessage(g.toJson(answerInfo), answerInfo.getShowTime() * 20, 0, false);
            } catch (IllegalStateException e) {
                Toast.makeText(mContext, "只能在推流时才能下发答案！", Toast.LENGTH_LONG).show();
            } catch (IllegalArgumentException e) {
                Toast.makeText(mContext, "答案最长不能超过4000字节", Toast.LENGTH_LONG).show();
            }
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mQuestionList.size();
    }

    class AnswerViewHolder extends RecyclerView.ViewHolder{
        TextView questionId;
        TextView questionInfo;
        TextView sendQuestion;
        TextView sendAnswer;

        public AnswerViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setAlivcLivePusher(AlivcLivePusher alivcLivePusher) {
        this.mAlivcLivePusher = alivcLivePusher;
    }


    public String getJson(String fileName,Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    private QuestionData getQuestion(Context context) {
        String json = getJson("question.json", context);
        Type type = new TypeToken<QuestionData>() {
        }.getType();
        //这里的json是字符串类型 = jsonArray.toString();
        QuestionData data = new Gson().fromJson(json, type);
        return data;
    }

    private AnswerData getAnswer(Context context) {
        String json = getJson("answer.json", context);
        Type type = new TypeToken<AnswerData>() {
        }.getType();
        //这里的json是字符串类型 = jsonArray.toString();
        AnswerData data = new Gson().fromJson(json, type);
        return data;
    }

    private void showToast(final String text) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
