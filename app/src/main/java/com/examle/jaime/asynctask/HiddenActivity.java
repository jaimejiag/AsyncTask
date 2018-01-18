package com.examle.jaime.asynctask;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HiddenActivity extends AppCompatActivity implements HiddenFragment.TaskCallbacks {
    private TextView txvMessage;
    private Button btnSort;
    private Button btnCancel;

    private ProgressBar mProgressBar;
    private HiddenFragment mFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden);

        txvMessage = findViewById(R.id.txv_message2);
        btnSort = findViewById(R.id.btn_order);
        btnCancel = findViewById(R.id.btn_cancel);
        mProgressBar = findViewById(R.id.progressBar);

        mFragment = (HiddenFragment) getSupportFragmentManager().findFragmentByTag(HiddenFragment.TAG);

        if (mFragment != null) {
            if (mFragment.getProgressBarTask().getStatus() == AsyncTask.Status.RUNNING) {
                btnSort.setEnabled(false);
                btnCancel.setVisibility(View.VISIBLE);
                txvMessage.setText("");
            }
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFragment != null)
                    mFragment.cancelTask();
            }
        });
    }


    @Override
    public void onPreExecute() {
        btnSort.setEnabled(false);
        btnCancel.setVisibility(View.VISIBLE);
    }


    @Override
    public void onProgressUpdate(int i) {
        if (mProgressBar != null)
            mProgressBar.setProgress(i);

        if (txvMessage != null)
            txvMessage.setText(i + "%");
    }


    @Override
    public void onCancelled() {
        btnSort.setEnabled(true);
        btnCancel.setVisibility(View.INVISIBLE);
        txvMessage.setText("Operación cancelada");
    }


    @Override
    public void onPostExecute() {
        btnSort.setEnabled(true);
        btnCancel.setVisibility(View.INVISIBLE);
        txvMessage.setText("Operación terminada");
    }


    public void onClickSort(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        mFragment = new HiddenFragment();
        transaction.add(mFragment, HiddenFragment.TAG).commit();
    }
}
