### Swagger
`http://localhost:8080/swagger-ui.html`

### Build
`mvn clean package`

### Docker
`docker build --no-cache -f $PWD/docker/Dockerfile -t movie-rental-store-application .`

`docker run -p 8080:8080 --rm movie-rental-store-application`
