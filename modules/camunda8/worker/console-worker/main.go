package main

import (
	"context"
	"log"
	"os"
	"os/signal"
	"syscall"
	"time"

	"github.com/camunda/zeebe/clients/go/v8/pkg/entities"
	"github.com/camunda/zeebe/clients/go/v8/pkg/worker"
	"github.com/camunda/zeebe/clients/go/v8/pkg/zbc"
)

const (
	textToPrint = "textToPrint"
	jobType     = "printText"
	workerName  = "go-console-worker"
)

func main() {
	log.SetOutput(os.Stdout)

	shutdownBarrier := make(chan bool, 1)
	SetupShutdownBarrier(shutdownBarrier)

	client := MustCreateClient()
	defer MustCloseClient(client)
	w := MustStartWorker(client)
	defer w.Close()

	<-shutdownBarrier
}

func SetupShutdownBarrier(done chan bool) {
	sigs := make(chan os.Signal, 1)
	signal.Notify(sigs, syscall.SIGINT, syscall.SIGTERM)
	go func() {
		<-sigs
		done <- true
	}()
}

func getEnv(key, fallback string) string {
	value, exists := os.LookupEnv(key)
	if !exists {
		value = fallback
	}
	return value
}

func MustCreateClient() zbc.Client {
	gatewayAddress := getEnv("GATEWAY_ADDRESS", "localhost:26500")

	config := zbc.ClientConfig{
		UsePlaintextConnection: true,
		GatewayAddress:         gatewayAddress,
	}

	log.Println("Using config %s \n", config)

	client, err := zbc.NewClient(&config)
	if err != nil {
		panic(err)
	}

	return client
}

func MustCloseClient(client zbc.Client) {
	log.Println("closing client")
	_ = client.Close()
}

func MustStartWorker(client zbc.Client) worker.JobWorker {
	w := client.NewJobWorker().
		JobType(jobType).
		Handler(HandleJob).
		Concurrency(1).
		MaxJobsActive(10).
		RequestTimeout(1 * time.Second).
		PollInterval(1 * time.Second).
		Name(workerName).
		Open()

	log.Printf("started worker [%s] for jobs of type [%s] \n", workerName, jobType)
	return w
}

func HandleJob(client worker.JobClient, job entities.Job) {
	vars, err := job.GetVariablesAsMap()
	if err != nil {
		log.Printf("failed to get variables for job %d: [%s] \n", job.Key, err)
		return
	}

	log.Printf("Printing text to console: %s \n", vars[textToPrint])

	ctx, cancelFn := context.WithTimeout(context.Background(), 5*time.Second)
	defer cancelFn()

	_, err = client.NewCompleteJobCommand().JobKey(job.Key).Send(ctx)
	if err != nil {
		log.Printf("failed to complete job with key %d: [%s] \n", job.Key, err)
	}

	log.Printf("completed job %d successfully \n", job.Key)
}
