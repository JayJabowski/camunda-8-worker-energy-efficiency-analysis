# Mail Worker

Worker which should connect to the existing Mail-Service

See https://docs.camunda.io/docs/next/apis-tools/go-client/go-get-started/#set-up-a-project

```sh
# Build the container
docker build -t <name> .

# Run it
docker run --env-file ./camunda.env <name>
```

## Helmify

Have a look [here](https://medium.com/containerum/how-to-make-and-share-your-own-helm-package-50ae40f6c221)

```bash
helm package console-worker-chart
helm repo index ./ --url https://maxbehr801.github.io/helmrepotest
```

## Terraformify
todo
