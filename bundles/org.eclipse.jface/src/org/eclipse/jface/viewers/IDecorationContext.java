/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jface.viewers;

import org.eclipse.core.commands.contexts.Context;

/**
 * A decoration context provides additional information to
 * a label decorator.
 * <p>
 * This interface is not intended to be implemented by clients
 * 
 * @see LabelDecorator
 * 
 * @since 3.2
 */
public interface IDecorationContext {
	
	/**
	 * Return the ids of the contexts that are active for
	 * the element that is being decorated. Context ids identify
	 * a command {@link Context}.
	 * @return the ids of the contexts that are active for
	 * the element that is being decorated
	 * @see org.eclipse.core.commands.contexts.Context
	 */
	String[] getContextIds();

}
