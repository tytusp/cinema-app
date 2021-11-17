## How to run?
The easiest way to start the application is to run the released JAR file from the command line as specified below. Please note that it has been compiled using Java 17.  
The OMDb API key should be provided as the `cinema.externalcatalog.omdb.apiKey` application property (as requested, it has not been included in the code base).
```
java -jar cinema-api-0.0.1-SNAPSHOT.jar --cinema.externalcatalog.omdb.apiKey=[THE_API_KEY]
```
In case of starting the application from an IDE, please enter `--cinema.externalcatalog.omdb.apiKey=[THE_API_KEY]` in the programs arguments list.

## API documentation
Once the application is run, the documentation can be found at `http://localhost:8080/swagger-ui.html`

## Application architecture
* The application has been divided into four modules with the future development and scalability in mind.
  * The `cinema-api` module contains all the endpoints and integration tests.
  * The `cinema-core` module contains some required business logic and DTOs for transferring the data between the modules.
  * The `cinema-core-persistence` module contains the persistence layer (details below).
  * The `cinema-core-external-catalog` module contains code that connects with the OMDb API. It has been implemented in a way allowing to easily add other external movie catalogs integrations.
* There is a dependency inversion applied between the modules as presented in the diagram below. Thanks to that both `cinema-api` and `cinema-core` know nothing about details of the persistence and external catalogs implementations. It opens a possibility to easily extract the latter two as separate services in case the application or user base grow or even replace them with entirely different implementations.
````
cinema-core-persistence --------|
                                |---> cinema-core <--- cinema-api
cinema-core-external-catalog ---|
````
* MongoDB has been selected due to the following reasons:
  * It allows rapid prototyping and progress on early stages of the development - there is no need to define strict database schemas and then revise them and convert data when the requirements change. 
  * Its document-based nature seemed to be a good fit for the data to be stored by the application - for example movie reviews or show times (schedule items) may contain multiple different properties that can be persisted in a flexible way, without the need to update the entire data set.
  
## Test coverage:
* There are unit tests in place for the business logic implemented in the `com.tytuspawlak.cinema.core.logic.movie` package.
* There are integration tests implemented in `com.tytuspawlak.cinema.api.CinemaApiApplicationTests` that cover all the API methods (apart from the one for obtaining movie details from OMDb API as calls to that service are limited and for this reason I think it should not be included in automated tests). These tests require Docker to be installed as they use "real" MongoDB instance that is run in Docker via Testcontainers library. They can be run either from IDE or during Maven build.  

## Additional remarks:
* Fetching movies can be done both by internal MongoDB ID (default as the calls will be probably done by the client that would first fetch the available movies using `GET /api/movies` endpoint) or by IMDb ID (requires providing an additional parameter: `idType=IMDB`)
* System could be extended with a multi-tenancy possibility in order to scale it to be used by multiple cinemas - each DB entity could receive an additional property - a client/tenant ID - which would have to included in all the queries and could be used for MongoDB sharding once scaling is required.
