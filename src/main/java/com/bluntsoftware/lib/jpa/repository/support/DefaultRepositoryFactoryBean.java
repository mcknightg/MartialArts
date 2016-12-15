package com.bluntsoftware.lib.jpa.repository.support;


import com.bluntsoftware.lib.jpa.domain.Domain;
import com.bluntsoftware.lib.jpa.repository.Impl.GenericRepositoryImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.repository.core.RepositoryInformation;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import javax.persistence.EntityManager;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Alexander Mcknight
 * Date: 6/29/15
 * Time: 1:02 AM
 */
public class DefaultRepositoryFactoryBean<R extends JpaRepository<T, ID>, T  extends Domain, ID extends Serializable>
    extends JpaRepositoryFactoryBean<R, T, ID> {
    @Override
    public void setMappingContext(MappingContext<?, ?> mappingContext) {
        super.setMappingContext(mappingContext);
    }

    protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
        return new DefaultRepositoryFactory(entityManager);
    }

    private static class DefaultRepositoryFactory<T extends Domain, ID extends Serializable> extends JpaRepositoryFactory {
        private EntityManager entityManager;
        DefaultRepositoryFactory(EntityManager entityManager) {
            super(entityManager);
            this.entityManager = entityManager;
        }
        protected Object getTargetRepository(RepositoryMetadata metadata) {
            return new GenericRepositoryImpl<T, ID>((Class<T>) metadata.getDomainType(), entityManager);
        }
         protected Class<?> getRepositoryBaseClass(RepositoryMetadata metadata) {
            return GenericRepositoryImpl.class;
         }

        @Override
        protected RepositoryInformation getRepositoryInformation(RepositoryMetadata metadata, Class<?> customImplementationClass) {
            return super.getRepositoryInformation(metadata, customImplementationClass);
        }
    }
}
