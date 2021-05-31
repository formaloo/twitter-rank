# Twitter Rank Android Application
This is an open-source project connected to Formaloo CDP to calculate a tweeter and his/her followings rank.
For each tweeter you have a customer in CDP database. each customer has a field named score,
 that is calculated by CDP based on the tweeter account information like number of tweets, age of the tweeter account etc.  



https://en.formaloo.com/developers/

## Installation
Clone this repository and import into **Android Studio**

git clone https://github.com/formaloo/twitter-rank.git


## The App
You can install [TwitterRank APK](https://play.google.com/store/apps/details?id=co.idearun.twitter) on your phone.

![Screenshots](images/tweeter-rank.jpg)![Screenshots](images/twitter-rank.jpg)

# Archi
* __MVVM__: Model View ViewModel with LiveData.

## Api Token
To access CDP API token you need an account on [Formaloo CDP](https://cdp.formaloo.net/).
After sign in to your account go to connection tab and copy the provided key.

You can create your own [Twitter developer account](https://developer.twitter.com/en/portal/dashboard)
to get BEARER_TOKEN and replace yours.

### Libraries used
The application supports Android 4.4 KitKat (API level 19) and above.

* Kotlin
* AppCompat, CardView and RecyclerView
* Coroutines
* Koin di
* Retrofit 2
* Glide
* MPAndroidChart, speedView


