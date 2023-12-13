#!/bin/bash

START_TIMESTAMP=$1
LOOP_COUNT=$2
INCREASE_DELAY=$3

let LOAD_STARTED=1

EPOCH_START_TIME=$(date -d "$START_TIMESTAMP" +%s)
EPOCH_CURRENT_TIME=$(date +%s)

init_measurements()
{
    echo "resetting amt of load-controllers"
    kubectl scale --replicas=0 deployment/load-controller -n load-controller
}

reset_measurements(){
    echo "resetting cluster to 1/1/1"
    kubectl scale --replicas=1 deployment/load-controller -n load-controller
    kubectl scale --replicas=1 deployment/restworkerjava -n worker
    kubectl scale --replicas=1 deployment/test-responder -n measuring-endpoints
}

add_time_to_log(){ # add log name as $1
    let current_time=$(date +%s)
    let date_round_down=($current_time-$current_time%10)
    echo $(date -d "@$date_round_down" +'%Y-%m-%d %T') >> logs/$1
}

# usage
if [ $# -ne 3 ]; then
    echo "usage: start_worker_script.sh START_TIMESTAMP<'YYYY-MM-DD hh:mm:ss'> LOOP_COUNT INCREASE_DELAY<seconds>"
    exit 1
fi


# create log name
LOG_NAME="load-curve-start-$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%H-%M-%S')-loop-count=$LOOP_COUNT-delay=$INCREASE_DELAY"

echo "using log names: $LOG_NAME"

let REMAINING_MS_TO_START=$EPOCH_START_TIME-$EPOCH_CURRENT_TIME

if [ $REMAINING_MS_TO_START -le 0 ]
then
    echo "Start Time invalid (delay = $REMAINING_MS_TO_START). Aborting."
    exit 1
else
      # start phase
    echo "starting at $START_TIMESTAMP in $REMAINING_MS_TO_START seconds"
    init_measurements

    # wait for start
    sleep $REMAINING_MS_TO_START

    # create logs
    mkdir -p logs
    touch logs/$LOG_NAME

    echo "Starting..."

    # main loop
    while [ $LOAD_STARTED -lt $LOOP_COUNT ] 
    do

        # start worker
        add_time_to_log $LOG_NAME
        echo "increasing load: $LOAD_STARTED pods at $(date)"
        kubectl scale --replicas=$LOAD_STARTED deployment/load-controller -n load-controller
        sleep $INCREASE_DELAY
        
        let "LOAD_STARTED++"

    done

fi

#clean up
reset_measurements






