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

class Subscription
{
  private ICallback        callback;
  private String           messageType;
  private int              id;
  
  private static int       nextId = 1;
  
  Subscription(String messageType, ICallback cb)
  {
    this.messageType = messageType;
    this.callback = cb;
    this.id = nextId++;
  }
  
  String getType()
  {
    return this.messageType;
  }
  
  ICallback getCallback()
  {
    return this.callback;
  }
  
  int getId()
  {
    return this.id;
  }
}
