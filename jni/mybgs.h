#ifndef __BGS_H__
#define __BGS_H__
#include "jni.h"
#ifdef __cplusplus
extern "C" {
#endif
enum BGSMethod
{
	BGS_DEFAULT,
	STATIC_FRAME_DIFFERENCE,
	MIXTURE_OF_GAUSSIAN_1,
	MIXTURE_OF_GAUSSIAN_2,
	FRAME_DIFFERENCE,
	WEIGHTED_MOVING_VARIANCE
};
JNIEXPORT jlong JNICALL Java_com_doantotnghiep_motiondetector_BGS_createObject(JNIEnv *pEnv,jclass, int bgsmethod);
JNIEXPORT void JNICALL Java_com_doantotnghiep_motiondetector_BGS_process(JNIEnv *pEnv,jclass,jlong nativeObject,jlong img_input,jlong img_mask, jlong img_model);
JNIEXPORT void JNICALL Java_com_doantotnghiep_motiondetector_BGS_destroyObject(JNIEnv *pEnv,jclass,jlong object);
JNIEXPORT void JNICALL Java_com_doantotnghiep_motiondetector_BGS_reset(JNIEnv *pEnv,jclass, jlong object);
JNIEXPORT void JNICALL Java_com_doantotnghiep_motiondetector_BGS_enableThreshold(JNIEnv *pEnv,jclass, jlong obj, jboolean ena);
JNIEXPORT void JNICALL Java_com_doantotnghiep_motiondetector_BGS_setThreshold(JNIEnv *pEnv,jclass, jlong obj, jint threshold);
JNIEXPORT jboolean JNICALL Java_com_doantotnghiep_motiondetector_BGS_isEnabledThreshold(JNIEnv *pEnv,jclass, jlong obj);
JNIEXPORT jint JNICALL Java_com_doantotnghiep_motiondetector_BGS_getThreshold(JNIEnv *pEnv,jclass, jlong obj);
#ifdef __cplusplus
}
#endif
#endif
