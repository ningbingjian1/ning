# -*- coding: utf-8 -*-


class Field(object):
    def __init__(self,name,columnType):
        self.name = name
        self.columnType = columnType
    def __str__(self):
        return "<%s:%s>" % (self.name,self.columnType)
class StringField(Field):
    def __init__(self,name):
        super(StringField, self).__init__(name,"varchar(100)")
class IntegerField(Field):
    def __init__(self,name):
        super(IntegerField, self).__init__(name, 'bigint')


class ModelMetaclass(type):
    def __new__(cls, name,bases, attrs):
        if name == "Model":
            return type.__new__(cls,name,bases,attrs)
        mapping = dict()
        for k , v in attrs.iteritems():
            if isinstance(v,Field):
                print "Found mapping :%s = %s " % (k,v)
                mapping[k] = v
        for k in mapping.iterkeys():
            attrs.pop(k)
        attrs['__table__'] = name
        attrs['__mappings__'] = mapping
        return type.__new__(cls,name,bases,attrs)

class Model(dict):
    __metaclass__ = ModelMetaclass
    def __init__(self, **kw):
        super(Model, self).__init__(**kw)

    def __getattr__(self, key):
        try:
            return self[key]
        except KeyError:
            raise AttributeError(r"'Model' object has no attribute '%s'" % key)
    def __setattr__(self, key, value):
        self[key] = value

    def save(self):
        fields = []
        params = []
        args = []
        for k, v in self.__mappings__.iteritems():
            fields.append(v.name)
            params.append('?')
            args.append(getattr(self, k, None))
        sql = 'insert into %s (%s) values (%s)' % (self.__table__, ','.join(fields), ','.join(params))
        print('SQL: %s' % sql)
        print('ARGS: %s' % str(args))
class User(Model):
    id = IntegerField('id')
    id = IntegerField("id")
    name = StringField("name")
    email = StringField("email")
    password = StringField("password")
u = User(id=12345, name='Michael', email='test@orm.org', password='my-pwd')
u.save()