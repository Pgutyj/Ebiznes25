FROM ubuntu:22.04

ENV TZ=Europe/Warsaw


RUN apt-get update
RUN rm /bin/sh && ln -s /bin/bash /bin/sh
RUN apt-get install -y vim git curl zip python3.10 openjdk-8-jdk kotlin gradle



RUN useradd -ms /bin/bash student
RUN adduser student sudo

USER student

WORKDIR /home/student

COPY . /home/student

RUN curl -s "https://get.sdkman.io" | bash
RUN source "$HOME/.sdkman/bin/sdkman-init.sh" | bash

RUN sdk install gradle 8.13

EXPOSE 8080
VOLUME /home/student

SHELL ["/bin/bash", "-c"]
