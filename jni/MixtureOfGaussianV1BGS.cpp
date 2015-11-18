/*
This file is part of BGSLibrary.

BGSLibrary is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

BGSLibrary is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with BGSLibrary.  If not, see <http://www.gnu.org/licenses/>.
*/
#include "MixtureOfGaussianV1BGS.h"
#include "string"
MixtureOfGaussianV1BGS::MixtureOfGaussianV1BGS() : firstTime(true), alpha(0.05), enableThreshold(true), threshold(15)
{
  std::cout << "MixtureOfGaussianV1BGS()" << std::endl;
}

MixtureOfGaussianV1BGS::~MixtureOfGaussianV1BGS()
{
  std::cout << "~MixtureOfGaussianV1BGS()" << std::endl;
}

void MixtureOfGaussianV1BGS::process(const cv::Mat &img_input, cv::Mat &img_output, cv::Mat &img_bgmodel)
{
  if(img_input.empty())
    return;

  if(firstTime)
	 loadConfig();

  //------------------------------------------------------------------
  // BackgroundSubtractorMOG
  // http://opencv.itseez.com/modules/video/doc/motion_analysis_and_object_tracking.html#backgroundsubtractormog
  //
  // Gaussian Mixture-based Backbround/Foreground Segmentation Algorithm.
  //
  // The class implements the algorithm described in:
  //   P. KadewTraKuPong and R. Bowden, 
  //   An improved adaptive background mixture model for real-time tracking with shadow detection, 
  //   Proc. 2nd European Workshp on Advanced Video-Based Surveillance Systems, 2001
  //------------------------------------------------------------------

  mog(img_input, img_foreground, alpha);
  cv::Mat img_background;
  mog.getBackgroundImage(img_background);

  if(enableThreshold)
    cv::threshold(img_foreground, img_foreground, threshold, 255, cv::THRESH_BINARY);

  img_foreground.copyTo(img_output);
  img_background.copyTo(img_bgmodel);

  firstTime = false;
}
void MixtureOfGaussianV1BGS::setThreshold(int threshold)
{
	this->threshold=threshold;
	saveConfig();
}
void MixtureOfGaussianV1BGS::setEnableThreshold(bool enable)
{
	enableThreshold=enable;
	saveConfig();
}
void MixtureOfGaussianV1BGS::saveConfig()
{
  //CvFileStorage* fs = cvOpenFileStorage(configFileDir, 0, CV_STORAGE_WRITE);

  //cvWriteReal(fs, "Alpha", alpha);
  //cvWriteInt(fs, "EnableThreshold", enableThreshold);
  //cvWriteInt(fs, "Threshold", threshold);

  //cvReleaseFileStorage(&fs);
}

void MixtureOfGaussianV1BGS::loadConfig()
{
  //CvFileStorage* fs = cvOpenFileStorage(configFileDir, 0, CV_STORAGE_READ);

  //alpha = cvReadRealByName(fs, 0, "alpha", 0.05);
  //enableThreshold = cvReadIntByName(fs, 0, "enableThreshold", true);
  //threshold = cvReadIntByName(fs, 0, "threshold", 15);
  
  //cvReleaseFileStorage(&fs);

}
void MixtureOfGaussianV1BGS::reset()
{

}
bool MixtureOfGaussianV1BGS::isEnableThreshold()
{
	return enableThreshold;
}
int MixtureOfGaussianV1BGS::getThreshold()
{
	return threshold;
}

