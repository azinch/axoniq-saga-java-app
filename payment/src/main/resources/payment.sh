#!/usr/bin/ksh  
#========================================================================
# Name          : payment.sh
# Description   : Set environment and start payment
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
    "0" ) echo "payment: Completed."
          exit 0
          ;;
    "1" ) echo "payment: Failed."
          exit 1
          ;;
  esac
}

#------------------------------------------------------------------------
# Main
#------------------------------------------------------------------------

#-------------------------------------------------
# Start payment service
#-------------------------------------------------

echo
echo "**************************************************"
echo "           payment.sh started                       "
echo "**************************************************"
echo

### Configs
export AXONSAGA_CFG_DIR=$HOME/work/proj/axoniq-saga-java-app/cfg
echo AXONSAGA_CFG_DIR: $AXONSAGA_CFG_DIR

CUSER=`id -u -n`

RUN_P=`ps -ef | grep paymentSrvRun | grep -v grep | grep ${CUSER}`
if [[ -n ${RUN_P} ]] then
   echo "Payment service already Running: $RUN_P"
else
   nohup java -DpaymentSrvRun -Dspring.config.location=$AXONSAGA_CFG_DIR/app-payment.properties -Dlogging.config=$AXONSAGA_CFG_DIR/log-payment.xml -jar payment-1.0-SNAPSHOT.jar > /dev/null &
    echo "Payment service Started."
fi

JOB_STATUS=$?

if [ $JOB_STATUS -ne 0 ]
then
    ExitProcess 1
else
    ExitProcess 0
fi
