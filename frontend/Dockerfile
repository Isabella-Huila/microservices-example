# Etapa de construcción (Build Stage)
FROM node:8.17-alpine as build-stage
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Etapa final (Final Stage)
FROM nginx:alpine

# Copia tu archivo de configuración como una plantilla
COPY nginx.conf /etc/nginx/templates/default.conf.template

# Copia los archivos estáticos desde la etapa de construcción (SINTAXIS CORREGIDA)
COPY --from=build-stage /app/dist /usr/share/nginx/html

# Elimina la configuración por defecto de Nginx para evitar conflictos
RUN rm -f /etc/nginx/conf.d/default.conf

# CMD que especifica EXACTAMENTE qué variables reemplazar
CMD ["/bin/sh", "-c", "envsubst '${AUTH_API_URL} ${TODOS_API_URL} ${ZIPKIN_URL}' < /etc/nginx/templates/default.conf.template > /etc/nginx/conf.d/default.conf && nginx -g 'daemon off;'"]