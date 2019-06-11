package com.example.bamboo.encode;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.encode
 * @class describe
 * @time 2019/5/31 15:28
 * @change
 * @chang time
 * @class describe
 */
public class AudioRecordUtil {
    private AudioRecord audioRecord;
    private int sampleRate;
    private int channelLayout;
    private byte[] audioData;
    private static final String TAG = "AudioRecordUtil";
    private boolean isRecording;
    private Thread thread;

    public AudioRecordUtil(int sampleRate, int channelCount) {
        switch (channelCount) {
            case 1:
                channelLayout = AudioFormat.CHANNEL_IN_MONO;
                break;
            case 2:
                channelLayout = AudioFormat.CHANNEL_IN_STEREO;
                break;
            default:
                channelLayout = -1;
                break;
        }
        if (channelLayout != -1) {
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT, AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_STEREO, AudioFormat.ENCODING_PCM_16BIT) * 2);
        }
        audioData = new byte[sampleRate * channelCount];
        thread = new Thread(() -> {
            while (isRecording) {
                int rst = audioRecord.read(audioData, 0, audioData.length);
                Log.e(TAG, "run: 录制了" + rst + "个字节");
            }
        });
    }

    public void startRecord() {
        isRecording = true;
        Log.e(TAG, "startRecord: 开始录制");
        audioRecord.startRecording();
        thread.start();
    }

}
