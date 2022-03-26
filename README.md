# EAD - Educação a Distância (Distance Learning)

This project was built to practice the knowledge learned in the course "Projeto Decoder: Formação de Especialista em Microservices Java Spring", (Decoder Project: Java Spring Microservices Specialist Training).

The objective of the project was to build a small part of the Distance Learning System, but the focus was on microservices architecture and not in the business rules.

The project was build using Spring Boot, String Security and Spring Cloud mainly, and others projects from Spring family.

To run the App correctly, follow this steps:

1 - Run microservice service-registry. This is our Netflix Eureka Service Registry Server, where all the other microservices will registers itself.

2 - Run microservice config-server. This is our Spring Cloud Config, where all the other microservices use to get their properties, that are external, and are in another repository on GitHub.

3 - Run microservice api-gateway. The authuser and course microservices,  are using API Gateway to provide a way to route the Rest APIs.

4 - Now with all the previous microservices running, you can run authuser, course and notification. There is a another microservice, notification-hex. That microservice is it the same as notification, the only diference is that notification-hex was built using Hexagonal Architecture.

