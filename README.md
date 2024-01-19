# axoniq-saga-java-app

## Project Overview

Multi-module project composed of Event-driven microservices (mS) to be run on Openshift cluster.

mS, having subscribed to Axon cluster, communicate with each other using Axon command/query/event bus (internal conversation contour). Milestone events, created in the end of internal conversation (a flow), are published to Kafka event bus (external contour). Data generated in both contours are stored in Axon/Kafka event stores and Oracle RDBMS. Conversations (flow) are implemented via Axon Saga Orchestration pattern. In case the flow fails at some stage Compensation events are created/handled to move the system to consistent final state. Required events can be replayed again using Axon event sourcing functionality. Axon/Kafka/Oracle are external resources to Openshift (not managed containers unlike mS).

## Project Value

1. Demo for Axon, which is de-facto industry standard for implementing Event sourcing & CQRS. Axon is a decent candidate to migrate to from the monolith stacks such as F4AC CO/GCO & RT_TRX by OGS, (homegrown approach trying to build primitive Event-driven like architecture).
   
2. Demo for automation of containerizing and deploying mS to Openshift cluster. Can be used as a working example/template to deploy existing projects to Openshift (ensemble-etl, for example).

3. Demo for Axon-Kafka communication (event Sourcing & event Streaming contours).

**Note:** Project was tested in both Beeline environment (pl-fac01) and in my local env (see Devops activities below).

## User Order Flow

User triggers creating Order through Rest API, to be followed by Axon Saga controller to orchestrate remaining flow (batch). Payment is created using User's payment credentials, Shipment's created after processing Payment, etc. Axon Saga has Compensation logic moving all parts of the system to consistent final state, should any part fail, for example: Order - Cancelled, Payment - Cancelled, Shipment - Rejected Order - Cancelled, Payment - Rejected, etc (see events schema in order-flow.jpg). User can replay relevant Order events using Rest API of the order mS.

## User mS

Handles a user data relevant for the ordering (name/address/payment card, etc.)

## Order mS

1. Rest API to trigger:
   - Creating order(s),
   - Replaying order events,
   - Checking replay status.
2. Order saga (CQRS flow with compensatory logic, see order-flow.jpg ).

## Payment mS

Handles a payment part of the ordering.

## Shipment mS

Shipment part of the order saga.

## Devops Activities

On my PC with HW: AMD Ryzen 9 - 8 CPU, RAM DDR4 3200 - 64GB, SSD M.2 - 2Tb; SW: Windows 11 (home edt), manually added Hyper-V (type 1 hypervisor).

1. Create 2 RHEL8 VMs using performant Hyper-V providing direct access to HW;
2. Axon/Kafka/OracleXE run on the first VM (vCPU = 2, RAM = 24Gb)
3. Openshift CRC (single node cluster) run on the second VM (vCPU = 4, RAM = 24Gb);
4. Gradle build axoniq-saga-java-app (you can clone the proj from my github account).
5. Create mS images & deploy to Openshift using scripts in the project root:
   - deploy-all-to-openshift.sh
   - delete-all-to-openshift.sh
   - deploy-app-to-openshift.sh

## Technological Stack

1. Axon for event Sourcing, CQRS (command/query/event bus & store), a token store is JPA-based (OracleXE).
2. Kafka for event Streaming (external bus for so-called milestone events).
3. Openshift CRC (single node cluster) to manage containerized workload (S2I binary mode is used for image creation/deployment).
4. Spring Boot, REST, JPA (OracleXE /H2), Gradle build.
5. RHEL OS.
