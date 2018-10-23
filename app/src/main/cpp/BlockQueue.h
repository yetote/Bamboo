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
        lock_guard<decltype(mLock)> lock(mLock);
        mQueue.push(value);
        mCond.notify_one;
    }

    void push(const T &&value) {
        lock_guard<decltype(mLock)> lock(mLock);
        mQueue.push(move(value));
        mCond.notify_one;
    }

    POP_RESULT pop(T &out) {
        unique_lock<decltype(mLock)> lock(mLock);
        if (isStop && mQueue.empty()) return POP_STOP;
        if (mQueue.empty()) mCond.wait(lock);
        if (isStop && mQueue.empty()) return POP_STOP;
        if (mQueue.empty()) return POP_UNEXPECTED;
        out = move(mQueue.front());
        mQueue.pop();
        return POP_OK;
    }

private:
    mutex mLock;
    condition_variable mCond;
    queue<T> mQueue;
    bool isStop = false;
};


#endif //BAMBOO_BLOCKQUEUE_H
