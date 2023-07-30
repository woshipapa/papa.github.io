import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.papa.entity.User;
import com.papa.springMVC.XML.XMLParser;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.dom4j.DocumentException;
import org.junit.Test;

public class test {
    @Test
    public void testBasePackage() throws DocumentException {
        String res=XMLParser.getBasePackage("papa-springMVC.xml");
        System.out.println(res);
        System.out.println("\\");
    }
    @Test
    public void testJson() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String str=objectMapper.writeValueAsString(new User("xm",12));
        System.out.println(str);
    }
}
