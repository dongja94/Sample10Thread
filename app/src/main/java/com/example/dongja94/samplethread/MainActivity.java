package com.example.dongja94.samplethread;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView messageView;
    ProgressBar progressView;

    public static final int MESSAGE_PROGRESS = 0;
    public static final int MESSAGE_COMPLETE = 1;

    Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_PROGRESS :
                    int progress = msg.arg1;
                    messageView.setText("current progress : " + progress);
                    progressView.setProgress(progress);
                    break;
                case MESSAGE_COMPLETE :
                    messageView.setText("progress done");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        messageView = (TextView)findViewById(R.id.text_message);
        progressView = (ProgressBar)findViewById(R.id.progress_download);

        Button btn = (Button)findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressView.setMax(100);
                messageView.setText("start download...");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        int count = 0;
                        while(count <= 100) {
//                            messageView.setText("current progress : " + count);
//                            progressView.setProgress(count);

//                            Message msg = mHandler.obtainMessage(MESSAGE_PROGRESS, count, 0);
//                            mHandler.sendMessage(msg);

                            mHandler.post(new ProgressRunnable(count));

                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            count += 5;
                        }

//                        messageView.setText("progress done");
//                        mHandler.sendEmptyMessage(MESSAGE_COMPLETE);
                        mHandler.post(new CompleteRunnable());

                    }
                }).start();
            }
        });
    }

    class ProgressRunnable implements Runnable {
        int progress;
        public ProgressRunnable(int progress) {
            this.progress = progress;
        }

        @Override
        public void run() {
            messageView.setText("current progress : " + progress);
            progressView.setProgress(progress);
        }
    }

    class CompleteRunnable implements Runnable {
        @Override
        public void run() {
            messageView.setText("progress done");
        }
    }
}
