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

import java.util.HashMap;
import java.util.List;
import java.util.Iterator;
import org.lipman.MessageBus.Data;
import org.lipman.MessageBus.Subscription;

class MessageBus
{
  private static HashMap<String, HashMap<Integer, Subscription>> subscriptions =
    new HashMap<>();
  
  
  /**
   * Subscribe for messages of a given type
   *
   * @param messageType
   *   The message type to subscribe to.
   *
   * @param cb
   *   Callback function, called when a message of the specified type is
   *   dispatched.
   *
   * @return
   *   Subscription ID which may be used to unsubscribe.
   */
  public static int subscribe(String messageType, ICallback cb)
  {
    Integer                             id;
    Subscription                        sub;
    HashMap<Integer, Subscription>      typeSubscriptions;
    
    // If we don't have a HashMap to hold the subscriptions for this message
    // type...
    if (! subscriptions.containsKey(messageType))
    {
      // ... then create one and add it to the subscriptions map
      typeSubscriptions = new HashMap<Integer, Subscription>();
      subscriptions.put(messageType, typeSubscriptions);
    }
    else
    {
      // There's already a HashMap for this message type. Get it.
      typeSubscriptions =
        (HashMap<Integer, Subscription>) subscriptions.get(messageType);
    }
    
    // Create a new subscription and add it to  this message type
    sub = new Subscription(messageType, cb);
    id = sub.getId();
    typeSubscriptions.put(id, sub);
    
    System.out.println("Subscription #" + id);

    return id;
  }
  
  /**
   * Unsubscribe from receiving events for a message type
   *
   * @param messageType
   *   The message type which was subscribed
   *
   * @param subscriptionId
   *   The subscription ID previously returned by `subscribe()`
   */
  public static void unsubscribe(String messageType, int subscriptionId)
  {
    HashMap<Integer, Subscription>      typeSubscriptions;

    // Retrieve the subscriptions map for this message type
    typeSubscriptions =
      (HashMap<Integer, Subscription>) subscriptions.get(messageType);

    // If we didn't find one...
    if (typeSubscriptions == null)
    {
      // ... then there's nothing to do
      return;
    }

    // Delete the specified subscription
    typeSubscriptions.remove(subscriptionId);
  }


  /**
   * Dispatch a message of a given type, to all subscribers of that type.
   *
   * @param messageType
   *   The message type whose subscribers should receive the dispatched
   *   message.
   *
   * @param data
   *   The message data to dispatch to subscribers
   *
   * @return
   *   true if there were any subscribers to which the message was dispatched;
   *   false otherwise.
   */
  public static Boolean dispatch(String messageType, Data data)
  {
    Boolean                             ret = false;
    HashMap<Integer, Subscription>      typeSubscriptions;

    // Retrieve the subscriptions map for this message type
    typeSubscriptions =
      (HashMap<Integer, Subscription>) subscriptions.get(messageType);

    // If we didn't find one...
    if (typeSubscriptions == null)
    {
      // ... then tell 'em so
      return false;
    }

    // Dispatch the event to each subscriber
    for (Subscription sub : typeSubscriptions.values())
    {
      sub.getCallback().setData(data);
      ret = true;
    }
                                
    // Tell 'em whether we dispatched to any subscribers
    return ret;
  }
  
  public static void main(String args[])
  {
    int             idAbc1;
    int             idAbc2;
    int             idAbcd1;
    Boolean         b;
    
    // Example of implementing a handler class to receive dispatched events
    class Handler implements ICallback
    {
      public void setData(Data data)
      {
        switch(data.getType())
        {
        case NUMBER :
          System.out.println("NUMBER 1: " +  data.getNumber());
          break;

        case STRING :
          System.out.println("STRING 1: " +  data.getString());
          break;
        }
      }
    }

    // Example of using the handler class in a subscription
    idAbc1 = subscribe("abc", new Handler());
    
    // Example of using a lambda instead of a handler class. The lambda
    // implicitly implements the functional interface method setData().
    idAbc2 = subscribe(
              "abc",
              (Data data) ->
              {
                switch(data.getType())
                {
                case NUMBER :
                  System.out.println("NUMBER 2: " +  data.getNumber());
                  break;
                  
                case STRING :
                  System.out.println("STRING 2: " +  data.getString());
                  break;
                }
              });
    

    // One more example, using a different message type
    idAbcd1 = subscribe(
              "abcd",
              (Data data) ->
              {
                switch(data.getType())
                {
                case NUMBER :
                  System.out.println("NUMBER 3: " +  data.getNumber());
                  break;
                  
                case STRING :
                  System.out.println("STRING 3: " +  data.getString());
                  break;
                }
              });
    
    // Should see STRING 1 and STRING 2 printed, and dispatch return true
    b = dispatch("abc", new Data("xyz"));
    System.out.println("dispatch returned " + b);

    // Should see NUMBER 1 and NUMBER 2 printed, and dispatch return true
    b = dispatch("abc", new Data(23));
    System.out.println("dispatch returned " + b);

    // Should see NUMBER 1 and NUMBER 2 printed, and dispatch return true
    b = dispatch("abcd", new Data(42));
    System.out.println("dispatch returned " + b);

    // Should not see any data results, and dispatch return false
    b = dispatch("xxx", new Data(69));
    System.out.println("dispatch returned " + b);

    // Unsubscribe to one of the "abc" subscriptions
    unsubscribe("abc", idAbc1);

    // Should now see only STRING 2 printed, and dispatch return true
    b = dispatch("abc", new Data("xyz"));
    System.out.println("dispatch returned " + b);
  }
}

