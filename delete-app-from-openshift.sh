#!/usr/bin/bash

# Usage: delete-app-from-openshift <app-name>
#    app-name: order|user|payment|shipment
#    Example:
#       delete-app-from-openshift order


echo
echo "======================================================="
echo "Deleting app $1 from axoniq-saga-java-app on the cluster.."
echo "======================================================="
echo
case $1 in
  "user" )
    ;;
  "order" )
    ;;
  "payment" )
    ;;
  "shipment" )
    ;;
  *)
    echo "Incorrect app-name to delete, exit"
    echo
	  exit 0
esac

oc delete deployment $1-app

if [ $1 == "order" ]
then
  oc delete route $1-app
fi

oc delete service $1-app
oc delete imagestream $1-app
oc delete buildconfig $1-app

echo
echo "========================"
echo "Delete completed"
echo "========================"
echo

