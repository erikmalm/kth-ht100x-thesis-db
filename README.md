# Adhoc and Prototype Database Test Tool
 
This project is a command-line tool for testing the performance of adhoc and norm databases. The tool allows the user to run various tests on the databases and view the results.

## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites
- Java 11 or higher
- PostgreSQL

### Installing
1. Clone the repository:
```
git clone https://github.com/[USERNAME]/adhoc-norm-db-test.git
```
2. Change directories into the project root:
```
cd adhoc-norm-db-test
```
3. Compile the project:
```
javac se/experiment/view/*.java se/experiment/controller/*.java se/experiment/integration/*.java se/experiment/model/*.java se/experiment/exceptions/*.java
```
4. Run the main class:
```
java se.experiment.view.BlockingInterpreter
```

## Using the tool

The tool operates through a command-line interface. The following commands are available:

- **`HELP`**: Lists all available commands.
- **`GET`**: Retrieves data from the adhoc database.
- **`PRINT`**: Toggles the display of test results.
- **`TEST`**: Runs a test on either the adhoc or norm database.
- **`QUIT`**: Exits the tool.

## Built with
- Java - The programming language used
- PostgreSQL - The database management system

## Authors
- Erik Sävström Malm, with inspiration from Leif Lindbäcks Command Line Interpreter
