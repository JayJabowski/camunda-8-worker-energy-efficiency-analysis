# 2 Min load, 1 min idle x30 = 1,5h
# H: 48 Instances
# M: 24 Instances
# S: 4 Instances

# mja: 8, 64, 512

#!/bin/bash

# first arg must be start time as 'YYYY-MM-DD hh:mm:ss'
START_TIMESTAMP=$1
BATCH_RUNS=$2
MJA=$3 # maxJobsActive - recorded for logging purposes

# Load stages
load_stages=( 4 24 48 ) # Stages of load generation, 
n=${#load_stages[@]} # Total number of steps (length of the array)

LOADTIME=2
IDLETIME=1
COOLOFFTIME=10

# Zeebe
PROCESS_NAME="load_generator_8x60_parallel"
ZEEBE_ADDRESS="127.0.0.1:26500"

TEST_FACTOR=1 # set this to 1 to test the script with 1s intervals except for 1 min

EPOCH_START_TIME=$(date -d "$START_TIMESTAMP" +%s)
EPOCH_CURRENT_TIME=$(date +%s)

LOG_NAME=$0-$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%H-%M-%S')-$PROCESS_NAME-mja-$MJA
LOG_FOLDER="logs/mja"

# usage
if [ $# -ne 3 ]; then
    echo "usage: $0  START_TIMESTAMP<'YYYY-MM-DD hh:mm:ss'> BATCH_RUNS MJA"
    exit 1
fi

####################################################################################################

# Function to define foo()
burst_new_instance() {
    
    echo "$(date):   Starting $1 Instances..."

    for ((i=0; i<$1; i++)); do
        
        zbctl create instance $PROCESS_NAME --address $ZEEBE_ADDRESS --insecure >> /dev/null

    done
    echo "$(date):   ...started $1 Instances."

}

add_time_to_log(){ 
    let current_time=$(date +%s)
    let date_round_down=($current_time-$current_time%10)
    echo $(date -d "@$date_round_down" +'%Y-%m-%d %T') >> $LOG_FOLDER/$LOG_NAME
}

####################################################################################################


let REMAINING_S_TO_START=$EPOCH_START_TIME-$EPOCH_CURRENT_TIME

if [ $REMAINING_S_TO_START -le 0 ]
then
    echo "Start Time invalid (delay = $REMAINING_S_TO_START). Aborting."
    exit 1
else
    # create logs
    mkdir -p $LOG_FOLDER
    touch $LOG_FOLDER/$LOG_NAME
        
    # wait for start
    echo "Waiting until $START_TIMESTAMP ($REMAINING_S_TO_START s)"
    sleep $REMAINING_S_TO_START

    for((stage=0; stage<n; stage++)); do
        loadnum=${load_stages[$stage]}

        echo " "
        echo "Stage $(expr stage + 1): $loadnum parallel Instances:"


        for((batchno=0; batchno<BATCH_RUNS; batchno++)); do

            add_time_to_log
            echo "Starting Run $( expr $batchno + 1)"

            for((loadmin=0; loadmin<LOADTIME; loadmin++)); do

                burst_new_instance $loadnum
                sleep $TEST_FACTOR

            done

            echo "Cooling off for 1 minute."
            sleep $TEST_FACTOR

        done

        echo "Stage done. Cooling off for 5 minutes"
        sleep 300 # 5 min cooloff

    done

    echo "Finished at $(date)"

fi
