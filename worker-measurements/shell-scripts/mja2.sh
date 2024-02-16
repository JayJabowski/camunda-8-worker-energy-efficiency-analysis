# 2 Min load, 1 min idle x30 = 1,5h
# H: 48 Instances
# M: 24 Instances
# S: 4 Instances

# mja: 8, 64, 512

#!/bin/bash

# first arg must be start time as 'YYYY-MM-DD hh:mm:ss'
START_TIMESTAMP=$1
BATCH_RUNS=$2

# Load stages
load_stages=( 2 12 24 ) # Stages of load generation, 
n=${#load_stages[@]} # Total number of steps (length of the array)

# Load stages
MJA_LIST=( 8 64 512 ) # Stages of load generation, 
mja_num=${#load_stages[@]} # Total number of steps (length of the array)

LOADTIME=90
IDLETIME=30
COOLOFFTIME=300
MJARESETTIME=900

# Zeebe
PROCESS_NAME="load_generator_8x90_parallel"
ZEEBE_ADDRESS="127.0.0.1:26500"

TEST_FACTOR=1 # set this to 1 to test the script with 1s intervals except for 1 min

EPOCH_START_TIME=$(date -d "$START_TIMESTAMP" +%s)
EPOCH_CURRENT_TIME=$(date +%s)

LOG_NAME=$0-$(date -d "$START_TIMESTAMP" +'%Y-%m-%d-%H-%M-%S')-$PROCESS_NAME
LOG_FOLDER="worker-measurements/shell-scripts/logs/mja/"
BASE_PATH="/home/jabowski/bpmn-demo-no-aws/"

# usage
if [ $# -ne 2 ]; then
    echo "usage: $0  START_TIMESTAMP<'YYYY-MM-DD hh:mm:ss'> BATCH_RUNS"
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
    echo "$(date -d "@$date_round_down" +'%Y-%m-%d-%T') $1-$2" >> $LOG_FOLDER$LOG_NAME
}

restart_worker(){ # $1 = mja

    echo "Restarting Worker with mja=$1"

    # update envs
    helm upgrade restworkerjava modules/worker/restworkerjava/ \
    -n worker \
    --reuse-values \
    --set "container.env.maxjobsactive=$1" 
   
    # restart pod with new env-values
    kubectl -n worker rollout restart deployment restworkerjava

    echo "Wait 2 Minutes..."
    sleep 120

}

####################################################################################################


let REMAINING_S_TO_START=$EPOCH_START_TIME-$EPOCH_CURRENT_TIME

if [ $REMAINING_S_TO_START -le 0 ]
then
    echo "Start Time invalid (delay = $REMAINING_S_TO_START). Aborting."
    exit 1
else
    cd $BASE_PATH

    # create logs
    mkdir -p $LOG_FOLDER
    touch $LOG_FOLDER$LOG_NAME
        
    # wait for start
    echo "Waiting until $START_TIMESTAMP ($REMAINING_S_TO_START s)"
    sleep $REMAINING_S_TO_START

    for((mja_stage=0; mja_stage<mja_num; mja_stage++)); do
        
        current_mja=${MJA_LIST[$mja_stage]}

        restart_worker $current_mja
    
        for((stage=0; stage<n; stage++)); do
            loadnum=${load_stages[$stage]}

            echo " "
            echo "Stage $(expr $stage + 1): $loadnum parallel Instances:"


            for((batchno=0; batchno<BATCH_RUNS; batchno++)); do

                add_time_to_log
                echo "Starting Run $( expr $batchno + 1)"

                burst_new_instance $loadnum
                sleep $LOADTIME

                echo "Cooling off for $IDLETIME Seconds"
                sleep $IDLETIME

            done

            echo "$(date) Stage done. Cooling off for $COOLOFFTIME seconds"
            sleep $COOLOFFTIME

        done

        echo "waiting $MJARESETTIME seconds to reset"
        sleep $MJARESETTIME

    done

    echo "Finished at $(date)"

fi
