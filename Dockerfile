FROM openjdk:8

ADD build/libs/ontology-0.0.1-SNAPSHOT.jar ontology-0.0.1-SNAPSHOT.jar

ENTRYPOINT ["java","-jar","ontology-0.0.1-SNAPSHOT.jar"]