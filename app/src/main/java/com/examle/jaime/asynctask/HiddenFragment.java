package com.examle.jaime.asynctask;


import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import java.util.Random;


public class HiddenFragment extends Fragment {
    private static final int MAX_LENGTH = 1000;
    public static final String TAG = "hiddenfragment";

    private int[] mNumbers;
    private ProgressBarTask mProgressBarTask;
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

        mProgressBarTask = new ProgressBarTask();
        mProgressBarTask.execute();
    }


    private void generateNumbers() {
        Random rnd = new Random();

        for (int i = 0; i < MAX_LENGTH; i++)
            mNumbers[i] = rnd.nextInt();
    }


    public void cancelTask() {
        if (mProgressBarTask != null)
            mProgressBarTask.cancel(true);
    }


    public ProgressBarTask getProgressBarTask() {
        return mProgressBarTask;
    }


    public class ProgressBarTask extends AsyncTask<Void, Integer, Void> {


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
