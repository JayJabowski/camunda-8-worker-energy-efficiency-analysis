#!/bin/bash

init_measurements()
{
    echo "shutting down load-controller, worker and test-responder"
    kubectl scale --replicas=0 deployment/load-controller -n worker
    kubectl scale --replicas=0 deployment/restworkerjava -n worker
    kubectl scale --replicas=0 deployment/test-responder -n measuring-endpoints
}

reset_measurements(){
    echo "resetting cluster"
    kubectl scale --replicas=0 deployment/load-controller -n worker
    kubectl scale --replicas=0 deployment/restworkerjava -n worker
    kubectl scale --replicas=0 deployment/test-responder -n measuring-endpoints
}

# usage
if [ $# -ne 2 ]; then
    echo "usage: start_worker_script.sh START_TIMESTAMP<'YYYY-MM-DD hh:mm:ss'> LOOP_COUNT"
    exit 1
fi

START_TIMESTAMP=$1
LOOP_COUNT=$2
let SLEEPTIME=180

let WORKERS_STARTED=0

EPOCH_START_TIME=$(date -d "$START_TIMESTAMP" +%s)
EPOCH_CURRENT_TIME=$(date +%s)

# calculate finish time
# let total_seconds="$SLEEPTIME*$LOOP_COUNT"
# let EPOCH_FINISH=$(date +%s)+$total_seconds


# create log name
LOG_NAME="$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%T')"
echo "using log name: $LOG_NAME"

let REMAINING_MS_TO_START=$EPOCH_START_TIME-$EPOCH_CURRENT_TIME

if [ $REMAINING_MS_TO_START -le 0 ]
then
    echo "Start Time invalid (delay = $REMAINING_MS_TO_START). Aborting."
    exit 1
else
    # create log
    mkdir -p logs
    touch logs/$LOG_NAME

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
        
        # add to log
        let current_time=$(date +%s)
        let date_round_down=($current_time-$current_time%10)
        echo $(date -d "@$date_round_down" +'%Y-%m-%d %T') >> logs/$LOG_NAME

        let "WORKERS_STARTED++"
        echo "Starting Worker $WORKERS_STARTED at $(date)"
        kubectl scale --replicas=$WORKERS_STARTED deployment/restworkerjava -n worker
        sleep $SLEEPTIME
    done

fi

#clean up
reset_measurements






