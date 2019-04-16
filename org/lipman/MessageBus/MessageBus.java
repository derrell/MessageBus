/*
 * Copyright:
 *   2019 Derrell Lipman
 *
 * License:
 *   LGPL: http://www.gnu.org/licenses/lgpl.html
 *
 * Authors:
 *   * Derrell Lipman (derrell)
 */

import java.util.function.Function;

enum DataType
{
  INT,
  STRING
}

class Data extends Object
{
  private DataType      type;
  private Integer       i;
  private String        s;
  
  Data(Integer i)
    {
      this.set(i);
    }

  Data(String s)
    {
      this.set(s);
    }

  void set(Integer i)
    {
      this.type = DataType.INT;
      this.i = i;
    }
  
  void set(String s)
    {
      this.type = DataType.STRING;
      this.s = s;
    }
  
  Integer getInteger()
    {
      return this.i;
    }
  
  String getString()
    {
      return this.s;
    }
  
  DataType getType()
    {
      return this.type;
    }
}

interface Func
{
  void setData(Data data);
}

class MessageBus
{
  public static void addListener(String messageType, Func f)
    {
      f.setData(new Data("hello world"));
    }
  
  public static void main(String args[])
    {
      addListener(
        "abc",
        (Data data) ->
        {
          switch(data.getType())
          {
          case INT :
            System.out.println("INT: " +  data.getInteger());
            break;

          case STRING :
            System.out.println("STRING: " +  data.getString());
            break;
          }
        });
    }
}

