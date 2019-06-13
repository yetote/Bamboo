package com.example.bamboo.encode;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.util.Log;

import com.example.bamboo.util.WriteFile;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static android.media.MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_VBR;

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
public class VideoEncode {
    private int width, height;
    private MediaFormat mediaFormat;
    private MediaCodec mediaCodec;
    private BlockingQueue<byte[]> videoQueue;
    private Thread thread;
    private boolean isRecording;
    private static final String TAG = "VideoEncode";
    private byte[] videoData;
    private MediaCodec.BufferInfo bufferInfo;
    private byte[] pps;
    private WriteFile writeFile;
    private boolean isFinish=false;

    public VideoEncode(int width, int height, String path) {
        this.width = width;
        this.height = height;
        writeFile = new WriteFile(path);
        mediaFormat = MediaFormat.createVideoFormat(MediaFormat.MIMETYPE_VIDEO_AVC, width, height);
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, width * height * 30 * 3);
        mediaFormat.setInteger(MediaFormat.KEY_BITRATE_MODE, BITRATE_MODE_VBR);
        mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 30);
        mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Flexible);
        mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
        try {
            mediaCodec = MediaCodec.createEncoderByType(MediaFormat.MIMETYPE_VIDEO_AVC);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        videoQueue = new LinkedBlockingQueue<>();
        bufferInfo = new MediaCodec.BufferInfo();
//        thread = new Thread(() -> {
//            int flag = 0;
//            while (isRecording) {
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
//                    videoData = videoQueue.take();
//                    if (!isRecording && videoQueue.isEmpty()) {
//                        Log.e(TAG, "run: 最后一帧");
//                        flag = MediaCodec.BUFFER_FLAG_END_OF_STREAM;
//                    }
////                    Log.e(TAG, "run: videoSize=" + videoData.length);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                inputBuffer.put(videoData);
//                mediaCodec.queueInputBuffer(inputIndex, 0, videoData.length, System.currentTimeMillis(), flag);
//                int outputIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
//                if (outputIndex < 0) {
//                    continue;
//                }
//                while (outputIndex >= 0) {
//                    ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(outputIndex);
//                    if (outputBuffer == null) {
//                        Log.e(TAG, "encode: 未找到编码后容器");
//                        break;
//                    }
//                    if (pps == null) {
//                        if (bufferInfo.flags == 2) {
//                            Log.e(TAG, "run: 第一帧");
//                            pps = new byte[bufferInfo.size];
//                            Log.e(TAG, "run:第一帧长度 " + outputBuffer.limit());
//                            Log.e(TAG, "run:第一帧flag " + bufferInfo.flags);
//                            Log.e(TAG, "run: ptsSize" + pps.length);
//                            outputBuffer.get(pps);
//                        }
//                    }
//                    if (bufferInfo.flags == 1) {
//                        Log.e(TAG, "run: 关键帧");
////                        writeFile.write(pps);
//                    } else {
//                        outputBuffer.position(bufferInfo.offset);
//                        outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
//                    }
////                    Log.e(TAG, "VideoEncode: size=" + bufferInfo.size);
////                    writeFile.write(outputBuffer);
//                    mediaCodec.releaseOutputBuffer(outputIndex, false);
//                    outputIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
//                }
//            }
//        });
    }

    public MediaFormat getMediaFormat() {
        return mediaFormat;
    }

    public void pushData(byte[] yuvData) {
        try {
            videoQueue.put(yuvData);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startEncode(MutexUtil mutexUtil) {
        mediaCodec.start();
//        thread.start();
        new Thread(() -> {
            int flag = 0;
            int endFlag = 0;
            long presentationTimeUs = System.currentTimeMillis() * 1000;
            while (endFlag != MediaCodec.BUFFER_FLAG_END_OF_STREAM) {
                Log.e(TAG, "startEncode: 视频编码循环"+!isFinish);
                if (!isFinish) {
                    int inputIndex = mediaCodec.dequeueInputBuffer(-1);
                    if (inputIndex < 0) {
                        continue;
                    }
                    try {
                        videoData = videoQueue.take();
                        if (!isRecording && videoQueue.isEmpty()) {
                            Log.e(TAG, "startEncode: 视频最后一帧");
//                        Log.e(TAG, "run: 最后一帧");
                            flag = MediaCodec.BUFFER_FLAG_END_OF_STREAM;
                            isFinish = true;
                        }
//                    Log.e(TAG, "run: videoSize=" + videoData.length);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ByteBuffer inputBuffer = mediaCodec.getInputBuffer(inputIndex);
                    if (inputBuffer == null) {
                        continue;
                    }
                    inputBuffer.clear();

                    inputBuffer.put(videoData);
                    mediaCodec.queueInputBuffer(inputIndex, 0, videoData.length, System.currentTimeMillis() * 1000 - presentationTimeUs, flag);
                }
                int outputIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
                if (outputIndex == -2) {
                    mutexUtil.addFormat(mediaCodec.getOutputFormat(), false);

                }
                Log.e(TAG, "startEncode: 视频state" + mutexUtil.getState());
                if (mutexUtil.getState()) {
                    while (outputIndex >= 0) {
                        ByteBuffer outputBuffer = mediaCodec.getOutputBuffer(outputIndex);
                        if (outputBuffer == null) {
                            Log.e(TAG, "encode: 未找到编码后容器");
                            break;
                        }
//                    if (pps == null) {
//                        if (bufferInfo.flags == 2) {
//                            Log.e(TAG, "run: 第一帧");
//                            pps = new byte[bufferInfo.size];
//                            Log.e(TAG, "run:第一帧长度 " + outputBuffer.limit());
//                            Log.e(TAG, "run:第一帧flag " + bufferInfo.flags);
//                            Log.e(TAG, "run: ptsSize" + pps.length);
//                            outputBuffer.get(pps);
//                        }
//                    }
//                    if (bufferInfo.flags == 1) {
//                        Log.e(TAG, "run: 关键帧");
////                        writeFile.write(pps);
//                    } else {
                        outputBuffer.position(bufferInfo.offset);
                        outputBuffer.limit(bufferInfo.offset + bufferInfo.size);
//                    }
//                    Log.e(TAG, "VideoEncode: size=" + bufferInfo.size);
//                    writeFile.write(outputBuffer);
                        mutexUtil.pushData(outputBuffer, false, bufferInfo);
                        mediaCodec.releaseOutputBuffer(outputIndex, false);
                        outputIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 0);
                        endFlag = bufferInfo.flags;
                        Log.e(TAG, "startEncode: 视频endflag" + endFlag);
                    }
                }
            }
            Log.e(TAG, "startEncode: 视频结束录制");
            mutexUtil.stop(false);
        }).start();
    }

    public void setRecording(boolean recording) {
        isRecording = recording;
    }
}
