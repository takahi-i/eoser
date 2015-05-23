/**
 * eoser: a sentence segmenter
 * Copyright (c) 2014-2015 Takahiko Ito, Recruit Technologies Co., Ltd.
 * and contributors (see CONTRIBUTORS.md)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.redpen;

/**
 * Error used to model the failure of Validators.
 */
@SuppressWarnings("serial")
public class EOSerException extends Exception {
    /**
     * Constructor.
     */
    public EOSerException() {
        super();
    }

    /**
     * Constructor.
     *
     * @param message error message
     */
    public EOSerException(String message) {
        super(message);
    }

    /**
     * Constructor.
     *
     * @param message error message
     * @param cause   error cause
     */
    public EOSerException(String message, Exception cause) {
        super(message, cause);
    }

    /**
     * Constructor.
     * @param causeException seed exception.
     */
    public EOSerException(Exception causeException) {
        this(causeException.getMessage(), causeException);
    }
}
