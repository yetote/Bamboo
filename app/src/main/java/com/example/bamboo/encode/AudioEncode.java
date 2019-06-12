package com.example.bamboo.encode;

import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.util.Log;

import com.example.bamboo.util.WriteFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.encode
 * @class describe
 * @time 2019/5/31 14:49
 * @change
 * @chang time
 * @class describe
 */
public class AudioEncode {
    private int sampleRate, channelCount;
    private MediaFormat mediaFormat;
    private MediaCodec mediaCodec;
    private WriteFile writeFile;
    private BlockingQueue<byte[]> audioQueue;
    private byte[] audioData;
    private Thread thread;
    private boolean isRecording;
    private MediaCodec.BufferInfo bufferInfo;
    private long startPts;
    private static final String TAG = "AudioEncode";

    public AudioEncode(int sampleRate, int channelCount, String path) {
        this.sampleRate = sampleRate;
        this.channelCount = channelCount;
        writeFile = new WriteFile(path);
        audioQueue = new LinkedBlockingQueue<>();
        mediaFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, sampleRate, channelCount);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, sampleRate * channelCount);
        mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
        mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 512 * 1024);
        try {
            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
            mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        bufferInfo = new MediaCodec.BufferInfo();
//        thread = new Thread(() -> {
//            while (isRecording) {
//                int flag = 0;
//                int inputIndex = mediaCodec.dequeueInputBuffer(-1);
//                if (inputIndex < 0) {
//                    continue;
//                }
//                ByteBuffer inputBuffer = mediaCodec.getInputBuffer(inputIndex);
//                if (inputBuffer == null) {
//                    continue;
//                }
//                inputBuffer.clear();
//                try {
//                    audioData = audioQueue.take();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                inputBuffer.put(audioData);
//                if (!isRecording && audioQueue.isEmpty()) {
//                    flag = MediaCodec.BUFFER_FLAG_END_OF_STREAM;
//                }
//                mediaCodec.queueInputBuffer(inputIndex, 0, sampleRate * channelCount, 0, flag);
//                int outputIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
//                while (outputIndex >= 0) {
//                    ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(outputIndex);
//                    if (outputBuffer != null) {
//                        outputBuffer.position(bufferInfo.offset);
//                        outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
//                        byte[] audioData = new byte[bufferInfo.size + 7];
//                        outputBuffer.get(audioData, 7, bufferInfo.size);
//                        addADTStoPacket(audioData, bufferInfo.size + 7);
//                        writeFile.write(audioData);
//                    }
//                    mediaCodec.releaseOutputBuffer(outputIndex, false);
//                    outputIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
//                }
//            }
//        });
    }

    public void startEncode(MutexUtil mutexUtil) {
        mediaCodec.start();
        startPts = System.nanoTime();
        new Thread(() -> {
            int endFlag = 0;
            while (endFlag != MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                int flag = 0;
                int inputIndex = mediaCodec.dequeueInputBuffer(-1);
                if (inputIndex < 0) {
                    continue;
                }
                ByteBuffer inputBuffer = mediaCodec.getInputBuffer(inputIndex);
                if (inputBuffer == null) {
                    continue;
                }
                inputBuffer.clear();
                try {
                    audioData = audioQueue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                inputBuffer.put(audioData);
                Log.e(TAG, "audioStartEncode:结束标志 " + isRecording + audioQueue.isEmpty());
                if (!isRecording && audioQueue.isEmpty()) {
                    flag = MediaCodec.BUFFER_FLAG_END_OF_STREAM;
                    Log.e(TAG, "startEncode: 音频最后一帧");
                }
                mediaCodec.queueInputBuffer(inputIndex, 0, sampleRate * channelCount, (System.nanoTime() - startPts) / 1000, flag);
                int outputIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
//                Log.e(TAG, "startEncode: outputindex" + outputIndex);
                if (outputIndex == -2) {
                    mutexUtil.addFormat(mediaCodec.getOutputFormat(), true);
                }
                if (mutexUtil.getState()) {
                    while (outputIndex >= 0) {
                        ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(outputIndex);
                        if (outputBuffer != null) {
                            outputBuffer.position(bufferInfo.offset);
                            outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
//                        byte[] audioData = new byte[bufferInfo.size + 7];
//                        outputBuffer.get(audioData, 7, bufferInfo.size);
//                        addADTStoPacket(audioData, bufferInfo.size + 7);
//                        writeFile.write(audioData);
                            mutexUtil.pushData(outputBuffer, true, bufferInfo);
                        }
//                    Log.e(TAG, "startEncode: outputindex" + outputIndex);
                        mediaCodec.releaseOutputBuffer(outputIndex, false);
                        outputIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
                        endFlag = bufferInfo.flags;
                        Log.e(TAG, "startEncode: 音频endflag" + endFlag);
                    }
                }
            }
            Log.e(TAG, "startEncode: 音频结束录制");
            mutexUtil.stop(true);

        }).start();
    }


    public void pushData(byte[] audioData) {
        try {
            audioQueue.put(audioData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public MediaFormat getMediaFormat() {
        return mediaFormat;
    }

    public void setRecording(boolean recording) {
        this.isRecording = recording;
    }

    private static void addADTStoPacket(byte[] packet, int packetLen) {
        // AAC LC
        int profile = 2;
        // 48.0KHz
        int freqIdx = 3;
        // CPE
        int chanCfg = 2;

        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF1;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) | 0x1F);
        packet[6] = (byte) 0xFC;
    }
}
