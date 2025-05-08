FROM openjdk:17-jdk-slim

WORKDIR /app

COPY Server/src/WordSorterServer.java .

RUN javac WordSorterServer.java

EXPOSE 5000

CMD ["java", "WordSorterServer"] 