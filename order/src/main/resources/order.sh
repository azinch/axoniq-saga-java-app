#!/usr/bin/ksh  
#========================================================================
# Name          : order.sh
# Description   : Set environment and start order
# Author        : andreyz
#========================================================================
# $log: $
#
#====================================================================
# Subroutines:
#---------------------------------------------------------------------
ExitProcess ()
{
  case $1 in
    "0" ) echo "order: Completed."
          exit 0
          ;;
    "1" ) echo "order: Failed."
          exit 1
          ;;
  esac
}

#------------------------------------------------------------------------
# Main
#------------------------------------------------------------------------

#-------------------------------------------------
# Start order service
#-------------------------------------------------

echo
echo "**************************************************"
echo "           order.sh started                       "
echo "**************************************************"
echo

### Configs
export AXONSAGA_CFG_DIR=$HOME/work/proj/axoniq-saga-java-app/cfg
echo AXONSAGA_CFG_DIR: $AXONSAGA_CFG_DIR

CUSER=`id -u -n`

RUN_P=`ps -ef | grep orderSrvRun | grep -v grep | grep ${CUSER}`
if [[ -n ${RUN_P} ]] then
   echo "Order service already Running: $RUN_P"
else
   nohup java -DorderSrvRun -Dspring.config.location=$AXONSAGA_CFG_DIR/app-order.properties -Dlogging.config=$AXONSAGA_CFG_DIR/log-order.xml -jar order-1.0-SNAPSHOT.jar > /dev/null &
    echo "Order service Started."
fi

JOB_STATUS=$?

if [ $JOB_STATUS -ne 0 ]
then
    ExitProcess 1
else
    ExitProcess 0
fi
