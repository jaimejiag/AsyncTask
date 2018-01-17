package com.examle.jaime.asynctask;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;


public class HiddenFragment extends Fragment {
    private static final int MAX_LENGTH = 2000;

    private ProgressBar mProgressBar;
    private int[] mNumbers;
    private ProgressBasTask mProgressBasTask;
    private TaskCallbacks mCallback;


    interface TaskCallbacks {
        void onPreExecute();
        void onProgressUpdate(int i);
        void onCancelled();
        void onPostExecute();
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (TaskCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().getLocalClassName() + " must implement TaskCallbacks");
        }
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mNumbers = new int[MAX_LENGTH];
        generateNumbers();

        mProgressBasTask = new ProgressBasTask();
        mProgressBasTask.execute();
    }


    private void generateNumbers() {
        Random rnd = new Random();

        for (int i = 0; i < MAX_LENGTH; i++)
            mNumbers[i] = rnd.nextInt();
    }


    public class ProgressBasTask extends AsyncTask<Void, Integer, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (mCallback != null)
                mCallback.onPreExecute();
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

            if (mCallback != null)
                mCallback.onProgressUpdate(values[0]);
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (mCallback != null)
                mCallback.onPostExecute();
        }


        @Override
        protected void onCancelled() {
            super.onCancelled();

            if (mCallback != null)
                mCallback.onCancelled();
        }
    }
}
