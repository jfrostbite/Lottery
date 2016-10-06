import com.kevin.utils.RandomUtils;

import java.util.Random;

/**
 * Created by kevin on 2016/10/5.
 */
public class Test {
    public static void main(String... args) {
        Random random = new Random();
        while (true) {
            //19 位长整数，种子直说
//            long r = random.nextLong();
            //13位当前时间戳
//            long r = System.currentTimeMillis();
            //17到20de 随机双精度小数
//            double r = Math.random();
//            double r = random.nextDouble();
            //9到11位整数型（正负）
//            int r = random.nextInt();
            //9到11 单精度小叔
//            float r = random.nextFloat();
//            int r = random.nextInt(10000000);
            String r = RandomUtils.generateNumString(15);
            System.out.println(r+"----"+String.valueOf(r).length());
        }
    }
}
