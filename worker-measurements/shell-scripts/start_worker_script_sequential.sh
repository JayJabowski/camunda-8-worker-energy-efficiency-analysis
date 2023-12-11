#!/bin/bash

START_TIMESTAMP=$1
LOOP_COUNT=$2
BASE_WORKERS=$3
let SLEEPTIME=5

let WORKERS_STARTED=0

EPOCH_START_TIME=$(date -d "$START_TIMESTAMP" +%s)
EPOCH_CURRENT_TIME=$(date +%s)

init_measurements()
{
    echo "shutting down load-controller and test-responder, updating worker to $BASE_WORKERS"
    kubectl scale --replicas=$BASE_WORKERS deployment/load-controller -n worker
    kubectl scale --replicas=$BASE_WORKERS deployment/restworkerjava -n worker
    kubectl scale --replicas=$BASE_WORKERS deployment/test-responder -n measuring-endpoints
}

reset_measurements(){
    echo "resetting cluster to 1/1/1"
    kubectl scale --replicas=1 deployment/load-controller -n worker
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
    echo "usage: start_worker_script.sh START_TIMESTAMP<'YYYY-MM-DD hh:mm:ss'> LOOP_COUNT BASE_WORKERS"
    exit 1
fi


# create log name
LOG_NAME_START="$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%T')-STARTS"
LOG_NAME_STOP="$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%T')-STOPS"

echo "using log names: $LOG_NAME_STOP $LOG_NAME_START"

let REMAINING_MS_TO_START=$EPOCH_START_TIME-$EPOCH_CURRENT_TIME

if [ $REMAINING_MS_TO_START -le 0 ]
then
    echo "Start Time invalid (delay = $REMAINING_MS_TO_START). Aborting."
    exit 1
else
    # create logs
    mkdir -p logs
    touch logs/$LOG_NAME_START
    touch logs/$LOG_NAME_STOP

    # start phase
    echo "starting at $START_TIMESTAMP in $REMAINING_MS_TO_START seconds"

    # wait for start
    sleep $REMAINING_MS_TO_START

    # wait 3 min for initial idle measurements
    echo "STARTING MEASUREMENT: $SLEEPTIME seconds Idle"
    # echo "expect to finish at $(date -d "$EPOCH_FINISH" +'%Y-%m-%d %T')"
    init_measurements
    sleep $SLEEPTIME

    # main loop
    while [ $WORKERS_STARTED -lt $LOOP_COUNT ] 
    do
        let "WORKERS_STARTED++"

        # start worker
        add_time_to_log $LOG_NAME_START
        echo "Starting Worker $WORKERS_STARTED at $(date)"
        kubectl scale --replicas=$(($BASE_WORKERS+1)) deployment/restworkerjava -n worker
        sleep $SLEEPTIME

        # stop worker
        add_time_to_log $LOG_NAME_STOP
        echo "Stopping Worker $WORKERS_STARTED at $(date)"
        kubectl scale --replicas=$(($BASE_WORKERS)) deployment/restworkerjava -n worker
        sleep 5

    done

fi

#clean up
reset_measurements






