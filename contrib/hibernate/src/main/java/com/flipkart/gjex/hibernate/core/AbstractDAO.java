/*
 * Copyright (c) The original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.flipkart.gjex.hibernate.core;

import com.flipkart.gjex.hibernate.SessionFactoryContext;
import com.flipkart.gjex.hibernate.utils.Generics;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.hibernate.query.internal.AbstractProducedQuery;

import javax.persistence.criteria.CriteriaQuery;
import java.io.Serializable;
import java.util.List;

import static java.util.Objects.requireNonNull;

/**
 * An abstract base class for Hibernate DAO classes.
 *
 * @param <E> the class which this DAO manages
 * @author anand.pandey
 */
public class AbstractDAO<E> {
    private final SessionFactoryContext sessionFactoryContext;
    private final Class<?> entityClass;

    public AbstractDAO(SessionFactoryContext sessionFactoryContext) {
        this.sessionFactoryContext = requireNonNull(sessionFactoryContext);
        this.entityClass = Generics.getTypeParameter(getClass());
    }

    /**
     * Returns the current {@link Session}.
     *
     * @return the current session
     */
    protected Session currentSession() {
        return sessionFactoryContext.getThreadLocalSession();
    }

    /**
     * Creates a new {@link Criteria} query for {@code <E>}.
     *
     * @return a new {@link Criteria} query
     * @see Session#createCriteria(Class)
     * @deprecated Use {@link AbstractDAO#criteriaQuery()} instead.
     */
    @Deprecated
    protected Criteria criteria() {
        return currentSession().createCriteria(entityClass);
    }

    /**
     * Creates a new {@link CriteriaQuery} for {@code <E>}.
     *
     * @return a new {@link CriteriaQuery} query
     */
    protected CriteriaQuery<E> criteriaQuery() {
        return this.currentSession().getCriteriaBuilder().createQuery(getEntityClass());
    }

    /**
     * Returns a named {@link Query}.
     *
     * @param queryName the name of the query
     * @return the named query
     * @see Session#getNamedQuery(String)
     */
    protected Query<?> namedQuery(String queryName) throws HibernateException {
        return currentSession().getNamedQuery(requireNonNull(queryName));
    }

    /**
     * Returns a typed {@link Query<E>}
     *
     * @param queryString HQL query
     * @return typed query
     */
    protected Query<E> query(String queryString) {
        return currentSession().createQuery(requireNonNull(queryString), getEntityClass());
    }

    /**
     * Returns the entity class managed by this DAO.
     *
     * @return the entity class managed by this DAO
     */
    @SuppressWarnings("unchecked")
    public Class<E> getEntityClass() {
        return (Class<E>) entityClass;
    }

    /**
     * Convenience method to return a single instance that matches the criteria query,
     * or null if the criteria returns no results.
     *
     * @param criteriaQuery the {@link CriteriaQuery} query to run
     * @return the single result or {@code null}
     * @throws HibernateException if there is more than one matching result
     */
    protected E uniqueResult(CriteriaQuery<E> criteriaQuery) throws HibernateException {
        return AbstractProducedQuery.uniqueElement(
            currentSession()
                .createQuery(requireNonNull(criteriaQuery))
                .getResultList()
        );
    }

    /**
     * Convenience method to return a single instance that matches the criteria, or null if the
     * criteria returns no results.
     *
     * @param criteria the {@link Criteria} query to run
     * @return the single result or {@code null}
     * @throws HibernateException if there is more than one matching result
     * @see Criteria#uniqueResult()
     */
    @SuppressWarnings("unchecked")
    protected E uniqueResult(Criteria criteria) throws HibernateException {
        return (E) requireNonNull(criteria).uniqueResult();
    }

    /**
     * Convenience method to return a single instance that matches the query, or null if the query
     * returns no results.
     *
     * @param query the query to run
     * @return the single result or {@code null}
     * @throws HibernateException if there is more than one matching result
     * @see Query#uniqueResult()
     */
    protected E uniqueResult(Query<E> query) throws HibernateException {
        return requireNonNull(query).uniqueResult();
    }

    /**
     * Get the results of a {@link Criteria} query.
     *
     * @param criteria the {@link Criteria} query to run
     * @return the list of matched query results
     * @see Criteria#list()
     */
    @SuppressWarnings("unchecked")
    protected List<E> list(Criteria criteria) throws HibernateException {
        return requireNonNull(criteria).list();
    }

    /**
     * Get the results of a {@link CriteriaQuery} query.
     *
     * @param criteria the {@link CriteriaQuery} query to run
     * @return the list of matched query results
     */
    protected List<E> list(CriteriaQuery<E> criteria) throws HibernateException {
        return currentSession().createQuery(requireNonNull(criteria)).getResultList();
    }

    /**
     * Get the results of a query.
     *
     * @param query the query to run
     * @return the list of matched query results
     * @see Query#list()
     */
    protected List<E> list(Query<E> query) throws HibernateException {
        return requireNonNull(query).list();
    }

    /**
     * Return the persistent instance of {@code <E>} with the given identifier, or {@code null} if
     * there is no such persistent instance. (If the instance, or a proxy for the instance, is
     * already associated with the session, return that instance or proxy.)
     *
     * @param id an identifier
     * @return a persistent instance or {@code null}
     * @throws HibernateException
     * @see Session#get(Class, Serializable)
     */
    @SuppressWarnings("unchecked")
    protected E get(Serializable id) {
        return (E) currentSession().get(entityClass, requireNonNull(id));
    }

    /**
     * Either save or update the given instance, depending upon resolution of the unsaved-value
     * checks (see the manual for discussion of unsaved-value checking).
     * <p/>
     * This operation cascades to associated instances if the association is mapped with
     * <tt>cascade="save-update"</tt>.
     *
     * @param entity a transient or detached instance containing new or updated state
     * @throws HibernateException
     * @see Session#saveOrUpdate(Object)
     */
    protected E persist(E entity) throws HibernateException {
        currentSession().saveOrUpdate(requireNonNull(entity));
        return entity;
    }
}
