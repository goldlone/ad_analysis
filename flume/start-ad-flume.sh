#!/bin/bash

# 采集put
flume-ng agent --conf /app/cdh/flume-1.6.0-cdh5.13.2/conf/ --conf-file /app/nginx/ad/put/flume/ad-put-nginx-file-kafka.conf --name ad_click_log -Dflume.root.logger=INFO,console

# 采集click
flume-ng agent --conf /app/cdh/flume-1.6.0-cdh5.13.2/conf/ --conf-file /app/nginx/ad/put/flume/ad-put-nginx-file-kafka.conf --name ad_put -Dflume.root.logger=INFO,console

# 采集deal
flume-ng agent --conf /app/cdh/flume-1.6.0-cdh5.13.2/conf/ --conf-file /app/nginx/ad/put/flume/ad-put-nginx-file-kafka.conf --name ad_deal_log -Dflume.root.logger=INFO,console

