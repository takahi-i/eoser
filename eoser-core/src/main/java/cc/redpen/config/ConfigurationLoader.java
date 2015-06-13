package cc.redpen.config;
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

import cc.redpen.EOSerException;
import cc.redpen.util.SAXErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class ConfigurationLoader {
    private static final Logger LOG =
            LoggerFactory.getLogger(ConfigurationLoader.class);

    private static Symbol createSymbol(Element element) throws EOSerException {
        if (!element.hasAttribute("name") || !element.hasAttribute("value")) {
            throw new IllegalStateException("Found element does not have name and value attribute...");
        }
        String value = element.getAttribute("value");
        if (value.length() != 1) {
            throw new EOSerException("value should be one character, specified: " + value);
        }
        char charValue = value.charAt(0);
        return new Symbol(
                SymbolType.valueOf(element.getAttribute("name")),
                charValue,
                element.getAttribute("invalid-chars"),
                Boolean.parseBoolean(element.getAttribute("before-space")),
                Boolean.parseBoolean(element.getAttribute("after-space")));
    }


    /**
     * parse the input stream. stream will be closed.
     *
     * @param input stream
     * @return document object
     */
    private static Document toDocument(InputStream input) throws EOSerException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try (BufferedInputStream bis = new BufferedInputStream(input)) {
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            dBuilder.setErrorHandler(new SAXErrorHandler());
            return dBuilder.parse(bis);
        } catch (SAXException | IOException | ParserConfigurationException e) {
            throw new EOSerException(e);
        }
    }

    /**
     * load symbol settings.
     *
     * @param configFile input configuration file
     * @return Validator configuration resources
     * @throws cc.redpen.EOSerException when failed to load configuration from specified configuration file
     */
    public List<Symbol> load(File configFile) throws EOSerException {
        LOG.info("Loading config from specified config file: \"{}\"", configFile.getAbsolutePath());
        try (InputStream fis = new FileInputStream(configFile)) {
            return this.load(fis);
        } catch (IOException e) {
            throw new EOSerException(e);
        }
    }

    /**
     * load symbol settings.
     *
     * @param configString configuration as String
     * @return Validator configuration resources
     * @throws cc.redpen.EOSerException when failed to load Configuration from specified string
     */
    public List<Symbol> loadFromString(String configString) throws EOSerException {
        return load(new ByteArrayInputStream(configString.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * load symbol configuration.
     * Provided stream will be closed.
     *
     * @param stream input configuration settings
     * @return Configuration loaded from input stream
     * @throws cc.redpen.EOSerException when failed to load configuration from specified stream
     */
    public List<Symbol> load(InputStream stream) throws EOSerException {
        Document doc = toDocument(stream);

        Element rootElement = getRootNode(doc, "symbol-conf");

        Node langNode = rootElement.getAttributes().getNamedItem("lang");
        String language = "en";
        if (langNode != null) {
            language = langNode.getNodeValue();
            LOG.info("Language is set to \"{}\"", language);
        } else {
            LOG.warn("No language configuration...");
            LOG.info("Set language to en");
        }

        // extract symbol configurations
        return this.extractSymbolConfig(rootElement.getChildNodes());
    }

    private List<Symbol> extractSymbolConfig(NodeList symbolTableConfigElementList)
            throws EOSerException {
        NodeList symbolTableElementList =
                getSpecifiedNodeList((Element)
                        symbolTableConfigElementList.item(0), "symbol");
        List<Symbol> customSymbols = new ArrayList<>();
        if (symbolTableElementList == null) {
            LOG.warn("there is no character block");
            return customSymbols;
        }
        for (int temp = 0; temp < symbolTableElementList.getLength(); temp++) {
            Node nNode = symbolTableElementList.item(temp);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) nNode;
                Symbol currentSymbol = createSymbol(element);
                customSymbols.add(currentSymbol);
            }
        }
        return customSymbols;
    }

    private NodeList getSpecifiedNodeList(Element rootElement, String elementName) {
        NodeList elementList =
                rootElement.getElementsByTagName(elementName);
        if (elementList.getLength() == 0) {
            LOG.info("No \"" +
                    elementName + "\" block found in the configuration");
            return null;
        } else if (elementList.getLength() > 1) {
            LOG.info("More than one \"" + elementName + " \" blocks in the configuration");
        }
        return elementList;
    }

    private Element getRootNode(Document doc, String rootTag) {
        doc.getDocumentElement().normalize();
        NodeList rootConfigElementList =
                doc.getElementsByTagName(rootTag);
        if (rootConfigElementList.getLength() == 0) {
            throw new IllegalStateException("No \"" + rootTag
                    + "\" block found in the configuration");
        } else if (rootConfigElementList.getLength() > 1) {
            LOG.warn("More than one \"" +
                    rootTag + "\" blocks in the cnfiguration");
        }
        Node root = rootConfigElementList.item(0);
        Element rootElement = (Element) root;
        LOG.info("Succeeded to load configuration file");
        return rootElement;
    }
}
