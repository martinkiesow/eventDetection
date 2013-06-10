######################################################################
# Computational Approaches on how to infer events from observations 
# 
# corresponding Paper:
# http://www.geographicknowledge.de/pdf/eventconstruction
######################################################################

This project uses wheather observations from canada, 1964 to infer
blizzard events during this period.
To compute other use cases you have to create new parser and continuity
checker classes.


######################################################################
# Project Structure

/src/main			source code
/src/test/java		test classes
/src/test/resources	sample observations for test classes and canada/
					blizzard case


######################################################################
# Installation

Use Apache Maven to a get all dependencies and create a maven project 
from the given pom.xml and classes.


######################################################################
# Start Event Detection

1. adjust settings in (de.ifgi.musil.eventDetection)
   EventDetector.Main(). Change your project's local path, if necessary.
2. run EventDetector.Main()


######################################################################
# Weather data (CNCA)

The blizzard example case uses weather data from the National Climate 
Data and Information Archive of Environment Canada, as available here:

http://www.climate.weatheroffice.gc.ca/climateData/hourlydata_e.html?timeframe=1&Prov=MB&StationID=3471&hlyRange=1958-01-01&Month=3&Day=18&Year=1964

To create XMLBeans parser classes the schema file (and schema file 
reference) was modified.