# Kafka Streams workshop
## Prerequisites 
1. Install docker, CE (Community Edition). The installation instructions for Linux, Mac and Windows you can find [here](https://docs.docker.com/install/)
2. Install docker compose [here](https://docs.docker.com/compose/install/).
3. Verify the installation. Run *docker* , *docker-compose* commands from your terminal

   
 ### Docker Compose
 In this workshop we'll use docker-compose to create our dockerized development environment.
 
 docker-compose will start all required containers for this workshop: Kafka and Zookeeper conatiners.
 
 + git clone this repo
 + cd to  *./docker* folder
 + *docker-compose up -d*
 + check the docker are up and running : *docker ps*
 
 + In order to get into Kafka container, run :
   *docker exec -i -t container-id /bin/bash*
 + Check if all Kafka topics have been created properly :
 
       docker ps
       docker exec -i -t zk-container-id /bin/bash
       ifconfig  (take ZK IP)
       docker exec -i -t kafka-container-id /bin/bash
       $KAFKA_HOME/bin/kafka-topics.sh --list --zookeeper zk-IP
       
       E.g. : $KAFKA_HOME/bin/kafka-topics.sh --list --zookeeper 172.18.0.2:2181
       
  It should print list of topics like that : 
  
       transaction
       nis_transaction
       withdrawal_max_tran
   
 Looks like we're ready to go...
 
 
 ### Kafka consumer
 We need to start Kafka consumers for the topics of our interest.
 
 To do that run, inside of kafka container: 
      
      $KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server 172.18.0.3:9092 --topic transaction --from-beginning
      
 When we start Kafka Streams topology we supposed to see here a stream of incoming data (transaction) to this topic.
 
 Open a second terminal, get into Kafka container and run Kafka console consumer
 
 This is the output topic where we write to the processed by the topology original stream 
 
 In order to print properly the topology output, start the Kafka consumer this way:
 
      $KAFKA_HOME/bin/kafka-console-consumer.sh --bootstrap-server 172.18.0.3:9092 \
            --topic outputtopic2 \
            --formatter kafka.tools.DefaultMessageFormatter \
            --property print.key=true \
            --property print.value=true \
            --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer \
            --property value.deserializer=org.apache.kafka.common.serialization.LongDeserializer
          
    
    
    
    
   ### Troubleshooting 
   
   Sometimes stream is stuck (data is not streaming) to resolve it try to run:
   
      docker-compose stop
      docker-compose rm
      
   if the problem persists try to delete all images :
   
      docker stop $(docker ps -a -q)
      docker rm $(docker ps -a -q)
      docker rmi $(docker ps -a -q)
       
   And then rerun docker-compose.
   
   For Windows users if you have an issue starting docker-compose try to comment volume line:
   volumes:
      - /var/run/docker.sock:/var/run/docker.sock
      
   and then rerun
   
   ## Scaling Kafka Streams
    
   In order to scale out Kafka Streams application we'll start a few instances of the same topology.
    
   Every Kafka Streams instance is going to read from its dedicated topic partition. 
    
   (We've created 3 partitions of vantage_input topic. See in docker-compose.yml)
 
 

  ________________________

   1. **Level: Easy - DollarToNis. Producer - ProducerTransaction.** 
   In this exercise you will have to create new stream contain transaction in as NIS(instead Dollar). 
   filter Balance with less than 50K nis.
     push the result to the nis_transaction topic.

            
   **Hints:** 
   
   + [KStream](https://kafka.apache.org/10/javadoc/org/apache/kafka/streams/kstream/KStream.html)
    
  2. **Level : Easy - GetMaxDrawPerAccount. Producer - ProducerTransaction**
    In this Exercise you will have to Listens to the transaction topic,
    and write result withdrawal_max_tran
  
   **Hints:** 
     useful transformations and tools may be found here :
     
   **Hints:** 
   + [KGroupedStream](https://kafka.apache.org/0110/javadoc/org/apache/kafka/streams/kstream/KGroupedStream.html)
   + [KStream](https://kafka.apache.org/10/javadoc/org/apache/kafka/streams/kstream/KStream.html)
   
   
   3. **Level: Medium - AggregateDrawPerAccountPer10MIn. Producer - ProducerTransaction.** 
    In this Exercise you will have to Listens to the transaction topic,
    and aggregate the withdrawal per account in the last 5 minutes. print the result.
   **Hints:** 
   + [KGroupedStream](https://kafka.apache.org/0110/javadoc/org/apache/kafka/streams/kstream/KGroupedStream.html)
   + [KStream](https://kafka.apache.org/10/javadoc/org/apache/kafka/streams/kstream/KStream.html)


        
        
        
  
        

            
            
    

     
    
        
     
      
      
      
      
      
      
      
      
         
      
   
 
 
 
 
     
   
