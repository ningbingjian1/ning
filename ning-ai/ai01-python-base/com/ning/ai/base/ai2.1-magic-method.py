# coding=utf-8
class Person:
    def __init__(self,age):
        self.age = age
    def __getattr__(self, item):
        print "查找一个不存在的属性:%s" % item
        return None


class User(object):
    def __init__(self):
        super(User, self).__init__()
        print "__init__"



class Word:
    def __init__(self,chars):
        self.chars = chars

    def __add__(self, other):
        self.chars = self.chars + other.chars
        return self.chars

    def __str__(self):
        return "chars: %s" %(self.chars)
    def __repr__(self):
        return self.__str__(self)
    def __pos__(self):
        return (self.chars)
    def __neg__(self):
        return self.chars[::-1]

    # def __setattr__(self, name, value):
    #     print "set attr"
    #     self.name = value
        # 每当属性被赋值的时候， ``__setattr__()`` 会被调用，这样就造成了递归调用。
        # 这意味这会调用 ``self.__setattr__('name', value)`` ，每次方法会调用自己。这样会造成程序崩溃。
    #
    def __setattr__(self, name, value):
        self.__dict__[name] = value  # 给类中的属性名分配值
        # 定制特有属性

w = Word("abc")
w.name = "n1"
print