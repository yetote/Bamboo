package com.example.bamboo.myview;

import android.media.MediaCodec;
import android.media.MediaFormat;
import android.util.Log;
import android.view.Surface;

import com.example.bamboo.model.TimeInfoBean;
import com.example.bamboo.myinterface.ffmpeg.OnCompleteListener;
import com.example.bamboo.myinterface.ffmpeg.OnLoadListener;
import com.example.bamboo.myinterface.ffmpeg.OnPauseListener;
import com.example.bamboo.myinterface.ffmpeg.OnPreparedListener;
import com.example.bamboo.myinterface.ffmpeg.OnStartListener;
import com.example.bamboo.myinterface.ffmpeg.OnStopListener;
import com.example.bamboo.myinterface.ffmpeg.OnTimeInfoListener;
import com.example.bamboo.util.DecodeUtil;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author yetote QQ:503779938
 * @name Bamboo
 * @class name：com.example.bamboo.util
 * @class 播放器
 * @time 2018/10/20 14:02
 * @change
 * @chang time
 * @class describe
 */
public class PlayerView {
    static {
        System.loadLibrary("native-lib");
    }

    private OnPreparedListener preparedListener;
    private OnStartListener startListener;
    private OnStopListener stopListener;
    private OnPauseListener pauseListener;
    private OnTimeInfoListener timeInfoListener;
    private OnLoadListener loadListener;
    private OnCompleteListener completeListener;
    public static boolean isNext = false;
    String source;
    String vertexCode, fragCode;
    Surface surface;
    int w, h;
    private MediaFormat mediaFormat;
    private MediaCodec mediaCodec;
    private static final String TAG = "PlayerView";
    MediaCodec.BufferInfo bufferInfo;

    public PlayerView(String vertexCode, String fragCode, Surface surface, int w, int h) {
        this.vertexCode = vertexCode;
        this.fragCode = fragCode;
        this.surface = surface;
        this.w = w;
        this.h = h;
    }

    public void setCompleteListener(OnCompleteListener completeListener) {
        this.completeListener = completeListener;
    }

    public void setTimeInfoListener(OnTimeInfoListener timeInfoListener) {
        this.timeInfoListener = timeInfoListener;
    }

    public void setLoadListener(OnLoadListener loadListener) {
        this.loadListener = loadListener;
    }

    private static TimeInfoBean timeInfoBean;

    public void setStartListener(OnStartListener startListener) {
        this.startListener = startListener;
    }

    public void setStopListener(OnStopListener stopListener) {
        this.stopListener = stopListener;
    }

    public void setPauseListener(OnPauseListener pauseListener) {
        this.pauseListener = pauseListener;
    }


    public void setPreparedListener(OnPreparedListener preparedListener) {
        this.preparedListener = preparedListener;
    }

    public void prepared(String source) {
        new Thread(() -> ffmpegPrepared(source, vertexCode, fragCode, surface, w, h)).start();
    }


    public void start() {
        new Thread(() -> ffmpegStart()).start();
    }

    public void stop() {
        new Thread(() -> ffmpegStop()).start();
    }

    public void pause() {
        ffmpegPause();
        if (pauseListener != null) {
            pauseListener.onPause(true);
        }
    }

    public void onResume() {
        ffmpegResume();
        if (pauseListener != null) {
            pauseListener.onPause(false);
        }
    }

    public void seek(int secs) {
        ffmpegSeek(secs);
    }

    public void playNext(String url) {
        source = url;
        isNext = true;
        stop();
    }

    public void setVolume(int percent) {
        if (percent >= 0 && percent <= 100) {
            ffmpegSetVolume(percent);
        }
    }

    public boolean onCallIsSupportHardwareCodec(String codecName) {
        return DecodeUtil.isSupportCodec(codecName);
    }


    void onPreparedCall() {
        if (preparedListener != null) {
            preparedListener.onPrepared();
        }
    }

    void onStartCall() {
        if (startListener != null) {
            startListener.onStart();
        }
    }

    void onTimeInfoCall(int currentTime, int totalTime) {
        if (timeInfoListener != null) {
            if (timeInfoBean == null) {
                timeInfoBean = new TimeInfoBean();
            }
            timeInfoBean.setCurrentTime(currentTime);
            timeInfoBean.setTotalTime(totalTime);
        }
        timeInfoListener.onTimeInfo(timeInfoBean);
    }

    void onCallLoad(boolean isLoad) {
        if (loadListener != null) {
            loadListener.onLoad(isLoad);
        }
    }

    void onCallComplete() {
        stop();
        if (completeListener != null) {
            completeListener.onComplete();
        }
    }

    void onCallPlayNext() {
        if (isNext) {
            isNext = false;
            prepared(source);
        }
    }

    public void initMediaCodec(String codecName, int w, int h, byte[] csd0, byte[] csd1) {
        if (surface != null) {
            try {
                String mime = DecodeUtil.findHardwareCodec(codecName);
                mediaFormat = MediaFormat.createVideoFormat(mime, w, h);
                mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, w * h);
                mediaFormat.setByteBuffer("csd-0", ByteBuffer.wrap(csd0));
                mediaFormat.setByteBuffer("csd-1", ByteBuffer.wrap(csd1));
                mediaCodec = MediaCodec.createDecoderByType(mime);

                mediaCodec.configure(mediaFormat, null, null, 0);
                mediaCodec.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "initMediaCodec: surface为空");
        }
    }

    private void decode(int dataSize, byte[] data) {
        if (surface != null) {
            if (dataSize > 0 && data != null) {
                int inputIndex = mediaCodec.dequeueInputBuffer(10);
                if (inputIndex >= 0) {
                    ByteBuffer byteBuffer = mediaCodec.getInputBuffer(inputIndex);
                    byteBuffer.put(data);
                    mediaCodec.queueInputBuffer(inputIndex, 0, dataSize, 0, 0);
                }
                int outputIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10);
                while (outputIndex >= 0) {
                    mediaCodec.releaseOutputBuffer(outputIndex, true);
                    outputIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10);
                }
            }
        }
    }

    private native void ffmpegPrepared(String source, String vertexCode, String fragCode, Surface surface, int w, int h);

    private native void ffmpegStart();

    private native void ffmpegStop();

    private native void ffmpegPause();

    private native void ffmpegResume();

    private native void ffmpegSeek(int secs);

    private native void ffmpegSetVolume(int percent);

}
