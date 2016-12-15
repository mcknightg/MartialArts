package com.bluntsoftware.app.modules.user_manager.rest;

import com.bluntsoftware.lib.jpa.domain.Domain;
import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import java.io.Serializable;
import com.bluntsoftware.lib.jpa.rest.impl.JpaCRUDRestControllerImpl;
public abstract class CustomService <T extends Domain,ID extends Serializable, X extends GenericRepository<T,ID>> extends JpaCRUDRestControllerImpl<T,ID,X> {



}
