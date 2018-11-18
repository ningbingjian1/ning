# -*- coding: utf-8 -*-

'''
无参装饰器
'''
def t1():
    def outer(func):
        def inner():
            ret = func() + 1
            return ret
        return inner
    def one():
        return 1
    decorator = outer(one)
    print decorator()


def t2():
   def wrapper(func):
       def check():
           ret = func()
           if(ret < 0 ):
               ret = ret * -1
           return ret
       return check

   def add():
       return -100;
   decorator = wrapper(add)
   @wrapper
   def sub():
       return -1000
   print sub()


def t3():
    def add(x ,y ):
        return x + y ;
    print add(1,2)
    print add(*[1,2])





'''通用装饰器'''
def t4():
    def logger(func):
        def inner(*args,**kwargs):
            print "will execute func [%s] ,%s,%s" % (func.__name__,args,kwargs)
            ret = func(*args,**kwargs)
            return ret
        return inner

    @logger
    def add(x,y):
        return x + y
    print add(1 ,2 )



from collections import Iterable,Iterator
def t5():
    class MyObj(Iterable):
        def __init__(self,low,up):
            self.up = up
            self.low = low
        def __iter__(self):
            return  MyIterator(self)
    class MyIterator(Iterator):
        def __init__(self,obj):
            self.obj = obj
            self.cur = obj.low
        def next(self):
            if self.cur > self.obj.up:
                raise StopIteration
            else:
                ret = self.cur
                self.cur += 1
                return ret

    it = MyObj(1,5)
    for i in it:
        print i

class Student:
    def __init__(self,name):
        self.__name = name
    def __getattr__(self, item):
        if item == 'age':
            return 100
        if item == 'name':
            return lambda :1000
s = Student('n1')
print s.age
print s.name()



class Fib(object):
    def __init__(self):
        self.a, self.b = 0, 1 # 初始化两个计数器a，b

    def __iter__(self):
        return self # 实例本身就是迭代对象，故返回自己

    def next(self):
        self.a, self.b = self.b, self.a + self.b # 计算下一个值
        if self.a > 100000: # 退出循环的条件
            raise StopIteration();
        return self.a # 返回下一个值
    def __getitem__(self, n):
        a,b = 1,1
        if isinstance(n,int):
            for x in range(n):
                a,b = b ,a + b
            return a

        if isinstance(n,slice):
            start = n.start
            stop = n.stop
            a,b = 1,1
            L = []
            for x in range(stop):
                if x >= start:
                    L.append(a)
                a ,b = b, a + b
            return L
class Chain:
    def __init__(self,path = ''):
        self._path = path
    def __getattr__(self, path):
        return Chain("%s/%s" %(self._path,path))
    def __str__(self):
        return self._path
print Chain().user.name.list


class T:
    def __call__(self, *args, **kwargs):
        print "call T"
s = T()
s()
print callable(T())


def t6():
    class MyListMetaClass(type):
        def __new__(cls, name, bases, attrs):
            attrs['add'] = lambda self,value:self.append(value)
            return type.__new__(cls, name, bases, attrs)
    class MyList(list):
        __metaclass__ = MyListMetaClass

    ml = MyList()
    ml.add(111)
    ml.add(112)
    print ml
print t6()




