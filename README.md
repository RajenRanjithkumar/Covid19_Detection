## COVID-19 Detector
<P align="justify">
An Android application that predicts the presence of COVID-19 from Chest X-rays using deep convolution neural networks. A complex well-trained 20 layer CNN model that classifies Covid X-rays from the normal ones is integrated into the App. The developed app takes Name, phone number and Chest X-Rays image as inputs. Before predicting an output, the App checks if the given image is a chest X-ray or not, and a lite weight CNN model for that is also integrated into the App. This App also uses the firebase database as a backend to store and fetch all the users details and the CNN model’s prediction in real-time. Once the model generates the prediction (Covid/Normal), the App send an SMS along with the model result to the user’s registered phone number.</P></br>

[![version](https://img.shields.io/badge/version-v1.1%20-orange.svg)](https://github.com/ranjithbing/Covid19_Detection/releases/tag/v1.1) [![platform](https://img.shields.io/badge/Platform-Android-brightgreen)](https://www.android.com/intl/en_in/) ![language](https://img.shields.io/badge/Language-java-yellow) ![open_source](https://camo.githubusercontent.com/97d4586afa582b2dcec2fa8ed7c84d02977a21c2dd1578ade6d48ed82296eb10/68747470733a2f2f6261646765732e66726170736f66742e636f6d2f6f732f76312f6f70656e2d736f757263652e7376673f763d313033) [![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)<br>
[![Twitter Badge](https://img.shields.io/badge/-@ranjith_bing-1ca0f1?style=flat&labelColor=1ca0f1&logo=twitter&logoColor=white&link=https://twitter.com/ranjith_bing)](https://twitter.com/ranjith_bing)
[![Linkedin Badge](https://img.shields.io/badge/-Ranjithkumar-Rajendran?style=flat&logo=Linkedin&logoColor=white&link=https://www.linkedin.com/in/ranjithkumar-rajendran-565402182/)](https://www.linkedin.com/in/ranjithkumar-rajendran-565402182/)
</br>

## Table of Contents :beginner:

* [Manifest](#manifest)
* [Screenshots](#screenshots)
* [Installation](#installation)
* [Usage](#usage)
* [Road-map](#Road-map)
* [Project Status](#ProjectStatus)


## :gear: Manifest

```
   app (folder) ----------------> Contains all the Source code of the Covid Detector App
   Covid_CNN_Model (folder) ----> Contains all the Source code of the trained 20 CNN Image classification model
```


## :camera_flash: Screenshots 

| <img src="App screenshots/Splash.jpg" height=400 width=210 > <P>Splashscreen Layout| <img src="App screenshots/home.jpg" height=400 width=210 > <P>Homescreen Layout | <img src="App screenshots/crop.jpg"  height=400 width=210> <P>Crop Layout  | <img src="App screenshots/home2.jpg" height=400 width=210> <P>Display progress bar  
| ---------------------------------------------- | -------------------------------------------- | ------------------------------------------- | ------------------------------------------- |
</br>

| <img src="App screenshots/output.jpg" height=400 width=210> <P>Model Output Layout | <img src="App screenshots/search1.jpg"  height=400 width=210> <P>Patients List Layout | <img src="App screenshots/search2.jpg" height=400 width=210> <P>Searchable List | <img src="App screenshots/metrics.jpg" height=400 width=210> <P>Model Metrics Layout
| ---------------------------------------------- | -------------------------------------------- | ------------------------------------------- |------------------------------------------- |
  
## :pencil: Installation 
  
  - <b>For easy APK installation</b>, you can download the APK file of Covid Detetor app from <a href="https://github.com/ranjithbing/Covid19_Detection/raw/master/Apk/Covid%20Detector.apk"><b>here</b></a> and install it on your Android smartphone.<br>
  or
  - <b>To Clone the project</b>,
       1. Download and install <a href="https://developer.android.com/studio">Android Studio</a>.
       2. After installing, from the Android Studio's Toolbar, Select <b>File > New > Project Version Control</b>
        <P align="center">
        <img src="App screenshots/clone1.jpg" ></P>
       3. Paste the repositorie's url (https://github.com/ranjithbing/Covid19_Detection.git) under the URL input field and click on the <b>Clone</b> button to clone the project successfully.<br>
        <P align="center">
        <img src="App screenshots/clone2.jpg"></P>
       4. If you want to change the projects Package names, You can check <a href="https://stackoverflow.com/questions/16804093/rename-package-in-android-studio"><b>here</b></a> on how to do that (Optional).
       5. If you want to change the database with your own Firebase account, You can check <a href="https://firebase.google.com/docs/android/setup"><b>here</b></a> on how to do that     (Optional). <br>
  - To generate the <b>APK</b> from the cloned project,
       1. Select <b>Build > Build Bundles(s)/APK(s) > Build APK(s)</b> from the Android Studio's Toolbar
       <img src="App screenshots/GenerateApk.jpg"></P>
 ## :zap: Usage 
  - For testing the app, Download the sample chest X-rays images from <a href="https://github.com/ranjithbing/Covid19_Detection/tree/master/Covid_CNN_Model/X-ray%20Images%20for%20Testing"><b>here</b><a> first.
  - <a href="https://github.com/ranjithbing/Covid19_Detection/raw/master/Apk/Covid%20Detector.apk"><b>Click here</b></a> to Download the App.
  - After Installing it, open the app on your Android smartphone where App's home screen will be displyed by default.
  - Enter the <b>Name</b> and <b>Phone number</b> on the respective input fields.
  - After that, Click on the *__<b>Pick X-ray</b>__* button to select a chest X-ray image from your smartphone's local storage. 
  - You can crop the selected selected X-ray image as per you need.
  - Finally, Click on the *__<b>Predict</b>__* button to get the prediction from the trained CNN Covid19 model.
  - For Navigation, You can use the app's *__<b>Bottom Navigation Bar</b>__* to navigate throughout the app.
  - You can also use the App's <b>:mag:Search Bar</b> to locate your record.
  
## :coffee: Support 
  <a href="https://www.buymeacoffee.com/ranjith">
  <img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" height=100 width=350></a>
  
  Contact: :e-mail: [Email me](mailto:ranjithkumarcena@gmail.com) for any other queries.

## :dart: Road-map 
   <b>In future</b>,
   - The Integrated CNN classifier will be trained with more Covid chest X-ray datasets to make the app even more reliable.
   - User <b>Login/Sign_up</b> layout with 2 step-verification will be developed for the app, so that users can have their own set of patients record.
   - Sort feature will be developed for the patient's list.
   - Contextual Menu for deleting multiple patient's record at once will be developed.
   - Local Imaging caching feature will be implemented to load the patient's X-ray images quicker.
## :scroll: License 
  This project is licensed under the [Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0).
```
   Copyright 2021 Ranjithkumar Rajendran

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
```
   See [`LICENSE`](LICENSE) for the full license.
## :clipboard: Project Status 
  - Currently, I am working on some additional features for the App.
  - You're always welcome to contribute to this project.  
