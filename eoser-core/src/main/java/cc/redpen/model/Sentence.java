/**
 * redpen: a text inspection tool
 * Copyright (c) 2014-2015 Recruit Technologies Co., Ltd. and contributors
 * (see CONTRIBUTORS.md)
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
package cc.redpen.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Sentence block in a Document.
 */
public final class Sentence implements Serializable {
    private static final long serialVersionUID = 3761982769692999924L;
    /**
     * Links (including internal and external ones).
     */
    private final List<String> links;
    /**
     * Sentence position in a file.
     */
    private int lineNumber;
    /**
     * Content of string.
     */
    private String content;
    /**
     * Position which the sentence starts with.
     */
    private int startPositionOffset;
    /**
     * Flag for knowing if the sentence is the first sentence
     * of a block, such as paragraph, list, header.
     */
    private boolean isFirstSentence;

    /**
     * Constructor.
     *
     * @param sentenceContent content of sentence
     * @param lineNum         line number of sentence
     */
    public Sentence(String sentenceContent, int lineNum) {
        this(sentenceContent, lineNum, 0);
    }

    /**
     * Constructor.
     *
     * @param sentenceContent  content of sentence
     * @param sentencePosition sentence position
     * @param startOffset      offset of the start position in the line
     */
    public Sentence(String sentenceContent, int sentencePosition, int startOffset) {
        super();
        this.content = sentenceContent;
        this.lineNumber = sentencePosition;
        this.isFirstSentence = false;
        this.links = new ArrayList<>();
        this.startPositionOffset = startOffset;
    }

    /**
     * Get content of sentence.
     *
     * @return sentence
     */
    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "links=" + links +
                ", lineNumber=" + lineNumber +
                ", content='" + content + '\'' +
                ", startPositionOffset=" + startPositionOffset +
                ", isFirstSentence=" + isFirstSentence +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sentence sentence = (Sentence) o;

        if (isFirstSentence != sentence.isFirstSentence) return false;
        if (lineNumber != sentence.lineNumber) return false;
        if (startPositionOffset != sentence.startPositionOffset) return false;
        if (content != null ? !content.equals(sentence.content) : sentence.content != null) return false;
        if (links != null ? !links.equals(sentence.links) : sentence.links != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = links != null ? links.hashCode() : 0;
        result = 31 * result + lineNumber;
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + startPositionOffset;
        result = 31 * result + (isFirstSentence ? 1 : 0);
        return result;
    }
}
