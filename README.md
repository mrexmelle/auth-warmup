# auth-warmup

## Compiling

```
$ gradle clean build
```

## Building Docker image

Note that only the owner of the repository is allowed to build the image. 

```
$ docker build -t ghcr.io/mrexmelle/auth-warmup:${VERSION} .
$ docker push ghcr.io/mrexmelle/auth-warmup:${VERSION}
```

## Running

### For local environment

```
$ docker pull postgres:15-alpine
$ docker run \
	-v data:/var/lib/postgresql/data \
	-v init-db:/docker-entrypoint-initdb.d \
	-p 5432:5432 \
	-e POSTGRES_USER=matthew \
	-e POSTGRES_PASSWORD=123 \
	-e POSTGRES_DB=auth
	--restart always \
	postgres:15-alpine
$ gradle -Dspring.profiles.active=local bootRun
```

### For docker environment

```
$ docker compose up
```
Note that you cannot alter the docker image in the container registry. Only the owner of the repository is allowed to do so.


