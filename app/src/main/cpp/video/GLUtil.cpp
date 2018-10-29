//
// Created by ether on 2018/10/24.
//

#include "GLUtil.h"

#define LOG_TAG "GLUtil"
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#define null NULL

void GLUtil::createProgram(const char *vertexShaderCode, const char *fragShaderCode) {
    this->program = glCreateProgram();
    if (program == 0) {
        LOGE("创建program失败");
        return;
    }
    GLuint vertexShaderId = loadShader(GL_VERTEX_SHADER, vertexShaderCode);
    GLuint fragShaderId = loadShader(GL_FRAGMENT_SHADER, fragShaderCode);
    if (vertexShaderId == 0 || fragShaderId == 0) {
        LOGE("创建shader失败");
        return;
    }
    glAttachShader(this->program, vertexShaderId);
    glAttachShader(this->program, fragShaderId);
    glLinkProgram(this->program);
    GLint linkStatus;
    glGetProgramiv(program, GL_LINK_STATUS, &linkStatus);
    if (linkStatus == 0) {
        glDeleteProgram(program);
        LOGE("程序连接失败");
    }
}

GLuint GLUtil::loadShader(GLenum type, const char *shaderCode) {
    GLuint shader = glCreateShader(type);
    if (shader == 0) {
        LOGE("创建shader失败");
        return 0;
    }
    glShaderSource(shader, 1, &shaderCode, null);
    glCompileShader(shader);
    GLint compileStatus = 0;
    glGetShaderiv(shader, GL_COMPILE_STATUS, &compileStatus);
    if (!compileStatus) {
        glDeleteShader(shader);
        LOGE("加载着色器代码失败");
        return 0;
    }
    return shader;
}

GLuint *GLUtil::createTexture() {
    GLuint textureArr[3];
    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
    glGenTextures(3, textureArr);
    if (textureArr[0] == 0) {
        LOGE("创建纹理失败");
        return nullptr;
    }
    for (int i = 0; i < 3; ++i) {
        glActiveTexture(GL_TEXTURE0 + i);
        glBindTexture(GL_TEXTURE_2D, textureArr[i]);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }
    return &textureArr;
}
