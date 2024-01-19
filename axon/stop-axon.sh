#!/usr/bin/ksh

ExitProcess ()
{
  case $1 in
    "0" ) echo "stop-axon: Completed."
          exit 0
          ;;
    "1" ) echo "stop-axon: Failed."
          exit 1
          ;;
  esac
}

MY_USER=`id | cut -f 2 -d '(' | cut -f 1 -d ')'`

RUN_P=`ps -f -u $MY_USER | grep "axonserver.jar" | grep -v grep | grep -v stop-axon.sh | awk '{ print $2 }'|tr '\n' ' '`
if [[ -z ${RUN_P} ]] then
   echo "No Axon server Running"
else
   echo "Sending signal TERM to Axon server: $RUN_P"
   kill -TERM ${RUN_P}
   wait $RUN_P
   #Optionally delete axon data
   if [[ -n $1 ]] then
      rm -rf $1
   fi
fi

ExitProcess 0

