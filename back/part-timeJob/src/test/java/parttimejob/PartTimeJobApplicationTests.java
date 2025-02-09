package parttimejob;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import parttimejob.Utils.minio.FileStorageService;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@SpringBootTest
@RunWith(SpringRunner.class)
class PartTimeJobApplicationTests {

    @Autowired
    private FileStorageService fileStorageService;

    @Test
    public void testUpdateImgFile() {
        try {
            FileInputStream fileInputStream = new FileInputStream("C:\\Users\\86151\\Desktop\\图片\\2a2e9d66-b41d-4645-87bd-95f2cfeed218.jpg");
            String filePath = fileStorageService.uploadImgFile("BOOS", "ak47.jpg", fileInputStream);


            System.out.println(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
