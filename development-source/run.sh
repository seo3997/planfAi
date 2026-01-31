#!/bin/bash

# =================================================================
# 1. 환경 설정
# - SERVER_PORT: Spring Boot 서버 포트 (application.yml과 일치)
# - MVN_PATH: mvn command의 전체 경로 (mvn command not found 에러 발생 시)
# =================================================================
SERVER_PORT=9000
# MVN_PATH="/path/to/your/mvn" # 예: /opt/homebrew/bin/mvn

# 2. 프로젝트 정보
PROJECT_PATH=$(dirname "$0")
JAR_NAME="planfAi-0.0.1-SNAPSHOT.jar"
JAR_PATH="$PROJECT_PATH/target/$JAR_NAME"

# Maven 명령어 설정 (MVN_PATH가 설정되어 있으면 사용, 아니면 기본 mvn 사용)
MVN_CMD=${MVN_PATH:-mvn}

# Java 21 사용 강제 (Lombok 호환성 문제 해결)
if [ -z "$JAVA_HOME" ] || [[ "$JAVA_HOME" == *"25"* ]]; then
  JAVA_21_HOME=$(/usr/libexec/java_home -v 21 2>/dev/null)
  if [ -n "$JAVA_21_HOME" ]; then
    export JAVA_HOME="$JAVA_21_HOME"
    export PATH="$JAVA_HOME/bin:$PATH"
    echo "Lombok 호환성을 위해 Java 21을 사용합니다: $JAVA_HOME"
  fi
fi

# =================================================================
# 함수 정의
# =================================================================

# 서버 상태 확인
get_pid() {
  lsof -t -i:$SERVER_PORT
}

# 상태 출력
status() {
  echo "Checking status for port $SERVER_PORT..."
  PID=$(get_pid)
  if [ -n "$PID" ]; then
    echo "Server is running with PID: $PID"
  else
    echo "Server is stopped."
  fi
}

# 서버 중지
stop() {
  echo "Stopping server on port $SERVER_PORT..."
  PID=$(get_pid)
  if [ -n "$PID" ]; then
    echo "Process found with PID $PID. Killing process..."
    kill -9 $PID
    sleep 2
    echo "Server stopped."
  else
    echo "No process found to stop on port $SERVER_PORT."
  fi
}

# 서버 시작
start() {
  echo "Starting server..."
  stop

  echo "Building project with Maven..."
  (cd "$PROJECT_PATH" && $MVN_CMD clean package -DskipTests)

  if [ $? -ne 0 ]; then
    echo "Maven build failed. Exiting."
    exit 1
  fi

  if [ ! -f "$JAR_PATH" ]; then
    echo "JAR file not found at $JAR_PATH. Build may have failed."
    exit 1
  fi

  echo "Starting Spring Boot application..."
  java -jar "$JAR_PATH" &
  echo "Server started in background. Check logs for details."

  echo "Waiting 10 seconds for server to start..."
  sleep 10

  echo "Opening browser at http://localhost:$SERVER_PORT/index.html"
  open -a "Google Chrome" "http://localhost:$SERVER_PORT/index.html"
  echo "Start command finished."
}


# =================================================================
# 메인 로직
# =================================================================
COMMAND=$1

if [ -z "$COMMAND" ]; then
  echo "Usage: $0 {start|stop|status}"
  exit 1
fi

case $COMMAND in
  start)
    start
    ;;
  stop)
    stop
    ;;
  status)
    status
    ;;
  *)
    echo "Invalid command: $COMMAND"
    echo "Usage: $0 {start|stop|status}"
    exit 1
    ;;
esac

exit 0
