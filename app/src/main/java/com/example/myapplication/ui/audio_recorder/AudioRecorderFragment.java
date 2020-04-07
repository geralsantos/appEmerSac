package com.example.myapplication.ui.audio_recorder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.myapplication.R;
import com.example.myapplication.config;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AudioRecorderFragment extends Fragment {

    private AudioRecorderViewModel slideshowViewModel;

    private static final String LOG_TAG = "AudioRecordTest";
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private static String fileName = null;

    //private RecordButton recordButton = null;
    private MediaRecorder recorder = null;

    //private PlayButton   playButton = null;
    private MediaPlayer player = null;

    // Requesting permission to RECORD_AUDIO
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    config conf = new config();

    Button btn_iniciar_grabar_audio;

    TextView tvTimeRecord;
    long startTime = 0;
    Handler timerHandler = new Handler();
    boolean mStartRecording = true;
    boolean mPauseRecording = false;
    Integer proyecto_id=null;
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            tvTimeRecord.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(AudioRecorderViewModel.class);
        View root = inflater.inflate(R.layout.fragment_audio_recorder, container, false);
        // final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                // textView.setText(s);
            }
        });
        return root;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) getActivity().finish();

    }

    private void onRecord(boolean start) {
        if (start) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void onPlay(boolean start) {
        if (start) {
            startPlaying();
        } else {
            stopPlaying();
        }
    }

    private void startPlaying() {
        player = new MediaPlayer();
        try {
            player.setDataSource(fileName);
            player.prepare();
            player.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        player.release();
        player = null;
    }

    private void startRecording() {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setOutputFile(fileName);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }

        recorder.start();
    }

    private void stopRecording() {
        recorder.stop();
        recorder.release();
        recorder = null;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle args = getArguments();
        proyecto_id = args.getInt("proyecto_id", 0);
        // Record to the external cache directory for visibility
        String root = conf.getRutaArchivos()+proyecto_id+"/";
        Calendar c = Calendar.getInstance(); SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String strDate = sdf.format(c.getTime());
        fileName = root+"Audios/";
        fileName += "Audio_"+strDate+".3gp";

        ActivityCompat.requestPermissions(getActivity(), permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        tvTimeRecord = (TextView) getActivity().findViewById(R.id.tvTimeRecord);

        btn_iniciar_grabar_audio = (Button) getActivity().findViewById(R.id.btn_iniciar_grabar_audio);
        btn_iniciar_grabar_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnActionInitRecord();
            }
        });

    }
    public void btnActionInitRecord(){
            if (mStartRecording) {
                onRecord(mStartRecording);
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
                btn_iniciar_grabar_audio.setText("Detener Grabación");
            } else {
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
                timerHandler.removeCallbacks(timerRunnable);
                //timerHandler.postDelayed(timerRunnable, 0);
                stopRecording();
                btn_iniciar_grabar_audio.setText("Iniciar Grabación");
                Toast.makeText(getActivity().getApplicationContext(), "Grabación guardada.", Toast.LENGTH_LONG).show();
            }
            mStartRecording = !mStartRecording;
    }
    @Override
    public void onStop() {
        super.onStop();
        if (recorder != null) {
            recorder.release();
            recorder = null;
        }

        if (player != null) {
            player.release();
            player = null;
        }

    }
    /*private boolean isStoragePermissionGranted(){
        if (Build.VERSION.SDK_INT >= 23) {
            if (getActivity().checkSelfPermission(android.Manifest.permission.RECORD_AUDIO)
                    == PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(getActivity().getApplicationContext(), "Permisos habilitados", Toast.LENGTH_LONG).show();
                return true;
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.RECORD_AUDIO}, 2);
                Toast.makeText(getActivity().getApplicationContext(), "Permisos revocados, intente de nuevo", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Toast.makeText(getActivity().getApplicationContext(), "Permisos habilitados", Toast.LENGTH_LONG).show();
            return true;
        }
    }*/
}