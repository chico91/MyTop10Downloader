package com.example.mytop10downloader;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;

public class parseApplications {
    private static final String TAG = "parseApplications";
    private ArrayList<FeedEntry> application;

    public parseApplications() {
        this.application = new ArrayList<>();
    }

    public ArrayList<FeedEntry> getApplication() {
        return application;
    }

    public boolean parse(String xmlData) {
        boolean status = true;
        FeedEntry currentRecord = null;
        boolean inEntry = false;
        String texValue = "";

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xmlData));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName = xpp.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        Log.d(TAG, "parse:Starting tag for " + tagName);
                        if ("entry".equalsIgnoreCase(tagName)) {
                            inEntry = true;
                            currentRecord = new FeedEntry();
                        }
                        break;
                    case XmlPullParser.TEXT:
                        texValue = xpp.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        Log.d(TAG, "parse: Ending tag for" + tagName);
                        if (inEntry) {
                            if ("entry".equalsIgnoreCase(tagName)) {
                                application.add(currentRecord);
                                inEntry = false;
                            } else if ("name".equalsIgnoreCase(tagName)) {
                                currentRecord.setName(texValue);
                            } else if ("artist".equalsIgnoreCase(tagName)) {
                                currentRecord.setArtist(texValue);
                            } else if ("releaseDate".equalsIgnoreCase(tagName)) {
                                currentRecord.setReleaseDate(texValue);
                            } else if ("summary".equalsIgnoreCase(tagName)) {
                                currentRecord.setSummary(texValue);
                            } else if ("image".equalsIgnoreCase(tagName)) {
                                currentRecord.setImageURL(texValue);
                            }
                        }
                        break;
                    default:
                }
                eventType = xpp.next();

            }
            for (FeedEntry app : application) {
                Log.d(TAG, "*********");
                Log.d(TAG, app.toString());
            }


        } catch (Exception e) {
            status = false;
        }
        return status;
    }
}
