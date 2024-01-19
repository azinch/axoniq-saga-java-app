#!/usr/bin/ksh
#========================================================================
# Name          : stop_payment.sh
# Description   : Set environment and stop payment
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
    "0" ) echo "stop_payment: Completed."
          exit 0
          ;;
    "1" ) echo "stop_payment: Failed."
          exit 1
          ;;
  esac
}

#------------------------------------------------------------------------
# Main
#------------------------------------------------------------------------

#-------------------------------------------------
# Stop payment service
#-------------------------------------------------

echo
echo "**************************************************"
echo "           stop_payment.sh started                  "
echo "**************************************************"
echo

MY_USER=`id | cut -f 2 -d '(' | cut -f 1 -d ')'`

RUN_P=`ps -f -u $MY_USER | grep paymentSrvRun | grep -v grep | grep -v stop_payment.sh | awk '{ print $2 }'|tr '\n' ' '`
if [[ -z ${RUN_P} ]] then
   echo "No Payment service Running"
else
   echo "Sending signal TERM to Payment service: $RUN_P"   
   kill -TERM ${RUN_P}
   wait $RUN_P
fi

ExitProcess 0

