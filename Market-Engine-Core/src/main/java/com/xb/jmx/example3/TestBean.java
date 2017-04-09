/**
 * 
 */
package com.xb.jmx.example3;

/**
 * 被ModelMBean管理的一个简单定义的Bean --TestBean 这个Bean展示了两个属性,和一个操作 State属性是可读可写的,NbChanges是只读的,还有reset()操作
 */
public class TestBean implements java.io.Serializable
{
    // 私有变量
    private String state = "initial state";
    private int nbChanges = 0;
    private int nbResets = 0;


    public TestBean()
    {
        echo("\tTestBean Constructor Invoked: State " + state + " nbChanges: " + nbChanges + " nbResets: " + nbResets);
    }


    /**
     * Getter
     */
    public String getState()
    {
        echo("\tTestBean: getState invoked: " + state);
        return state;
    }


    /**
     * Setter
     */
    public void setState(String s)
    {
        state = s;
        nbChanges++;
        echo("\tTestBean: setState to " + state + " nbChanges: " + nbChanges);
    }


    /**
     * Getter
     */
    public Integer getNbChanges()
    {
        echo("\tTestBean: getNbChanges invoked: " + nbChanges);
        return new Integer(nbChanges);
    }


    /**
     * reset操作
     */
    public void reset()
    {
        echo("\tTestBean: reset invoked ");
        state = "reset initial state";
        nbChanges = 0;
        nbResets++;
    }


    /**
     * 返回reset次数属性值 This method is not a Getter in the JMX sense because it is not returned by the getMBeanInfo()
     * method.
     */
    public Integer getNbResets()
    {
        echo("\tTestBean: getNbResets invoked: " + nbResets);
        return new Integer(nbResets);
    }


    private void echo(String outstr)
    {
        System.out.println(outstr);
    }
}
