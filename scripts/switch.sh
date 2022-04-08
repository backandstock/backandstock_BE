#!/bin/bash

# Crawl current connected port of WAS
CURRENT_PORT=$(cat /etc/nginx/conf.d/service_url.inc | grep -Po '[0-9]+' | tail -1)
TARGET_PORT=0

echo "> Nginx currently proxies to ${CURRENT_PORT}."

# Toggle port number
if [ ${CURRENT_PORT} -eq 8081 ]; then
  TARGET_PORT=8082
elif [ ${CURRENT_PORT} -eq 8082 ]; then
  TARGET_PORT=8081
else
  echo "> No WAS is connected to nginx"
  exit 1
fi

# Change proxying port into target port
echo "set \$service_url http://127.0.0.1:${TARGET_PORT};" | sudo tee /etc/nginx/conf.d/service_url.inc

echo "> Now Nginx proxies to ${TARGET_PORT}."

# Reload nginx
sudo service nginx reload

echo "> Nginx reloaded."

#sleep 30
#echo "> 30초 슬립."

# Kill CurrentPort
if [ ${TARGET_PORT} -eq 8081 ]; then
  KILL_PORT=8082
  IDLE_PID=$(lsof -ti tcp:${KILL_PORT})
  echo "> ${KILL_PORT} 포트를 종료합니다."
  kill -9 ${IDLE_PID}
else
  KILL_PORT=8081
  IDLE_PID=$(lsof -ti tcp:${KILL_PORT})
  echo "> ${KILL_PORT} 포트를 종료합니다."
  kill -9 ${IDLE_PID}
fi