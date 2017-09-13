package ouasmine.text.nlp.ner;
import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.util.CoreMap;

import java.io.File;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ouasmine on 12/09/2017.
 */
public class NameEntityRecognition {


    static String DIR_PATH = "NER";
    static String NER_CLASSIFIER_FILE = "english.muc.7class.distsim.crf.ser.gz";
    //static String NER_CLASSIFIER_FILE = "english.all.3class.distsim.crf.ser.gz";

    static AbstractSequenceClassifier<CoreMap> classifier = null;
    static {
        try {
            String classifierPath = DIR_PATH + File.separator
                    + NER_CLASSIFIER_FILE;
            if (!new File(classifierPath).exists()) {
                System.out.println(classifierPath + " does not exists.");
            } else {
                classifierPath = URLDecoder.decode(classifierPath, "UTF-8");
                classifier = CRFClassifier
                        .getClassifierNoExceptions(classifierPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String annotateText(String query) {
        String text = query;
        String output = null;
        try {
            if ((text != null && !text.equals("")) && classifier != null) {
                output = classifier.classifyToString(text);
                System.out.println(output);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return output;
    }

    private static void parseText(String text) {
        String[] ENTITY_LIST = { "person", "location", "date", "organization",
                "time", "money", "percentage" };
        try {
            if (text != null) {
                for (String entityValue : ENTITY_LIST) {
                    String entity = entityValue.toUpperCase();
                    Pattern pattern = Pattern
                            .compile("([a-zA-Z0-9.%]+(/" + entity
                                    + ")[ ]*)*[a-zA-Z0-9.%]+(/" + entity + ")");
                    Matcher matcher = pattern.matcher(text);
                    while (matcher.find()) {
                        int start = matcher.start();
                        int end = matcher.end();
                        String inputText = text.substring(start, end);
                        inputText = inputText.replaceAll("/" + entity, "");
                        System.out.println(inputText + " : " + entity);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String text = "After graduating from Madras Institute of Technology (MIT – Chennai) in 1960, Dr. Abdul Kalam " +
                "joined Aeronautical Development Establishment of Defence Research and Development Organisation (DRDO) as a scientist.";

        String text2 = "... de candidat, des experts dans leur domaine et des partenaires commerciaux. ... " +
                "Directeur Commercial Energie & Infra de Transport chez Clemessy ...";

        String text3 = "Responsable d'un pôle d'activité et du développement commercial au sein de l'agence IDF : " +
                "- Management d'une équipe de 5 personnes - Gestion financière";


         parseText(annotateText(text));
    }
}
