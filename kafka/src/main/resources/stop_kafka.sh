#!/usr/bin/ksh
#========================================================================
# Name          : stop_kafka.sh
# Description   : Set environment and stop kafka
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
    "0" ) echo "stop_kafka: Completed."
          exit 0
          ;;
    "1" ) echo "stop_kafka: Failed."
          exit 1
          ;;
  esac
}

#------------------------------------------------------------------------
# Main
#------------------------------------------------------------------------

#-------------------------------------------------
# Stop kafka service
#-------------------------------------------------

echo
echo "**************************************************"
echo "           stop_kafka.sh started                  "
echo "**************************************************"
echo

MY_USER=`id | cut -f 2 -d '(' | cut -f 1 -d ')'`

RUN_P=`ps -f -u $MY_USER | grep kafkaSrvRun | grep -v grep | grep -v stop_kafka.sh | awk '{ print $2 }'|tr '\n' ' '`
if [[ -z ${RUN_P} ]] then
   echo "No Kafka service Running"
else
   echo "Sending signal TERM to Kafka service: $RUN_P"   
   kill -TERM ${RUN_P}
   wait $RUN_P
fi

ExitProcess 0

