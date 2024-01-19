#!/usr/bin/ksh
#========================================================================
# Name          : stop_shipment.sh
# Description   : Set environment and stop shipment
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
    "0" ) echo "stop_shipment: Completed."
          exit 0
          ;;
    "1" ) echo "stop_shipment: Failed."
          exit 1
          ;;
  esac
}

#------------------------------------------------------------------------
# Main
#------------------------------------------------------------------------

#-------------------------------------------------
# Stop shipment service
#-------------------------------------------------

echo
echo "**************************************************"
echo "           stop_shipment.sh started                  "
echo "**************************************************"
echo

MY_USER=`id | cut -f 2 -d '(' | cut -f 1 -d ')'`

RUN_P=`ps -f -u $MY_USER | grep shipmentSrvRun | grep -v grep | grep -v stop_shipment.sh | awk '{ print $2 }'|tr '\n' ' '`
if [[ -z ${RUN_P} ]] then
   echo "No Shipment service Running"
else
   echo "Sending signal TERM to Shipment service: $RUN_P"   
   kill -TERM ${RUN_P}
   wait $RUN_P
fi

ExitProcess 0

