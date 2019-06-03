package com.example.bamboo.encode;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

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
    }
}
