/*
 * Copyright:
 *   2019 Derrell Lipman
 *
 * License:
 *   LGPL: http://www.gnu.org/licenses/lgpl.html
 *
 * Authors:
 *   Derrell Lipman (derrell)
 */

package org.lipman.MessageBus;

import org.lipman.MessageBus.Data;
import org.lipman.MessageBus.Subscription;

class MessageBus
{
  private static Subscription  sub;
  
  public static void subscribe(String messageType, ICallback cb)
  {
    sub = new Subscription(messageType, cb);
    System.out.println("Subscription #" + sub.getId());
  }
  
  public static Boolean post(String type, Data data)
  {
    sub.getCallback().setData(data);
    return true;
  }
  
  public static void main(String args[])
  {
    Boolean               b;
    
    subscribe(
              "abc",
              (Data data) ->
              {
                switch(data.getType())
                  {
                  case NUMBER :
                    System.out.println("NUMBER: " +  data.getNumber());
                    break;
                    
                  case STRING :
                    System.out.println("STRING: " +  data.getString());
                    break;
                  }
              });
    
    b = post("abc", new Data(42));
    System.out.println("post returned " + b);
  }
}

