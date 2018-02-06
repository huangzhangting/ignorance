package nom.ignorance.test.test.jvm;

import lombok.Data;

@Data
public class FinalTest {
    final int num;

    public FinalTest() {
        num = 5;
    }

}
