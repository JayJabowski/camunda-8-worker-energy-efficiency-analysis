#!/bin/bash

# first arg must be start time as 'YYYY-MM-DD hh:mm:ss'
START_TIMESTAMP=$1

# Load-Curve

SLEEPTIME=60 # interval between bursts in seconds
burst_list=(0 0 1 2 4 8 16 32 48 64 96 128 192 256 ) # Stages of load generation, each corresponding to instances with 60J/min
n=${#burst_list[@]} # Total number of steps (length of the array)

# Zeebe
PROCESS_NAME="load_generator_60j_in_60s"
ZEEBE_ADDRESS="127.0.0.1:26500"

EPOCH_START_TIME=$(date -d "$START_TIMESTAMP" +%s)
EPOCH_CURRENT_TIME=$(date +%s)

# usage
if [ $# -ne 1 ]; then
    echo "usage: $0  START_TIMESTAMP<'YYYY-MM-DD hh:mm:ss'>"
    exit 1
fi

# Function to define foo()
burst_new_instance() {
    
    echo " $(date):   Starting $1 Instances..."

    for ((i=0; i<$1; i++)); do
        
        # zeebectl here
        zbctl create instance $PROCESS_NAME --address $ZEEBE_ADDRESS --insecure >> /dev/null
        echo '. '
    done
    echo " $(date):   ...started $1 Instances."

}

let REMAINING_MS_TO_START=$EPOCH_START_TIME-$EPOCH_CURRENT_TIME

if [ $REMAINING_MS_TO_START -le 0 ]
then
    echo "Start Time invalid (delay = $REMAINING_MS_TO_START). Aborting."
    exit 1
else
    # create logs
    mkdir -p logs/load-test
    touch logs/load-test/$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%H-%M-%S')-load-test-$n-stages
        
    # wait for start
    echo "Waiting until $START_TIMESTAMP"
    sleep $REMAINING_MS_TO_START
    

    # Loop through each step
    for ((step=0; step<n; step++)); do
        # Get the number of executions for this step from the array
        executions=${burst_list[$step]}

        burst_new_instance $executions

        echo "Waiting for $SLEEPTIME seconds"
        sleep $SLEEPTIME

    done

    echo $START_TIMESTAMP >> logs/load-test/$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%H-%M-%S')-load-test-$n-stages 
    echo $(date) >> logs/load-test/$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%H-%M-%S')-load-test-$n-stages 

fi
