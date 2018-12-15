//
// Created by ether on 2018/12/10.
//

#include "BlockQueue.h"

BlockQueue::BlockQueue(PlayerStatus *playstatus) {
    this->playstatus = playstatus;
    pthread_mutex_init(&mutexPacket, NULL);
    pthread_cond_init(&condPacket, NULL);

}

BlockQueue::~BlockQueue() {
    clearPacket();
    pthread_cond_destroy(&condPacket);
    pthread_mutex_destroy(&mutexPacket);
}

int BlockQueue::putAvpacket(AVPacket *packet) {

    pthread_mutex_lock(&mutexPacket);
//    while (queuePacket.size() == 100) {
//        pthread_cond_wait(&condPacket, &mutexPacket);
//        LOGE("队列已满，等待中%d", queuePacket.size());
//    }
    queuePacket.push(packet);
//    LOGE("放入一个AVpacket到队里里面， 个数为：%d", queuePacket.size());
    pthread_cond_signal(&condPacket);
    pthread_mutex_unlock(&mutexPacket);

    return 0;
}

int BlockQueue::getAvpacket(AVPacket *packet) {

    pthread_mutex_lock(&mutexPacket);

    while (playstatus != NULL && !playstatus->isExit) {
        if (queuePacket.size() > 0) {
            AVPacket *avPacket = queuePacket.front();
            if (av_packet_ref(packet, avPacket) == 0) {
                queuePacket.pop();
//                pthread_cond_signal(&condPacket);
            }
            av_packet_free(&avPacket);
            av_free(avPacket);
            avPacket = NULL;

//            LOGE("从队列里面取出一个AVpacket，还剩下 %d 个", queuePacket.size());

            break;
        } else {
            pthread_cond_wait(&condPacket, &mutexPacket);
        }
    }
    pthread_mutex_unlock(&mutexPacket);
    return 0;
}

int BlockQueue::getQueueSize() {

    int size = 0;
    pthread_mutex_lock(&mutexPacket);
    size = queuePacket.size();
    pthread_mutex_unlock(&mutexPacket);

    return size;
}

void BlockQueue::clearPacket() {
    pthread_cond_signal(&condPacket);
    pthread_mutex_unlock(&mutexPacket);
    while (!queuePacket.empty()) {
        AVPacket *packet = queuePacket.front();
        queuePacket.pop();
        av_packet_free(&packet);
        av_free(packet);
        packet = NULL;
    }
    pthread_mutex_unlock(&mutexPacket);
}

void BlockQueue::noticeQueue() {
    pthread_cond_signal(&condPacket);

}
