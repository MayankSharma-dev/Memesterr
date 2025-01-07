<h1 align="center"><img width=20% height=50% alt="Memesterr V1(Java)" src="https://github.com/user-attachments/assets/0a05833b-bc80-46ae-a37d-e1bc864ac84b"></h1>

<h1 align="center">Memesterr V1 (Java)</h1>

# Memesterr-V1

**Memesterr-V1** is a third-party client for Reddit and Twitter specially created for memes.<br>
It is a native Android application built using Java, designed to provide an easy and enhanced way for users to browse, download, share, and bookmark memes from Reddit and Twitter and also uses Omegle for chatting. By integrating both platforms in one app, Memesterr-V1 brings an optimized and seamless experience for meme lovers. The app supports Firebase Email Authentication for secure sign-in and utilizes Firebase Realtime Database to store users' favorite memes for easy access across devices.

---
<!---
<h2 align="start" id="Demo">Demo
<br>
 <video width="320" height="240" src="https://github.com/user-attachments/assets/455e85b2-6ec1-439c-a7f5-6cc1f41d982c" type="video/mp4"> Your browser does not support the video tag. </video>
//Here is the video demo
---
--->

## Features

- **Integrated Meme Feed**: Access memes from both Reddit and Twitter in one place, offering a simplified browsing experience.
- **Auto Pause/Play for Videos**: The app supports automatic video playback pause and play in the RecyclerView. When you scroll through memes, videos automatically pause when they are no longer visible, and start playing when they come into view, ensuring a smooth and efficient experience for users.
- **Bookmark Favorites**: Save your favorite memes using Firebase Realtime Database and access them anytime, from any device.
- **Firebase Authentication**: Secure login with Firebase Email Authentication.
- **Download Memes**: Easily download memes for offline access.
- **Share Memes**: Share memes with your friends and family across different social platforms.
- **Cloud Sync**: All bookmarked memes are stored in the cloud, ensuring data is accessible across devices.
- **Omegle API**: Integrated to enable chatting with strangers through the app.

---

## Technologies Used

- **Java**: Core programming language for building the application.
- **Android SDK**: Android development framework used to build the native app.
- **Firebase Authentication**: For secure email sign-in and user authentication.
- **Firebase Realtime Database**: To store users' bookmarked memes in the cloud.
- **Retrofit**: To handle network calls for retrieving memes from Reddit and Twitter.
- **Glide**: For efficient image loading and caching of memes.
- **Material Design**: To ensure a smooth and responsive user interface.

---

## Installation

To get started with the Memesterr-V1 app on your local machine, follow these steps:

### Prerequisites

- **Android Studio**: Make sure you have the latest version of Android Studio installed.
- **Java**: Java 8 or above installed.

### Steps

1. **Clone the repository:**

   ```bash
   git clone https://github.com/yourusername/Memesterr-V1.git
   ```
2. **Open the project in Android Studio:**

Open Android Studio and navigate to the directory where the project is cloned.

3. **Configure Firebase:**

1. Go to the Firebase Console: [Firebase Console](https://console.firebase.google.com/).
2. Create a new Firebase project or select an existing one.
3. Add Firebase Authentication and Realtime Database to your project.
4. Download the `google-services.json` file from the Firebase console and place it in the `app/` directory of your project.

4. **Build and Run:**

1. Build the project by clicking on `Build > Make Project`.
2. Connect an Android device or start an emulator.
3. Run the application from Android Studio by clicking the `Run` button.

---

## Usage

### Sign in:
- Upon launching the app, users are prompted to sign in with their email address via Firebase Authentication.

### Browse Memes:
- Navigate through a curated list of memes from Reddit and Twitter.

### Bookmark:
- Tap the bookmark icon to save memes to your Firebase account.

### Download & Share:
- Tap the download icon to save memes locally or use the share button to share them via other platforms.

---

## Contributing

Contributions are welcome! If you'd like to contribute to the project, please follow these steps:

1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Make your changes and commit (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Open a pull request and describe your changes.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
