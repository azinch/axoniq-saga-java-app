#!/usr/bin/ksh  
#========================================================================
# Name          : shipment.sh
# Description   : Set environment and start shipment
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
    "0" ) echo "shipment: Completed."
          exit 0
          ;;
    "1" ) echo "shipment: Failed."
          exit 1
          ;;
  esac
}

#------------------------------------------------------------------------
# Main
#------------------------------------------------------------------------

#-------------------------------------------------
# Start shipment service
#-------------------------------------------------

echo
echo "**************************************************"
echo "           shipment.sh started                    "
echo "**************************************************"
echo

### Configs
export AXONSAGA_CFG_DIR=$HOME/work/proj/axoniq-saga-java-app/cfg
echo AXONSAGA_CFG_DIR: $AXONSAGA_CFG_DIR

CUSER=`id -u -n`

RUN_P=`ps -ef | grep shipmentSrvRun | grep -v grep | grep ${CUSER}`
if [[ -n ${RUN_P} ]] then
   echo "Shipment service already Running: $RUN_P"
else
   nohup java -DshipmentSrvRun -Dspring.config.location=$AXONSAGA_CFG_DIR/app-shipment.properties -Dlogging.config=$AXONSAGA_CFG_DIR/log-shipment.xml -jar shipment-1.0-SNAPSHOT.jar > /dev/null &
    echo "Shipment service Started."
fi

JOB_STATUS=$?

if [ $JOB_STATUS -ne 0 ]
then
    ExitProcess 1
else
    ExitProcess 0
fi
