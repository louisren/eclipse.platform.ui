package org.eclipse.ui.views.navigator;

/*
 * (c) Copyright IBM Corp. 2000, 2002.
 * All Rights Reserved.
 */
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.SelectionListenerAction;
import org.eclipse.ui.help.WorkbenchHelp;
import org.eclipse.ui.part.ResourceTransfer;

/**
 * Standard action for pasting resources on the clipboard to the selected resource's location.
 * <p>
 * This class may be instantiated; it is not intended to be subclassed.
 * </p>
 * 
 * @since 2.0
 */
/*package*/ class PasteAction extends SelectionListenerAction {

	/**
	 * The id of this action.
	 */
	public static final String ID = PlatformUI.PLUGIN_ID + ".PasteAction";//$NON-NLS-1$
	
	/**
	 * The shell in which to show any dialogs.
	 */
	private Shell shell;

	/**
	 * System clipboard
	 */
	private Clipboard clipboard;

/**
 * Creates a new action.
 *
 * @param shell the shell for any dialogs
 */
public PasteAction(Shell shell) {
	super(ResourceNavigatorMessages.getString("PasteAction.title")); //$NON-NLS-1$
	Assert.isNotNull(shell);
	this.shell = shell;
	setToolTipText(ResourceNavigatorMessages.getString("PasteAction.toolTip")); //$NON-NLS-1$
	setId(PasteAction.ID);
	clipboard = new Clipboard(shell.getDisplay());
	WorkbenchHelp.setHelp(this, new Object[] {INavigatorHelpContextIds.PASTE_ACTION});
}
/**
 * Implementation of method defined on <code>IAction</code>.
 */
public void run() {
	// try a resource transfer
	ResourceTransfer resTransfer = ResourceTransfer.getInstance();
	IResource[] resourceData = (IResource[])clipboard.getContents(resTransfer);
	
	if (resourceData != null) {
		if (resourceData[0].getType() == IResource.PROJECT){
			CopyProjectOperation operation = new CopyProjectOperation(this.shell);
			operation.copyProject((IProject) resourceData[0]);
		}
		else {
			CopyFilesAndFoldersOperation operation = new CopyFilesAndFoldersOperation(this.shell);
			// enablement should ensure that we always have a container
			List selection = getSelectedResources();
			if (selection.size() == 1 && selection.get(0) instanceof IContainer)
				operation.copyResources(resourceData, (IContainer)selection.get(0));
		}
		return;
	}
	
	// try a file transfer
	FileTransfer fileTransfer = FileTransfer.getInstance();
	String[] fileData = (String[])clipboard.getContents(fileTransfer);
	
	if (fileData != null) {
		CopyFilesAndFoldersOperation operation = new CopyFilesAndFoldersOperation(this.shell);
		// enablement should ensure that we always have a container
		List selection = getSelectedResources();
		if (selection.size() == 1 || selection.get(0) instanceof IContainer)
			operation.copyFiles(fileData, (IContainer)selection.get(0));
	}
}

/**
 * The <code>PasteAction</code> implementation of this
 * <code>SelectionListenerAction</code> method enables this action if 
 * a resource compatible with what is on the clipboard is selected.
 */
protected boolean updateSelection(IStructuredSelection selection) {
	if (!super.updateSelection(selection)) 
		return false;
	
	// clipboard must have resources or files
	ResourceTransfer resTransfer = ResourceTransfer.getInstance();
	IResource[] resourceData = (IResource[])clipboard.getContents(resTransfer);
	FileTransfer fileTransfer = FileTransfer.getInstance();
	String[] fileData = (String[])clipboard.getContents(fileTransfer);
	if (resourceData == null && fileData == null)
		return false;

	// can paste a project regardless of selection
	if (resourceData != null && resourceData[0].getType() == IResource.PROJECT)
		return true;

	// can only paste files and folders to a single selection of a project (open) or folder
	if (getSelectedNonResources().size() > 0) 
		return false;
	List selectedResources = getSelectedResources();
	if (selectedResources.size() != 1)
		return false;
	if (selectionIsOfType(IResource.FOLDER))
		return true;
	if (selectedResources.get(0) instanceof IProject)
		return ((IProject)selectedResources.get(0)).isOpen();

	return false;
}

}

