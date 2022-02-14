# Argumentation Solver # 

This is a spring boot command line application to solve Abstract Argumentation Frameworks according to ICCMA participation rules.

## Congifuration ##

Set author and version numbers in `src/main/java/resources/application.properties`

## Build ##

Using maven:
```
mvn package
```

## Usage ##

Get help:
```
java -jar target/argumentation-solver-0.0.1-SNAPSHOT.jar -?
```
Usage according to [ICCMA rules](http://argumentationcompetition.org/2021/rules.html)
```
usage: java -jar argumentation-solver
 -?,--help               Print usage instructions
 -a,--additional <arg>   Additional parameter required for the function
 -f,--file <arg>         Input filename
 -fo,--format <arg>      Input file format
 -formats,--formats      Prints the supported formats of the solver in the
                         form
                         [supportedFormat1,...,supportedFormatN]
 -p,--problem <arg>      Problem to solve
 -problems,--problems    Prints the supported computational problems in
                         the form
                         [supportedProblem1,...,supportedProblemN]
```

Parse an example TGF file
```
java -jar target/argumentation-solver-0.0.1-SNAPSHOT.jar -f input/figure2.tgf
```

## Notes ##

**Preview version:** can read files but does not yet calculate extensions
