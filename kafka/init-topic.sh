#!/bin/bash

# 创建推送主题
/app/kafka_2.10-0.10.2.2/bin/kafka-topics.sh --create --zookeeper hh:2181 --topic ad_put_log --replication-facto 1 --partitions 1

# 创建点击主题
/app/kafka_2.10-0.10.2.2/bin/kafka-topics.sh --create --zookeeper hh:2181 --topic ad_click_log --replication-facto 1 --partitions 1

# 创建交易主题
/app/kafka_2.10-0.10.2.2/bin/kafka-topics.sh --create --zookeeper hh:2181 --topic ad_deal_log --replication-facto 1 --partitions 1

