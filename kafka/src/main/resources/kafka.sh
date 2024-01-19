#!/usr/bin/ksh  
#========================================================================
# Name          : kafka.sh
# Description   : Set environment and start kafka
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
    "0" ) echo "kafka: Completed."
          exit 0
          ;;
    "1" ) echo "kafka: Failed."
          exit 1
          ;;
  esac
}

#------------------------------------------------------------------------
# Main
#------------------------------------------------------------------------

#-------------------------------------------------
# Start kafka service
#-------------------------------------------------

echo
echo "**************************************************"
echo "           kafka.sh started                       "
echo "**************************************************"
echo

### Configs
export AXONSAGA_CFG_DIR=$HOME/andreyz/work/axoniq-saga-java-app/cfg
echo AXONSAGA_CFG_DIR: $AXONSAGA_CFG_DIR

### Security
export KERBEROS_STORAGE=$AXONSAGA_CFG_DIR
echo KERBEROS_STORAGE: $KERBEROS_STORAGE

export KEYTAB_FILE_NAME=tech_ens2kfktst_ms.keytab
export SSL_TRUSTSTORE_PASSWORD="changeit"
export KRB5_USER="tech_ens2kfktst_ms/kafka_prod"
export KRB5_REALM="BEE.VIMPELCOM.RU"
export CURL_CA_BUNDLE=$KERBEROS_STORAGE/vimpelcom-root_base64.cer

CUSER=`id -u -n`

RUN_P=`ps -ef | grep kafkaSrvRun | grep -v grep | grep ${CUSER}`
if [[ -n ${RUN_P} ]] then
   echo "Kafka service already Running: $RUN_P"
else
   nohup java -DkafkaSrvRun -Dspring.config.location=$AXONSAGA_CFG_DIR/app-kafka.properties -Dlogging.config=$AXONSAGA_CFG_DIR/log-kafka.xml -jar kafka-1.0-SNAPSHOT.jar > /dev/null &
    echo "Kafka service Started."
fi

JOB_STATUS=$?

if [ $JOB_STATUS -ne 0 ]
then
    ExitProcess 1
else
    ExitProcess 0
fi
