#!/usr/bin/ksh
#========================================================================
# Name          : stop_user.sh
# Description   : Set environment and stop user
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
    "0" ) echo "stop_user: Completed."
          exit 0
          ;;
    "1" ) echo "stop_user: Failed."
          exit 1
          ;;
  esac
}

#------------------------------------------------------------------------
# Main
#------------------------------------------------------------------------

#-------------------------------------------------
# Stop user service
#-------------------------------------------------

echo
echo "**************************************************"
echo "           stop_user.sh started                  "
echo "**************************************************"
echo

MY_USER=`id | cut -f 2 -d '(' | cut -f 1 -d ')'`

RUN_P=`ps -f -u $MY_USER | grep userSrvRun | grep -v grep | grep -v stop_user.sh | awk '{ print $2 }'|tr '\n' ' '`
if [[ -z ${RUN_P} ]] then
   echo "No User service Running"
else
   echo "Sending signal TERM to User service: $RUN_P"   
   kill -TERM ${RUN_P}
   wait $RUN_P
fi

ExitProcess 0

