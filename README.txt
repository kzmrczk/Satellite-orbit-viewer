OrbitViewer100

Software for displaying orbits. It uses Orekit library to generate an orbit from user's input and displays
the orbit using Worldwind library. 

Authors: Hamish McPhee, Dawid Kazimierczak
Published: 14/03/2020

***Architecture***
The software is delivered in a .zip file 'OrbitViewer1000.zip'. After the content is extracted, the folder should containt several subfolders, README file, 'orekit-master-data.zip' file and other residual files. Subfolder 'libs' contains all classes and examples that support Orekit and Worldwind and subfolder 'src' stores all .java files that run the software. These files and the can be accessed following the path 'src\main\java\fr\isae\mae\ss'. These files have different functions in the software:

1. OrbitViewer1000.java: It is the main file of the software. It calls other classes to obtain orbital data and displays the orbit in Worldwind.
2. Dialogging.java: Runs an interface between the user and the software.
3. OrbitGenerator.java: Builds an orbit from user's input at the interface. Uses Orekit library to generate the orbit.
4. AbsoluteFilePath.java: It is used to find the location of specified files. It helps automatize the configuration.

***Configuration***
For maintaining accurate time models from the OreKit data set, the software is loading the file 'orekit-data-master.zip' and it can be found in the projects folder. The software does is automatically. In case the software fails to find the file it will print an error:

"You need to specify the location and name of the orekit-data-master file in OrbitGenerator.java"

If it happens, you must ensure the file 'orekit-data-master.zip' is in the main folder. If the file is not on your computer, you can download it by following the link:

https://gitlab.orekit.org/orekit/orekit-data/-/archive/master/orekit-data-master.zip

You can also select the directory manually by editing a line of code in OrbitGenerator.java file. You must define the location of the data file in your directory within the same .java file (simply edit in notepad). Uncomment following line to select the location manually:

File orekitData = new File("C:/Users/username/Documents/orekit-data-master");

Once the data file has been located and identified in the OrbitGenerator code, you can start the software.

***Running the software***
Running the software can be done in an IDE such as Eclipse or simply in the terminal. The file to run is \soft{}.java. Upon running the file, the first input dialog should pop up. The first selection to make is whether you would like to visualise LEO, SSO, GEO, or a custom orbit of your own design. Simply clicking on the first three options will provide you with an example of the type of orbit you picked. Clicking the "Custom" option will lead to a new input dialog being opened. You must then begin to type each of the orbital parameters of the orbit you wish to visualise.

***Outputs***
After the software is successfully run a Worldwind window. It depicts the Earth's globe as a 3D model which can be manipulated in space using a mouse. The window has an interface on the left side which allows to turn on various layers in the 3D model, like atmosphere or Earth at night layers which change the globe's appearance. There's also a possibility to view the Earth as a map by changing the Globe option to Flat. The generated orbit is depicted as a red line. OrbitViewer1000 generates also a file 'dataFile' with positions of the orbit at each step of the propagation. The file can be found in the main folder of the software.

Congratulations on displaying an orbit! Stay tuned for later versions that improve the educational capability of the application with coverage cones and visualisation of a complete orbit instead of half!