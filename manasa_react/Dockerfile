FROM node:14-bullseye

WORKDIR /app

COPY package.json package-lock.json ./
RUN npm install --legacy-peer-deps

COPY . .

EXPOSE 3000
ENV CI=true
CMD ["npm", "start"]
