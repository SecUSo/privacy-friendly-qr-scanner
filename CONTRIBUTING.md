# How to contribute

We encourage open source developers to support the Privacy Friendly Apps. 
We also wish to keep it as easy as possible to contribute. There are a few 
guidelines that we need contributors to follow.
For further questions we refer to the contact details on the [Privacy Friendly Apps website](https://secuso.org/pfa). 

## Reporting of Issues

* Make sure you have a [GitHub account](https://github.com/signup/free).
* Open an issue in the corresponding app's repository, assuming one does not already exist.
  * Clearly describe the issue including steps to reproduce when it is a bug.
    In some cases screenshots can be supportive. 
  * Make sure you mention the Android version and the device you have used when 
    you encountered the issue. 
* Make your description as precise as possible.
	
## Making Changes

* Make sure you have a [GitHub account](https://github.com/signup/free).
* Find an issue that you wish to close. If the issue you wish to close is not 
  present, open it. Make sure that the issue has one of the following labels
  which are set by our team:
  * Bug
  * Enhancement
  * Help wanted
  * No integration planned 
  That means that we have already reviewed the issue. If you wish to add a 
  translation, opening an issue is not required. 
* Fork the repository on GitHub.
* Create a topic branch from where you want to base your work (usually master branch).
  * To quickly create a topic branch based on master, run `git checkout -b
    fix/master/my_contribution master`. 
  *	Please avoid working directly on the `master` branch.
* Make commits of logical units in english language.
* Make sure your commit messages are in the proper format. If the commit
  addresses an issue filed in the Github repository, start the first line 
  of the commit with a hash followed by the issue number (e.g. #42).
* Make sure you have added the necessary tests for your changes.
* Run all available tests to assure nothing else was accidentally broken.

### Unwanted Changes

The Privacy Friendly Apps are a group of Android apps that are optimized regarding
the user's privacy. Therefore, Pull Requests that contain the following functionality
will be rejected:
* Analytics or advertisement frameworks
* User tracking (e.g. sending of data to a third party)
* Any that use of libraries that do not comply the license of the corresponding Privacy
  Friendly App (GPLv3 or Apache2).  
* Unnecessary use of Android permissions. If new functionality is added that requires
  the usage of an Android permission you should clearly explain the Pull Request why
  this permission is required.
* New translations/languages  

## Submitting Changes

* Push your changes to a topic branch in your fork of the repository.
* Submit a Pull Request to the repository of the corresponding Privacy Friendly App.
* Indicate that you have read this policy by writing the second word of the section "unwanted changes"
* Our team looks at Pull Requests on a regular basis and assigns a reviewer.  
* After feedback has been given we expect responses within one month. After one
  month we might close the pull request if no activity is shown.
   
