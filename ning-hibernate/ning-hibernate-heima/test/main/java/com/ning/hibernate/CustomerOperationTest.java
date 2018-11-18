package com.ning.hibernate;

import com.ning.hibernate.domain.Customer;
import com.ning.hibernate.util.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by ning on 2018/1/24.
 * User:ning
 * Date:2018/1/24
 * tIME:13:17
 */
public class CustomerOperationTest {
    Session session = null;
    @Before
    public void setUp(){
        session = HibernateUtils.openSession();
    }
    @Test
    public void testSave(){
        Customer c = new Customer();
        c.setCust_name("传智播客1");
        session.save(c);

    }
    @Test
    public void testTransaction(){
        Customer c = new Customer();
        c.setCust_name("传智播客2");
        //Transaction tx = session.getTransaction();
        //开启事务并获得操作事务的tx对象(建议使用)
        Transaction transaction = session.beginTransaction();
        session.save(c);
        transaction.commit();
    }
    @Test
    public void testGet(){
        Customer customer = session.get(Customer.class,1l);
        System.out.println(customer);
    }
    @Test
    public void testUpdate(){
        Transaction tx = session.beginTransaction();
        Customer customer = session.get(Customer.class,1l);
        customer.setCust_name("牛魔王");
        session.update(customer);;
        tx.commit();;
    }
    @Test
    public void delete(){
        Transaction tx = session.beginTransaction();
        Customer customer = session.get(Customer.class,1L);
        session.delete(customer);
        tx.commit();

    }
    @After
    public void tearDown(){
        session.close();
    }

}
