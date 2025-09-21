# Auth API Dockerfile
FROM golang:1.18-alpine

WORKDIR /app

COPY . .

RUN go mod init github.com/bortizf/microservice-app-example/tree/master/auth-api
RUN go mod tidy
RUN go build -o auth-api

ENV JWT_SECRET=PRFT
ENV AUTH_API_PORT=8000
ENV USERS_API_ADDRESS=http://users-api:8083

EXPOSE 8000

CMD ["./auth-api"]