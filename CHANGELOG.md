# Changelog

## [0.8.0] - 06-04-2019

- Updated dependencies
- Improved documentation
- Fixed some possible issues
- Add FloatValueSlider
- mScale from NumberSlider now needs to be implemented in implementations

## [0.7.0] - 23-06-2018

- Updated to AndroidX
- Target changed to API 28
- Updated dependencies

## [0.6.0] - 16-02-2018

- Converted to Kotlin
- Several methods were changed as part of the move to Kotlin
- Callbacks were changed as part of move to Kotlin and its higher order functions
- Moved from native SeekBar to AppCompatSeekBar to provide more uniform experience across all versions of Android

## [0.5.0] - 24-11-2017

- ValueSlider reworked to abstract class so integers etc. can make better use of shared preferences
- Add IntValueSlider
- Add generic ObjectValueSlider

## [0.4.0] - 24-11-2017

- Reworked how shared preferences are used
- Slider class no longer contains number specific functions and value support
- Add ValueSlider which replaces value implementation in Slider and makes it even better with generic type support
- Add abstract NumberSlider which implements Slider and serves as parent class for Int and Float Sliders
- Bugfixes

## [0.3.0] - 20-10-2017

- Support for better custom value handling (Previously could be somehow achieved with custom Scale function)
- Support for automatic saving of slider value to SharedPreferences
- More tests

- Refactoring

## [0.2.0] - 16-10-2017

- Set max slider value
- Set min slider value
- Set on value change listener
- Set progress value
- New IntSlider
- Reworked Slider which is now FloatSlider
- Many tests to make sure everything works as it should
- Javadoc to Slider and EMath

- Slider class rework
- Changed Scale classes
- Slider properties now reflect their type
- Updated to project to Android Studio 3.0 RC 1

- Float LinearScale rounding issue

## [0.1.0] - 14-10-2017

- Initial release
