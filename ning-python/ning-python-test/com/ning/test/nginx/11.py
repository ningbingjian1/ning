#coding=utf-8
import re
ip = r"?P<ip>[\d.]*"
date = r"?P<date>\d+"
month = r"?P<month>\w+"
year = r"?P<year>\d+"
log_time = r"?P<time>\S+"
method = r"?P<method>\S+"
request = r"?P<request>\S+"
status = r"?P<status>\d+"
bodyBytesSent = r"?P<bodyBytesSent>\d+"
refer = r"""?P<refer>
           [^\"]*
           """
userAgent = r"""?P<userAgent>
              .*
             """
p = re.compile(r"(%s)\ -\ -\ \[(%s)/(%s)/(%s)\:(%s)\ [\S]+\]\ \"(%s)?[\s]?(%s)?.*?\"\ (%s)\ (%s)\ \"(%s)\"\ \"(%s).*?\"" %( ip, date, month, year, log_time, method, request, status, bodyBytesSent, refer, userAgent ), re.VERBOSE)
m = re.findall(p, '120.239.107.203 - - [21/Mar/2018:11:15:21 +0800] "POST /v4/identify/request HTTP/1.1" 200 67 "-" "-" "0.061" "0.000"')
print m