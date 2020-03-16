package ru.mikhail.converter.CurrentCurrencyValue;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;


public class ProductXmlParser {

    public ArrayList<Collection> collections;

    public ProductXmlParser() {
        collections = new ArrayList<>();
    }

    public ArrayList<Collection> getCollections() {
        return collections;
    }

    public boolean parse(String xmlData) {
        boolean status = true;
        Collection currentCollection = null;
        boolean inEntry = false;
        String textValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new StringReader(xmlData));
            int evenType = xpp.getEventType();
            while (evenType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (evenType) {
                    case XmlPullParser.START_TAG:
                        if ("valute".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentCollection = new Collection();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        textValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (inEntry) {
                            if ("valute".equalsIgnoreCase(tagName)) {
                                collections.add(currentCollection);
                                inEntry = false;
                            } else if ("charcode".equalsIgnoreCase((tagName))) {
                                currentCollection.setCharCode(textValue);
                            } else if ("value".equalsIgnoreCase(tagName)) {
                                currentCollection.setValue(textValue);
                            }
                        }
                        break;
                    default:
                }
                evenType = xpp.next();
            }
        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }
        return status;
    }


}
