#!/bin/bash

#!/bin/bash

# first arg must be start time as 'YYYY-MM-DD hh:mm:ss'
START_TIMESTAMP=$1
WAIT_INTERVAL=$2
# maxJobsActive - recorded for logging purposes

# Poll Interval
POLL_INTERVAL_LIST=( 2 10 30 60 ) # Stages of load generation, 
n_pi=${#POLL_INTERVAL_LIST[@]} # Total number of steps (length of the array)

# Poll Interval
REQUEST_TIMEOUT_LIST=( -1 0 2 10 30 60 ) # Stages of load generation, 
n_rto=${#REQUEST_TIMEOUT_LIST[@]} # Total number of steps (length of the array)

EPOCH_START_TIME=$(date -d "$START_TIMESTAMP" +%s)
EPOCH_CURRENT_TIME=$(date +%s)

LOG_NAME=$0-$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%H-%M-%S')
LOG_FOLDER="worker-measurements/shell-srcipts/logs/polling/"
BASE_PATH="/home/jabowski/bpmn-demo-no-aws/"

# usage
if [ $# -ne 2 ]; then
    echo "usage: $0  START_TIMESTAMP<'YYYY-MM-DD hh:mm:ss'> WAIT_INTERVAL(Minutes)"
    exit 1
fi

####################################################################################################

add_time_to_log(){ 
    let current_time=$(date +%s)
    let date_round_down=($current_time-$current_time%10)
    echo "$(date -d "@$date_round_down" +'%Y-%m-%d %T') $1 $2" >> $LOG_FOLDER$LOG_NAME
}

####################################################################################################

SAVED_PATH=$(echo $PWD)
cd $BASE_PATH

# wait until start
let REMAINING_S_TO_START=$EPOCH_START_TIME-$EPOCH_CURRENT_TIME

if [ $REMAINING_S_TO_START -le 0 ]
then
    echo "Start Time invalid (delay = $REMAINING_S_TO_START). Aborting."
    exit 1
else
    # main loop
    for((pi = 0; pi < n_pi; pi++)); do 

        CURRENT_PI=${POLL_INTERVAL_LIST[$pi]}

        for((rto=0; rto < n_rto; rto++)); do

            CURRENT_RTO=${REQUEST_TIMEOUT_LIST[$rto]}

            echo "$(date): Using pollInterval: $CURRENT_PI, requestTimeout: $CURRENT_RTO"

            # update envs
            helm upgrade restworkerjava modules/worker/restworkerjava/ \
            -n worker \
            --reuse-values \
            --set "container.env.pollinterval=$CURRENT_PI" \
            --set "container.env.requestTimeout=$CURRENT_RTO"

            # restart pod with new env-values
            kubectl -n worker rollout restart deployment restworkerjava

            add_time_to_log $CURRENT_PI $CURRENT_RTO
            echo "$(date): Waiting $WAIT_INTERVAL Minutes..."

            sleep $( expr $WAIT_INTERVAL * 60 )

        done

    done

fi

echo "Resetting Worker"
helm upgrade restworkerjava modules/worker/restworkerjava/ \
-n worker \
--reuse-values


