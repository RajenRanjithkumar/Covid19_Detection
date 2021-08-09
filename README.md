## COVID-19 Detector

<P align="justify">
An Android application that predicts the presence of COVID-19 from Chest X-rays using deep convolution neural networks. A complex well-trained 20 layer CNN model that classifies Covid X-rays from the normal ones is integrated into the App. The developed app takes Name, phone number and Chest X-Rays image as inputs. Before predicting an output, the App checks if the given image is a chest X-ray or not, and a lite weight CNN model for that is also integrated into the App. This App also uses the firebase database as a backend to store and fetch all the users details and the CNN model’s prediction in real-time. Once the model generates the prediction (Covid/Normal), the App send an SMS along with the model result to the user’s registered phone number.</P></br>

![version](https://img.shields.io/badge/Version-v1.1-orange) ![platform](https://img.shields.io/badge/Platform-Android-brightgreen) ![language](https://img.shields.io/badge/Language-java-yellow) ![open_source](https://camo.githubusercontent.com/97d4586afa582b2dcec2fa8ed7c84d02977a21c2dd1578ade6d48ed82296eb10/68747470733a2f2f6261646765732e66726170736f66742e636f6d2f6f732f76312f6f70656e2d736f757263652e7376673f763d313033) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0) ![Twitter Follow](https://img.shields.io/twitter/follow/ranjith_bing?style=social)
</br>

## Table of Contents :beginner:

* [Screenshots](#screenshots)
  
* [Installation](#installation)
* [Roadmap](#roadmap)
* [Features](#feature)
* [About](#about)
* [License](#license)


## Screenshots

| <img src="App screenshots/Splash.jpg" height=400 width=210 > <P>Splashscreen Layout| <img src="App screenshots/home.jpg" height=400 width=210 > <P>Homescreen Layout | <img src="App screenshots/crop.jpg"  height=400 width=210> <P>Crop Layout  | <img src="App screenshots/home2.jpg" height=400 width=210> <P>Display progress bar  
| ---------------------------------------------- | -------------------------------------------- | ------------------------------------------- | ------------------------------------------- |
</br>

| <img src="App screenshots/output.jpg" height=400 width=210> <P>Model Output Layout | <img src="App screenshots/search1.jpg"  height=400 width=210> <P>Patients List Layout | <img src="App screenshots/search2.jpg" height=400 width=210> <P>Searchable List | <img src="App screenshots/metrics.jpg" height=400 width=210> <P>Model Metrics Layout
| ---------------------------------------------- | -------------------------------------------- | ------------------------------------------- |------------------------------------------- |
  
## Installation
  
  - You can easily download the APK file of Covid Detetor app from <a href="https://github.com/ranjithbing/Covid19_Detection/raw/master/Apk/Covid%20Detector.apk">here</a> and install it on your Android smartphone.
 
