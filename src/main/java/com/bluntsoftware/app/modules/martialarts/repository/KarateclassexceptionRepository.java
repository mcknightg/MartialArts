package com.bluntsoftware.app.modules.martialarts.repository;

import com.bluntsoftware.app.modules.martialarts.domain.Karateclassexception;
import com.bluntsoftware.lib.jpa.repository.GenericRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.beans.factory.annotation.Qualifier;
/**
* Repository interface for table: Karateclassexception.
* @author autogenerated
*/

@Repository("martialartsKarateclassexceptionRepository")
@Qualifier("martialarts")
//@RepositoryRestResource(collectionResourceRel="martialarts.Karateclassexception", path="martialarts/Karateclassexception")
public interface KarateclassexceptionRepository extends GenericRepository<Karateclassexception,Integer>  {

}