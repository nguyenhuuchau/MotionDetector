#include "mybgs.h"
#include "opencv2/opencv.hpp"
#include "opencv2/features2d/features2d.hpp"
#include "WeightedMovingVarianceBGS.h"
#include "FrameDifferenceBGS.h"
#include "StaticFrameDifferenceBGS.h"
#include "WeightedMovingMeanBGS.h"
#include "MixtureOfGaussianV1BGS.h"
#include "MixtureOfGaussianV2BGS.h"
#include "FrameDifferenceBGS.h"
#include "opencv2/highgui/highgui.hpp"
#include "vector"
struct BGSAgregator
{
	cv::Ptr<IBGS> bgs;
	BGSAgregator(BGSMethod method)
	{
		switch(method)
				{
				case BGS_DEFAULT:
					bgs=cv::makePtr<StaticFrameDifferenceBGS>();
					break;
				case STATIC_FRAME_DIFFERENCE:
					bgs=cv::makePtr<StaticFrameDifferenceBGS>();
					break;
				case MIXTURE_OF_GAUSSIAN_1:
					bgs=cv::makePtr<MixtureOfGaussianV1BGS>();
					break;
				case MIXTURE_OF_GAUSSIAN_2:
					bgs=cv::makePtr<MixtureOfGaussianV2BGS>();
					break;
				case FRAME_DIFFERENCE:
					bgs=cv::makePtr<FrameDifferenceBGS>();
					break;
				case WEIGHTED_MOVING_VARIANCE:
					bgs=cv::makePtr<WeightedMovingVarianceBGS>();
					break;
				default:
					bgs=cv::makePtr<StaticFrameDifferenceBGS>();
					break;
				}
	}
};
JNIEXPORT jlong JNICALL Java_com_doantotnghiep_motiondetector_BGS_createObject(JNIEnv *pEnv,jclass, int bgsmethod)
{
	jlong result = 0;
	try
	{
		result = (jlong)new BGSAgregator((BGSMethod)bgsmethod);
	}
	catch(cv::Exception& e)
	{
		jclass je = pEnv->FindClass("org/opencv/core/CvException");
		if(!je)
			je = pEnv->FindClass("java/lang/Exception");
		pEnv->ThrowNew(je, e.what());
	}
	catch (...)
	{
		jclass je = pEnv->FindClass("java/lang/Exception");
		pEnv->ThrowNew(je, "Unknown exception in JNI code of createObject()");
		return 0;
	}
	return result;
}
JNIEXPORT void JNICALL Java_com_doantotnghiep_motiondetector_BGS_process(JNIEnv *pEnv,jclass,jlong nativeObject,jlong img_input,jlong img_mask, jlong img_model)
{
	if(nativeObject!=0)
	{
		try
		{
			cv::Mat input_gray;
			cv::Mat *input=(cv::Mat*)img_input;
			cv::cvtColor(*input,input_gray,CV_BGRA2GRAY);
			cv::Mat *mask=(cv::Mat*)img_mask;
			cv::Mat *model=(cv::Mat*)img_model;
			((BGSAgregator*)nativeObject)->bgs->process(input_gray,*mask,*model);
			//cv::Mat erode;
			//cv::Mat element=cv::getStructuringElement(cv::MORPH_CROSS,cv::Size(9,9),cv::Point(1,1));
			//cv::dilate(*mask,erode,element,cv::Point(-1,-1),3);
			//std::vector<std::vector<cv::Point> > contours;
			//cv::findContours(erode,contours,CV_RETR_LIST,CV_CHAIN_APPROX_NONE);
			//for(std::size_t i=0;i<contours.size();i++)
			//{
			//	cv::Rect r=cv::boundingRect(cv::Mat(contours[i]));
			//	if(r.width>=40&&r.height>=40)
			//	cv::rectangle(*input,r,cv::Scalar(255,255,255),1,8,0);
			//}

		}
		catch(cv::Exception& e)
		{
			jclass je = pEnv->FindClass("org/opencv/core/CvException");
			if(!je)
				je = pEnv->FindClass("java/lang/Exception");
			pEnv->ThrowNew(je, e.what());
		}
		catch (...)
		{
			jclass je = pEnv->FindClass("java/lang/Exception");
			pEnv->ThrowNew(je, "Unknown exception in JNI code process()");
		}
	}
}
JNIEXPORT void JNICALL Java_com_doantotnghiep_motiondetector_BGS_destroyObject(JNIEnv *pEnv,jclass,jlong object)
{
	try
	{
		if(object != 0)
		{
			delete (BGSAgregator*)object;
		}
	}
	catch(cv::Exception& e)
	{
		jclass je = pEnv->FindClass("org/opencv/core/CvException");
		if(!je)
			je = pEnv->FindClass("java/lang/Exception");
	    pEnv->ThrowNew(je, e.what());
	}
	catch (...)
	{
		jclass je = pEnv->FindClass("java/lang/Exception");
		pEnv->ThrowNew(je, "Unknown exception in JNI code of destroyObject()");
	}
}
JNIEXPORT void JNICALL Java_com_doantotnghiep_motiondetector_BGS_reset(JNIEnv *pEnv,jclass, jlong object)
{
	if(object!=0)
	{
		try
		{
			((BGSAgregator*)object)->bgs->reset();
		}
		catch(cv::Exception& e)
		{
			jclass je = pEnv->FindClass("org/opencv/core/CvException");
			if(!je)
				je = pEnv->FindClass("java/lang/Exception");
			pEnv->ThrowNew(je, e.what());
		}
		catch (...)
		{
			jclass je = pEnv->FindClass("java/lang/Exception");
			pEnv->ThrowNew(je, "Unknown exception in JNI code reset()");
		}
	}
}
JNIEXPORT void JNICALL Java_com_doantotnghiep_motiondetector_BGS_enableThreshold(JNIEnv *pEnv,jclass, jlong obj, jboolean ena)
{
	if(obj!=0)
		{
			try
			{
				((BGSAgregator*)obj)->bgs->setEnableThreshold(ena);
			}
			catch(cv::Exception& e)
			{
				jclass je = pEnv->FindClass("org/opencv/core/CvException");
				if(!je)
					je = pEnv->FindClass("java/lang/Exception");
				pEnv->ThrowNew(je, e.what());
			}
			catch (...)
			{
				jclass je = pEnv->FindClass("java/lang/Exception");
				pEnv->ThrowNew(je, "Unknown exception in JNI code setEnableThreshold()");
			}
		}
}
JNIEXPORT void JNICALL Java_com_doantotnghiep_motiondetector_BGS_setThreshold(JNIEnv *pEnv,jclass, jlong obj, jint threshold)
{
	if(obj!=0)
	{
		try
		{
			((BGSAgregator*)obj)->bgs->setThreshold(threshold);
		}
		catch(cv::Exception& e)
		{
			jclass je = pEnv->FindClass("org/opencv/core/CvException");
			if(!je)
				je = pEnv->FindClass("java/lang/Exception");
			pEnv->ThrowNew(je, e.what());
		}
		catch (...)
		{
			jclass je = pEnv->FindClass("java/lang/Exception");
			pEnv->ThrowNew(je, "Unknown exception in JNI code setEnableThreshold()");
		}
	}
}
JNIEXPORT jboolean JNICALL Java_com_doantotnghiep_motiondetector_BGS_isEnabledThreshold(JNIEnv *pEnv,jclass, jlong obj)
{
	if(obj!=0)
	{
		try
		{
			return ((BGSAgregator*)obj)->bgs->isEnableThreshold();
		}
		catch(cv::Exception& e)
		{
			jclass je = pEnv->FindClass("org/opencv/core/CvException");
			if(!je)
				je = pEnv->FindClass("java/lang/Exception");
			pEnv->ThrowNew(je, e.what());
			return false;
		}
		catch (...)
		{
			jclass je = pEnv->FindClass("java/lang/Exception");
			pEnv->ThrowNew(je, "Unknown exception in JNI code isEnabledThreshold()");
			return false;
		}
	}
	else return false;
}
JNIEXPORT jint JNICALL Java_com_doantotnghiep_motiondetector_BGS_getThreshold(JNIEnv *pEnv,jclass, jlong obj)
{
	if(obj!=0)
	{
		try
		{
			return ((BGSAgregator*)obj)->bgs->getThreshold();
		}
		catch(cv::Exception& e)
		{
			jclass je = pEnv->FindClass("org/opencv/core/CvException");
			if(!je)
				je = pEnv->FindClass("java/lang/Exception");
			pEnv->ThrowNew(je, e.what());
			return 0;
		}
		catch (...)
		{
			jclass je = pEnv->FindClass("java/lang/Exception");
			pEnv->ThrowNew(je, "Unknown exception in JNI code getThreshold()");
			return 0;
		}
	}
	else return 0;

}
