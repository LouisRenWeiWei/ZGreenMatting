LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_SRC_FILES := ZGreenMattingUtil.cpp

LOCAL_MODULE := zgreenmattingutil

LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)