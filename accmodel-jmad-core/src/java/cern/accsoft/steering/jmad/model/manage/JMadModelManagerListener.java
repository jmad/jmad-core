/*
 * $Id: ModelManagerListener.java,v 1.1 2008-08-11 11:58:45 kfuchsbe Exp $
 * 
 * $Date: 2008-08-11 11:58:45 $ $Revision: 1.1 $ $Author: kfuchsbe $
 * 
 * Copyright CERN, All Rights Reserved.
 */
package cern.accsoft.steering.jmad.model.manage;

import cern.accsoft.steering.jmad.model.JMadModel;

/**
 * this interface defines a listener to a ModelManager
 * 
 * @author kfuchsbe
 */
public interface JMadModelManagerListener {

    /**
     * this method id called, when the model manager switches to a different model somehow.
     * <p>
     * NOTE: The new model might be null! This might be the case, e.g. a model is removed from the model manager
     * 
     * @param newActiveModel the actual (new) model
     */
    public void changedActiveModel(JMadModel newActiveModel);

    /**
     * fired when a new model is added.
     * 
     * @param newModel the new model
     */
    public void addedModel(JMadModel newModel);

    /**
     * fired, when a model was removed from the manager.
     * 
     * @param removedModel the model which was removed from the manager
     */
    public void removedModel(JMadModel removedModel);
}