package com.example.bamboo.encode;

import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;

import java.io.IOException;

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

    public VideoEncode(int width, int height) {
        this.width = width;
        this.height = height;
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
    }

    public MediaFormat getMediaFormat() {
        return mediaFormat;
    }
}
