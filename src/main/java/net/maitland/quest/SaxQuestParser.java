package net.maitland.quest;

import net.maitland.quest.model.*;
import net.maitland.quest.player.QuestStateException;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by David on 06/01/2017.
 */
public class SaxQuestParser extends AbstractQuestParser {

    @Override
    public Quest parseQuest(InputStream story) throws IOException {
        try {

            String storyStr = removeMixedContent(story);
            StringReader sr = new StringReader(storyStr);

            QuestContentHandler qch = new QuestContentHandler();
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();
            XMLReader xmlReader = saxParser.getXMLReader();
            xmlReader.setContentHandler(qch);
            xmlReader.parse(new InputSource(sr));
            return qch.getQuest();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    private class QuestContentHandler extends DefaultHandler {

        private Quest quest;
        private About about;
        private QuestStation station;
        private QuestSection questSection;
        private Text text;
        private Choice choice;
        private StringBuilder currentCharacters = new StringBuilder();

        public Quest getQuest() {
            return quest;
        }

        @Override
        public void startDocument() throws SAXException {
            this.quest = new Quest();
        }

        @Override
        public void endDocument() throws SAXException {

            // index stations
            Map<String, QuestStation> stations = new HashMap<>();
            stations.put(QuestStation.BACK_STATION_ID, QuestStation.getBackStation());
            for(QuestStation qs : this.quest.getStations())
            {
                stations.put(qs.getId(), qs);
            }

            // update choice stations
            for(QuestStation qs : this.quest.getStations())
            {
                updateChoiceStations(qs, stations);

                for(IfSection is : qs.getConditions())
                {
                    updateChoiceStations(is, stations);
                }

                if(qs.getElseCondition() != null)
                {
                    updateChoiceStations(qs.getElseCondition(), stations);
                }
            }
        }

        protected void updateChoiceStations(QuestSection qs, Map<String, QuestStation> stations)
        {
            for(Choice c : qs.getChoices())
            {
                c.setStation(stations.get(c.getStationAlias()));
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {

            for(int i = start; i < start+length; i++)
            {
                currentCharacters.append(ch[i]);
            }
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {

            this.currentCharacters = new StringBuilder();

            if (qName.equals("about")) {
                startAbout(attributes);
            }

            if (qName.equals("title")) {
                startTitle(attributes);
            }

            if (qName.equals("station")) {
                startStation(attributes);
            }

            if (qName.equals("text")) {
                startText(attributes);
            }

            if (qName.equals("choice")) {
                startChoice(attributes);
            }

            if (qName.equals("number")) {
                startNumber(attributes);
            }

            if (qName.equals("string")) {
                startString(attributes);
            }

            if (qName.equals("state")) {
                startState(attributes);
            }

            if (qName.equals("if")) {
                startIf(attributes);
            }

            if (qName.equals("else")) {
                startElse(attributes);
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {

            if (qName.equals("about")) {
                endAbout();
            }

            if (qName.equals("title")) {
                endTitle();
            }

            if (qName.equals("station")) {
                endStation();
            }

            if (qName.equals("text")) {
                endText();
            }

            if (qName.equals("choice")) {
                endChoice();
            }

            if (qName.equals("number")) {
                endNumber();
            }

            if (qName.equals("string")) {
                endString();
            }

            if (qName.equals("state")) {
                endState();
            }

            if (qName.equals("if")) {
                endIf();
            }

            if (qName.equals("else")) {
                endElse();
            }
        }

        protected void startAbout(Attributes attributes) throws SAXException {
            this.about = new About();
        }

        protected void endAbout() throws SAXException {
            this.quest.setAbout(this.about);
        }

        protected void startTitle(Attributes attributes) throws SAXException {
        }

        protected void endTitle() throws SAXException {
            this.about.setTitle(this.currentCharacters.toString());
        }

        protected void startStation(Attributes attributes) throws SAXException {

            String id = attributes.getValue("id");
            this.station = new QuestStation();
            this.station.setId(id);
            this.questSection = this.station;
        }

        protected void endStation() throws SAXException {
            this.quest.addStation(this.station);
            this.station = null;
            this.questSection = null;
        }

        protected void startText(Attributes attributes) throws SAXException {
            this.text = new Text();
        }

        protected void endText() throws SAXException {
            this.text.setValue(this.currentCharacters.toString());
            this.questSection.setText(this.text);
            this.text = null;
        }

        protected void startChoice(Attributes attributes) throws SAXException {
            String stationId = attributes.getValue("station");
            String check = attributes.getValue("check");
            this.choice = new Choice();
            this.choice.setStationAlias(stationId);
            this.choice.setCheck(check);
        }

        protected void endChoice() throws SAXException {
            this.choice.setText(this.currentCharacters.toString());
            this.questSection.addChoice(this.choice);
            this.choice = null;
        }

        protected void startNumber(Attributes attributes) throws SAXException {
            String name = attributes.getValue("name");
            String value = attributes.getValue("value");
            NumberAttribute attribute = new NumberAttribute();
            attribute.setName(name);
            attribute.setValue(value);
            this.questSection.addAttribute(attribute);
        }

        protected void endNumber() throws SAXException {

        }

        protected void startString(Attributes attributes) throws SAXException {
            String name = attributes.getValue("name");
            String value = attributes.getValue("value");
            StringAttribute attribute = new StringAttribute();
            attribute.setName(name);
            attribute.setValue(value);
            this.questSection.addAttribute(attribute);
        }

        protected void endString() throws SAXException {

        }

        protected void startState(Attributes attributes) throws SAXException {
            String name = attributes.getValue("name");
            String value = attributes.getValue("value");
            StateAttribute attribute = new StateAttribute();
            attribute.setName(name);
            attribute.setValue(String.valueOf(Boolean.parseBoolean(value)));
            this.questSection.addAttribute(attribute);

        }

        protected void endState() throws SAXException {

        }

        protected void startIf(Attributes attributes) throws SAXException {
            String check = attributes.getValue("check");
            IfSection ifSection = new IfSection();
            ifSection.setCheck(check);
            this.questSection = ifSection;
        }

        protected void endIf() throws SAXException {
            this.station.addCondition((IfSection) this.questSection);
            this.questSection = null;
        }

        protected void startElse(Attributes attributes) throws SAXException {
            ElseSection elseSection = new ElseSection();
            this.questSection = elseSection;
        }

        protected void endElse() throws SAXException {
            this.station.setElseCondition((ElseSection) this.questSection);
            this.questSection = null;
        }
    }
}
