package com.mountain.library.helper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarshallingUtils {

    private static final Logger logger = LoggerFactory.getLogger(MarshallingUtils.class);

    static ObjectMapper mapper = new ObjectMapper();

    public static List<Object> unmarshallJson(String[] daftarItem, Class c){

        if( daftarItem == null ){
            return Collections.emptyList();
        }

        List<Object> daftar = new ArrayList<>();
        for (String str : daftarItem) {
            try (StringReader reader = new StringReader(str)) {
                Object item = MarshallingUtils.unmarshalJson(reader, c);
                daftar.add(item);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return daftar;
    }

    public static Object unmarshalJson(Reader reader, Class c) throws IOException {
        Object value = mapper.readValue(reader, c);
        return value;
    }

    public static Object unmarshalJson(String str, Class c) throws IOException {
        StringReader reader = new StringReader(str);
        Object value = mapper.readValue(reader, c);
        return value;
    }

    public static String marshalJson(Object o, boolean prettify) throws IOException {
        if (prettify) {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(o);
        }
        return mapper.writeValueAsString(o);
    }

    public static String marshalJson(Object o) throws IOException {
        return marshalJson(o, false);
    }

    public static List<String> unmarshalJsonList(String str) throws IOException {
        StringReader reader = new StringReader(str);
        List<String> val = (List<String>) mapper.readValue(reader, List.class);
        return val;
    }

    public static String marshalXml(Object o, Class classz) throws JAXBException {
        StringWriter writer = new StringWriter();
        JAXBContext context = JAXBContext.newInstance(classz);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(o, writer);
        return writer.toString();
    }

    public static Object unmarshalXml(Reader reader, Class classz) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(classz);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return unmarshaller.unmarshal(reader);
    }

    public static Object unmarshalXml(String str, Class c) throws IOException, JAXBException {
        StringReader reader = new StringReader(str);
        return unmarshalXml(reader, c);
    }

    public static void printJson(Object object) {
        try {
            String json = MarshallingUtils.marshalJson(object, true);
            logger.info(json);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void printXML(Object object, Class clazz) throws JAXBException {
        String xml = MarshallingUtils.marshalXml(object, clazz);
        logger.info(xml);
    }
}
