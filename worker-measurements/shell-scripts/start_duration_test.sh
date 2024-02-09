#!/bin/bash

# first arg must be start time as 'YYYY-MM-DD hh:mm:ss'
START_TIMESTAMP=$1
BATCH_RUNS=$2

# Zeebe
PROCESS_NAME="load_generator_8x60_parallel"
ZEEBE_ADDRESS="127.0.0.1:26500"

EPOCH_START_TIME=$(date -d "$START_TIMESTAMP" +%s)
EPOCH_CURRENT_TIME=$(date +%s)
LOG_NAME=$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%H-%M-%S')-duratiom-start-test

# usage
if [ $# -ne 2 ]; then
    echo "usage: $0  START_TIMESTAMP<'YYYY-MM-DD hh:mm:ss'> BATCH_RUNS"
    exit 1
fi

####################################################################################################


# Function to define foo()
burst_new_instance() {
    
    echo " $(date):   Starting Instance..."

    zbctl create instance $PROCESS_NAME --address $ZEEBE_ADDRESS --insecure >> /dev/null

}

add_time_to_log(){ 
    let current_time=$(date +%s)
    let date_round_down=($current_time-$current_time%10)
    echo $(date -d "@$date_round_down" +'%Y-%m-%d %T') >> logs/load-test/$LOG_NAME
}

####################################################################################################


let REMAINING_MS_TO_START=$EPOCH_START_TIME-$EPOCH_CURRENT_TIME

if [ $REMAINING_MS_TO_START -le 0 ]
then
    echo "Start Time invalid (delay = $REMAINING_MS_TO_START). Aborting."
    exit 1
else
    # create logs
    mkdir -p logs/start-duration-test
    touch logs/start-duration-test/$LOG_NAME
        
    # wait for start
    echo "Waiting until $START_TIMESTAMP"
    
    echo "Killing Worker"
    kubectl scale --replicas=0 deployment/restworkerjava -n worker
    
    sleep $REMAINING_MS_TO_START    

    for((batchno=0; batchno<BATCH_RUNS; batchno++)); do
        
        echo "Starting run $batchno..."

        add_time_to_log
        burst_new_instance

        sleep 60
        
        echo "$(date): Starting Worker, sleeping 60"
        kubectl scale --replicas=1 deployment/restworkerjava -n worker

        sleep 60

        echo "$(date): Stopping Worker, sleeping 60"
        kubectl scale --replicas=0 deployment/restworkerjava -n worker

        sleep 60

    done

    echo $(date) >> logs/load-test/$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%H-%M-%S')-load-test-$n-stages 
    echo "$(date): Starting Worker, sleeping 60"
    kubectl scale --replicas=1 deployment/restworkerjava -n worker

fi
