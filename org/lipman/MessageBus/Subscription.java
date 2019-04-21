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

/**
 * An individual subscription to the message bus
 */
class Subscription
{
  private ICallback        callback;
  private String           messageType;
  private int              id;
  
  private static int       nextId = 1;
  
  /**
   * Constructor
   *
   * @param messageType
   *   The message type which should generate a call to the specified
   *   callback, should that message type be dispatched on the message bus.
   *
   * @param cb
   *   Function to be called when the specified messageType is dispatched on
   *   the message bus.
   */
  Subscription(String messageType, ICallback cb)
  {
    // Save the message type and callback
    this.messageType = messageType;
    this.callback = cb;

    // Assign a unique ID for this subscription
    this.id = nextId++;
  }
  
  /**
   * Retrieve the message type of this subscription
   */
  String getType()
  {
    return this.messageType;
  }
  
  /**
   * Retrieve the callback function of this subscription
   */
  ICallback getCallback()
  {
    return this.callback;
  }
  
  /**
   * Retrieve the assigned ID of this subscription
   */
  int getId()
  {
    return this.id;
  }
}
