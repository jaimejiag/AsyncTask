package com.examle.jaime.asynctask;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hidden);

        txvMessage = findViewById(R.id.txv_message);
        btnSort = findViewById(R.id.btn_order);
        btnCancel = findViewById(R.id.btn_cancel);
        mProgressBar = findViewById(R.id.progressBar);
    }


    @Override
    public void onPreExecute() {

    }


    @Override
    public void onProgressUpdate(int i) {

    }


    @Override
    public void onCancelled() {

    }


    @Override
    public void onPostExecute() {

    }


    public void onClickSort(View view) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment fragment = new HiddenFragment();
        transaction.add(fragment, "hiddenfragment").commit();
    }
}
