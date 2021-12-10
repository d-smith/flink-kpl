# flink-kpl

This project illustrates integrating a KPL Kinesis producer with a Flink application

## Set up

Create the stream and EFO consumer

```
cdk deploy
```

## Observations

### Non-KPL Consumer

On the producer side, if we change the default buffer time to 1500 ms via the RECORD_MAX_BUFFERED_TIME
environment variable, we see dense packing of records in the producer output:

```
09:21:05,261 INFO  com.amazonaws.services.kinesis.producer.LogInputStreamReader [] - [2021-11-05 09:21:05.260537] [0x000124e8][0x00007000030f2000] [info] [processing_statistics_logger.cc:111] Stage 1 Triggers: { stream: 'kpltest', manual: 0, count: 0, size: 387, matches: 0, timed: 1, UserRecords: 704147, KinesisRecords: 388 }
09:21:05,261 INFO  com.amazonaws.services.kinesis.producer.LogInputStreamReader [] - [2021-11-05 09:21:05.260627] [0x000124e8][0x00007000030f2000] [info] [processing_statistics_logger.cc:114] Stage 2 Triggers: { stream: 'kpltest', manual: 0, count: 22, size: 0, matches: 63, timed: 2, KinesisRecords: 11750, PutRecords: 87 }
09:21:05,261 INFO  com.amazonaws.services.kinesis.producer.LogInputStreamReader [] - [2021-11-05 09:21:05.260659] [0x000124e8][0x00007000030f2000] [info] [processing_statistics_logger.cc:129] (kpltest) Average Processing Time: 3843.7568 ms
```

Reading records in the Jupyter notebook reflects the user record packing into a Kinesis data record:

```
b'\xf3\x89\x9a\xc2\n\x04FAMI\n\x03CEI\n\x04KMDN\n\x04DWAC\n\x04SNDL\x1a\x1b\x08\x00\x1a\x17FAMI,458.68998676910815\x1a\x19\x08\x01\x1a\x15CEI,513.1611707741936\x1a\x1b\x08\x02\x1a\x17KMDN,238.01503970098105\x1a\x1a\x08\x03\x1a\x16DWAC,579.6327253600374\x1a\x1a\x08\x00\x1a\x16FAMI,44.49897274223018\x1a\x1b\x08\x00\x1a\x17FAMI,479.66504686661335\x1a\x1a\x08\x02\x1a\x16KMDN,272.0587937056389\x1a\x1b\x08\x04\x1a\x17SNDL,193.93834184994603\x1a\x1b\x08\x00\x1a\x17FAMI,224.12015631172247\x1a\x1b\x08\x03\x1a\x17DWAC,173.07568379636302\x1a\x1a\x08\x03\x1a\x16DWAC,263.2969909628329\x1a\x1a\x08\x03\x1a\x16DWAC,502.5642058379419\x1a\x1a\x08\x02\x1a\x16KMDN,134.3686258087817\x1a\x1a\x08\x02\x1a\x16KMDN,263.5903389923182\x1a\x1b\x08\x03\x1a\x17DWAC,502.80941465262174\x1a\x1b\x08\x03\x1a\x17DWAC,440.93082405285844\x1a\x19\x08\x01\x1a\x15CEI,436.0861814300826\x1a\x1a\x08\x01\x1a\x16CEI,23.376560489265664\x1a\x1a\x08\x02\x1a\x16KMDN,8.650091406363858\x1a\x1b\x08\x00\x1a\x17FAMI,105.25268476706793\x1a\x1a\x08\x01\x1a\x16CEI,234.01041847670263\x1a\x1a\x08\x01\x1a\x16CEI,29.433832039889296\x1a\x1a\x08\x01\x1a\x16CEI,318.48856094856916\x1a\x18\x08\x01\x1a\x14CEI,512.840027943295\x1a\x1a\x08\x02\x1a\x16KMDN,191.2116275083792\x1a\x1a\x08\x03\x1a\x16DWAC,64.73389075264086\x1a\x1a\x08\x01\x1a\x16CEI,393.61589026798686\x1a\x1b\x08\x00\x1a\x17FAMI,446.22634655830564\x1a\x1a\x08\x04\x1a\x16SNDL,309.1913113203476\x1a\x1b\x08\x02\x1a\x17KMDN,226.23377029621963\x1a\x1a\x08\x03\x1a\x16DWAC,580.4091662875409\x1a\x1a\x08\x03\x1a\x16DWAC,264.7152396379922\x1a\x1b\x08\x04\x1a\x17SNDL,194.73609406309296\x1a\x1a\x08\x03\x1a\x16DWAC,198.3549901570926\x1a\x1a\x08\x01\x1a\x16CEI,125.34823005998777\x1a\x1a\x08\x02\x1a\x16KMDN,516.9404990537549\x1a\x1a\x08\x00\x1a\x16FAMI,184.6207732384316\x1a\x1a\x08\x01\x1a\x16CEI,200.52300210530845\x1a\x1a\x08\x00\x1a\x16FAMI,489.6784713955246\x1a\x1a\x08\x00\x1a\x16FAMI,138.0331623734082\x1a\x1a\x08\x00\x1a\x16FAMI,49.25419601493981\x1a\x1a\x08\x02\x1a\x16KMDN,67.36019365563024\x1a\x1b\x08\x00\x1a\x17FAMI,459.81321001975994\x1a\x19\x08\x01\x1a\x15CEI,364.9891590066556\x1a\x1a\x08\x03\x1a\x16DWAC,546.3617597699777\x1a\x19\x08\x02\x1a\x15KMDN,270.126680378647\x1a\x1b\x08\x02\x1a\x17KMDN,399.56981744440327\x1a\x1b\x08\x00\x1a\x17FAMI,253.83150992612963\x1a\x1a\x08\x04\x1a\x16SNDL,45.79835688212486\x1a\x19\x08\x00\x1a\x15FAMI,438.002271785507\x1a\x1b\x08\x00\x1a\x17FAMI,140.33285083922695\x1a\x1a\x08\x02\x1a\x16KMDN,432.1605606825426\x1a\x1b\x08\x02\x1a\x17KMDN,196.58530887141993\x1a\x1a\x08\x02\x1a\x16KMDN,87.80354312759285\x1a\x1a\x08\x03\x1a\x16DWAC,355.2383039138264\x1a\x19\x08\x01\x1a\x15CEI,70.68628399086727\x1a\x1a\x08\x04\x1a\x16SNDL,557.2653110768948\x1a\x1b\x08\x03\x1a\x17DWAC,420.72970628881933\x1a\x1b\x08\x00\x1a\x17FAMI,13.472590314555566\x1a\x1a\x08\x00\x1a\x16FAMI,407.3362344241067\x1a\x1b\x08\x04\x1a\x17SNDL,60.668901676359674\x1a\x1b\x08\x03\x1a\x17DWAC,194.73106459037612\x1a\x1b\x08\x00\x1a\x17FAMI,60.780011387642794\x1a\x1b\x08\x03\x1a\x17DWAC,107.53283833405074\x1a\x1a\x08\x02\x1a\x16KMDN,534.1863465978256\x1a\x1a\x08\x04\x1a\x16SNDL,431.0936776303724\x1a\x19\x08\x01\x1a\x15CEI,222.2724623946632\x1a\x19\x08\x01\x1a\x15CEI,294.2340262186729\x1a\x1b\x08\x00\x1a\x17FAMI,446.63864635250087\x1a\x1a\x08\x02\x1a\x16KMDN,463.8342757880617\xb8\xb9\x1f\x93-"\xdfKz\xc1\xa2*\xb6iQV'
```
If we go back to the default buffer time of 100 ms, and slow the producer rate to one event per second,
in the Jupyter notebook we see the records come through unpacked:

