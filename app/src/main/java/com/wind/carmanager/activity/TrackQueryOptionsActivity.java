package com.wind.carmanager.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.baidu.trace.model.SortType;
import com.baidu.trace.api.track.SupplementMode;
import com.baidu.trace.model.CoordType;
import com.baidu.trace.model.TransportMode;
import com.wind.carmanager.MyApplication;
import com.wind.carmanager.base.BaseActivity;
import com.wind.carmanager.dialog.DateDialog;
import com.wind.carmanager.utils.CommonUtil;
import com.wind.carmanager.utils.Constants;
import com.wind.carmanager.R;

import java.text.SimpleDateFormat;

public class TrackQueryOptionsActivity extends BaseActivity {
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    private MyApplication trackApp = null;
    private Intent result = null;
    private DateDialog dateDialog = null;
    private Button startTimeBtn = null;
    private Button endTimeBtn = null;
    private DateDialog.Callback startTimeCallback = null;
    private DateDialog.Callback endTimeCallback = null;
    private long startTime = CommonUtil.getCurrentTime();
    private long endTime = CommonUtil.getCurrentTime();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trackquery_options);
        setTitle(R.string.track_query_options_title);
//        setOptionsButtonInVisible();
        init();
        trackApp = (MyApplication) getApplication();
    }

    private void init() {
        result = new Intent();
        startTimeBtn = (Button) findViewById(R.id.start_time);
        endTimeBtn = (Button) findViewById(R.id.end_time);

        StringBuilder startTimeBuilder = new StringBuilder();
        startTimeBuilder.append(getResources().getString(R.string.start_time));
        startTimeBuilder.append(simpleDateFormat.format(System.currentTimeMillis()));
        startTimeBtn.setText(startTimeBuilder.toString());

        StringBuilder endTimeBuilder = new StringBuilder();
        endTimeBuilder.append(getResources().getString(R.string.end_time));
        endTimeBuilder.append(simpleDateFormat.format(System.currentTimeMillis()));
        endTimeBtn.setText(endTimeBuilder.toString());
    }

    public void onStartTime(View v) {
        if (null == startTimeCallback) {
            startTimeCallback = new DateDialog.Callback() {
                @Override
                public void onDateCallback(long timeStamp) {
                    TrackQueryOptionsActivity.this.startTime = timeStamp;
                    StringBuilder startTimeBuilder = new StringBuilder();
                    startTimeBuilder.append(getResources().getString(R.string.start_time));
                    startTimeBuilder.append(simpleDateFormat.format(timeStamp * 1000));
                    startTimeBtn.setText(startTimeBuilder.toString());
                }
            };
        }
        if (null == dateDialog) {
            dateDialog = new DateDialog(this, startTimeCallback);
        } else {
            dateDialog.setCallback(startTimeCallback);
        }
        dateDialog.show();
    }

    public void onEndTime(View v) {
        if (null == endTimeCallback) {
            endTimeCallback = new DateDialog.Callback() {
                @Override
                public void onDateCallback(long timeStamp) {
                    TrackQueryOptionsActivity.this.endTime = timeStamp;
                    StringBuilder endTimeBuilder = new StringBuilder();
                    endTimeBuilder.append(getResources().getString(R.string.end_time));
                    endTimeBuilder.append(simpleDateFormat.format(timeStamp * 1000));
                    endTimeBtn.setText(endTimeBuilder.toString());
                }
            };
        }
        if (null == dateDialog) {
            dateDialog = new DateDialog(this, endTimeCallback);
        } else {
            dateDialog.setCallback(endTimeCallback);
        }
        dateDialog.show();
    }

    public void onCancel(View v) {
        super.onBackPressed();
    }

    public void onFinish(View v) {
        result.putExtra("startTime", startTime);
        result.putExtra("endTime", endTime);
        result.putExtra("processed", true);
        result.putExtra("denoise", true);
        result.putExtra("vacuate", true);
        result.putExtra("mapmatch", true);

        result.putExtra("radius", 10);
        TransportMode transportMode = TransportMode.riding;
        result.putExtra("transportMode", transportMode.name());
        SupplementMode supplementMode = SupplementMode.driving;
        result.putExtra("supplementMode", supplementMode.name());

        RadioGroup sortTypeGroup = (RadioGroup) findViewById(R.id.sort_type);
        RadioButton sortTypeRadio = (RadioButton) findViewById(sortTypeGroup.getCheckedRadioButtonId());
        SortType sortType = SortType.asc;
        switch (sortTypeRadio.getId()) {
            case R.id.asc:
                sortType = SortType.asc;
                break;

            case R.id.desc:
                sortType = SortType.desc;
                break;

            default:
                break;
        }
        result.putExtra("sortType", sortType.name());

        CoordType coordTypeOutput = CoordType.bd09ll;
        result.putExtra("coordTypeOutput", coordTypeOutput.name());

        setResult(Constants.RESULT_CODE, result);
        super.onBackPressed();
    }

}