/*******************************************************************************
 * Copyright (c) 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.ui.tests.performance.layout;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.tests.performance.BasicPerformanceTest;
import org.eclipse.ui.tests.performance.TestRunnable;

/**
 * @since 3.1
 */
public class ComputeSizeTest extends BasicPerformanceTest {

    private TestWidgetFactory widgetFactory;
    private int xHint;
    private int yHint;
    private boolean flushState;
    
    /**
     * @param testName
     */
    public ComputeSizeTest(TestWidgetFactory widgetFactory, int xHint, int yHint, boolean flushState) {
        super(widgetFactory.getName() + " computeSize(" 
                + ((xHint == SWT.DEFAULT)? "SWT.DEFAULT" : "" + xHint) + ", "
                + ((yHint == SWT.DEFAULT)? "SWT.DEFAULT" : "" + yHint) + ", "
                + (flushState ? "true" : "false") + ")");
        
        this.widgetFactory = widgetFactory;
        this.flushState = flushState;
        this.xHint = xHint;
        this.yHint = yHint;
    }

    /**
     * Run the test
     */
    protected void runTest() throws CoreException, WorkbenchException {

        widgetFactory.init();
        final Composite widget = widgetFactory.getControl();
        
        exercise(new TestRunnable() {
           public void run() {
               processEvents();
               
               // Place some bogus size queries to reduce the chance of a cached value being returned
               widget.computeSize(100, SWT.DEFAULT, false);
               widget.computeSize(SWT.DEFAULT, 100, false);
               
               startMeasuring();
               
               widget.computeSize(xHint, yHint, flushState);
               
               stopMeasuring();                
            } 
        });
        
        commitMeasurements();
        assertPerformance();
        widgetFactory.done();
    }
}
