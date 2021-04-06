# CATS AND DOGS TECHNICAL TEST

This is a simple android application to analyze the number of cats or dogs displayed on a recycleView

## Installation

Pull this repository and import it with AndroidStudio

```bash
git clone git@github.com:HellboyVN/catsAnddogsContentSquare.git
```

## Tasks

1. Each time the user taps on the screen, we should count the number of cats or dogs already scrolled by the user.
>* Using only the two methods from the CsAnalytics​ ​ class to achieve this objective. No need to call any other class outside of the SDK package.
>>* *public void track(@NonNull RecyclerView.ViewHolder holder, int position {}* : Save the pet at the current position to the pet tracker. The pet tracker will store the pet positions like:
>>>* <dog> <0, 1, 4, 5, 6, 7, 9 ,10>
>>>* <cat> <2, 3, 8>
>>* *public void trigger(@NonNull RecyclerView.ViewHolder holder, int position) {}*: calculate how many pets are the same with the pet at current position base on the index of current position in the pet tracker, then display the result in logcats and android Toast.

>* Log the result to the Logcat.

2. Implement a debounce ​ without​ using library. Implement a debounce of 2 seconds before printing the result to the logcat.
>* I have implemented 2 debounce classes, one using thread runable, one is not. And I choose to use the DebounceRunable class.
>* Example​ : If the user performs 4 taps in a second, only the last tap should be printed to the Logcat.
3. Write the result to a file, in addition of the Logcat
>* Use the previously implemented debounce code to write to file at a maximum rate of one access every 2 seconds.
4. Read the same file and show the result
>* Create a broadcast receiver class, register and un-register it inside MainActivity.class. When the file is updated, I will send an intent to notify the receiver, and then read the file content.
>* Show the result read from the file with an Android Toast.

# Bonus exercise
1. Update the model to allow data to survive configuration changes such as screen rotations using ViewModel and ViewModelProvider.Factory.\
Another method is to add below text to AndroidManifest.xml
```android
android:configChanges="orientation|screenSize|screenLayout|keyboardHidden"
```

2. Specifies one flavor dimension named "**levanTechnicalTest**" contains one productFlavors "**catsAndDogs**" and two  build variants:
>* debug: enable logcat and debug information.
>* release: signing app with singing key, enable proguardFiles to strip all the logs information in debug mode.
>* Two build variants have the different configurations like string app_name,  applicationIdSuffix, versionNameSuffix...

# Unit tests

1. Local unit test for DebounceRunable:\
Implement a counter which is increase the count value by one each time it is called. The DebounceRunable has the debounce value = 2000 ms.

>* Call debouncedRunnable.run() 10 times in 5000 ms, and expected the count variable just print 3 times at time = 0, time = 2000 and time = 4000 ms.
>* Call debouncedRunnable.run() 10 times in 1000ms, and expected the count variable just print only 1 times at time = 0.
>* Call debouncedRunnable.run() 10 times in 25000ms, and expected the count variable just print 10 times at time = 0, 2500, 5000, 7500, 10000, 12500, 15000, 17500, 20000, 22500 ms.

2. Instrumented tests for CsAnalytics class:\
Launch the app on real android device and execute three tests.

>* Open application, execute screen rotation to test data can survive or not.
>* Open application, scroll to specific position and press the item, make sure the app features working as expected.
>* Compare the pet tracker values of CsAnalytics and the values on the initial list of pets.

3. Right now the test is running on debug build type. To run the test:
>* For local unit test: ```./gradlew test```
>* For instrumented test: ```./gradlew connectedAndroidTest``` (make sure your device is connected)

        

