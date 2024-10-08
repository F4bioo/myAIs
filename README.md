# myAIs

\#buildwithgemini

**myAIs** is an Android application primarily focused on accessibility. The app is designed to assist individuals with visual impairments by providing a simple and effective way to understand their surroundings. By leveraging artificial intelligence, myAIs describes images captured by the device's camera, offering users easy access to visual information. Additionally, the app features a user-friendly interface specifically designed to be easily navigable for those with visual limitations. The emphasis on accessibility is central to the app’s design and functionality, ensuring it serves as a genuinely useful and inclusive tool in the daily lives of its users.

## Features

- **AI-powered Image Description**: Provides detailed descriptions of images captured through the camera.
- **Save Visual Memories**: Allows users to save images and their descriptions for future reference.
- **User-Friendly Interface**: Designed with accessibility in mind, ensuring ease of use for visually impaired users.

## Setting Up the Gemini API Key for `myAIs`

The `myAIs` app requires an API key from the Gemini API to function correctly. This key is managed securely using the `secrets-gradle-plugin`. If the `secrets/apiKey.properties` file is not present, the project will use fallback values provided in the `default.properties` file.

### Steps to Set Up the Gemini API Key

1. **Ensure the `default.properties` File is Configured**:
   - In the root directory of the project, ensure there is a `default.properties` file. This file provides default values, including a placeholder for the Gemini API key.

   Example `default.properties`:
   ```properties
   GEMINI_API_KEY=*********************************
   GEMINI_BASE_URL="https://generativelanguage.googleapis.com/"
   DRIVE_BASE_URL="https://www.googleapis.com/"
   PROMPT_BASE_URL="http://localhost/"
   ```
    - The placeholder value `*********************************` will be used during the build process if no real key is provided.

### Important Notes

- **Do Not Modify Base URLs**: The base URLs for services used by the application are defined in `default.properties`. To maintain consistent functionality, avoid modifying these URLs.
- **Security**: It's advisable to exclude files containing sensitive information, such as API keys, from version control to protect your credentials.

By following these steps, you can ensure that the `myAIs` project builds successfully in all environments.

## Screens
<img src="media/screens.png" alt="Screens" width="997" />

## Video Demonstrations

|                                            Permission Request                                            |                                             Capture Feature                                              |
|:--------------------------------------------------------------------------------------------------------:|:--------------------------------------------------------------------------------------------------------:|
| <video src='https://github.com/user-attachments/assets/8f472104-c6a7-4cc2-bd1c-98cd2d564497' width=180/> | <video src='https://github.com/user-attachments/assets/81b85c06-817a-44bb-8eed-b85e2880d528' width=180/> |
