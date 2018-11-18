# coding=utf-8
def test1():
    try:
        x = 1
        return x
    except ZeroDivisionError:
        print '0除异常'
        return 1
    finally:
        x = 2
        return x


def test2():
    f = open("ai1.9metaclass.py")
    print f.read()
    f.close()

def test3():
    f = open("abc.txt","w+")
    f.write("aaaa")
    f.close()
#print test1()
#test2()
#test3()



