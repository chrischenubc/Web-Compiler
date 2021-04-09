# Web Compiler

Web Compiler is a web service written in Java, which allows users to send and compile their code
on the server side.

## Feature
* Has a simple front-end page to send requests
* Supports Java(7-14) and Golang(1.10-1.12)
* Supports two use cases
* Returns .class for Java
* Returns an executable for Go
* Provides ways to send flags and options to compiler
* Allows user-defined command
* Includes Junit tests

## How To Use

```bash
#build the project
./mvnw clean package

# Start the application
docker-compose up --build

# View the log
docker-compose logs
```

After starting the application, visit [localhost:8080](localhost:8080)

![index.html](img/index.png)

After uploading the code and enter the proper values, the server will compile the code and send it back to you.

Your brower will automatically start downloading the file.

You can also refresh the page to see the files that are already stored on the server.

## Implementations
The command used to compile Java:

`javac --release [version] [options] [source_file] `

The command used to compile Go:

`go tool compile -lang=go[version] [options] [source_file] && go tool link -o binary_file 
object_file.o`

## APIs
There are two use cases for the system, so I define two REST APIs:

#### First Use Case:

User uploads a source code file, choose language version(optional) 
and enter options/flags (optional) for the compiler. 

```$xslt
curl --location --request POST 'http://localhost:8080/compiler?language=java&version=9' \
--form 'file=@/test_files/Sample.java' \
--form 'flags=-verbose'
```
@RequestParam: **'version'** and **'flags'** are optional.
 
If **'version'** is not set by the user,
the compiler will compile the Java code with Java 8 and compile the Go code with golang 1.12.

If **'flags'** is not set by the user,
the compiler will compile the code without any options/flags.

#### Second Use Case:

User submits the file along with the user-defined compile command.

The second one allows user to define the compile command based on their needs.

```$xslt
curl --location --request POST 'http://localhost:8080/command?language=go' \
--form 'file=@/path/to/file' \
--form 'command=go tool compile -lang=go1.11 main.go && go tool link -o main main.o'
```

The user-defined compiler command for Java **must** be in this format:

`javac --release version [options] [source_file] `

The user-defined command for Go **must** be in this format:

`go tool compile -lang=go[version] [options] [source_file] && go tool link -o binary_file 
object_file.o`

## FAQ
What language versions supported?
    
    * Java 7 to Java 15
    * Golang 1.10 to 1.12
    
What are some limitations of the system?

   * Only support one source file to be compiled
    
   * Only support standard library for Java and Go

What is the framework used in the project?

    Spring Boot

What about the code you submit to the server?

    The code is stored in a temporary folder and will be removed once the server restarts.
    
Where is the log file?
    
    var/log/application.log
    
How to run tests?
    
    ./mvnw clean test
    
How to check the jdk version used to compile a .class file?

> [https://stackoverflow.com/questions/1096148/how-to-check-the-jdk-version-used-to-compile-a-class-file](https://stackoverflow.com/questions/1096148/how-to-check-the-jdk-version-used-to-compile-a-class-file)

What is the maximum upload size for the file?

    10MB. 
    Any request with file exceeds the limit will result in an error
