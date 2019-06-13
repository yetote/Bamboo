package com.example.bamboo.encode;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaFormat;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.Toast;

import com.example.bamboo.util.WriteFile;

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
    private AudioEncode audioEncode;

    public AudioRecordUtil(int sampleRate, int channelCount, String path) {
        this.sampleRate = sampleRate;
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

        audioData = new byte[sampleRate * channelCount];
        audioEncode = new AudioEncode(sampleRate, channelCount, path);
        thread = new Thread(() -> {
            while (isRecording) {
                audioRecord.read(audioData, 0, audioData.length);
                Log.e(TAG, "AudioRecordUtil: 获取数据" + isRecording);
                audioEncode.pushData(audioData);
            }
            audioRecord.stop();
            Log.e(TAG, "AudioRecordUtil: 停止");
        });
    }

    public void startRecord(MutexUtil mutexUtil) {
        if (channelLayout != -1 && audioRecord == null) {
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelLayout, AudioFormat.ENCODING_PCM_16BIT, AudioRecord.getMinBufferSize(sampleRate, channelLayout, AudioFormat.ENCODING_PCM_16BIT) * 2);
        }
        if (audioRecord.getState() == AudioRecord.STATE_UNINITIALIZED) {
            Log.e(TAG, "startRecord: audioRecord未初始化成功");
            return;
        }
        isRecording = true;
        Log.e(TAG, "startRecord: 开始录制");
        audioRecord.startRecording();
        thread.start();
        audioEncode.setRecording(true);
        audioEncode.startEncode(mutexUtil);
    }

    public void stop() {
        isRecording = false;
        audioEncode.setRecording(false);
//        audioRecord.stop();
    }

    public MediaFormat getAudioFormat() {
        if (audioEncode != null) {
            return audioEncode.getMediaFormat();
        }
        return null;
    }
}
