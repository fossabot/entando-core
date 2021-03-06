/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package com.agiletec.aps.system.common.notify;

import org.springframework.context.ApplicationEvent;

import com.agiletec.aps.system.common.IManager;

/**
 * Base class for the implementation of the events to be notified to the event management
 * service.
 * The class that implements the event must exclusively know the interface through which the
 * observing service (the final destination of the notification) must implement to properly handle
 * the specific event.
 * @author M.Diana - E.Santoboni
 */
public abstract class ApsEvent extends ApplicationEvent {
	
	/**
	 * Event constructor
	 */
	public ApsEvent() {
		super("Entando Event");
		this.setSource(LOCAL_EVENT);
	}
	
	/**
	 * Notify the event to the observer service. This method must be invoked 
	 * inside the update() method of the observer service. 
	 * @param srv The listening service
	 */
	public abstract void notify(IManager srv);
	
	/**
	 * Return the object class of the interface that the observer service  have to implement
	 * to handle to event.
	 * @return The interface class which the observer must implement 
	 */
	public abstract Class getObserverInterface();
	
	/**
	 * Return the source of the event. 
	 * The property can be a IP address, an host name, or else.
	 * @return The source of the event.
	 */
	@Override
	public String getSource() {
		return _source;
	}
	
	/**
	 * Set the source of the event.
	 * @param source The source of the event to set.
	 */
	public void setSource(String source) {
		this._source = source;
	}
	
	private String _source;
	
	/**
	 * The default source event.
	 */
	public static final String LOCAL_EVENT = "localhost";
	
}