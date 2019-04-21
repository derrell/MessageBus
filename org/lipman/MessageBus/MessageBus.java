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
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;
import org.lipman.MessageBus.Data;
import org.lipman.MessageBus.Subscription;

class MessageBus
{
  private static HashMap<String, ArrayList<Subscription>> subscriptions =
    new HashMap<>();
  
  
  public static void subscribe(String messageType, ICallback cb)
  {
    Subscription                sub;
    ArrayList<Subscription>     typeSubscriptions;
    
    // If we don't have an ArrayList to hold the subscriptions for this
    // message type...
    if (! subscriptions.containsKey(messageType))
    {
      // ... then create one and add it to the subscriptions map
      typeSubscriptions = new ArrayList<Subscription>();
      subscriptions.put(messageType, typeSubscriptions);
    }
    else
    {
      // There's already an ArrayList for this message type. Get it.
      typeSubscriptions =
        (ArrayList<Subscription>) subscriptions.get(messageType);
    }
    
    // Create a new subscription and add it to  this message type
    sub = new Subscription(messageType, cb);
    typeSubscriptions.add(sub);
    
    System.out.println("Subscription #" + sub.getId());
  }
  
  public static Boolean dispatch(String messageType, Data data)
  {
    Boolean                     ret = false;
    Iterator<Subscription>      iter;
    ArrayList<Subscription>     typeSubscriptions;

    // Retrieve the subscriptions map for this message type
    typeSubscriptions =
      (ArrayList<Subscription>) subscriptions.get(messageType);

    // If we didn't find one...
    if (typeSubscriptions == null)
    {
      // ... then tell 'em so
      return false;
    }

    // Dispatch the event to each subscriber
    iter = typeSubscriptions.iterator();
    while (iter.hasNext())
    {
      Subscription sub = iter.next();
      sub.getCallback().setData(data);
      ret = true;
    }
                                
    // Tell 'em whether we dispatched to any subscribers
    return ret;
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
                  System.out.println("NUMBER 1: " +  data.getNumber());
                  break;
                  
                case STRING :
                  System.out.println("STRING 1: " +  data.getString());
                  break;
                }
              });
    
    subscribe(
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
    
    subscribe(
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
    
    b = dispatch("abc", new Data("xyz"));
    System.out.println("dispatch returned " + b);
    b = dispatch("abc", new Data(23));
    System.out.println("dispatch returned " + b);
    b = dispatch("abcd", new Data(42));
    System.out.println("dispatch returned " + b);
  }
}

