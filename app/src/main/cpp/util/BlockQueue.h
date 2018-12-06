//
// Created by ether on 2018/12/6.
//

#ifndef BLOCKQUEUE_BLOCKQUEUE_H
#define BLOCKQUEUE_BLOCKQUEUE_H

#include <pthread.h>
#include <queue>
#include <android/log.h>

#define LOG_TAG "blockQueue"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define null NULL

template<typename T>
class BlockQueue {
public:
    void init() {
        pthread_mutex_init(&mutex, null);
        pthread_cond_init(&cond, null);
    }

    void push(const T &value) {
        pthread_mutex_lock(&mutex);
        while (queue.size() == 100) {
            pthread_cond_wait(&cond, &mutex);
            LOGE("队列已满阻塞中queue大小为%d", queue.size());
        }
        queue.push(value);
        LOGE("queue放入了%d,queue大小为%d", value, queue.size());
        pthread_cond_signal(&cond);
        pthread_mutex_unlock(&mutex);
    }

    void pop(T &out) {
        pthread_mutex_lock(&mutex);
        if (!queue.empty()) {
            out = (std::move(queue.front()));
            queue.pop();
            pthread_cond_signal(&cond);
        } else {
            pthread_cond_wait(&cond, &mutex);
        }
        pthread_mutex_unlock(&mutex);
    }

    uint getSize() {
        return queue.size();
    }

private:
    pthread_mutex_t mutex;
    pthread_cond_t cond;
    std::queue<T> queue;
};


#endif //BLOCKQUEUE_BLOCKQUEUE_H
