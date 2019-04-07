import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.leyou.upload.LyUploadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author zhang
 * @date 2019年04月07日 13:28
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = LyUploadService.class)
public class UploadServiceTest {
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Test
    public void test() {
        fastFileStorageClient.deleteFile("group1","M00/00/00/wKgZgVyphomAMBPfADA7VtXSZwo798.jpg");
    }
}
