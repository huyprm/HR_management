package org.ptithcm2021.hr_management.util;

import jakarta.annotation.PostConstruct;
import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.docx4j.Docx4J;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.wml.R;
import org.docx4j.wml.SdtElement;
import org.docx4j.wml.Text;
import org.ptithcm2021.hr_management.service.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public final class FillDocxWithTagsUtil {
    @Value("${form-fields-url.name}")
    private String urlFormFields;

    public static String URL_FORM_FIELDS;

    @PostConstruct
    public void init() {
        URL_FORM_FIELDS = urlFormFields;
    }

    public static ByteArrayOutputStream fillDocxWithTags(Map<String, String> data) throws Exception {
       URL url = new URL(URL_FORM_FIELDS);
       WordprocessingMLPackage wordMLPackage= null;
       try (InputStream is = url.openStream()) {
           wordMLPackage = WordprocessingMLPackage.load(is);
       } catch (Exception e) {
           e.printStackTrace();
       }

       WordprocessingMLPackage filledPackage = (WordprocessingMLPackage) wordMLPackage.clone();

       List<Object> sdtElements = filledPackage.getMainDocumentPart()
               .getJAXBNodesViaXPath("//w:sdt", true);

        for (Object obj : sdtElements) {
           SdtElement sdt = null;

           if (obj instanceof SdtElement) {
               sdt = (SdtElement) obj;
           } else if (obj instanceof JAXBElement && ((JAXBElement<?>) obj).getValue() instanceof SdtElement) {
               sdt = (SdtElement) ((JAXBElement<?>) obj).getValue();
           }

           if (sdt != null && sdt.getSdtPr() != null && sdt.getSdtPr().getTag() != null) {
               String tag = sdt.getSdtPr().getTag().getVal();
               if (data.containsKey(tag)) {
                   replaceTextInSdt(sdt, data.get(tag));
               }
           }
       }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        Docx4J.toPDF(filledPackage, outStream);

        return outStream;
   }

    // Hàm ghi text vào SdtElement
    private static void replaceTextInSdt(SdtElement sdtElement, String value) {
        List<Object> contents = sdtElement.getSdtContent().getContent();
        for (Object content : contents) {
            Object realContent = unwrap(content);
            if (realContent instanceof R) {
                R run = (R) realContent;
                for (Object runContent : run.getContent()) {
                    Object realRunContent = unwrap(runContent);
                    if (realRunContent instanceof Text) {
                        ((Text) realRunContent).setValue(value);
                    }
                }
            }
        }
    }

    // Hàm gỡ bọc JAXBElement
    private static Object unwrap(Object obj) {
        if (obj instanceof JAXBElement) {
            return ((JAXBElement<?>) obj).getValue();
        }
        return obj;
    }

}
