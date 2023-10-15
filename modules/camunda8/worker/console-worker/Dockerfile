FROM golang:1.20.5-alpine AS builder
RUN apk add build-base
WORKDIR /src

COPY go.mod go.sum ./
RUN go mod download

COPY . .

RUN go build -o /console-worker .

FROM alpine:3.18.2
RUN apk add --no-cache ca-certificates

WORKDIR /src
COPY --from=builder /console-worker /src/console-worker

ENTRYPOINT ["/src/console-worker"]
