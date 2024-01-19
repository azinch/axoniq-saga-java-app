#!/usr/bin/bash

# Usage: deploy-all-to-openshift <target-port>
#    target-port: a target port for an order container (rest api) inside a pod    
#    Example: deploy-all-to-openshift 9097


echo
echo "==================================================="
echo "Creating axoniq-saga-java-app project on the cluster.."
echo "==================================================="
echo
oc new-project axoniq-saga-java-app
echo
echo "========================="
echo "Adding user app.."
echo "========================="
echo
mkdir -p user/build/libs/ocp/deployments
cp user/build/libs/*.jar user/build/libs/ocp/deployments
oc new-build --binary=true --name=user-app --image-stream=redhat-openjdk18-openshift:1.8
oc start-build user-app --from-dir=user/build/libs/ocp --follow
oc new-app user-app
echo
echo "========================================================"
echo "Adding order app [container port in a pod: $1].."
echo "========================================================"
echo
mkdir -p order/build/libs/ocp/deployments
cp order/build/libs/*.jar order/build/libs/ocp/deployments
oc new-build --binary=true --name=order-app --image-stream=redhat-openjdk18-openshift:1.8
oc start-build order-app --from-dir=order/build/libs/ocp --follow
oc new-app order-app
oc delete service order-app
oc expose deployment order-app --port=8080 --target-port=$1
oc expose service order-app
echo
echo "============================"
echo "Adding payment app.."
echo "============================"
echo
mkdir -p payment/build/libs/ocp/deployments
cp payment/build/libs/*.jar payment/build/libs/ocp/deployments
oc new-build --binary=true --name=payment-app --image-stream=redhat-openjdk18-openshift:1.8
oc start-build payment-app --from-dir=payment/build/libs/ocp --follow
oc new-app payment-app
echo
echo "============================="
echo "Adding shipment app.."
echo "============================="
echo
mkdir -p shipment/build/libs/ocp/deployments
cp shipment/build/libs/*.jar shipment/build/libs/ocp/deployments
oc new-build --binary=true --name=shipment-app --image-stream=redhat-openjdk18-openshift:1.8
oc start-build shipment-app --from-dir=shipment/build/libs/ocp --follow
oc new-app shipment-app
echo
echo "========================"
echo "Project deployed"
echo "========================"
echo

