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
package cc.redpen;

import cc.redpen.config.SymbolTable;
import cc.redpen.model.Sentence;
import cc.redpen.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public class SentenceExtractorTest {

    private List<Sentence> createSentences(List<Pair<Integer, Integer>> outputPositions, String line) {
        List<Sentence> output = new ArrayList<>();
        for (Pair<Integer, Integer> outputPosition : outputPositions) {
            output.add(new Sentence(line.substring(outputPosition.first, outputPosition.second), 0));
        }
        return output;
    }

    @Test
    public void testSimple() {
        SentenceExtractor extractor = new SentenceExtractor(new SymbolTable("en", Optional.<String>empty(), new ArrayList<>()));
        List<Pair<Integer, Integer>> outputPositions = new ArrayList<>();
        final String input = "this is a pen.";
        int lastPosition = extractor.extract(input, outputPositions);
        List<Sentence> outputSentences = createSentences(outputPositions, input);
        assertEquals(1, outputSentences.size());
        assertEquals(input, outputSentences.get(0).getContent());
        assertEquals(14, lastPosition);
    }

    @Test
    public void testMultipleSentences() {
        SentenceExtractor extractor = new SentenceExtractor(new SymbolTable("en", Optional.<String>empty(), new ArrayList<>()));
        final String input = "this is a pen. that is a paper.";
        List<Pair<Integer, Integer>> outputPositions = new ArrayList<>();
        int lastPosition = extractor.extract(input, outputPositions);
        List<Sentence> outputSentences = createSentences(outputPositions, input);
        assertEquals(2, outputSentences.size());
        assertEquals("this is a pen.", outputSentences.get(0).getContent());
        assertEquals(" that is a paper.", outputSentences.get(1).getContent());
        assertEquals(31, lastPosition);
    }
}
