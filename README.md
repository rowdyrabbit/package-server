Digital Ocean Assignment
========================

Design Rationale
----------------

The design of this app was broken into four main pieces, as indicated by the package names in the source repo.

### Server
The three classes Main, PackageServer and ConnectionHandler are responsible for the bare bones of the multi-threaded server code, and don't implement
any application-specific logic.

### Repository
This class manages the state of the application, i.e. it stores all package information and contains a thread-safe API for querying the repository.
The Repository class synchronizes all access to the repository HashMap data store to ensure consistency in reads/writes to the data store. It is 
possible to reduce lock contention and increase throughput if required, depending on the performance requirements of the repository server. For now 
I have implemented it in a simple way.

### Parser
The classes in this package are responsible for parsing and validating all messages received by the server. The main handler is the MessageParser class.

### Protocol
The MessageHandler class is responsible for liasing between the MessageParser and Repository, it takes the result of MessageParser and either invokes the 
corect Repository method and returns OK/FAIL or returns ERROR to the server thread which then outputs the result to the client.


Running the Server
------------------

### Requirements
Java JDK Version 8 [download here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

To run the server:
1. Execute `java -jar bin/do-assignment-server-jar.jar` - this will start the server on port 8080, which you can then make requests to.


Running as a Docker Image
-------------------------

### Building the image
Run this command from the directory containing the Dockerfile file:
`docker build -t do-package-server .`

### Running the image
Execute this:
`docker run -d -p 8080:8080 do-package-server`

### Connecting to the docker container
This depends on where you're running the image. If you're running it on OSX, you'll need to find the IP of docker-machine using:
`docker-machine ls`, then connect to the server running in the container using that ip and port 8080.


Building from Source
--------------------

### Requirements
Java JDK Version 8 [download here](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)
Apache Maven Version 3+ [download here] (https://maven.apache.org/download.cgi)

To build the executable jar:
1. Change to the top level directory `do_assignment`
2. Run `mvn package` - this will run all the tests and produce an executable jar in the `./target/` directory.
3. Now to start the server, run `java -jar target/do-assignment-1.0-SNAPSHOT-jar.jar`

