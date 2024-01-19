#!/usr/bin/ksh  
#========================================================================
# Name          : user.sh
# Description   : Set environment and start user
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
    "0" ) echo "user: Completed."
          exit 0
          ;;
    "1" ) echo "user: Failed."
          exit 1
          ;;
  esac
}

#------------------------------------------------------------------------
# Main
#------------------------------------------------------------------------

#-------------------------------------------------
# Start user service
#-------------------------------------------------

echo
echo "**************************************************"
echo "           user.sh started                       "
echo "**************************************************"
echo

### Configs
export AXONSAGA_CFG_DIR=$HOME/work/proj/axoniq-saga-java-app/cfg
echo AXONSAGA_CFG_DIR: $AXONSAGA_CFG_DIR

CUSER=`id -u -n`

RUN_P=`ps -ef | grep userSrvRun | grep -v grep | grep ${CUSER}`
if [[ -n ${RUN_P} ]] then
   echo "User service already Running: $RUN_P"
else
   nohup java -DuserSrvRun -Dspring.config.location=$AXONSAGA_CFG_DIR/app-user.properties -Dlogging.config=$AXONSAGA_CFG_DIR/log-user.xml -jar user-1.0-SNAPSHOT.jar > /dev/null &
    echo "User service Started."
fi

JOB_STATUS=$?

if [ $JOB_STATUS -ne 0 ]
then
    ExitProcess 1
else
    ExitProcess 0
fi
