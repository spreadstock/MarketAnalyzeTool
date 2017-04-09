/**
 * 
 */
package com.xb.jmx.example3;

/**
 * 被ModelMBean管理的一个简单定义的Bean --TestBeanFriend. 和TestBean几乎一样.
 */
public class TestBeanFriend implements java.io.Serializable
{
    private String state = "initial state";
    private int nbChanges = 0;
    private int nbResets = 0;


    public TestBeanFriend()
    {
        echo("\tTestBeanFriend Constructor invoked: State " + state + " nbChanges: " + nbChanges + " nbResets: "
                        + nbResets);
    }


    /**
     * Getter.
     */
    public String getState()
    {
        echo("\tTestBeanFriend: getState invoked:" + state);
        return state;
    }


    /**
     * Setter.
     */
    public void setState(String s)
    {
        state = s;
        nbChanges++;
        echo("\tTestBeanFriend: setState invoked: to " + state + " nbChanges: " + nbChanges);
    }


    /**
     * Getter.
     */
    public Integer getNbChanges()
    {
        echo("\tTestBeanFriend:getNbChanges invoked: " + nbChanges);
        return new Integer(nbChanges);
    }


    /**
     * reset Operation.
     */
    public void reset()
    {
        echo("\tTestBeanFriend: reset invoked ");
        state = "reset initial state";
        nbChanges = 0;
        nbResets++;
    }


    /**
     * Return the "NbResets" property. This method is not a Getter in the JMX sense because it is not returned by the
     * getMBeanInfo() method.
     */
    public Integer getNbResets()
    {
        echo("\tTestBeanFriend: getNbResets invoked " + nbResets);
        return new Integer(nbResets);
    }


    private void echo(String outstr)
    {
        System.out.println(outstr);
    }
}