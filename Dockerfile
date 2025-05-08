FROM openjdk:17-jdk-slim

WORKDIR /app

COPY Server/src/WordSorterServer.java .

RUN javac WordSorterServer.java

ENV PORT=5000
ENV HOST=0.0.0.0

EXPOSE ${PORT}

CMD ["java", "WordSorterServer"] 