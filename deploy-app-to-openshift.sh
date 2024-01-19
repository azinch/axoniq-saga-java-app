#!/usr/bin/bash

# Usage: deploy-app-to-openshift <app-name> <target-port>
#    app-name: order|user|payment|shipment
#    target-port: a target port for an order container (rest api) inside a pod
#    Examples: 
#       deploy-app-to-openshift order 9097
#       deploy-app-to-openshift payment
# Note! Before staring deploy-app-to-openshift, run delete-app-from-openshift
#       to delete all the app's resources on the cluster.


echo
echo "==================================================="
echo "Adding app $1 to axoniq-saga-java-app on the cluster.."
echo "==================================================="
echo

case $1 in
  "user" )
    ;;
  "order" )
    echo "Container port in a pod: $2"
	  echo
    ;;
  "payment" )
    ;;
  "shipment" )
    ;;
  *)
    echo "Incorrect app-name to deploy, exit"
    echo
	  exit 0
esac

mkdir -p $1/build/libs/ocp/deployments
cp $1/build/libs/*.jar $1/build/libs/ocp/deployments
oc new-build --binary=true --name=$1-app --image-stream=redhat-openjdk18-openshift:1.8
oc start-build $1-app --from-dir=$1/build/libs/ocp --follow
oc new-app $1-app

if [ $1 == "order" ]
then
  oc delete service $1-app
  oc expose deployment $1-app --port=8080 --target-port=$2
  oc expose service $1-app
fi

echo
echo "============================"
echo "Deployment completed"
echo "============================"
echo
