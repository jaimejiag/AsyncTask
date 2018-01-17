package com.examle.jaime.asynctask;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static final int MAX_LENGTH = 2000;

    private TextView txvMessage;
    private Button btnSort;
    private Button btnCancel;

    private ProgressBar mProgressBar;
    private int[] mNumbers;
    SimpleAsyncTask mAsyncTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txvMessage = findViewById(R.id.txv_message);
        btnSort = findViewById(R.id.btn_order);
        btnCancel = findViewById(R.id.btn_cancel);
        mNumbers = new int[MAX_LENGTH];

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAsyncTask.cancel(true);
            }
        });

        generateNumbers();
    }


    @Override
    protected void onPause() {
        super.onPause();

        if (mAsyncTask != null)
        mAsyncTask.cancel(true);
    }


    private void generateNumbers() {
        Random rnd = new Random();

        for (int i = 0; i < MAX_LENGTH; i++)
            mNumbers[i] = rnd.nextInt();
    }


    public void onClickSort(View view) {
        //OPCION 1: La mala.
        //bubbleSort(mNumbers);
        //txvMessage.setText("Operación terminada");

        //OPCION 2: Crear un hilo.
        //execWithThread();

        mAsyncTask = new SimpleAsyncTask();
        mAsyncTask.execute();
    }


    private void execWithThread() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                bubbleSort();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        txvMessage.setText("Operación terminada");
                    }
                });
            }
        }).start();
    }


    private void bubbleSort() {
        for(int i = 0; i < mNumbers.length - 1; i++) {

            for(int j = 0; j < mNumbers.length - 1; j++) {

                if (mNumbers[j] < mNumbers[j + 1]) {
                    int tmp = mNumbers[j+1];
                    mNumbers[j+1] = mNumbers[j];
                    mNumbers[j] = tmp;
                }
            }
        }
    }


    private class SimpleAsyncTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            btnSort.setEnabled(false);
            btnCancel.setVisibility(View.VISIBLE);
        }


        @Override
        protected Void doInBackground(Void... voids) {
            for(int i = 0; i < mNumbers.length - 1; i++) {

                for (int j = 0; j < mNumbers.length - 1; j++) {

                    if (mNumbers[j] < mNumbers[j + 1]) {
                        int tmp = mNumbers[j + 1];
                        mNumbers[j + 1] = mNumbers[j];
                        mNumbers[j] = tmp;

                    }

                    if (!isCancelled())
                        publishProgress((i * 100) / mNumbers.length);
                }
            }

            return null;
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            txvMessage.setText(values[0] + "%");
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            btnSort.setEnabled(true);
            btnCancel.setVisibility(View.INVISIBLE);
            txvMessage.setText("Operación terminada");
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();

            btnSort.setEnabled(true);
            btnCancel.setVisibility(View.INVISIBLE);
            txvMessage.setText("Operación cancelada");
        }
    }
}
