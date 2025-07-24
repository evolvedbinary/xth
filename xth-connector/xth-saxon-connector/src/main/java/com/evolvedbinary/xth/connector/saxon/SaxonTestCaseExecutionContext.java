/*
 * X Test Harness
 * Copyright (C) 2024, Evolved Binary Ltd
 *
 * admin@evolvedbinary.com
 * https://www.evolvedbinary.com
 *
 * SPDX-License-Identifier: BUSL-1.1
 *
 * Use of this software is governed by the Business Source License 1.1
 * included in the LICENSE file and at www.mariadb.com/bsl11.
 *
 * Change Date: 2029-06-06
 *
 * On the date above, in accordance with the Business Source License, use
 * of this software will be governed by the Apache License, Version 2.0.
 *
 * Additional Use Grant: Production use of the Licensed Work for a permitted
 * purpose. A Permitted Purpose is any purpose other than a Competing Use.
 * A Competing Use means making the Software available to others in a commercial
 * product or service that: substitutes for the Software; substitutes for any
 * other product or service we offer using the Software that exists as of the
 * date we make the Software available; or offers the same or substantially
 * similar functionality as the Software.
 */
package com.evolvedbinary.xth.connector.saxon;

import com.evolvedbinary.xth.connector.impl.AbstractCompiledTestCaseExecutionContext;
import com.evolvedbinary.xth.tsom.TestCase;
import com.evolvedbinary.xth.tsom.TestSet;
import net.sf.saxon.s9api.XQueryEvaluator;
import net.sf.saxon.trans.XPathException;
import org.jspecify.annotations.Nullable;

import java.time.Instant;

/**
 * Holds either a compilation setup exception, compilation exception, or an xquery evaluator.
 */
class SaxonTestCaseExecutionContext extends AbstractCompiledTestCaseExecutionContext {

    @Nullable private final IllegalArgumentException compilationSetupException;
    @Nullable private final XPathException compilationException;
    @Nullable private final XQueryEvaluator xqueryEvaluator;

    SaxonTestCaseExecutionContext(final TestSet testSet, final TestCase testCase, final Instant processingStarted, final Instant compilationStarted, final Instant compilationFinished, @Nullable final IllegalArgumentException compilationSetupException, @Nullable final XPathException compilationException, @Nullable final XQueryEvaluator xqueryEvaluator) {
        super(testSet, testCase, processingStarted, compilationStarted, compilationFinished);
        this.compilationSetupException = compilationSetupException;
        this.compilationException = compilationException;
        this.xqueryEvaluator = xqueryEvaluator;
    }

    @Nullable IllegalArgumentException getCompilationSetupException() {
        return compilationSetupException;
    }

    @Nullable
    XPathException getCompilationException() {
        return compilationException;
    }

    @Nullable
    XQueryEvaluator getXqueryEvaluator() {
        return xqueryEvaluator;
    }
}
