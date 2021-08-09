## COVID-19 Detector
-  An Android application that predicts the presence of COVID-19 from Chest X-rays using deep convolution neural networks. A complex well-trained 20 layer CNN model that classifies Covid X-rays from the normal ones is integrated into the App. The developed app takes Name, phone number and Chest X-Rays image as inputs. Before predicting an output, the App checks if the given image is a chest X-ray or not, and a lite weight CNN model for that is also integrated into the App. This App also uses the firebase database as a backend to store and fetch all the users details and the CNN model’s prediction in real-time. Once the model generates the prediction (Covid/Normal), the App send an SMS along with the model result to the user’s registered phone number.</br>

![version](https://img.shields.io/badge/Version-1.1-orange)

