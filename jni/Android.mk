LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE := avcodec-prebuilt
LOCAL_SRC_FILES := armeabi/libavcodec.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avdevice-prebuilt
LOCAL_SRC_FILES := armeabi/libavdevice.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avfilter-prebuilt
LOCAL_SRC_FILES := armeabi/libavfilter.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avformat-prebuilt
LOCAL_SRC_FILES := armeabi/libavformat.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := avutil-prebuilt
LOCAL_SRC_FILES := armeabi/libavutil.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniARToolKitPlus-prebuilt
LOCAL_SRC_FILES := armeabi/libjniARToolKitPlus.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniavcodec-prebuilt
LOCAL_SRC_FILES := armeabi/libjniavcodec.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniavdevice-prebuilt
LOCAL_SRC_FILES := armeabi/libjniavdevice.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniavfilter-prebuilt
LOCAL_SRC_FILES := armeabi/libjniavfilter.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniavformat-prebuilt
LOCAL_SRC_FILES := armeabi/libjniavformat.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniavutil-prebuilt
LOCAL_SRC_FILES := armeabi/libjniavutil.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jnicvkernels-prebuilt
LOCAL_SRC_FILES := armeabi/libjnicvkernels.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_calib3d-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_calib3d.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_contrib-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_contrib.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_core-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_core.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_features2d-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_features2d.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_flann-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_flann.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_highgui-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_highgui.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_imgproc-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_imgproc.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_legacy-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_legacy.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_ml-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_ml.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_nonfree-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_nonfree.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_objdetect-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_objdetect.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_photo-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_photo.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_stitching-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_stitching.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_video-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_video.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniopencv_videostab-prebuilt
LOCAL_SRC_FILES := armeabi/libjniopencv_videostab.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jnipostproc-prebuilt
LOCAL_SRC_FILES := armeabi/libjnipostproc.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniswresample-prebuilt
LOCAL_SRC_FILES := armeabi/libjniswresample.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := jniswscale-prebuilt
LOCAL_SRC_FILES := armeabi/libjniswscale.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_calib3d-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_calib3d.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_contrib-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_contrib.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_core-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_core.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_features2d-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_features2d.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_flann-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_flann.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_highgui-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_highgui.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_imgproc-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_imgproc.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_legacy-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_legacy.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_ml-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_ml.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_nonfree-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_nonfree.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_objdetect-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_objdetect.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_photo-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_photo.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_stitching-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_stitching.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_ts-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_ts.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_video-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_video.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := opencv_videostab-prebuilt
LOCAL_SRC_FILES := armeabi/libopencv_videostab.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := postproc-prebuilt
LOCAL_SRC_FILES := armeabi/libpostproc.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := swresample-prebuilt
LOCAL_SRC_FILES := armeabi/libswresample.so
include $(PREBUILT_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE := swscale-prebuilt
LOCAL_SRC_FILES := armeabi/libswscale.so
include $(PREBUILT_SHARED_LIBRARY)
include $(CLEAR_VARS)

# OpenCV
#OPENCV_CAMERA_MODULES:=on
#OPENCV_INSTALL_MODULES:=on
OPENCV_LIB_TYPE:=STATIC
include D:\OpenCV-android-sdk\sdk\native\jni\OpenCV.mk
LOCAL_MODULE     := motion_detection
LOCAL_SRC_FILES  := FrameDifferenceBGS.cpp \
					MixtureOfGaussianV1BGS.cpp \
					MixtureOfGaussianV2BGS.cpp \
					StaticFrameDifferenceBGS.cpp \
					WeightedMovingMeanBGS.cpp \
					WeightedMovingVarianceBGS.cpp \
					mybgs.cpp
LOCAL_C_INCLUDES += $(LOCAL_PATH)
LOCAL_LDLIBS     += -llog -ldl
include $(BUILD_SHARED_LIBRARY)