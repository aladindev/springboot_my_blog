import java.math.BigDecimal;
import java.util.UUID;

public class test {
    public static void main(String[] args) {

        String sessionId = UUID.randomUUID().toString();
        System.out.println(sessionId);
        System.out.println(sessionId.length());
    }
}
