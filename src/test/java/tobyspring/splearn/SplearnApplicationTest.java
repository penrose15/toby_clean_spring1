package tobyspring.splearn;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

// 보통 static Method 는 클래스에 종속되므로 DI가 불가능함 (mock 불가능)
// 그래서 실제 static method를 실행시키는데 그 과정에서 준비되지 않은 자원으로 인해 에러 발생

class SplearnApplicationTest  {
    @Test
    void run() {
        // static 메서드 mocking
        // 한 스레드에 staticMocking은 한번밖에 못하므로 .close() 를 테스트 끝내고 수행해야 함
        try (MockedStatic<SplearnApplication> mocked = Mockito.mockStatic(SplearnApplication.class)) {

            SplearnApplication.main(new String[0]);

            mocked.verify(() -> SpringApplication.run(SplearnApplication.class, new String[0]));
        }
    }

}