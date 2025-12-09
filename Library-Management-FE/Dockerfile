# Stage 1: Build the Vue application
FROM node:18-alpine AS build
WORKDIR /app
COPY package*.json .
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Serve the application using a minimal Nginx server
FROM nginx:alpine
# Remove Nginx's default config
RUN rm /etc/nginx/conf.d/default.conf
# Copy the built static files
COPY --from=build /app/dist /usr/share/nginx/html
# Add our routing config (will be overridden by main Nginx, but good practice)
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
