# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```
## SequenceDiagram for Phase 3
```sh
[![Sequence Diagram](Sequence Diagram3.png)]
(https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=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-PRvtWb3yMd8p+KAwAbO9P7YZbD1BjN-lk9K1kGZfPpRdalBieHpzWVSzpITlZguZOqCp7JGwVH6GKlAHNpCNktglMciongtUyJmXyTcpg6AQKAHs6r0rFyeNKgAculCuewYCNCibIpFpQ4loulaoCVUr0qyvlYq5VUxVVCRjvcPoXxtW6v1aMQ1UwTVmv9Raq1ZhEld3KoEbAPgoDYG4PAMOzLRgpDHm1WRRSFHlG6g0cplSJGtzVOG0YbsSh1OEdU8t6VTWVsaSfOWXtMgbNhKHAS7aBUmixMs-x4Iu2CQ2SCvxjJUEQoxdQoUnYjR9oRRgW1yobnortjOw0YpDpgDxadXplKX4WOqTYoOdigzjJgIe1xgCZk2QJZJX65LFlAx5XLYdPbgWCuQROiJtz12HOwcc55pyCHnJtZcso1zlGUIOVglGTy8GMOYbMn6vyB1wI+aUd9KBR1fsET+8Ff6xFYo0XC6RYHEUQaVCi6DkLYPiKdmRrRKHdFXp6bozsHAuUWIrSgE9oyz1YfTdx1x7jtIwF4+hja2HcNlhzvhtW0q3XlAVUqijS6qP2tFa6uVKmPUd1jWVHuARLCOK2skGAAApCAYYM2GACIGkAPYCmyJbaQ6ozpeotGlVU7x6A1QpuAKZqAcAIBbSgLMJT0gq1rT3n5tAAWdXBdC+FyLOmVpuTc+CAAVjZtAHbrNhlw326W-zB2kkK-lgVcnQmgp2SKojUKiKbuNNuxdciMOXBXbR6daiWsLroqS-dYAr0DNytYuln9GXhivdM+L2i5m6IgWeKBL6ysYfBJV2Tb5v1q1EU18mgGEMvKQ+121UG0X7fo4d+DuCTtnKrfi9lhK0Ovsw1t6rO2FOTsa9d7WpHDbketZRzryLUVTv-dCgH8LBt3uew+tjQ3dFOhE7CKL-GGWCYvSJt6UWFs-RE1J9OH3P01aFfVy4EPiMGjnVuiU6mOsWyJr9+5gp+ttbom9zbeXtvye2QRhra7qdAPbAJZcq5woM+XU4Ccl39ms9bKLzs3ZgD5Vh+7MlHLSh+C0B+lANLAvJbC9AaY81pjo8m7YmSzpsC68MN6zMjbDDQCFDqpzjv0orggO9X1VTlBLDDI4SAMBDeUBSy72zOOxOeKizAIncsSebLw-z2awONOg7tUTBJJU43Ga8EF+A3A8BAOwCmwg8Rnl5PHpPDypCZ5zwXkvVoxhlYBnsjAF1oxlOFWbZ1dOMhHGUpQLCBxTiun9tJTIXRT5hgQBoAoLhmYNDnW-iYofIuEARg6P6Wz3oABC+gxCT5+jPufOGcmW9PTJNfmQJOe939oA-BhDFT-JQ5DYZ+fat2X4-Vfg-b8+wwAP7yBP5iAr5Zakiv4crPoURsw1q7JPg1aj5D7NpuZeQs6Yo0JHZ3YgZvLq7VoFBxTlAXZU4HZYG3b0K0ygaPZ5jwHMiIHljIHjyVjszFKRI7yEFEKJRZ6mCdxAA)
```
