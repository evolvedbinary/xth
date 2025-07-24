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

import net.jcip.annotations.NotThreadSafe;
import org.jspecify.annotations.Nullable;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Records all Error events from Saxon.
 */
@NotThreadSafe
class SaxonErrorListener implements ErrorListener {

    SaxonErrorListener() {
    }

    private @Nullable List<ErrorInfo> errorList = null;

    @Override
    public void warning(final TransformerException exception) throws TransformerException {
        // TODO(AR) should we throw TransformerException - or just record it here - how do we report failure to our TestResultsListener?
        add(ErrorInfo.Severity.WARNING, exception);
    }

    @Override
    public void error(final TransformerException exception) throws TransformerException {
        // TODO(AR) should we throw TransformerException - or just record it here - how do we report failure to our TestResultsListener?
        add(ErrorInfo.Severity.ERROR, exception);
    }

    @Override
    public void fatalError(final TransformerException exception) throws TransformerException {
        // TODO(AR) should we throw TransformerException - or just record it here - how do we report failure to our TestResultsListener?
        add(ErrorInfo.Severity.FATAL, exception);
    }

    private void add(final ErrorInfo.Severity severity, final TransformerException transformerException) {
        if (errorList == null) {
            errorList = new ArrayList<>();
        }
        errorList.add(new ErrorInfo(severity, transformerException));
    }

    public static class ErrorInfo {
        public enum Severity {
            WARNING,
            ERROR,
            FATAL
        }

        public ErrorInfo(final Severity severity, final TransformerException transformerException) {
            this.timestamp = Instant.now();
            this.severity = severity;
            this.transformerException = transformerException;
        }

        private final Instant timestamp;
        private final Severity severity;
        private final TransformerException transformerException;
    }
}
