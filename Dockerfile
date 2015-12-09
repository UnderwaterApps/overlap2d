FROM java:8-jdk
RUN apt-get update && apt-get install -qqy xvfb
ENV DISPLAY=:99.0
WORKDIR /root/overlap2d