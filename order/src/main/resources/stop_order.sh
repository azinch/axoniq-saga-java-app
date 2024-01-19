#!/usr/bin/ksh
#========================================================================
# Name          : stop_order.sh
# Description   : Set environment and stop order
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
    "0" ) echo "stop_order: Completed."
          exit 0
          ;;
    "1" ) echo "stop_order: Failed."
          exit 1
          ;;
  esac
}

#------------------------------------------------------------------------
# Main
#------------------------------------------------------------------------

#-------------------------------------------------
# Stop order service
#-------------------------------------------------

echo
echo "**************************************************"
echo "           stop_order.sh started                  "
echo "**************************************************"
echo

MY_USER=`id | cut -f 2 -d '(' | cut -f 1 -d ')'`

RUN_P=`ps -f -u $MY_USER | grep orderSrvRun | grep -v grep | grep -v stop_order.sh | awk '{ print $2 }'|tr '\n' ' '`
if [[ -z ${RUN_P} ]] then
   echo "No Order service Running"
else
   echo "Sending signal TERM to Order service: $RUN_P"   
   kill -TERM ${RUN_P}
   wait $RUN_P
fi

ExitProcess 0

