package com.example.bamboo.encode;

import android.content.Context;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.util.Log;

import com.example.bamboo.util.WriteFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;

import static android.media.MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_VBR;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.encode
 * @class describe
 * @time 2019/6/12 13:28
 * @change
 * @chang time
 * @class describe
 */
public class EncodeUtil {
    private int width, height;
    private MediaFormat audioFormat, videoFormat;
    private MediaCodec audioCodec, videoCodec;
    private BlockingQueue<byte[]> videoQueue, audioQueue;
    private Thread audioThread, videoThread;
    private boolean isRecording;
    private static final String TAG = "VideoEncode";
    private byte[] videoData;
    private MediaCodec.BufferInfo audioBufferInfo, videoBufferInfo;
    private byte[] pps;
    private WriteFile writeFile;
    private int sampleRate, channelCount;
    private byte[] audioData;
    private String path;
    private Context context;

    public EncodeUtil(Context context, int width, int height, int sampleRate, int channelCount, String path) {
        this.context = context;
        this.width = width;
        this.height = height;
        this.sampleRate = sampleRate;
        this.channelCount = channelCount;
        this.path = path;
        init();
    }

    private void init() {
        videoFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
        videoFormat.setInteger(MediaFormat.KEY_BIT_RATE, width * height * 30 * 3);
        videoFormat.setInteger(MediaFormat.KEY_BITRATE_MODE, BITRATE_MODE_VBR);
        videoFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        videoFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
        videoFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);

        audioFormat = MediaFormat.createAudioFormat(MediaFormat.MIMETYPE_AUDIO_AAC, sampleRate, channelCount);
        audioFormat.setInteger(MediaFormat.KEY_BIT_RATE, sampleRate * channelCount);
        audioFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
        audioFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 512 * 1024);

        try {
            audioCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_AUDIO_AAC);
            videoCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
        } catch (IOException e) {
            e.printStackTrace();
        }
        audioCodec.configure(audioFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        videoCodec.configure(videoFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        audioBufferInfo = new MediaCodec.BufferInfo();
        videoBufferInfo = new MediaCodec.BufferInfo();

        initCodecThread();
    }

    private void initCodecThread() {
        audioThread = new Thread(() -> {
            while (isRecording) {
                int inputIndex = audioCodec.dequeueInputBuffer(-1);
                if (inputIndex < 0) {
                    continue;
                }
                ByteBuffer inputBuffer = audioCodec.getInputBuffer(inputIndex);
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
                audioCodec.queueInputBuffer(inputIndex, 0, sampleRate * channelCount, 0, 0);
                int outputIndex = audioCodec.dequeueOutputBuffer(audioBufferInfo, 10000);
                while (outputIndex >= 0) {
                    ByteBuffer outputBuffer = audioCodec.getOutputBuffer(outputIndex);
                    if (outputBuffer != null) {
                        outputBuffer.position(audioBufferInfo.offset);
                        outputBuffer.limit(audioBufferInfo.offset + audioBufferInfo.size);
                        byte[] audioData = new byte[audioBufferInfo.size + 7];
                        outputBuffer.get(audioData, 7, audioBufferInfo.size);
                        addADTStoPacket(audioData, audioBufferInfo.size + 7);
                        writeFile.write(audioData);
                    }
                    audioCodec.releaseOutputBuffer(outputIndex, false);
                    outputIndex = audioCodec.dequeueOutputBuffer(audioBufferInfo, 10000);
                }
            }
        });
        videoThread = new Thread(() -> {
            int flag = 0;
            while (isRecording) {
                int inputIndex = videoCodec.dequeueInputBuffer(-1);
                if (inputIndex < 0) {
                    continue;
                }
                ByteBuffer inputBuffer = videoCodec.getInputBuffer(inputIndex);
                if (inputBuffer == null) {
                    continue;
                }
                inputBuffer.clear();
                try {
                    videoData = videoQueue.take();
                    if (!isRecording && videoQueue.isEmpty()) {
                        Log.e(TAG, "run: 最后一帧");
                        flag = MediaCodec.BUFFER_FLAG_END_OF_STREAM;
                    }
//                    Log.e(TAG, "run: videoSize=" + videoData.length);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                inputBuffer.put(videoData);
                videoCodec.queueInputBuffer(inputIndex, 0, videoData.length, System.currentTimeMillis(), flag);
                int outputIndex = videoCodec.dequeueOutputBuffer(videoBufferInfo, 10000);
                if (outputIndex < 0) {
                    continue;
                }
                while (outputIndex >= 0) {
                    ByteBuffer outputBuffer = videoCodec.getOutputBuffer(outputIndex);
                    if (outputBuffer == null) {
                        Log.e(TAG, "encode: 未找到编码后容器");
                        break;
                    }
                    if (pps == null) {
                        if (videoBufferInfo.flags == 2) {
                            Log.e(TAG, "run: 第一帧");
                            pps = new byte[videoBufferInfo.size];
                            Log.e(TAG, "run:第一帧长度 " + outputBuffer.limit());
                            Log.e(TAG, "run:第一帧flag " + videoBufferInfo.flags);
                            Log.e(TAG, "run: ptsSize" + pps.length);
                            outputBuffer.get(pps);
                        }
                    }
                    if (videoBufferInfo.flags == 1) {
                        Log.e(TAG, "run: 关键帧");
                        writeFile.write(pps);
                    } else {
                        outputBuffer.position(videoBufferInfo.offset);
                        outputBuffer.limit(videoBufferInfo.offset + videoBufferInfo.size);
                    }
                    writeFile.write(outputBuffer);
                    videoCodec.releaseOutputBuffer(outputIndex, false);
                    outputIndex = videoCodec.dequeueOutputBuffer(videoBufferInfo, 0);
                }
            }
        });
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