```
09:32:08,085 INFO  com.amazonaws.services.kinesis.producer.LogInputStreamReader [] - [2021-11-05 09:32:08.084648] [0x000129df][0x00007000006e7000] [info] [processing_statistics_logger.cc:111] Stage 1 Triggers: { stream: 'kpltest', manual: 0, count: 0, size: 0, matches: 0, timed: 93, UserRecords: 93, KinesisRecords: 93 }
09:32:08,085 INFO  com.amazonaws.services.kinesis.producer.LogInputStreamReader [] - [2021-11-05 09:32:08.084777] [0x000129df][0x00007000006e7000] [info] [processing_statistics_logger.cc:114] Stage 2 Triggers: { stream: 'kpltest', manual: 0, count: 0, size: 0, matches: 0, timed: 97, KinesisRecords: 98, PutRecords: 97 }
09:32:08,085 INFO  com.amazonaws.services.kinesis.producer.LogInputStreamReader [] - [2021-11-05 09:32:08.084808] [0x000129df][0x00007000006e7000] [info] [processing_statistics_logger.cc:129] (kpltest) Average Processing Time: 202.95833 ms


---------------
b'SNDL,423.8898749408191'
---------------
b'FAMI,163.69246089371697'
---------------
b'CEI,386.44085443301117'
---------------
b'FAMI,588.7074183119951'
```

### Flink App

The Flink app was able to operate successfully on the stream regardless of the data thrown at it. Even with the
producer running with a large buffer delay and packing multiple records in the app continued to
process the stream data with no problems.

```
10:01:03,358 INFO  com.amazonaws.services.kinesis.producer.LogInputStreamReader [] - [2021-11-05 10:01:03.358618] [0x00012abc][0x00007000014d0000] [info] [processing_statistics_logger.cc:111] Stage 1 Triggers: { stream: 'kpltest', manual: 0, count: 0, size: 0, matches: 0, timed: 10, UserRecords: 3074, KinesisRecords: 10 }
10:01:03,359 INFO  com.amazonaws.services.kinesis.producer.LogInputStreamReader [] - [2021-11-05 10:01:03.358706] [0x00012abc][0x00007000014d0000] [info] [processing_statistics_logger.cc:114] Stage 2 Triggers: { stream: 'kpltest', manual: 0, count: 0, size: 0, matches: 0, timed: 10, KinesisRecords: 10, PutRecords: 10 }
10:01:03,359 INFO  com.amazonaws.services.kinesis.producer.LogInputStreamReader [] - [2021-11-05 10:01:03.358736] [0x00012abc][0x00007000014d0000] [info] [processing_statistics_logger.cc:129] (kpltest) Average Processing Time: 204.2 ms

3> Quote{symbol='DWAC', price=172.88936050056853}
1> Quote{symbol='FAMI', price=554.2552671371109}
2> Quote{symbol='KMDN', price=175.84808278718452}
1> Quote{symbol='SNDL', price=352.52319863604856}
2> Quote{symbol='CEI', price=405.3980521438108}
```