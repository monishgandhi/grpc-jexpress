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
package com.flipkart.gjex.hibernate;

import org.hibernate.CacheMode;
import org.hibernate.FlushMode;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;

/**
 * This annotation has to be compulsorily used whenever {@link Transactional} is used.
 *
 * It specifies name of HibernateBundle whose {@link org.hibernate.SessionFactory} will be used for
 * creating {@link org.hibernate.Session}.
 *
 * Other {@link org.hibernate.Session} properties can also be configured.
 *
 * @author anand.pandey
 */
@Inherited
@Target({TYPE, METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SelectedDataSource {

    /**
     * The name of a hibernate bundle (session factory) that specifies
     * a data source against which a transaction will be opened.
     */
    String name() default HibernateBundle.DEFAULT_NAME;

    /**
     * The {@link CacheMode} for the session.
     *
     * @see CacheMode
     * @see org.hibernate.Session#setCacheMode(CacheMode)
     */
    CacheMode cacheMode() default CacheMode.NORMAL;

    /**
     * The {@link FlushMode} for the session.
     *
     * @see FlushMode
     * @see org.hibernate.Session#setFlushMode(org.hibernate.FlushMode)
     */
    FlushMode flushMode() default FlushMode.AUTO;

}
