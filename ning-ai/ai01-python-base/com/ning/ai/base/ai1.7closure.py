# -*- coding: utf-8 -*-

# 闭包

def t1():
    def outer():
        x = 1
        def inner():
            print x
        return inner
    i1 = outer()
    i1()

def t2():
    def outer(x):
        def inner():
            print x
        return inner
    i1 = outer(1)
    i1()
    i2 = outer(2)
    i2()
# t1()
t2()

