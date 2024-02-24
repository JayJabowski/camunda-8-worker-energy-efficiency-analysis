#!/bin/bash

# first arg must be start time as 'YYYY-MM-DD hh:mm:ss'
START_TIMESTAMP=$1
BATCH_RUNS=$2

# Load-Curve

SLEEPTIME=60 # interval between bursts in seconds

# burst_list=( 1 2 4 8 12 16 20 24 28 32 0 0 0 0 0 0 ) # prev burst_list
burst_list=( 2 4 8 12 16 20 24 0 0 0 )  # Stages of load generation, each corresponding to instances with 60J/min
n=${#burst_list[@]} # Total number of steps (length of the array)

# Zeebe
PROCESS_NAME="load_generator_8x60_parallel"
ZEEBE_ADDRESS="127.0.0.1:26500"

EPOCH_START_TIME=$(date -d "$START_TIMESTAMP" +%s)
EPOCH_CURRENT_TIME=$(date +%s)
LOG_NAME=$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%H-%M-%S')-load-test-$n-stages-$PROCESS_NAME

# usage
if [ $# -ne 2 ]; then
    echo "usage: $0  START_TIMESTAMP<'YYYY-MM-DD hh:mm:ss'> BATCH_RUNS"
    exit 1
fi

####################################################################################################


# Function to define foo()
burst_new_instance() {
    
    echo " $(date):   Starting $1 Instances..."

    for ((i=0; i<$1; i++)); do
        
        # zeebectl here
        zbctl create instance $PROCESS_NAME --address $ZEEBE_ADDRESS --insecure >> /dev/null

    done
    echo " $(date):   ...started $1 Instances."

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
    mkdir -p logs/load-test
    touch logs/load-test/$LOG_NAME
        
    # wait for start
    echo "Waiting until $START_TIMESTAMP"
    sleep $REMAINING_MS_TO_START    

    for((batchno=0; batchno<BATCH_RUNS; batchno++)); do

        # Loop through each step

        for ((step=0; step<n; step++)); do
            # Get the number of executions for this step from the array
            executions=${burst_list[$step]}

            burst_new_instance $executions

            echo "Waiting for $SLEEPTIME seconds (Run $batchno of $BATCH_RUNS)"
            sleep $SLEEPTIME

        done

        
        echo $(date) >> logs/load-test/$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%H-%M-%S')-load-test-$n-stages 

    done

fi
