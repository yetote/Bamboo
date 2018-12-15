//
// Created by ether on 2018/12/10.
//

#ifndef BAMBOO_BLOCKQUEUE_H
#define BAMBOO_BLOCKQUEUE_H


#include <pthread.h>
#include <queue>
#include "PlayerStatuts.h"

extern "C" {
#include <libavcodec/avcodec.h>
};

class BlockQueue {
public:
    std::queue<AVPacket *> queuePacket;
    pthread_mutex_t mutexPacket;
    pthread_cond_t condPacket;
    PlayerStatus *playstatus = NULL;

    void clearPacket();

public:

    BlockQueue(PlayerStatus *playstatus);

    ~BlockQueue();

    int putAvpacket(AVPacket *packet);

    int getAvpacket(AVPacket *packet);

    int getQueueSize();
    void noticeQueue();
};


#endif //BAMBOO_BLOCKQUEUE_H
