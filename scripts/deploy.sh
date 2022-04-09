#!/bin/bash

cd /home/ec2-user/miniBacktesting_be

sudo chmod +x ./gradlew # gradlew 읽기 권한 부여

sudo ./gradlew bootJar # jar 파일 생성

# 실행 중인 도커 컴포즈 확인
EXIST_A=$(docker-compose -p springboot-a -f /home/ec2-user/miniBacktesting_be/docker-compose.a.yml ps | grep Up)

if [ -z "${EXIST_A}" ] # -z는 문자열 길이가 0이면 true. A가 실행 중이지 않다는 의미.
then
        # B가 실행 중인 경우
        START_CONTAINER=a
        TERMINATE_CONTAINER=b
        START_PORT=8080
        TERMINATE_PORT=8081
else
        # A가 실행 중인 경우
        START_CONTAINER=b
        TERMINATE_CONTAINER=a
        START_PORT=8081
        TERMINATE_PORT=8080
fi

echo "springboot-${START_CONTAINER} up"

# 실행해야하는 컨테이너 docker-compose로 실행. -p는 docker-compose 프로젝트에 이름을 부여
# -f는 docker-compose파일 경로를 지정
docker-compose -p springboot-${START_CONTAINER} -f /home/ec2-user/miniBacktesting_be/docker-compose.${START_CONTAINER}.yml up -d --build

for cnt in {1..10} # 10번 실행
do
        echo "check server start.."

        # 스프링부트에 등록했던 actuator로 실행되었는지 확인
        UP=$(curl -s http://127.0.0.1:${START_PORT}/actuator/health | grep 'UP')
        if [ -z "${UP}" ] # 실행되었다면 break
        then
                echo "server not start.."
        else
                break
        fi

        echo "wait 10 seconds" # 10 초간 대기
        sleep 10
done

if [ $cnt -eq 10 ] # 10번동안 실행이 안되었으면 배포 실패, 강제 종료
then
        echo "deployment failed."
        exit 1
fi

echo "server start!"
#echo "change nginx server port"

# sed 명령어를 이용해서 아까 지정해줬던 service-url.inc의 url값을 변경해줍니다.
# sed -i "s/기존문자열/변경할문자열" 파일경로 입니다.
# 종료되는 포트를 새로 시작되는 포트로 값을 변경해줍니다.
#sudo sed -i "s/${TERMINATE_PORT}/${START_PORT}/" /etc/nginx/conf.d/service-url.inc

# 새로운 포트로 스프링부트가 구동 되고, nginx의 포트를 변경해주었다면, nginx 재시작해줍니다.
#echo "nginx reload.."
#sudo service nginx reload

# 기존에 실행 중이었던 docker-compose는 종료시켜줍니다.
echo "springboot-${TERMINATE_CONTAINER} down"
docker-compose -p springboot-${TERMINATE_CONTAINER} -f /home/ec2-user/miniBacktesting_be/docker-compose.${TERMINATE_CONTAINER}.yml down
echo "success deployment"