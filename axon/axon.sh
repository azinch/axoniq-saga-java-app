#!/usr/bin/ksh

ExitProcess ()
{
  case $1 in
    "0" ) echo "axon: Completed."
          exit 0
          ;;
    "1" ) echo "axon: Failed."
          exit 1
          ;;
  esac
}

CUSER=`id -u -n`

RUN_P=`ps -ef | grep "axonserver.jar" | grep -v grep | grep ${CUSER}`
if [[ -n ${RUN_P} ]] then
   echo "Axon server already Running: $RUN_P"
else
   nohup java -jar $HOME/bin/axon/axonserver.jar > /dev/null &
   echo "Axon server Started."
fi

JOB_STATUS=$?

if [ $JOB_STATUS -ne 0 ]
then
    ExitProcess 1
else
    ExitProcess 0
fi

