#coding=utf-8
file = open('fortest-log-2018-03-19-15.log')
oldDict = {}
newDict = {}
for line in file:
    fields = line.split('#')
    type = fields[0].strip()
    userId = fields[1].strip()
    clientCode = fields[2].strip()
    resultChannelId = fields[6].strip()
    key = userId + "-" + clientCode
    if(type == 'old:'):
        oldDict[key] = resultChannelId
    else:
        newDict[key] = resultChannelId

count = 0
for k,v in oldDict.items():
    newV = newDict.get(k,None)
    if newV != None and  newV == v:
        count = count + 1

print 'old size:' ,len(oldDict), len(newDict),count




