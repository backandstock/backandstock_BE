#!/bin/bash

CURRENT_PORT=$(cat /home/ubuntu/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Current port of running WAS is ${CURRENT_PORT}."

if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "> No WAS is connected to nginx"
fi

TARGET_PID=$(lsof -Fp -i TCP:${TARGET_PORT} | grep -Po 'p[0-9]+' | grep -Po '[0-9]+')

if [ ! -z ${TARGET_PID} ]; then
  echo "> Kill WAS running at ${TARGET_PORT}."
  sudo kill ${TARGET_PID}
fi

#nohup java -jar -Dspring.config.location=/home/ubuntu/miniBacktesting_be/build/libs/ -Dserver.port=${TARGET_PORT} /home/ubuntu/miniBacktesting_be/build/libs/* >/home/ubuntu/nohup.out 2>&1 &
nohup java -jar -Dspring.config.location=/home/ubuntu/miniBacktesting_be/build/libs/ -Dserver.port=${TARGET_PORT} /home/ubuntu/miniBacktesting_be/build/libs/miniBacktesting_be-0.0.1-SNAPSHOT.jar &
echo "> Now new WAS runs at ${TARGET_PORT}."
exit 0
