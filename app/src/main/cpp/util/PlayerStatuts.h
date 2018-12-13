//
// Created by ether on 2018/12/10.
//

#ifndef BAMBOO_PLAYERSTAUTS_H
#define BAMBOO_PLAYERSTAUTS_H


class PlayerStatus {
public:
    bool isExit;
    bool isLoad;
    bool isSeek;
    bool isPause;

    PlayerStatus();

    ~PlayerStatus();
};


#endif //BAMBOO_PLAYERSTAUTS_H
