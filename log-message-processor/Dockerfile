# Log Message Processor Dockerfile
FROM python:3.6-alpine

# Install build dependencies
RUN apk add --no-cache --virtual .build-deps gcc musl-dev

WORKDIR /app
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Remove build dependencies to keep image small
RUN apk del .build-deps

COPY . .

ENV REDIS_HOST=redis
ENV REDIS_PORT=6379
ENV REDIS_CHANNEL=log_channel

CMD ["python", "main.py"]