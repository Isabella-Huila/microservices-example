FROM node:8.17-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
# Instala envsubst para reemplazar variables en nginx.conf
RUN apk add --no-cache bash
COPY nginx.conf /etc/nginx/templates/nginx.conf.template  
# ¡Nota la extensión .template!
COPY --from=build-stage /app/dist /usr/share/nginx/html
CMD ["/bin/sh", "-c", "envsubst '${AUTH_API_URL} ${TODOS_API_URL}' < /etc/nginx/templates/nginx.conf.template > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"]