package com.example.nothingexpo;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nothing.ketchum.Common;
import com.nothing.ketchum.GlyphException;
import com.nothing.ketchum.GlyphFrame;
import com.nothing.ketchum.GlyphManager;

public class MainActivity extends AppCompatActivity {
    private GlyphManager mGM = null;
    private GlyphManager.Callback mCallback = null;

    private Button channelABtn;
    private Button channelBBtn;
    private Button channelCBtn;
    private Button channelDBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        mGM = GlyphManager.getInstance(getApplicationContext());
        mGM.init(mCallback);

        channelABtn = findViewById(R.id.channelA_btn);
        channelBBtn = findViewById(R.id.channelB_btn);
        channelCBtn = findViewById(R.id.channelC_btn);
        channelDBtn = findViewById(R.id.channelD_btn);


        initView();
    }

    @Override
    public void onDestroy() {
        try {
            mGM.closeSession();
        } catch (GlyphException e) {
            Log.e(TAG, e.getMessage());
        }
        mGM.unInit();
        super.onDestroy();
    }

    private void init() {
        mCallback = new GlyphManager.Callback() {
            @Override
            public void onServiceConnected(ComponentName componentName) {
                if (Common.is20111()) mGM.register(Common.DEVICE_20111);
                if (Common.is22111()) mGM.register(Common.DEVICE_22111);
                if (Common.is23111()) mGM.register(Common.DEVICE_23111);
                try {
                    mGM.openSession();
                } catch(GlyphException e) {
                    Log.e(TAG, e.getMessage());
                }
            }
            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                try {
                    mGM.closeSession();
                } catch (GlyphException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }

    private void initView() {
        channelABtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlyphFrame.Builder builder = mGM.getGlyphFrameBuilder();
                GlyphFrame frame = builder.buildChannelA().build();
                mGM.toggle(frame);
            }
        });
        channelBBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlyphFrame.Builder builder = mGM.getGlyphFrameBuilder();
                GlyphFrame frame = builder.buildChannelB().buildInterval(10)
                        .buildCycles(2).buildPeriod(3000).build();
                mGM.animate(frame);
            }
        });
        channelCBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GlyphFrame.Builder builder = mGM.getGlyphFrameBuilder();
                GlyphFrame frame = builder.buildChannelC().build();
                mGM.animate(frame);
            }
        });
        channelDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGM.turnOff();
            }
        });
    }
}