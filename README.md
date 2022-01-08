# CitySearchAndroid
Before proceeding, ensure that you have the following requirements:

- JDK 1.8+
- Android SDK
- Kotlin v1.5+
- Android Studio 4.0+

## Getting Started

1. Onboard WorldCities(app name) project.

2. Clone the project from github:

git clone 
https://github.com/city-search-kotlin

3. Install Android Studio and run through setup from the installer

4. Open up the project and let all the libs and gradle been setup automatically

5. Then you are good to go!
## Core Logic: Why TreeMap:

"TreeMap is an example of a SortedMap.
It is implemented by the Red-Black tree, which means that the order of the keys is sorted." 
Along with this, TreeMap has many features like: 
    *TreeMap also contains value based on the key.
    *TreeMap is sorted by keys.
    *It contains unique elements.
    *It cannot have a null key but have multiple null values.
    *Keys are in ascending order.
    *It stores the object in the tree structure.
which are perfectly suited for the requirement of this task.
Inside the TreeMap , the keys are are stored in a binary search tree
and uses a BinarySearch which is more efficient in the case of larger datasets.

The TreeMap itself is implemented using a red-black tree which is a self-balancing binary search tree.
Since it uses a binary tree, the put(), contains() and remove() operations have a time complexity of O(log n).
Furthermore, since the tree is balanced, the worst-case time complexity is also O(log n).

## Project Structure
 📂 data/            // → For data-layer related classes (Local/remote Data Source, Entities)
        ├─ 📂 entities/         // → For data-layer related classes (Models, entities etc)
             ├─ CitiDataSource  // → For fetching data from remote/local (here from raw folder)
             ├─ CityRepositoryImpl  // → To implement the logic(if any) needed to fetch data in required format
             
 📂 di/        // → For Dependency injection related classed -to contain Dagger-Hilt component and modules

 📂 domain/           // → For domain-layer related classes (UseCases, Domain models)
        ├─ 📂 interfaces/   // → For data-layer related interfaces (which can be implemented by any Data Repositories)
        ├─ 📂 usecase/     // → Classes that connects domain layer and data layer and cab be used directly by UI/presentation layer

📂 ui/         //For presentation-layer related classes (Fragments, ViewModels, Adapters etc)
        ├─ base   //For common/base classes used throughout UI package
        ├─ home, map  //Features divided by screen/module
              
📂 utils/        // → For constants, Extensions, UI alerts etc
            
## Dependency details
    1. Dependency Injection - Dagger-Hilt -com.google.dagger:hilt
    2. Navigation - androidx.navigation:navigation
    3. Unit testing - mockito, Espresso and others for androidTest(UI testing)
    4. Map testing - uiautomator
