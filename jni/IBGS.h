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
#pragma once

#include <opencv2/opencv.hpp>
#include <android/log.h>
#define LOG_TAG "MotionDetect/NativeLog"
class IBGS
{
public:
  virtual void process(const cv::Mat &img_input, cv::Mat &img_foreground, cv::Mat &img_background) = 0;
  virtual ~IBGS(){
  }
  virtual void reset() = 0;
  virtual void setThreshold(int threshold) = 0;
  virtual void setEnableThreshold(bool enable) = 0;
  virtual int getThreshold()=0;
  virtual bool isEnableThreshold()=0;
private:
  virtual void saveConfig() = 0;
  virtual void loadConfig() = 0;
};
