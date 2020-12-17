package com.donny;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {

        //BeanFactory bean工厂顶层接口

        //BeanFactory子类3大接口
        //HierarchicalBeanFactory 分层的
        // 主要提供两个方法：
        // getParentBeanFactory()：获取父级工厂
        // containsLocalBean(String name)：判断当前工厂是否包含bean

        //AutowireCapableBeanFactory 自动装配能力的工厂
        //AUTOWIRE_BY_NAME 通过名称自动装配
        //AUTOWIRE_BY_TYPE 通过类型自动装配
        //AUTOWIRE_CONSTRUCTOR 通过构造函数自动装配
        /*
         * 主要几个方法如下：
         * 1、createBean(Class<T> beanClass)：通过class创建bean
         * RootBeanDefinition bd = new RootBeanDefinition(beanClass); 创建BeanDefinition
         * bd.setScope(SCOPE_PROTOTYPE); 设置作用域为原型
         * 2、autowireBean(Object existingBean)：装配bean
         * 1)创建BeanDefinition
         * 2)initBeanWrapper 初始化beanWrapper
         * 3)populateBean 填充bean
         * 3、applyBeanPostProcessorsBeforeInitialization 在bean初始化前调用PostProcessor
         * 4、applyBeanPostProcessorsAfterInitialization 在bean初始化后调用PostProcessor
         * */

        //ListableBeanFactory 可列出的bean工厂,主要提供查找bean 获取所有bean的列表 bean的数量等等
        //主要方法如下：
        //1、containsBeanDefinition(String beanName):通过bean名称判断是否含有这个bean
        //2、getBeanDefinitionCount():获取bean的数量
        //3、getBeanDefinitionNames()：获取所有bean的名称
        //4、getBeanNamesForType(Class<?> type：获取指定类型的所有bean名称


        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        //注册
        User user = new User();
        user.setName("test1");
        user.setAge(100);
        beanFactory.registerSingleton("test1", user);

        //通过名称获取
        User test1 = (User) beanFactory.getBean("test1");
        System.out.println(test1);
        //通过类型获取
        User test2 = beanFactory.getBean(User.class);
        System.out.println(test2);

        //注入role
        Role role = new Role();
        role.setName("admin");
        beanFactory.registerSingleton("role1", role);

        //通过工厂创建bean
        User user1 = beanFactory.createBean(User.class);
        user1.setName("user1");
        user1.setAge(20);
        System.out.println(user1);

        //自动装配
        beanFactory.autowireBean(user1);
        System.out.println(user1);
    }
}
