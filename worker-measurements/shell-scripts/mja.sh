#!/bin/bash

# first arg must be start time as 'YYYY-MM-DD hh:mm:ss'
START_TIMESTAMP=$1
BATCH_RUNS=$2
NO_OF_INSTANCES=$3
LOADTIME=$4 # minutes under load
SLEEPTIME=$5 # idle period in the end
MJA=$6

# Zeebe
PROCESS_NAME="load_generator_8x60_parallel"
ZEEBE_ADDRESS="127.0.0.1:26500"

EPOCH_START_TIME=$(date -d "$START_TIMESTAMP" +%s)
EPOCH_CURRENT_TIME=$(date +%s)

LOG_NAME=$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%H-%M-%S')-mja-$n-stages-$PROCESS_NAME
LOG_FOLDER="logs/mja"

# usage
if [ $# -ne 6 ]; then
    echo "usage: $0  START_TIMESTAMP<'YYYY-MM-DD hh:mm:ss'> BATCH_RUNS NO_OF_INSTANCES LOADTIME SLEEPTIME MJA(for log)"
    exit 1
fi

####################################################################################################

# Function to define foo()
burst_new_instance() {
    
    echo " $(date):   Starting $1 Instances..."

    for ((i=0; i<$1; i++)); do
        
        # zeebectl here
        zbctl create instance $PROCESS_NAME --address $ZEEBE_ADDRESS --insecure >> /dev/null
        # echo "."

    done
    echo " $(date):   ...started $1 Instances."

}

add_time_to_log(){ 
    let current_time=$(date +%s)
    let date_round_down=($current_time-$current_time%10)
    echo $(date -d "@$date_round_down" +'%Y-%m-%d %T') >> $LOG_FOLDER/$LOG_NAME
}

####################################################################################################


let REMAINING_MS_TO_START=$EPOCH_START_TIME-$EPOCH_CURRENT_TIME

if [ $REMAINING_MS_TO_START -le 0 ]
then
    echo "Start Time invalid (delay = $REMAINING_MS_TO_START). Aborting."
    exit 1
else
    # create logs
    mkdir -p $LOG_FOLDER
    touch $LOG_FOLDER/$LOG_NAME
        
    # wait for start
    echo "Waiting until $START_TIMESTAMP"
    sleep $REMAINING_MS_TO_START    

    for((batchno=0; batchno<BATCH_RUNS; batchno++)); do

        add_time_to_log
        echo "Starting Run $batchno"

        for((loadmin=0; loadmin<LOADTIME; loadmin++)); do

            burst_new_instance $NO_OF_INSTANCES
            sleep 60

        done

        echo "Cooling off for $SLEEPTIME seconds (Run $batchno of $BATCH_RUNS)"
        sleep $SLEEPTIME


    done

fi
