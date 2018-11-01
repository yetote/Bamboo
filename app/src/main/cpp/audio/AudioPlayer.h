//
// Created by ether on 2018/11/1.
//

#ifndef BAMBOO_AUDIOPLAYER_H
#define BAMBOO_AUDIOPLAYER_H

#include <SLES/OpenSLES.h>
#include <SLES/OpenSLES_Android.h>
#include <android/log.h>

class AudioPlayer {
    SLObjectItf objectItf, outMaxObjItf, playerObjItf;
    SLEngineItf engineItf;
    SLPlayItf playItf;
    SLBufferQueueItf bufferQueueItf;
    SLEnvironmentalReverbItf environmentalReverbItf;

    void play();

public:
private:
    void start();

    void setDataSource();

    void prepare();
};


#endif //BAMBOO_AUDIOPLAYER_H
