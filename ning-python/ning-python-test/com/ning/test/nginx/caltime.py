#coding=utf-8
import re
import pandas as pd
import sys
pd.set_option('display.max_rows', None)
'''
这是一个统计nginx访问时间的程序
ngnix的配置
log_format  main  '$remote_addr - $remote_user [$time_local] "$request" '
					  '$status $body_bytes_sent "$http_referer" '
					  '"$http_user_agent" "$request_time" "$upstream_response_time"';
					  
$request_time 整个请求耗时 毫秒
$upstream_response_time: nginx到后台tomcat的耗时  毫秒
$upstream_response_time < 	$request_time			  
'''
ip = r"?P<ip>[\d.]*"
date = r"?P<date>\d+"
month = r"?P<month>\w+"
year = r"?P<year>\d+"
log_time = r"?P<time>\S+"
method = r"?P<method>\S+"
url = r"?P<url>\S+"
httpVersion= r"?P<httpVersion>\S+"
status= r"?P<status>\d+"
bodyBytesSent = r"?P<bodyBytesSent>\d+"
request_time = r"?P<request_time>\S+"
upstream_response_time = r"?P<upstream_response_time>\S+"
p = re.compile(r'(%s)\ \- \- ((%s)/(%s)/(%s):(%s)) \+0800 \"(%s) (%s) (%s)\" (%s) (%s) \"\-\" \"\-\" \"(%s)\" \"(%s)\"' % (ip,date,month,year,log_time,method,url,httpVersion,status,bodyBytesSent,request_time,upstream_response_time))
'''
result =  p.match(r'120.239.107.203 - - 21/Mar/2018:11:15:21 +0800 "POST /v4/identify/request HTTP/1.1" 200 67 "-" "-" "0.061" "0.100"')
result =  p.match(r'42.236.127.250 - - [21/Mar/2018:12:12:00 +0800] "HEAD / HTTP/1.1" 200 0 "-" "-" "0.000" "-"')
print result
print result.group('method')
print 'ip=' ,result.group('ip'),',','date=',result.group('date'),',','month=',result.group('month') \
,',','year=',result.group('year') \
,',','time=',result.group('time') \
,',','url=',result.group('url') \
,',','status=',result.group('status') \
,',','request_time=',result.group('request_time') \
,',','upstream_response_time=',result.group('upstream_response_time')

'''
c = 0
urlDicts = {}

#f = open('nginx.log')
f = open('D:/tmp/ifmis_1.log')
for line in f :
    rline = line.replace('[','').replace(']','')
    try:
        matchResult = p.match(rline)
        if matchResult != None :
            status = matchResult.group('status')
            if int(status) >= 200 and int(status) <= 300:
                url = matchResult.group('url')
                request_time = matchResult.group('request_time')
                upstream_response_time = matchResult.group('upstream_response_time')
                urlDict = urlDicts.get(url)
                if urlDict == None:
                    urlDict = {}
                    urlDicts[url] = urlDict
                requestList = urlDict.get('request_time')
                if requestList == None:
                    requestList = []
                    urlDict['request_time'] = requestList
                upstreamList = urlDict.get('upstream_response_time')
                if upstreamList == None:
                    upstreamList = []
                    urlDict['upstream_response_time'] = upstreamList
                requestList.append(float(request_time))
                upstreamList.append(float(upstream_response_time))
    except Exception ,e:
        print e
        print line
        if line.find('favicon.ico') != -1 or line.find('HEAD') != -1 :
            print 'head request ,pass',line
        else:
            print 'error match ,exit', line
f.close()
for k,urlDict in urlDicts.items():
    try:
        print 'url = ',k
        df = pd.DataFrame(urlDict)
        print df.describe()
        print '------------------------------------------------------------'
    except Exception ,e:
        print e
        print 'k=' ,k, 'urlDict = ' ,urlDict

