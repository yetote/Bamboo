
cmake_minimum_required(VERSION 3.4.1)
set(path_project D:/AndroidStudioProjects/Bamboo)
#set(libs_path  ${path_project}/app/build/intermediates/cmake/debug/obj)
set(libs_path ${path_project}/app/src/main/jniLibs/libs)

add_library( # Sets the name of the library.
        native-lib

        # Sets the library as a shared library.
        SHARED

        # Provides a relative path to your source file(s).
        native-lib.cpp
        )

    #配置头文件的包含路径
    include_directories(${path_project}/app/src/main/jni/includes)

    #添加FFmpeg预编译的so库
    add_library(
            avutil
            SHARED
            IMPORTED
    )

    #设置两个预编译的库的路径
    set_target_properties(
            avutil
            PROPERTIES IMPORTED_LOCATION
            ${libs_path}/${ANDROID_ABI}/libavutil.so
    )

    add_library(swresample
            SHARED
            IMPORTED
            )

    set_target_properties(
            swresample
            PROPERTIES IMPORTED_LOCATION
            ${libs_path}/${ANDROID_ABI}/libswresample.so
    )

    add_library(avcodec
            SHARED
            IMPORTED
            )

    set_target_properties(
            avcodec
            PROPERTIES IMPORTED_LOCATION
            ${libs_path}/${ANDROID_ABI}/libavcodec.so
    )

    add_library(avformat
            SHARED
            IMPORTED
            )

    set_target_properties(
            avformat
            PROPERTIES IMPORTED_LOCATION
            ${libs_path}/${ANDROID_ABI}/libavformat.so
    )

    add_library(swscale
            SHARED
            IMPORTED
            )

    set_target_properties(
            swscale
            PROPERTIES IMPORTED_LOCATION
            ${libs_path}/${ANDROID_ABI}/libswscale.so
    )


    add_library(avfilter
            SHARED
            IMPORTED
            )

    set_target_properties(
            avfilter
            PROPERTIES IMPORTED_LOCATION
            ${libs_path}/${ANDROID_ABI}/libavfilter.so
    )

    add_library(avdevice
            SHARED
            IMPORTED
            )

    set_target_properties(
            avdevice
            PROPERTIES IMPORTED_LOCATION
            ${libs_path}/${ANDROID_ABI}/libavdevice.so
    )
find_library( # Sets the name of the path variable.
        log-lib

        # Specifies the name of the NDK library that
        # you want CMake to locate.
        log)

target_link_libraries( # Specifies the target library.
        native-lib
        android
        EGL
        GLESv2
        OpenSLES
        avutil
        swresample
        avformat
        avcodec
        swscale
        avfilter
        avdevice
        ${log-lib})