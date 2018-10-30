//
// Created by ether on 2018/10/23.
//

#ifndef BAMBOO_BLOCKQUEUE_H
#define BAMBOO_BLOCKQUEUE_H

#include <queue>
#include <mutex>

using namespace std;

enum POP_RESULT {
    POP_OK, POP_STOP, POP_UNEXPECTED
};

template<typename T>
class BlockQueue : public queue<T> {
public:
    void push(const T &value) {
        lock_guard<decltype(this->mLock)> lock(this->mLock);
        this->mQueue.push(value);
        this->mCond.notify_one();
    }

    void push(const T &&value) {
        lock_guard<decltype(this->mLock)> lock(this->mLock);
        this->mQueue.push(move(value));
        this->mCond.notify_one();
    }

    POP_RESULT pop(T &out) {
        unique_lock<decltype(this->mLock)> lock(this->mLock);
        if (isStop && this->mQueue.empty()) return POP_STOP;
        if (this->mQueue.empty()) mCond.wait(lock);
        if (isStop && this->mQueue.empty()) return POP_STOP;
        if (this->mQueue.empty()) return POP_UNEXPECTED;
        out = move(this->mQueue.front());
        this->mQueue.pop();
        return POP_OK;
    }

    void stop() {
        lock_guard<decltype(mLock)> lock(mLock);
        isStop = true;
        mCond.notify_all();
    }

    virtual ~BlockQueue() = default;

private:
    mutex mLock;
    condition_variable mCond;
    queue<T> mQueue;
    bool isStop = false;
};


#endif //BAMBOO_BLOCKQUEUE_H
